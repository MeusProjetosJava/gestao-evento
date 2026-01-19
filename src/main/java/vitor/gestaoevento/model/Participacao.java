package vitor.gestaoevento.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "participacoes",uniqueConstraints = {@UniqueConstraint(columnNames = {"usuario_id",
"evento_id"})})
public class Participacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "evento_id")
    private Evento evento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPagamento statusPagamento;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusCheckin statusCheckin;
    private LocalDateTime dataCheckin;

    protected Participacao(){}

    public Participacao(Usuario usuario, Evento evento) {
        this.usuario = usuario;
        this.evento = evento;
        this.statusPagamento = StatusPagamento.PENDENTE;
        this.statusCheckin = StatusCheckin.NAO_REALIZADO;
    }

    public Long getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Evento getEvento() {
        return evento;
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

    public boolean podeFazerChecKing(){
        return StatusPagamento.PAGO.equals(this.statusPagamento)
                && StatusCheckin.NAO_REALIZADO.equals(this.statusCheckin)
                && evento.isAtivo();
    }

    public void confirmarPagamento() {
        this.statusPagamento = StatusPagamento.PAGO;
    }

    public void realizarCheckin(){
        if(!podeFazerChecKing()){
            throw new IllegalArgumentException("Checkin não permitido");
        }
        this.statusCheckin = StatusCheckin.REALIZADO;
        this.dataCheckin = LocalDateTime.now();
    }
}
