package vitor.gestaoevento.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vitor.gestaoevento.exception.UserAlreadyRegisteredException;
import vitor.gestaoevento.model.UserType;
import vitor.gestaoevento.model.User;
import vitor.gestaoevento.repository.UserRepository;
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(String nome, String telefone, String email,
                             String senha, UserType userType){

        if (userRepository.existsByEmail(email)){
            throw new UserAlreadyRegisteredException("Email do usuário já foi cadastrado");
        }

        String senhaCriptografada = passwordEncoder.encode(senha);

        User user = new User(nome,telefone,email,senhaCriptografada, userType);

        return userRepository.save(user);
    }
}
