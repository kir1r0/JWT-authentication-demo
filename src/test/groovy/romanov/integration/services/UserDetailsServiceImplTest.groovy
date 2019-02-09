package com.romanov.integration.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import spock.lang.Specification

@SpringBootTest
class UserDetailsServiceImplTest extends Specification {

    @Autowired
    private UserDetailsService userDetailsService

    def "If user wasn't found then throw UsernameNotFoundException"() {
        when:
        userDetailsService.loadUserByUsername("asd")
        then:
        thrown UsernameNotFoundException
    }

    def "If user was found then UserDetails will be not null"() {
        when:
        def userDetails = userDetailsService.loadUserByUsername("admin")
        then:
        userDetails != null
    }
}
