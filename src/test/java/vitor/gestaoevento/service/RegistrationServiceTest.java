package vitor.gestaoevento.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import vitor.gestaoevento.exception.*;
import vitor.gestaoevento.integration.infra.QrCodeService;
import vitor.gestaoevento.integration.sms.SmsService;
import vitor.gestaoevento.model.*;
import vitor.gestaoevento.repository.EventRepository;
import vitor.gestaoevento.repository.RegistrationRepository;
import vitor.gestaoevento.repository.UserRepository;
import vitor.gestaoevento.security.AuthenticatedUserService;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private RegistrationRepository registrationRepository;

    @Mock
    private SmsService smsService;

    @Mock
    private QrCodeService qrCodeService;

    @Mock
    private AuthenticatedUserService authenticatedUserService;

    @InjectMocks
    private RegistrationService registrationService;

    @Test
    @DisplayName("Should register participation when event is active and user is not enrolled")
    void shouldRegisterParticipationWhenEventIsActiveAndUserIsNotEnrolled() {

        User user = new User(
                "John",
                "85999999999",
                "john@email.com",
                "encoded-password",
                UserType.USER
        );
        ReflectionTestUtils.setField(user, "id", 1L);

        Event event = new Event(
                "Tech Event",
                LocalDateTime.now().plusDays(5),
                "Location",
                "Attraction",
                100.0,
                EventStatus.ACTIVE
        );

        when(authenticatedUserService.getAuthenticatedUser()).thenReturn(user);
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(registrationRepository.existsByUserAndEvent(user, event)).thenReturn(false);

        Registration savedRegistration = new Registration(user, event);
        ReflectionTestUtils.setField(savedRegistration, "id", 1L);

        when(registrationRepository.save(any(Registration.class)))
                .thenReturn(savedRegistration);

        Registration result = registrationService.registerParticipation(1L);

        assertNotNull(result);
        assertEquals(user, result.getUser());
        assertEquals(event, result.getEvent());
        assertEquals(PaymentStatus.PENDING, result.getPaymentStatus());

        verify(authenticatedUserService).getAuthenticatedUser();
        verify(eventRepository).findById(1L);
        verify(registrationRepository).existsByUserAndEvent(user, event);
        verify(registrationRepository).save(any(Registration.class));
    }

    @Test
    @DisplayName("Should throw exception when user tries to register in inactive event")
    void shouldThrowExceptionWhenEventIsInactive() {

        User user = new User(
                "John",
                "85999999999",
                "john@email.com",
                "encoded-password",
                UserType.USER
        );
        ReflectionTestUtils.setField(user, "id", 1L);

        Event inactiveEvent = new Event(
                "Inactive Event",
                LocalDateTime.now().minusDays(1),
                "Location",
                "Attraction",
                80.0,
                EventStatus.CLOSED
        );

        when(authenticatedUserService.getAuthenticatedUser()).thenReturn(user);
        when(eventRepository.findById(1L)).thenReturn(Optional.of(inactiveEvent));

        InactiveEventException exception = assertThrows(
                InactiveEventException.class,
                () -> registrationService.registerParticipation(1L)
        );

        assertEquals("O evento não está ativo", exception.getMessage());

        verify(eventRepository).findById(1L);
        verifyNoInteractions(registrationRepository);
    }

    @Test
    @DisplayName("Should throw exception when user is already enrolled in event")
    void shouldThrowExceptionWhenUserIsAlreadyEnrolled() {

        User user = new User(
                "John",
                "85999999999",
                "john@email.com",
                "encoded-password",
                UserType.USER
        );
        ReflectionTestUtils.setField(user, "id", 1L);

        Event event = new Event(
                "Tech Event",
                LocalDateTime.now().plusDays(3),
                "Location",
                "Attraction",
                100.0,
                EventStatus.ACTIVE
        );

        when(authenticatedUserService.getAuthenticatedUser()).thenReturn(user);
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(registrationRepository.existsByUserAndEvent(user, event)).thenReturn(true);

        UserAlreadyEnrolledException exception = assertThrows(
                UserAlreadyEnrolledException.class,
                () -> registrationService.registerParticipation(1L)
        );

        assertEquals("Usuário já está inscrito neste evento", exception.getMessage());

        verify(registrationRepository).existsByUserAndEvent(user, event);
        verify(registrationRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should confirm payment and send SMS notification")
    void shouldConfirmPaymentAndSendSmsNotification() {

        User user = new User(
                "John",
                "85999999999",
                "john@email.com",
                "encoded-password",
                UserType.USER
        );
        ReflectionTestUtils.setField(user, "id", 1L);

        Event event = new Event(
                "Tech Event",
                LocalDateTime.now().plusDays(2),
                "Location",
                "Attraction",
                120.0,
                EventStatus.ACTIVE
        );

        Registration registration = new Registration(user, event);
        ReflectionTestUtils.setField(registration, "id", 1L);

        when(registrationRepository.findById(1L))
                .thenReturn(Optional.of(registration));

        when(registrationRepository.save(any(Registration.class)))
                .thenReturn(registration);

        Registration result = registrationService.confirmPayment(1L);

        assertEquals(PaymentStatus.PAID, result.getPaymentStatus());

        verify(registrationRepository).findById(1L);
        verify(registrationRepository).save(registration);
        verify(smsService).sendPaymentConfirmation(registration);
    }

    @Test
    @DisplayName("Should perform check-in when QR code is valid")
    void shouldPerformCheckInWhenQrCodeIsValid() {

        User user = new User(
                "John",
                "85999999999",
                "john@email.com",
                "encoded-password",
                UserType.USER
        );
        ReflectionTestUtils.setField(user, "id", 1L);

        Event event = new Event(
                "Tech Event",
                LocalDateTime.now().plusDays(2),
                "Location",
                "Attraction",
                120.0,
                EventStatus.ACTIVE
        );

        Registration registration = new Registration(user, event);
        ReflectionTestUtils.setField(registration, "id", 1L);
        registration.confirmPayment(); // regra de domínio respeitada

        when(registrationRepository.findById(1L))
                .thenReturn(Optional.of(registration));

        registrationService.checkIn("participacao:1");

        assertEquals(CheckInStatus.COMPLETED, registration.getCheckInStatus());

        verify(registrationRepository).save(registration);
    }

    @Test
    @DisplayName("Should generate QR code when user is owner and payment is confirmed")
    void shouldGenerateQrCodeWhenUserIsOwnerAndPaymentIsConfirmed() {

        User user = new User(
                "John",
                "85999999999",
                "john@email.com",
                "encoded-password",
                UserType.USER
        );
        ReflectionTestUtils.setField(user, "id", 1L);

        Event event = new Event(
                "Tech Event",
                LocalDateTime.now().plusDays(2),
                "Location",
                "Attraction",
                120.0,
                EventStatus.ACTIVE
        );

        Registration registration = new Registration(user, event);
        ReflectionTestUtils.setField(registration, "id", 1L);
        registration.confirmPayment();

        when(registrationRepository.findById(1L))
                .thenReturn(Optional.of(registration));

        when(authenticatedUserService.getAuthenticatedUser())
                .thenReturn(user);

        byte[] qrCodeBytes = new byte[]{1, 2, 3};
        when(qrCodeService.generateQrCodePng("participacao:" + registration.getId()))
                .thenReturn(qrCodeBytes);

        byte[] result = registrationService.generateParticipationQrCode(1L);

        assertNotNull(result);
        assertArrayEquals(qrCodeBytes, result);

        verify(qrCodeService).generateQrCodePng("participacao:" + registration.getId());
    }
}
