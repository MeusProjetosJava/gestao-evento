package vitor.gestaoevento.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import vitor.gestaoevento.dto.EventoRequestDTO;
import vitor.gestaoevento.dto.EventoResponseDTO;
import vitor.gestaoevento.model.Evento;
import vitor.gestaoevento.service.EventoService;

import java.util.List;
import java.util.stream.Collectors;
@Tag(name = "Eventos", description = "Gerenciamento de eventos")
@RestController
@RequestMapping("/eventos")
public class EventoController {
    private EventoService eventoService;

    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @Operation(
            summary = "Criar evento",
            description = "Cria um novo evento no sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Evento criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Sem permissão")
    })
    @SecurityRequirement(name = "basicAuth")

    @PostMapping
    public EventoResponseDTO criarEvento(@RequestBody @Valid EventoRequestDTO eventoRequestDTO) {
        Evento evento = eventoService.criarEvento(
                eventoRequestDTO.getNome(), eventoRequestDTO.getDataHoraEvento(), eventoRequestDTO.getLocal(),
                eventoRequestDTO.getAtracao(), eventoRequestDTO.getPreco()
        );

        return new EventoResponseDTO(evento);
    }

    @Operation(
            summary = "Listar eventos ativos",
            description = "Lista todos os eventos com status ATIVO"
    )
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")

    @GetMapping("/ativos")
    public List<EventoResponseDTO> listarEventosAtivos() {
        return eventoService.listarEventosAtivos()
                .stream()
                .map(EventoResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Operation(
            summary = "Buscar evento por ID",
            description = "Retorna os dados de um evento específico"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Evento encontrado"),
            @ApiResponse(responseCode = "404", description = "Evento não encontrado")
    })

    @GetMapping("/{id}")
    public EventoResponseDTO buscarEventoPorId(@PathVariable Long id) {
        Evento evento = eventoService.buscarEventoPorId(id);
        return new EventoResponseDTO(evento);
    }

    @Operation(
            summary = "Encerrar evento",
            description = "Encerra um evento ativo"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Evento encerrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Evento não encontrado"),
            @ApiResponse(responseCode = "400", description = "Evento já encerrado")
    })
    @SecurityRequirement(name = "basicAuth")


    @PatchMapping("/{id}/encerrar")
    public EventoResponseDTO encerrarEventoPorId(@PathVariable Long id) {
        Evento evento = eventoService.encerrarEventoPorId(id);
        return new EventoResponseDTO(evento);
    }
}
