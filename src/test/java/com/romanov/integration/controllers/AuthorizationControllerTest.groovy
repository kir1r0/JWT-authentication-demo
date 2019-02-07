package com.romanov.integration.controllers

import com.romanov.application.configuration.ApplicationProperties
import com.romanov.application.model.dto.UserDto
import com.romanov.application.repositories.UserRepository
import com.romanov.application.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import javax.servlet.http.Cookie

import static org.hamcrest.Matchers.containsString
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc(secure = false)
class AuthorizationControllerTest extends Specification {

    @Autowired
    private UserService userService

    @Autowired
    private UserRepository userRepository

    @Autowired
    private ApplicationProperties applicationProperties

    @Autowired
    private MockMvc mvc

    def "OK status should be returned"() {
        expect:
        mvc.perform(get("/signin")).andExpect(status().isOk())
    }

    def "Without csrf token request should be rejected"() {
        expect:
        mvc.perform(post("/signup")).andExpect(status().isForbidden())
    }

    def "New user should be registered"() {
        def USERNAME = "ivan123"
        def PASSWORD = "password"
        def REPEATED_PASSWORD = "password"
        def EMAIL = "ivan@mail.com"

        expect:
        userService.getUserByUserName(USERNAME) == null

        mvc.perform(post("/signup")
                .param("username", USERNAME)
                .param("password", PASSWORD)
                .param("repeatedPassword", REPEATED_PASSWORD)
                .param("email", EMAIL)
                .flashAttr("userSignUpForm", new UserDto())
                .with(csrf())).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/signin"))

        userService.getUserByUserName(USERNAME) != null

        cleanup:
        userRepository.deleteByUsername(USERNAME)
    }

    def "Cookie with JWT token should be returned for default user admin/admin"() {
        expect:
        mvc.perform(post("/signin")
                .param("username", "admin")
                .param("password", "admin")
                .flashAttr("userSignInForm", new UserDto())
                .with(csrf()))
                .andExpect(cookie().exists(applicationProperties.AUTHORIZATION))
    }

    def "Message 'username or password is not valid' should be returned"() {
        expect:
        mvc.perform(post("/signin")
                .param("username", "a")
                .param("password", "a")
                .flashAttr("userSignInForm", new UserDto())
                .with(csrf()))
                .andExpect(content().string(containsString("username or password is invalid!")))
    }

    def "Redirect should be happened after successful login for default user admin/admin"() {
        def countOfAllowedUrls = applicationProperties.ALLOWED_URLS.length

        expect:
        mvc.perform(post("/signin")
                .param("username", "admin")
                .param("password", "admin")
                .flashAttr("userSignInForm", new UserDto())
                .with(csrf())
                .cookie(new Cookie(applicationProperties.REDIRECT_URL, getLastAllowedUrl(countOfAllowedUrls))))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(getLastAllowedUrl(countOfAllowedUrls)))
    }

    private String getLastAllowedUrl(int countOfAllowedUrls) {
        Arrays.stream(applicationProperties.ALLOWED_URLS).skip(countOfAllowedUrls - 1).findFirst().get()
    }
}
