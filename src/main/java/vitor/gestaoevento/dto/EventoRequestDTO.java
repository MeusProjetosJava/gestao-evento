package vitor.gestaoevento.dto;
import java.time.LocalDateTime;

public class EventoRequestDTO {
    private String nome;
    private LocalDateTime dataHoraEvento;
    private String local;
    private String atracao;
    private Double preco;

    public EventoRequestDTO(){}

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
}
