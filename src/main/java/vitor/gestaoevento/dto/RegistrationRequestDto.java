package vitor.gestaoevento.dto;

import jakarta.validation.constraints.NotNull;

public class RegistrationRequestDto {
    @NotNull
    private Long eventId;

    public Long getEventId() {
        return eventId;
    }
}
