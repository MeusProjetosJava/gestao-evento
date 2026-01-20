package vitor.gestaoevento.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vitor.gestaoevento.dto.UsuarioRequestDTO;
import vitor.gestaoevento.dto.UsuarioResponseDTO;
import vitor.gestaoevento.model.Usuario;
import vitor.gestaoevento.service.UsuarioService;
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public UsuarioResponseDTO criarUsuario(@RequestBody UsuarioRequestDTO usuarioRequestDTO) {
        Usuario usuario = usuarioService.cadastrarUsuario(usuarioRequestDTO.getNome(),
                usuarioRequestDTO.getEmail(), usuarioRequestDTO.getSenha(),
                usuarioRequestDTO.getTipoUsuario());

        return new UsuarioResponseDTO(usuario);
    }
}
