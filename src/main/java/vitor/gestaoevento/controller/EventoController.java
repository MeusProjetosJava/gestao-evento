package vitor.gestaoevento.controller;

import org.springframework.web.bind.annotation.*;
import vitor.gestaoevento.dto.EventoRequestDTO;
import vitor.gestaoevento.dto.EventoResponseDTO;
import vitor.gestaoevento.model.Evento;
import vitor.gestaoevento.service.EventoService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/eventos")
public class EventoController {
    private EventoService eventoService;

    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @PostMapping
    public EventoResponseDTO criarEvento(@RequestBody EventoRequestDTO eventoRequestDTO) {
        Evento evento = eventoService.criarEvento(
                eventoRequestDTO.getNome(), eventoRequestDTO.getDataHoraEvento(), eventoRequestDTO.getLocal(),
                eventoRequestDTO.getAtracao(), eventoRequestDTO.getPreco()
        );

        return new EventoResponseDTO(evento);
    }

    @GetMapping
    public List<EventoResponseDTO> listarEventosAtivos() {
        return eventoService.listarEventosAtivos()
                .stream()
                .map(EventoResponseDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public EventoResponseDTO buscarEventoPorId(@PathVariable Long id) {
        Evento evento = eventoService.buscarEventoPorId(id);
        return new EventoResponseDTO(evento);
    }

    public EventoResponseDTO encerrarEventoPorId(@PathVariable Long id) {
        Evento evento = eventoService.encerrarEventoPorId(id);
        return new EventoResponseDTO(evento);
    }
}
