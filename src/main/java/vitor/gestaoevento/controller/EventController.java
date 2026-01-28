package vitor.gestaoevento.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vitor.gestaoevento.dto.EventRequestDto;
import vitor.gestaoevento.dto.EventResponseDto;
import vitor.gestaoevento.model.Event;
import vitor.gestaoevento.service.EventService;

import java.util.List;
import java.util.stream.Collectors;

@Tag(
        name = "Events",
        description = "Event management endpoints"
)
@RestController
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @Operation(
            summary = "Create event",
            description = "Creates a new event in the system"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Event successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthenticated user"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @SecurityRequirement(name = "basicAuth")

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public EventResponseDto createEvent(@RequestBody @Valid EventRequestDto eventRequestDto) {
        Event event = eventService.createEvent(
                eventRequestDto.getName(), eventRequestDto.getEventDateTime(), eventRequestDto.getLocation(),
                eventRequestDto.getAttraction(), eventRequestDto.getPrice()
        );

        return new EventResponseDto(event);
    }

    @Operation(
            summary = "List active events",
            description = "Returns all events with ACTIVE status"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Active events successfully retrieved"
    )
    @GetMapping("/active")
    public List<EventResponseDto> listActiveEvents() {
        return eventService.listActiveEvents()
                .stream()
                .map(EventResponseDto::new)
                .collect(Collectors.toList());
    }

    @Operation(
            summary = "Get event by ID",
            description = "Returns the details of a specific event"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Event found"),
            @ApiResponse(responseCode = "404", description = "Event not found")
    })
    @GetMapping("/{id}")
    public EventResponseDto getEventById(@PathVariable Long id) {
        Event event = eventService.getEventById(id);
        return new EventResponseDto(event);
    }

    @Operation(
            summary = "Close event",
            description = "Closes an active event"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Event successfully closed"),
            @ApiResponse(responseCode = "404", description = "Event not found"),
            @ApiResponse(responseCode = "400", description = "Event is already closed")
    })
    @SecurityRequirement(name = "basicAuth")
    @PatchMapping("/{id}/close")
    public EventResponseDto closeEventById(@PathVariable Long id) {
        Event event = eventService.closeEventById(id);
        return new EventResponseDto(event);
    }
}
