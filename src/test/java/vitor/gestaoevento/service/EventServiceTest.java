package vitor.gestaoevento.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vitor.gestaoevento.exception.AccessDeniedException;
import vitor.gestaoevento.exception.EventNotFoundException;
import vitor.gestaoevento.model.Event;
import vitor.gestaoevento.model.EventStatus;
import vitor.gestaoevento.model.User;
import vitor.gestaoevento.model.UserType;
import vitor.gestaoevento.repository.EventRepository;
import vitor.gestaoevento.security.AuthenticatedUserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private AuthenticatedUserService authenticatedUserService;

    @InjectMocks
    private EventService eventService;

    @Test
    @DisplayName("Should create event when authenticated user is admin")
    void shouldCreateEventWhenAuthenticatedUserIsAdmin() {

        // Arrange
        User adminUser = new User(
                "Admin User",
                "85999999999",
                "admin@email.com",
                "encoded-password",
                UserType.ADMIN
        );

        when(authenticatedUserService.getAuthenticatedUser()).thenReturn(adminUser);

        LocalDateTime eventDateTime = LocalDateTime.now().plusDays(10);

        Event savedEvent = new Event(
                "Tech Conference",
                eventDateTime,
                "Convention Center",
                "Keynote Speaker",
                100.0,
                EventStatus.ACTIVE
        );

        when(eventRepository.save(any(Event.class))).thenReturn(savedEvent);

        Event result = eventService.createEvent(
                "Tech Conference",
                eventDateTime,
                "Convention Center",
                "Keynote Speaker",
                100.0
        );

        assertNotNull(result);
        assertEquals(EventStatus.ACTIVE, result.getStatus());
        assertEquals("Tech Conference", result.getName());

        verify(authenticatedUserService).getAuthenticatedUser();
        verify(eventRepository).save(any(Event.class));
        verifyNoMoreInteractions(eventRepository, authenticatedUserService);
    }

    @Test
    @DisplayName("Should throw exception when non-admin user tries to create event")
    void shouldThrowExceptionWhenNonAdminUserTriesToCreateEvent() {

        User regularUser = new User(
                "Regular User",
                "85999999999",
                "user@email.com",
                "encoded-password",
                UserType.USER
        );

        when(authenticatedUserService.getAuthenticatedUser()).thenReturn(regularUser);

        AccessDeniedException exception = assertThrows(
                AccessDeniedException.class,
                () -> eventService.createEvent(
                        "Tech Conference",
                        LocalDateTime.now().plusDays(5),
                        "Convention Center",
                        "Keynote Speaker",
                        100.0
                )
        );

        assertEquals("Only admins can create events", exception.getMessage());

        verify(authenticatedUserService).getAuthenticatedUser();
        verifyNoInteractions(eventRepository);
    }

    @Test
    @DisplayName("Should return only active events")
    void shouldReturnOnlyActiveEvents() {

        Event event1 = new Event(
                "Active Event",
                LocalDateTime.now().plusDays(1),
                "Location A",
                "Attraction A",
                50.0,
                EventStatus.ACTIVE
        );

        when(eventRepository.findByStatus(EventStatus.ACTIVE))
                .thenReturn(List.of(event1));


        List<Event> result = eventService.listActiveEvents();

        assertEquals(1, result.size());
        assertEquals(EventStatus.ACTIVE, result.get(0).getStatus());

        verify(eventRepository).findByStatus(EventStatus.ACTIVE);
        verifyNoMoreInteractions(eventRepository);
    }

    @Test
    @DisplayName("Should return event when event exists")
    void shouldReturnEventWhenEventExists() {

        // Arrange
        Event event = new Event(
                "Sample Event",
                LocalDateTime.now().plusDays(2),
                "Location",
                "Attraction",
                80.0,
                EventStatus.ACTIVE
        );

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));


        Event result = eventService.getEventById(1L);

        assertNotNull(result);
        assertEquals("Sample Event", result.getName());

        verify(eventRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when event does not exist")
    void shouldThrowExceptionWhenEventDoesNotExist() {


        when(eventRepository.findById(1L)).thenReturn(Optional.empty());


        EventNotFoundException exception = assertThrows(
                EventNotFoundException.class,
                () -> eventService.getEventById(1L)
        );

        assertEquals("Evento não encontrado", exception.getMessage());

        verify(eventRepository).findById(1L);
    }

    @Test
    @DisplayName("Should close event when event is active")
    void shouldCloseEventWhenEventIsActive() {


        Event event = new Event(
                "Event to Close",
                LocalDateTime.now().plusDays(3),
                "Location",
                "Attraction",
                120.0,
                EventStatus.ACTIVE
        );

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenReturn(event);


        Event result = eventService.closeEventById(1L);


        assertEquals(EventStatus.CLOSED, result.getStatus());

        verify(eventRepository).findById(1L);
        verify(eventRepository).save(event);
    }
}
