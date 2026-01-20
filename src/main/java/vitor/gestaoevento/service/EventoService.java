package vitor.gestaoevento.service;

import org.springframework.stereotype.Service;
import vitor.gestaoevento.model.Evento;
import vitor.gestaoevento.model.StatusEvento;
import vitor.gestaoevento.repository.EventoRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;

    public EventoService(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    public Evento criarEvento(String nome, LocalDateTime dataHoraEvento,
                              String local, String atracao, Double preco) {

        Evento evento = new Evento(nome, dataHoraEvento, local, atracao, preco, StatusEvento.ATIVO);

        return eventoRepository.save(evento);
    }

    public List<Evento> listarEventosAtivos() {
        return eventoRepository.findByStatus(StatusEvento.ATIVO);
    }

    public Evento buscarEventoPorId(Long eventoId) {
        return eventoRepository.findById(eventoId).orElseThrow(() ->
                new IllegalArgumentException("Evento não encontrado"));
    }

    public Evento encerrarEventoPorId(Long eventoId) {
        Evento evento = buscarEventoPorId(eventoId);
        evento.encerrar();
        return eventoRepository.save(evento);
    }
}
