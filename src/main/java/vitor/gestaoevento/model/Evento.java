package vitor.gestaoevento.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "eventos")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;
    @Column(name = "horario", nullable = false)
    private LocalDateTime dataHoraEvento;
    @Column(name = "localizacao", nullable = false)
    private String local;
    @Column(nullable = false)
    private String atracao;
    @Column(nullable = false)
    private Double preco;
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private StatusEvento status;

    public Evento() {
    }

    public Evento(String nome, LocalDateTime dataHoraEvento, String local,
                  String atracao, Double preco, StatusEvento status) {
        this.nome = nome;
        this.dataHoraEvento = dataHoraEvento;
        this.local = local;
        this.atracao = atracao;
        this.preco = preco;
        this.status = status;
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

    public void encerrar() {
        this.status = StatusEvento.ENCERRADO;
    }

    public boolean isAtivo() {
        return this.status.equals(StatusEvento.ATIVO);
    }
}
