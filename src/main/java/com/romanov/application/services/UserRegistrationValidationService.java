package com.romanov.application.services;

import com.romanov.application.model.dto.UserDto;
import com.romanov.application.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

@Service
public class UserRegistrationValidationService implements Validator {
    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return UserDto.class.equals(aClass);
    }

    private final Pattern notWordCharacters = Pattern.compile("\\W+");
    private final Pattern validEmailAddress = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    public void validate(Object o, Errors errors) {
        UserDto user = (UserDto) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "username.size");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password.size");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "repeatedPassword", "password.size");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "empty.email");

        if (isNotEmpty(user.getUsername()) && (user.getUsername().length() < 6 || user.getUsername().length() > 32)) {
            errors.rejectValue("username", "username.size");
        }
        if (notWordCharacters.matcher(user.getUsername()).find()) {
            errors.rejectValue("username", "allowed.letters");
        }
        if (isNotEmpty(user.getUsername()) && userRepository.existsByUsername(user.getUsername())) {
            errors.rejectValue("username", "username.already.exists");
        }
        if (isNotEmpty(user.getEmail()) && !validEmailAddress.matcher(user.getEmail()).find()) {
            errors.rejectValue("email", "invalid.email");
        }
        if (isNotEmpty(user.getPassword()) && (user.getPassword().length() < 6 || user.getPassword().length() > 32)) {
            errors.rejectValue("password", "password.size");
        }
        if (notWordCharacters.matcher(user.getPassword()).find()) {
            errors.rejectValue("password", "allowed.letters");
        }
        if (isNotEmpty(user.getRepeatedPassword()) && !user.getRepeatedPassword().equals(user.getPassword())) {
            errors.rejectValue("repeatedPassword", "passwords.dont.match");
        }
    }

    private boolean isNotEmpty(String string) {
        return string != null && !string.isEmpty();
    }
}
