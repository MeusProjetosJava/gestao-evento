package vitor.gestaoevento.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vitor.gestaoevento.dto.CheckinRequestDto;
import vitor.gestaoevento.service.RegistrationService;

@Tag(
        name = "Check-ins",
        description = "Event check-in operations"
)
@RestController
@RequestMapping("/check-ins")
public class CheckinController {

    RegistrationService registrationService;

    public CheckinController(RegistrationService RegistrationService) {
        this.registrationService = RegistrationService;
    }

    @Operation(
            summary = "Perform check-in using QR code",
            description = "Allows an ADMIN user to perform the check-in of a registration using a QR code"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Check-in successfully performed"),
            @ApiResponse(responseCode = "400", description = "Business rule violation"),
            @ApiResponse(responseCode = "401", description = "Unauthenticated user"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Registration not found")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> checkIn(@RequestBody @Valid CheckinRequestDto checkinRequestDto) {

        registrationService.checkIn(checkinRequestDto.getQrCode());

        return ResponseEntity.noContent().build();
    }
}
