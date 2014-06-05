package org.modeshape.example.springsecurity.jcr.security;

import org.springframework.security.core.Authentication;

import javax.jcr.Credentials;

/**
 * @author M.Sarhan
 */
public class SpringSecurityCredentials implements Credentials {

    private static final long serialVersionUID = 1L;

    private transient Authentication auth;

    public SpringSecurityCredentials(Authentication auth) {
        this.auth = auth;
    }

    public Authentication getAuth() {
        return auth;
    }

}
