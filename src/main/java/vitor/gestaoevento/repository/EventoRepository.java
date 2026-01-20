package vitor.gestaoevento.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vitor.gestaoevento.model.Evento;
import vitor.gestaoevento.model.StatusEvento;
import vitor.gestaoevento.model.Usuario;

import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<Evento,Long> {
    List<Evento> findByStatus(StatusEvento status);
}
