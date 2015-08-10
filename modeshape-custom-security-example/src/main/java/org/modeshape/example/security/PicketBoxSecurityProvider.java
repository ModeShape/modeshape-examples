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

package org.modeshape.example.security;

import java.security.Principal;
import java.security.acl.Group;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.jcr.Credentials;
import javax.jcr.SimpleCredentials;
import javax.security.auth.Subject;
import org.jboss.security.AuthenticationManager;
import org.jboss.security.AuthorizationManager;
import org.jboss.security.authorization.AuthorizationException;
import org.modeshape.jcr.ExecutionContext;
import org.modeshape.jcr.security.AuthenticationProvider;
import org.modeshape.jcr.security.AuthorizationProvider;
import org.modeshape.jcr.security.SecurityContext;
import org.modeshape.jcr.value.Path;
import org.picketbox.config.PicketBoxConfiguration;
import org.picketbox.core.authorization.resources.POJOResource;
import org.picketbox.factories.SecurityFactory;

/**
 * A custom security provider which uses some default PicketBox modules.
 * 
 * This is both an {@link org.modeshape.jcr.security.AuthenticationProvider} and an {@link org.modeshape.jcr.security.AuthorizationProvider}
 * performing both custom authentication and authorization. If more repository data would be required by this class, it could
 * implement {@link org.modeshape.jcr.security.AdvancedAuthorizationProvider} instead of {@link org.modeshape.jcr.security.AuthorizationProvider}
 * 
 * @author Horia Chiorean (hchiorea@redhat.com)
 */
public final class PicketBoxSecurityProvider implements AuthorizationProvider, AuthenticationProvider, SecurityContext {
    /**
     *  The name of the security domain, defined in the security configuration file (see security.conf.xml)
     */
    private static final String SECURITY_DOMAIN_NAME = "modeshape-jcr";
    
    private final AuthenticationManager authenticationManager;
    private final AuthorizationManager authorizationManager;   
    
    private Subject authenticatedSubject; 
    private Set<String> roles;

    public PicketBoxSecurityProvider() {
        PicketBoxConfiguration idtrustConfig = new PicketBoxConfiguration();
        idtrustConfig.load(PicketBoxSecurityProvider.class.getClassLoader().getResourceAsStream("security.conf.xml"));

        this.authenticationManager = SecurityFactory.getAuthenticationManager(SECURITY_DOMAIN_NAME);
        if (this.authenticationManager == null) {
            throw new IllegalStateException("Authentication Manager is null");
        }
        
        this.authorizationManager = SecurityFactory.getAuthorizationManager(SECURITY_DOMAIN_NAME);
        if (this.authorizationManager == null) {
            throw new IllegalStateException("Authorization Manager is null");
        }
        
        // no one is authenticated yet
        this.authenticatedSubject = null;
        this.roles = null;
    }

    private PicketBoxSecurityProvider( AuthenticationManager authenticationManager,
                                       AuthorizationManager authorizationManager,
                                       Subject authenticatedSubject ) {
        assert authenticatedSubject != null;
        this.authenticationManager = authenticationManager;
        this.authorizationManager = authorizationManager;
        this.authenticatedSubject = authenticatedSubject;
        this.roles = roles();
    }

    @Override
    public ExecutionContext authenticate( Credentials credentials, String repositoryName, String workspaceName,
                                          ExecutionContext repositoryContext, Map<String, Object> sessionAttributes ) {
        if (!(credentials instanceof SimpleCredentials)) {
            return null;
        }
        final SimpleCredentials simpleCredentials = (SimpleCredentials) credentials;
        final Principal principal = new Principal() {
            @Override
            public String getName() {
                return simpleCredentials.getUserID();
            }
        };
        String pass = String.valueOf(simpleCredentials.getPassword());
        Subject subject = new Subject();
        if (this.authenticationManager.isValid(principal, pass, subject)) {
            //we've been successfully authenticated, so we need to set ourselves as a security context in order to be 
            //able to perform custom authorization
            return repositoryContext.with(newProviderWithSubject(subject));
        } else {
            // were unable to perform authentication
            return null;
        }
    }

    @Override
    public boolean hasPermission( ExecutionContext context, String repositoryName, String repositorySourceName,
                                  String workspaceName, Path absPath, String... actions ) {
        if (absPath == null) {
            // we'll let all authenticated users to have rights on the workspaces
            return true;
        }
        try {
            // we don't care about the resource, this is just a simple example which checks the roles of the subject
            // against the configured roles in the configuration file
            authorizationManager.authorize(new POJOResource(absPath), this.authenticatedSubject);
            return true;
        } catch (AuthorizationException e) {
            return false;
        }
    }

    @Override
    public boolean isAnonymous() {
        return false;
    }

    @Override
    public String getUserName() {
        if (authenticatedSubject == null) {
            return null;
        }
        Set<Principal> principals = authenticatedSubject.getPrincipals();
        return principals.isEmpty() ? null : principals.iterator().next().getName();
    }

    @Override
    public boolean hasRole( String roleName ) {
        // for the purpose of the example we don't care about ModeShape specific roles, so we'll always return true here
        return true;
    }

    @Override
    public void logout() {
        this.authenticatedSubject = null;
        this.roles = null;
    }
    
    private Set<String> roles() {
        if (authenticatedSubject == null) {
            return Collections.emptySet();
        }
        if (roles != null) {
            return roles;
        }
        Group rolesGroup = null;
        for (Group group : authenticatedSubject.getPrincipals(Group.class)) {
            if (group.getName().equalsIgnoreCase("roles")) {
                rolesGroup = group;
                break;
            }
        }
        if (rolesGroup == null) {
            return null;
        }
        Set<String> result = new HashSet<>();
        Enumeration<? extends Principal> members = rolesGroup.members();
        while (members.hasMoreElements()) {
            result.add(members.nextElement().getName());
        }
        return result;
    }
    
    private PicketBoxSecurityProvider newProviderWithSubject( Subject subject ) {
        if (subject == null) {
            throw new IllegalArgumentException("Null subject");
        }
        return new PicketBoxSecurityProvider(this.authenticationManager, this.authorizationManager, subject);    
    }
}
