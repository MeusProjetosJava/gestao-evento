package vitor.gestaoevento.dto;

import vitor.gestaoevento.model.UserType;
import vitor.gestaoevento.model.User;

public class UserResponseDto {
    private Long id;
    private String name;
    private String email;
    private UserType userType;

    public UserResponseDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.userType = user.getUserType();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public UserType getUserType() {
        return userType;
    }
}
