package vitor.gestaoevento.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vitor.gestaoevento.exception.UsuarioJaCadastradoException;
import vitor.gestaoevento.model.TipoUsuario;
import vitor.gestaoevento.model.Usuario;
import vitor.gestaoevento.repository.UsuarioRepository;
@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario cadastrarUsuario(String nome, String telefone, String email,
                                              String senha, TipoUsuario tipoUsuario){

        if (usuarioRepository.existsByEmail(email)){
            throw new UsuarioJaCadastradoException("Email do usuário já foi cadastrado");
        }

        String senhaCriptografada = passwordEncoder.encode(senha);

        Usuario usuario = new Usuario(nome,telefone,email,senhaCriptografada,tipoUsuario);

        return usuarioRepository.save(usuario);
    }
}
