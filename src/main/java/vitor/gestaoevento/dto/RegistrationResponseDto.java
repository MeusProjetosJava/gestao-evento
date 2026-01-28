package vitor.gestaoevento.dto;

import vitor.gestaoevento.model.Registration;
import vitor.gestaoevento.model.CheckInStatus;
import vitor.gestaoevento.model.PaymentStatus;

import java.time.LocalDateTime;

public class RegistrationResponseDto {

    private Long id;
    private Long userId;
    private Long eventId;
    private PaymentStatus paymentStatus;
    private CheckInStatus checkInStatus;
    private LocalDateTime checkInDateTime;

    public RegistrationResponseDto(Registration registration) {
        this.id = registration.getId();
        this.userId = registration.getUser().getId();
        this.eventId = registration.getEvent().getId();
        this.paymentStatus = registration.getPaymentStatus();
        this.checkInStatus = registration.getCheckInStatus();
        this.checkInDateTime = registration.getDataCheckin();
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getEventId() {
        return eventId;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public CheckInStatus getCheckInStatus() {
        return checkInStatus;
    }

    public LocalDateTime getCheckInDateTime() {
        return checkInDateTime;
    }
}
