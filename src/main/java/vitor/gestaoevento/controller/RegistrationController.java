package vitor.gestaoevento.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vitor.gestaoevento.dto.RegistrationRequestDto;
import vitor.gestaoevento.dto.RegistrationResponseDto;
import vitor.gestaoevento.model.Registration;
import vitor.gestaoevento.service.RegistrationService;

@Tag(
        name = "Registrations",
        description = "Event registration management"
)
@RequestMapping("/registrations")
@RestController
public class RegistrationController {

    RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @Operation(
            summary = "Register for an event",
            description = "Registers the authenticated user for an event"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registration successfully created"),
            @ApiResponse(responseCode = "400", description = "User already registered or invalid event"),
            @ApiResponse(responseCode = "401", description = "Unauthenticated user")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public RegistrationResponseDto registerParticipation(@RequestBody RegistrationRequestDto
                                                                  registrationRequestDto) {
        Registration registration = registrationService.registerParticipation(
                registrationRequestDto.getEventId()
        );

        return new RegistrationResponseDto(registration);
    }

    @Operation(
            summary = "Confirm registration payment",
            description = "Confirms the payment of a registration"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payment successfully confirmed"),
            @ApiResponse(responseCode = "404", description = "Registration not found"),
            @ApiResponse(responseCode = "400", description = "Payment already confirmed")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PatchMapping("/{id}/payment")
    public RegistrationResponseDto confirmPayment(@PathVariable Long id) {
        Registration registration = registrationService.confirmPayment(id);
        return new RegistrationResponseDto(registration);
    }

    @Operation(
            summary = "Generate registration QR code",
            description = "Generates the QR code used for event check-in"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "QR code successfully generated"),
            @ApiResponse(responseCode = "400", description = "Payment not confirmed or inactive event"),
            @ApiResponse(responseCode = "404", description = "Registration not found")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(value = "/{id}/qrcode", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> generateQrCode(@PathVariable Long id) {

        byte[] imagem = registrationService.generateParticipationQrCode(id);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(imagem);
    }


}
