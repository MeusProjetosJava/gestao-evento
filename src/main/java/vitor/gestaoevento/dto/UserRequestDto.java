package vitor.gestaoevento.dto;

import vitor.gestaoevento.model.UserType;

public class UserRequestDto {
    private String name;
    private String phone;
    private String email;
    private String password;
    private UserType userType;

    public UserRequestDto() {
    }

    public UserRequestDto(String name, String phone, String email, String password, UserType userType) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.userType = userType;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public UserType getUserType() {
        return userType;
    }
}


