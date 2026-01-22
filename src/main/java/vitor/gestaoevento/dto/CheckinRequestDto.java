package vitor.gestaoevento.dto;

public class CheckinRequestDto {

    private Long usuarioId;
    private Long eventoId;

    public CheckinRequestDto() {}

    public Long getUsuarioId() {
        return usuarioId;
    }

    public Long getEventoId() {
        return eventoId;
    }


}
