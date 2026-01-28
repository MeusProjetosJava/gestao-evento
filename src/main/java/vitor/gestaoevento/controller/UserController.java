package vitor.gestaoevento.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vitor.gestaoevento.dto.UserRequestDto;
import vitor.gestaoevento.dto.UserResponseDto;
import vitor.gestaoevento.model.User;
import vitor.gestaoevento.service.UserService;

@Tag(name = "Users", description = "User registration endpoints")
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Create user",
            description = "Registers a new user in the system"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User successfully created"),
            @ApiResponse(responseCode = "400", description = "User already exists or invalid data")
    })

    @PostMapping
    public UserResponseDto createUser(@RequestBody UserRequestDto userRequestDto) {
        User user = userService.registerUser(userRequestDto.getName(),
                userRequestDto.getPhone(),
                userRequestDto.getEmail(), userRequestDto.getPassword(),
                userRequestDto.getUserType());

        return new UserResponseDto(user);
    }
}
