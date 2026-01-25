package vitor.gestaoevento.service;

import org.springframework.stereotype.Service;
import vitor.gestaoevento.model.TipoUsuario;
import vitor.gestaoevento.model.Usuario;
import vitor.gestaoevento.repository.UsuarioRepository;
@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario cadastrarUsuario(String nome, String telefone, String email,
                                              String senha, TipoUsuario tipoUsuario){

        if (usuarioRepository.existsByEmail(email)){
            throw new IllegalArgumentException("Já existe um usuario cadastrado com esse email");
        }

        Usuario usuario = new Usuario(nome,telefone,email,senha,tipoUsuario);

        return usuarioRepository.save(usuario);
    }
}
