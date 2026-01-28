package vitor.gestaoevento.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vitor.gestaoevento.model.Event;
import vitor.gestaoevento.model.Registration;
import vitor.gestaoevento.model.User;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration,Long> {

    boolean existsByUserAndEvent(User user, Event event);

}
