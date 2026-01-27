package vitor.gestaoevento.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vitor.gestaoevento.dto.CheckinRequestDto;
import vitor.gestaoevento.service.ParticipacaoService;

@RestController
@RequestMapping("/checkins")
public class CheckinController {

    ParticipacaoService participacaoService;

    public CheckinController(ParticipacaoService ParticipacaoService) {
        this.participacaoService = ParticipacaoService;
    }

    @Operation(
            summary = "Realizar check-in via QR Code",
            description = "Permite que um ADMIN realize o check-in de uma participação a partir do QR Code"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Check-in realizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Regra de negócio violada"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Sem permissão"),
            @ApiResponse(responseCode = "404", description = "Participação não encontrada")
    })
    @SecurityRequirement(name = "basicAuth")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> realizarChekin(@RequestBody @Valid CheckinRequestDto checkinRequestDto) {
        participacaoService.realizarCheckin(checkinRequestDto.getQrCode());

        return ResponseEntity.noContent().build();
    }
}
