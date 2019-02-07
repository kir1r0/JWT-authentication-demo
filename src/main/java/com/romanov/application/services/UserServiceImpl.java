package com.romanov.application.services;

import com.romanov.application.model.dto.UserDto;
import com.romanov.application.model.entities.UserEntity;
import com.romanov.application.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void registerNewUser(UserDto user) {
        UserEntity userEntity = new UserEntity(user);
        String encodedUserPassword = passwordEncoder.encode(user.getPassword());
        userEntity.setPassword(encodedUserPassword);
        userRepository.save(userEntity);
    }

    @Override
    public UserEntity getUserByUserName(String username) {
        return userRepository.findByUsername(username);
    }
}
