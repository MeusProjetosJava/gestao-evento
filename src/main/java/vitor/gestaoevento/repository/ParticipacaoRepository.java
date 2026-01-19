package vitor.gestaoevento.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vitor.gestaoevento.model.Evento;
import vitor.gestaoevento.model.Participacao;
import vitor.gestaoevento.model.Usuario;

import java.util.Optional;

@Repository
public interface ParticipacaoRepository extends JpaRepository<Participacao,Long> {

    boolean existsByUsuarioAndEvento(Usuario usuario, Evento evento);

    // Busca a participação para check-in
    Optional<Participacao> findByUsuarioAndEvento(Usuario usuario, Evento evento);
}
