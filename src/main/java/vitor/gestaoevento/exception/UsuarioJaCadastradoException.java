package vitor.gestaoevento.exception;

public class UsuarioJaCadastrado extends RuntimeException {
  public UsuarioJaCadastrado(String message) {
    super(message);
  }
}
