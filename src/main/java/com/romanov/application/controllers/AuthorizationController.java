package com.romanov.application.controllers;

import com.romanov.application.configuration.ApplicationProperties;
import com.romanov.application.configuration.security.JwtTokenProvider;
import com.romanov.application.model.dto.UserDto;
import com.romanov.application.services.UserRegistrationValidationService;
import com.romanov.application.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@Controller
public class AuthorizationController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRegistrationValidationService userRegistrationValidationService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private ApplicationProperties applicationProperties;

    private Logger logger = LoggerFactory.getLogger(AuthorizationController.class);

    @GetMapping(value = {"/", "/signin"})
    public String getLoginPage(Model model, @RequestParam(value = "redirect_url", required = false) String redirectUrl, HttpServletRequest request, HttpServletResponse response) {
        if (isUserNotAnonymous()) {
            redirectUrl = getRedirectUrl(request);
            return "redirect:".concat(redirectUrl);
        }
        if (StringUtils.hasText(redirectUrl) && isMatchAllowedUrls(redirectUrl)) {
            setRedirectUrlCookie(response, redirectUrl);
        }
        model.addAttribute("userSignInForm", new UserDto());
        return "login";
    }

    @GetMapping(value = {"/signup"})
    public String getRegistrationPage(Model model, HttpServletRequest request) {
        if (isUserNotAnonymous()) {
            String redirectUrl = getRedirectUrl(request);
            return "redirect:".concat(redirectUrl);
        }
        model.addAttribute("userSignUpForm", new UserDto());
        return "registration";
    }

    @PostMapping("/signin")
    public String signin(@ModelAttribute("userSignInForm") UserDto userForm, HttpServletResponse response, HttpServletRequest request, Model model) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userForm.getUsername(), userForm.getPassword()));
        } catch (AuthenticationException e) {
            logger.warn("Bad credentials, username:{}", userForm.getUsername());
            model.addAttribute("errorMessage", "username or password is invalid!");
            return "login";
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        Cookie authorizationCookie = getAuthorizationCookie(jwt);
        response.addCookie(authorizationCookie);

        return "redirect:".concat(getRedirectUrl(request));
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute("userSignUpForm") UserDto userForm, BindingResult bindingResult) {
        userRegistrationValidationService.validate(userForm, bindingResult);
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        userService.registerNewUser(userForm);
        return "redirect:/signin";
    }

    @PostMapping("/logout")
    public String logout() {
        return "redirect:/signin";
    }

    private boolean isUserNotAnonymous() {
        return !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken);
    }

    private String getRedirectUrl(HttpServletRequest request) {
        Cookie redirectCookie = WebUtils.getCookie(request, applicationProperties.REDIRECT_URL);
        if (redirectCookie != null) {
            String redirectUrl = redirectCookie.getValue();
            if (StringUtils.hasText(redirectUrl) && isMatchAllowedUrls(redirectUrl)) {
                return redirectUrl;
            }
        }
        return applicationProperties.DEFAULT_REDIRECT_URL;
    }

    private boolean isMatchAllowedUrls(String redirectUrl) {
        return Arrays.asList(applicationProperties.ALLOWED_URLS).contains(redirectUrl);
    }

    private void setRedirectUrlCookie(HttpServletResponse response, String redirectUrl) {
        Cookie redirectUrlCookie = new Cookie(applicationProperties.REDIRECT_URL, redirectUrl);
        redirectUrlCookie.setHttpOnly(true);
        response.addCookie(redirectUrlCookie);
    }

    private Cookie getAuthorizationCookie(String jwt) {
        Cookie authorizationCookie = new Cookie("Authorization", jwt);
        authorizationCookie.setHttpOnly(true);
        authorizationCookie.setMaxAge(applicationProperties.COOKIE_EXPIRATION_TIME);
        return authorizationCookie;
    }

}
