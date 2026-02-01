package vitor.gestaoevento.dto.auth;

public record LoginRequest(
        String email,
        String password
) {}
