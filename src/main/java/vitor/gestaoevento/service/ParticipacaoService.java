package vitor.gestaoevento.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vitor.gestaoevento.infra.qrcode.QrCodeService;
import vitor.gestaoevento.integration.sms.SmsService;
import vitor.gestaoevento.model.Evento;
import vitor.gestaoevento.model.Participacao;
import vitor.gestaoevento.model.StatusPagamento;
import vitor.gestaoevento.model.Usuario;
import vitor.gestaoevento.repository.EventoRepository;
import vitor.gestaoevento.repository.ParticipacaoRepository;
import vitor.gestaoevento.repository.UsuarioRepository;
import vitor.gestaoevento.security.AuthenticatedUserService;

@Slf4j
@Service
public class ParticipacaoService {

    private final UsuarioRepository usuarioRepository;
    private final EventoRepository eventoRepository;
    private final ParticipacaoRepository participacaoRepository;
    private final SmsService smsService;
    private final QrCodeService qrCodeService;
    private final AuthenticatedUserService authenticatedUserService;


    public ParticipacaoService(UsuarioRepository usuarioRepository,
                               EventoRepository eventoRepository,
                               ParticipacaoRepository participacaoRepository,
                               SmsService smsService, QrCodeService qrCodeService,
                               AuthenticatedUserService authenticatedUserService) {
        this.usuarioRepository = usuarioRepository;
        this.eventoRepository = eventoRepository;
        this.participacaoRepository = participacaoRepository;
        this.smsService = smsService;
        this.qrCodeService = qrCodeService;
        this.authenticatedUserService = authenticatedUserService;
    }

    public Participacao cadastrarParticipacao(Long usuarioId, Long eventoId) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new IllegalArgumentException("Evento não encontrado"));

        if (!evento.isAtivo()) {
            throw new IllegalArgumentException("O evento não está ativo");
        }

        boolean jaExisteParticipacao =
                participacaoRepository.existsByUsuarioAndEvento(usuario, evento);

        if (jaExisteParticipacao) {
            throw new IllegalArgumentException("Usuário já está inscrito neste evento");
        }

        Participacao participacao = new Participacao(usuario, evento);

        return participacaoRepository.save(participacao);
    }

    @Transactional
    public Participacao confirmarPagamento(Long participacaoId) {

        Participacao participacao = participacaoRepository.findById(participacaoId)
                .orElseThrow(() -> new IllegalArgumentException("Participação não encontrada"));

        participacao.confirmarPagamento();

        Participacao salva = participacaoRepository.save(participacao);

        smsService.enviarConfirmacaoPagamento(salva);


        return salva;
    }

    public void realizarCheckin(String qrCode) {
        Long participacaoId = extrairParticipacaoId(qrCode);

        Participacao participacao = participacaoRepository.findById(participacaoId).orElseThrow(()
        -> new IllegalArgumentException("Participação não encontrada"));

        participacao.realizarCheckin();

        participacaoRepository.save(participacao);


    }

    private Long extrairParticipacaoId(String qrCode) {

        if (qrCode == null || qrCode.isBlank()) {
            throw new IllegalArgumentException("QR Code inválido");
        }


        if (!qrCode.startsWith("participacao:")) {
            throw new IllegalArgumentException("QR Code com formato inválido");
        }

        String[] partes = qrCode.split(":");

        if (partes.length != 2) {
            throw new IllegalArgumentException("QR Code malformado");
        }

        try {
            return Long.parseLong(partes[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ID da participação inválido");
        }
    }



    public void validarParaGerarQr(Participacao participacao) {
        if (participacao.getStatusPagamento() != StatusPagamento.PAGO) {
            throw new IllegalArgumentException("Qr code só pode ser gerado com o pagamento pago");
        }

        if (!participacao.getEvento().isAtivo()){
            throw new IllegalArgumentException("Evento não está ativo");
        }

    }

    public byte[] gerarQrCodeParticipacao(Long participacaoId) {
        Participacao participacao = participacaoRepository.findById(participacaoId)
                .orElseThrow(() -> new IllegalArgumentException("Participação não encontrada"));

        Usuario usuarioLogado = authenticatedUserService.getUsuarioLogado();

        if (!participacao.getUsuario().getId().equals(usuarioLogado.getId())){
            throw new SecurityException("Você não tem permissão para gerar este QR Code");
        }

        validarParaGerarQr(participacao);

        String conteudoQr = "participacao:" + participacao.getId();

        return qrCodeService.gerarQrCodePng(conteudoQr);
    }

}
