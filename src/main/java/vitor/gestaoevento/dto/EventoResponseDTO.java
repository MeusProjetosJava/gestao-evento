package vitor.gestaoevento.dto;
import vitor.gestaoevento.model.Evento;
import vitor.gestaoevento.model.StatusEvento;

import java.time.LocalDateTime;

public class EventoResponseDTO {
    private Long id;
    private String nome;
    private LocalDateTime dataHoraEvento;
    private String local;
    private String atracao;
    private Double preco;
    private StatusEvento status;

    public EventoResponseDTO(Evento evento) {
        this.id = evento.getId();
        this.nome = evento.getNome();
        this.dataHoraEvento = evento.getDataHoraEvento();
        this.local = evento.getLocal();
        this.atracao = evento.getAtracao();
        this.preco = evento.getPreco();
        this.status = evento.getStatus();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public LocalDateTime getDataHoraEvento() {
        return dataHoraEvento;
    }

    public String getLocal() {
        return local;
    }

    public String getAtracao() {
        return atracao;
    }

    public Double getPreco() {
        return preco;
    }

    public StatusEvento getStatus() {
        return status;
    }
}
