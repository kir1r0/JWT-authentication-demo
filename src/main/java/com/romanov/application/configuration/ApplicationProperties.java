package com.romanov.application.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@PropertySource("classpath:/application.properties")
public class ApplicationProperties {

    @Value("${allowed.urls}")
    public String[] ALLOWED_URLS;
    public String DEFAULT_REDIRECT_URL;
    @Value("${jwt.secret}")
    public String JWT_SECRET;
    @Value("${jwt.token.expiration.time.ms}")
    public long TOKEN_EXPIRATION_TIME;
    @Value("${cookie.expiration.time.s}")
    public int COOKIE_EXPIRATION_TIME;

    public final String AUTHORIZATION = "Authorization";
    public final String REDIRECT_URL = "Redirect-Url";

    @PostConstruct
    void initDefaultRedirectUrl() {
        DEFAULT_REDIRECT_URL = ALLOWED_URLS[0];
    }
}