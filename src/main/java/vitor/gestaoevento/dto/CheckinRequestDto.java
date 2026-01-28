package vitor.gestaoevento.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Requisição para realizar check-in via QR Code")
public class CheckinRequestDto {

    @Schema(
            description = "Texto extraído do QR Code",
            example = "participacao:24"
    )

    @NotBlank(message = "Qr code é obrigatório")
    private String qrCode;

    public String getQrCode() {
        return qrCode;
    }
}
