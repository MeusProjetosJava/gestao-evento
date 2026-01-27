package vitor.gestaoevento.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vitor.gestaoevento.exception.*;
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

    public Participacao cadastrarParticipacao(Long eventoId) {

        Usuario usuarioLogado = authenticatedUserService.getUsuarioLogado();

        log.info("USUARIO LOGADO -> ID: {} | EMAIL: {}",
                usuarioLogado.getId(),
                usuarioLogado.getEmail());

        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new EventoNaoEncontradoException("Evento não encontrado"));

        if (!evento.isAtivo()) {
            throw new EventoInativoException("O evento não está ativo");
        }

        boolean jaExisteParticipacao =
                participacaoRepository.existsByUsuarioAndEvento(usuarioLogado, evento);

        if (jaExisteParticipacao) {
            throw new UsuarioJaInscritoException("Usuário já está inscrito neste evento");
        }

        Participacao participacao = new Participacao(usuarioLogado, evento);

        return participacaoRepository.save(participacao);
    }

    @Transactional
    public Participacao confirmarPagamento(Long participacaoId) {

        Participacao participacao = participacaoRepository.findById(participacaoId)
                .orElseThrow(() -> new ParticipacaoNaoEncontradaException("Participação não encontrada"));

        participacao.confirmarPagamento();

        Participacao salva = participacaoRepository.save(participacao);

        smsService.enviarConfirmacaoPagamento(salva);


        return salva;
    }

    public void realizarCheckin(String qrCode) {
        Long participacaoId = extrairParticipacaoId(qrCode);

        Participacao participacao = participacaoRepository.findById(participacaoId).orElseThrow(()
        -> new ParticipacaoNaoEncontradaException("Participação não encontrada"));

        participacao.realizarCheckin();

        participacaoRepository.save(participacao);


    }

    private Long extrairParticipacaoId(String qrCode) {

        if (qrCode == null || qrCode.isBlank()) {
            throw new QrCodeInvalidoException("QR Code inválido");
        }


        if (!qrCode.startsWith("participacao:")) {
            throw new QrCodeInvalidoException("QR Code com formato inválido");
        }

        String[] partes = qrCode.split(":");

        if (partes.length != 2) {
            throw new QrCodeInvalidoException("QR Code malformado");
        }

        try {
            return Long.parseLong(partes[1]);
        } catch (NumberFormatException e) {
            throw new QrCodeInvalidoException("ID da participação inválido");
        }
    }



    public void validarParaGerarQr(Participacao participacao) {
        if (participacao.getStatusPagamento() != StatusPagamento.PAGO) {
            throw new QrCodeInvalidoException("Qr code só pode ser gerado com o pagamento pago");
        }

        if (!participacao.getEvento().isAtivo()){
            throw new EventoInativoException("Evento não está ativo");
        }

    }

    public byte[] gerarQrCodeParticipacao(Long participacaoId) {
        Participacao participacao = participacaoRepository.findById(participacaoId)
                .orElseThrow(() -> new ParticipacaoNaoEncontradaException("Participação não encontrada"));

        Usuario usuarioLogado = authenticatedUserService.getUsuarioLogado();

        if (!participacao.getUsuario().getId().equals(usuarioLogado.getId())){
            throw new SecurityException("Você não tem permissão para gerar este QR Code");
        }

        validarParaGerarQr(participacao);

        String conteudoQr = "participacao:" + participacao.getId();

        return qrCodeService.gerarQrCodePng(conteudoQr);
    }

}
