package vitor.gestaoevento.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vitor.gestaoevento.dto.UsuarioRequestDTO;
import vitor.gestaoevento.dto.UsuarioResponseDTO;
import vitor.gestaoevento.model.Usuario;
import vitor.gestaoevento.service.UsuarioService;
@Tag(name = "Usuários", description = "Cadastro de usuários")
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Operation(
            summary = "Criar usuário",
            description = "Cadastra um novo usuário no sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Usuário já cadastrado ou dados inválidos")
    })

    @PostMapping
    public UsuarioResponseDTO criarUsuario(@RequestBody UsuarioRequestDTO usuarioRequestDTO) {
        Usuario usuario = usuarioService.cadastrarUsuario(usuarioRequestDTO.getNome(),
                usuarioRequestDTO.getTelefone(),
                usuarioRequestDTO.getEmail(), usuarioRequestDTO.getSenha(),
                usuarioRequestDTO.getTipoUsuario());

        return new UsuarioResponseDTO(usuario);
    }
}
