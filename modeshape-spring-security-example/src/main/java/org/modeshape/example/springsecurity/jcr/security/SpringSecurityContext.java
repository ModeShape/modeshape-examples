/*
 * ModeShape (http://www.modeshape.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.modeshape.example.springsecurity.jcr.security;

import org.modeshape.jcr.security.SecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

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
