package com.romanov.application.repositories;

import com.romanov.application.model.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;


public interface UserRepository extends CrudRepository<UserEntity, Long> {

    boolean existsByUsername(String username);

    UserEntity findByUsername(String username);

    UserEntity findByEmail(String email);

    @Transactional
    void deleteByUsername(String username);
}
