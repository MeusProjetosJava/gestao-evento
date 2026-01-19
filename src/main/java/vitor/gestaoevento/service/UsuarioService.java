package vitor.gestaoevento.service;

import org.springframework.stereotype.Service;
import vitor.gestaoevento.model.TipoUsuario;
import vitor.gestaoevento.model.Usuario;
import vitor.gestaoevento.repository.UsuarioRepository;
@Service
public class UsuarioService {
    private final UsuarioRepository userRepo;

    public UsuarioService(UsuarioRepository userRepo) {
        this.userRepo = userRepo;
    }

    public Usuario cadastrarUsuario(String nome, String email,
                                              String senha, TipoUsuario tipoUsuario){

        if (userRepo.existsByEmail(email)){
            throw new IllegalArgumentException("Já existe um usuario cadastrado com esse email");
        }

        Usuario usuario = new Usuario(nome,email,senha,tipoUsuario);

        return userRepo.save(usuario);
    }
}
