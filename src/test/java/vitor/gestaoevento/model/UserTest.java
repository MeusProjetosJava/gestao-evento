package vitor.gestaoevento.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void shouldReturnTrueWhenUserIsAdmin() {
        User user = new User("Carlos","+55988888888","vc@gmail.com", "123456",UserType.ADMIN);
        assertTrue(user.isAdmin());
    }

    @Test
    void shouldReturnFalseWhenUserIsNotAdmin() {
        User user = new User("Carlos","+55988888888","vc@gmail.com", "123456",UserType.USER);
        assertFalse(user.isAdmin());
    }
}