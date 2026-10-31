package org.example.amazonlocker;

import java.time.Instant;

/**
 * @Author: eve
 * @Date: 2026-10-12 15:37
 */
public class AccessToken {
    private final String code;
    private final Instant expiration;
    private final Compartment compartment;

    AccessToken(String code, Instant expiration, Compartment compartment) {
        this.code = code;
        this.expiration = expiration;
        this.compartment = compartment;
    }

    public boolean isExpired() {
        return Instant.now().compareTo(expiration) >= 0;
    }

    public String getCode() {
        return code;
    }

    public Compartment getCompartment() {
        return compartment;
    }
}
