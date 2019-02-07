package com.romanov.unit

import com.romanov.application.configuration.security.JwtTokenProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class JwtTokenProviderTest extends Specification {

    @Autowired
    private JwtTokenProvider jwtTokenProvider



    def "Test getting username from token"() {
        authenticationManager.a
        jwtTokenProvider.generateToken()
    }
}
