package vitor.gestaoevento.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vitor.gestaoevento.integration.sms.SmsService;
import vitor.gestaoevento.model.Evento;
import vitor.gestaoevento.model.Participacao;
import vitor.gestaoevento.model.Usuario;
import vitor.gestaoevento.repository.EventoRepository;
import vitor.gestaoevento.repository.ParticipacaoRepository;
import vitor.gestaoevento.repository.UsuarioRepository;

@Slf4j
@Service
public class ParticipacaoService {

    private final UsuarioRepository usuarioRepository;
    private final EventoRepository eventoRepository;
    private final ParticipacaoRepository participacaoRepository;
    private final SmsService smsService;

    public ParticipacaoService(UsuarioRepository usuarioRepository,
                               EventoRepository eventoRepository,
                               ParticipacaoRepository participacaoRepository,
                               SmsService smsService) {
        this.usuarioRepository = usuarioRepository;
        this.eventoRepository = eventoRepository;
        this.participacaoRepository = participacaoRepository;
        this.smsService = smsService;
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

    public Participacao realizarCheckin(Long usuarioId, Long eventoId) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new IllegalArgumentException("Evento não encontrado"));

        Participacao participacao = participacaoRepository
                .findByUsuarioAndEvento(usuario, evento)
                .orElseThrow(() -> new IllegalArgumentException("Participação não encontrada"));

        participacao.realizarCheckin();

        return participacaoRepository.save(participacao);
    }
}
