package vitor.gestaoevento.dto;
import vitor.gestaoevento.model.Participacao;
import vitor.gestaoevento.model.StatusCheckin;
import vitor.gestaoevento.model.StatusPagamento;
import java.time.LocalDateTime;

public class ParticipacaoResponseDto {

    private Long id;
    private Long usuarioId;
    private Long eventoId;
    private StatusPagamento statusPagamento;
    private StatusCheckin statusCheckin;
    private LocalDateTime dataCheckin;

    public ParticipacaoResponseDto(Participacao participacao) {
        this.id = participacao.getId();
        this.usuarioId = participacao.getUsuario().getId();
        this.eventoId = participacao.getEvento().getId();
        this.statusPagamento = participacao.getStatusPagamento();
        this.statusCheckin = participacao.getStatusCheckin();
        this.dataCheckin = participacao.getDataCheckin();
    }

    public Long getId() {
        return id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public Long getEventoId() {
        return eventoId;
    }

    public StatusPagamento getStatusPagamento() {
        return statusPagamento;
    }

    public StatusCheckin getStatusCheckin() {
        return statusCheckin;
    }

    public LocalDateTime getDataCheckin() {
        return dataCheckin;
    }
}
