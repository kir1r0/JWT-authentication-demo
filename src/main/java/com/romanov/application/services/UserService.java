package com.romanov.application.services;

import com.romanov.application.model.dto.UserDto;
import com.romanov.application.model.entities.UserEntity;

public interface UserService {

    void registerNewUser(UserDto user);

    UserEntity getUserByUserName(String username);
}
