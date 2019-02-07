package com.romanov.application.model.entities;

import com.romanov.application.model.dto.UserDto;
import com.romanov.application.model.entities.util.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@Entity(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @NotEmpty
    private String username;

    @NotNull
    @NotEmpty
    @Email
    private String email;

    @NotNull
    @NotEmpty
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Role> roles = Collections.singletonList(Role.ROLE_USER);

    public UserEntity(UserDto user) {
        id = user.getId();
        username = user.getUsername();
        email = user.getEmail();
        password = user.getPassword();
    }
}
