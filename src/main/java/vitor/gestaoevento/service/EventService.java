package vitor.gestaoevento.service;

import org.springframework.stereotype.Service;
import vitor.gestaoevento.exception.AccessDeniedException;
import vitor.gestaoevento.exception.EventNotFoundException;
import vitor.gestaoevento.model.Event;
import vitor.gestaoevento.model.EventStatus;
import vitor.gestaoevento.model.User;
import vitor.gestaoevento.repository.EventRepository;
import vitor.gestaoevento.security.AuthenticatedUserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final AuthenticatedUserService authenticatedUserService;

    public EventService(EventRepository eventRepository, AuthenticatedUserService authenticatedUserService) {
        this.eventRepository = eventRepository;
        this.authenticatedUserService = authenticatedUserService;
    }

    public Event createEvent(String name, LocalDateTime eventDateTime,
                             String location, String attraction, Double price) {

        User user = authenticatedUserService.getAuthenticatedUser();

        if (!user.isAdmin()) {
            throw new AccessDeniedException("Only admins can create events");
        }


        Event event = new Event(name, eventDateTime, location, attraction, price, EventStatus.ACTIVE);

        return eventRepository.save(event);
    }

    public List<Event> listActiveEvents() {
        return eventRepository.findByStatus(EventStatus.ACTIVE);
    }

    public Event getEventById(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
               new EventNotFoundException("Evento não encontrado"));
    }

    public Event closeEventById(Long eventoId) {
        Event event = getEventById(eventoId);
        event.close();
        return eventRepository.save(event);
    }
}
