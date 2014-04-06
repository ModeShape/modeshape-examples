package org.modeshape.example.springsecurity.jcr.security;

import org.modeshape.jcr.security.SecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.jaas.JaasAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;

import java.security.Principal;
import java.security.acl.Group;
import java.util.Enumeration;
import java.util.Set;

/**
 * @author M.Sarhan
 */
public class SpringSecurityContext implements SecurityContext {

    final static Logger logger = LoggerFactory.getLogger(SpringSecurityContext.class);

    private Authentication auth;

    public SpringSecurityContext(Authentication auth) {
        this.auth = auth;
    }

    @Override
    public boolean isAnonymous() {
        return false;
    }

    @Override
    public String getUserName() {
        return auth.getName();
    }

    @Override
    public boolean hasRole(String roleName) {
        for (GrantedAuthority authority : auth.getAuthorities()) {
            if (roleName.equals(authority.getAuthority())) {
                logger.info("[{}] has [{}] role.", auth.getName(), roleName);
                return true;
            }
        }

        return false;
    }

    @Override
    public void logout() {
        logger.info("logout");
    }
}
