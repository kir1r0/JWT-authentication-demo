package com.romanov.unit

import com.romanov.application.configuration.security.JwtTokenProvider
import org.assertj.core.util.Lists
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.User
import spock.lang.Specification

import javax.annotation.PostConstruct

@SpringBootTest
class JwtTokenProviderTest extends Specification {

    @Autowired
    private JwtTokenProvider jwtTokenProvider

    private static final String USERNAME = "ivanSpaceBiker"

    private static final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(new User(USERNAME, "password", Lists.emptyList()), null)

    private String token

    @PostConstruct
    def initTokenValue() {
        token = jwtTokenProvider.generateToken(authenticationToken)
    }

    def "Token should be not null"() {
        expect:
        token
    }

    def "Username should be right from token"() {
        expect:
        jwtTokenProvider.getUsernameFromJWT(token) == USERNAME
    }

    def "Token should be valid"() {
        expect:
        jwtTokenProvider.validateToken(token)
    }

}
