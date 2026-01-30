package vitor.gestaoevento.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import vitor.gestaoevento.exception.UserAlreadyRegisteredException;
import vitor.gestaoevento.model.User;
import vitor.gestaoevento.model.UserType;
import vitor.gestaoevento.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private final String name = "John";
    private final String phone = "85999999999";
    private final String email = "john@email.com";
    private final String rawPassword = "123456";
    private final UserType userType = UserType.USER;

    @BeforeEach
    void setup() {
    }

    @Test
    @DisplayName("Should register user with encrypted password when email does not exist")
    void shouldRegisterUserWithEncryptedPasswordWhenEmailDoesNotExist() {

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(passwordEncoder.encode(rawPassword)).thenReturn("encoded-password");

        User savedUser = new User(
                name,
                phone,
                email,
                "encoded-password",
                userType
        );

        when(userRepository.save(any(User.class))).thenReturn(savedUser);


        User result = userService.registerUser(
                name,
                phone,
                email,
                rawPassword,
                userType
        );

        assertNotNull(result);
        assertEquals(email, result.getEmail());
        assertEquals("encoded-password", result.getPassword());
        assertEquals(userType, result.getUserType());

        verify(userRepository).existsByEmail(email);
        verify(passwordEncoder).encode(rawPassword);
        verify(userRepository).save(any(User.class));
    }
}
