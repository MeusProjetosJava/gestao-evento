package vitor.gestaoevento.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RegistrationTest {

    private User user;
    private Event event;
    private Registration registration;

    @BeforeEach
    void  setUp() {
    user = new User("Carlos","+55988888888","vc@gmail.com", "123456",UserType.ADMIN);

    event = new Event("Show de Rock", LocalDateTime.of(2023, 10, 27, 10, 15, 30),
            "Arena Castelão","Xandy",80.0,EventStatus.ACTIVE);

    registration = new Registration(user,event);

    }


    @DisplayName("Should return true when event is active and payment is paid and check-in is not completed")
    @Test
    void shouldReturnTrueWhenEventIsActiveAndPaymentIsPaidAndCheckInIsNotCompleted() {

        registration.confirmPayment();

        assertTrue(registration.canCheckIn());
    }

    @Test
    void shouldSetPaymentStatusToPaidWhenConfirmPaymentIsCalled() {

        registration.confirmPayment();

        assertEquals(PaymentStatus.PAID, registration.getPaymentStatus());

    }
    @DisplayName("Should perform check in when conditions are met")
    @Test
    void shouldPerformCheckInWhenConditionsAreMet() {
        registration.confirmPayment();
        registration.performCheckIn();

        assertEquals(CheckInStatus.COMPLETED, registration.getCheckInStatus());
        assertNotNull(registration.getCheckInDate());
    }
}