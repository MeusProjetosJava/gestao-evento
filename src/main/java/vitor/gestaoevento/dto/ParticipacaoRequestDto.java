package vitor.gestaoevento.dto;

import jakarta.validation.constraints.NotNull;

public class ParticipacaoRequestDto {
    @NotNull
    private Long eventoId;

    public Long getEventoId() {
        return eventoId;
    }
}
