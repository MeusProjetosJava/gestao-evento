package vitor.gestaoevento.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "registrations",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "event_id"})
        }
)
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CheckInStatus checkInStatus;
    private LocalDateTime checkInDate;

    protected Registration() {
    }

    public Registration(User user, Event event) {
        this.user = user;
        this.event = event;
        this.paymentStatus = PaymentStatus.PENDING;
        this.checkInStatus = CheckInStatus.NOT_COMPLETED;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Event getEvent() {
        return event;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public CheckInStatus getCheckInStatus() {
        return checkInStatus;
    }

    public LocalDateTime getCheckInDate() {
        return checkInDate;
    }

    public boolean canCheckIn() {
        return PaymentStatus.PAID.equals(this.paymentStatus)
                && CheckInStatus.NOT_COMPLETED.equals(this.checkInStatus)
                && event.isActive();
    }

    public void confirmPayment() {
        if (this.paymentStatus == PaymentStatus.PAID) {
            return;
        }
        this.paymentStatus = PaymentStatus.PAID;
    }


    public boolean isPaid() {
        return this.paymentStatus == PaymentStatus.PAID;
    }


    public void performCheckIn() {
        if (!canCheckIn()) {
            throw new IllegalArgumentException("Checkin não permitido");
        }
        this.checkInStatus = CheckInStatus.COMPLETED;
        this.checkInDate = LocalDateTime.now();
    }
}
