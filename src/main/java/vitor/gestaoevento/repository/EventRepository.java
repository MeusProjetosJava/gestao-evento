package vitor.gestaoevento.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vitor.gestaoevento.model.Event;
import vitor.gestaoevento.model.EventStatus;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event,Long> {
    List<Event> findByStatus(EventStatus status);
}
