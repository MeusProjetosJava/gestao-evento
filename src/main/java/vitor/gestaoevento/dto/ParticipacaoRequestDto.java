package vitor.gestaoevento.dto;

public class ParticipacaoRequestDto {
    private Long usuarioId;
    private Long eventoId;

    public ParticipacaoRequestDto(){}

    public Long getUsuarioId() {
        return usuarioId;
    }

    public Long getEventoId() {
        return eventoId;
    }
}
