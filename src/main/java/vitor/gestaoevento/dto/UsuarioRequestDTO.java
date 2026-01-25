package vitor.gestaoevento.dto;
import vitor.gestaoevento.model.TipoUsuario;

public class UsuarioRequestDTO {
    private String nome;
    private String telefone;
    private String email;
    private String senha;
    private TipoUsuario tipoUsuario;

    public UsuarioRequestDTO() {}

    public UsuarioRequestDTO(String nome, String telefone, String email, String senha, TipoUsuario tipoUsuario) {
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.senha = senha;
        this.tipoUsuario = tipoUsuario;
    }

    public String getNome() {
        return nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }
}


