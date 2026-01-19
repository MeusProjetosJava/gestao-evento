package vitor.gestaoevento.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vitor.gestaoevento.model.Evento;

@Repository
public interface EventoRepository extends JpaRepository<Evento,Long> {
    Long id(Long id);
}
