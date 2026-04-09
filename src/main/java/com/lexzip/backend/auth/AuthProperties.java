package com.lexzip.backend.auth;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.auth")
public class AuthProperties {

    private long sessionTtlHours = 24;

    public long getSessionTtlHours() {
        return sessionTtlHours;
    }

    public void setSessionTtlHours(long sessionTtlHours) {
        this.sessionTtlHours = sessionTtlHours;
    }
}
