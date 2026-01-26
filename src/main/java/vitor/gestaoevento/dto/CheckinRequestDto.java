package vitor.gestaoevento.dto;

import jakarta.validation.constraints.NotBlank;
import vitor.gestaoevento.model.Participacao;

public class CheckinRequestDto {
    @NotBlank(message = "Qr code é obrigatório")
    private String qrCode;

    public String getQrCode() {
        return qrCode;
    }
}
