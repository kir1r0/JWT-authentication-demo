package com.romanov.integration.services

import com.romanov.application.model.dto.UserDto
import com.romanov.application.repositories.UserRepository
import com.romanov.application.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class UserServiceImplTest extends Specification {

    @Autowired
    private UserService userService

    @Autowired
    private UserRepository userRepository

    def "New user should be registered"() {
        setup:
        UserDto userDto = new UserDto()
        userDto.setUsername("ivan")
        userDto.setEmail("ivan@mail.com")
        userDto.setPassword("password")

        expect:
        !userRepository.existsByUsername(userDto.getUsername())
        userService.registerNewUser(userDto)
        userRepository.existsByUsername(userDto.getUsername())

        cleanup:
        userRepository.deleteByUsername(userDto.getUsername())
    }
}
