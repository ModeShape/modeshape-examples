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

import java.net.URL;
import java.util.UUID;
import javax.jcr.AccessDeniedException;
import javax.jcr.LoginException;
import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import org.modeshape.common.collection.Problems;
import org.modeshape.jcr.ModeShapeEngine;
import org.modeshape.jcr.RepositoryConfiguration;
import org.picketbox.factories.SecurityFactory;

public class ModeShapeExample {

    private static boolean print = true;

    public static void main( String[] argv ) {

        // Create and start the engine ...
        ModeShapeEngine engine = new ModeShapeEngine();
        engine.start();

        // Load the configuration for a repository via the classloader (can also use path to a file)...
        Repository repository = null;
        try {
            URL url = ModeShapeExample.class.getClassLoader().getResource("my-repository-config.json");
            RepositoryConfiguration config = RepositoryConfiguration.read(url);

            // Verify the configuration for the repository ...
            Problems problems = config.validate();
            if (problems.hasErrors()) {
                System.err.println("Problems starting the engine.");
                System.err.println(problems);
                System.exit(-1);
            }
            // Deploy the repository ...
            repository = engine.deploy(config);
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(-1);
            return;
        }
        
        try {
            // setup some global Picketbox state
            SecurityFactory.prepare();
            
            verifyAuthenticationAndAuthorization(repository, "admin", true, true);
            verifyAuthenticationAndAuthorization(repository, "john", true, true);
            verifyAuthenticationAndAuthorization(repository, "sue", true, true);
            verifyAuthenticationAndAuthorization(repository, "bob", true, false);
            verifyAuthenticationAndAuthorization(repository, "dummy", false, false);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // cleanup the global picketbox state
            SecurityFactory.release();
            try {
                engine.shutdown().get();
                System.out.println("Success!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private static void print (String message) {
        if (print) {
            System.out.println(message);
        }
    }
    
    private static void verifyAuthenticationAndAuthorization(Repository repository,
                                                             String username, 
                                                             boolean expectSuccessfulAuthentication, 
                                                             boolean expectSuccessfulAuthorizaton) throws Exception {
        Session session = null;

        try {
            try {
                session = repository.login(new SimpleCredentials(username, username.toCharArray()));
                if (!expectSuccessfulAuthentication) {
                    throw new IllegalStateException(username + " was authenticated, even though he shouldn't have been");    
                }
                print("Successfully authenticated " + username);
            } catch (LoginException e) {
                if (expectSuccessfulAuthentication) {
                    throw new IllegalStateException(username + " was not authenticated, even though he should've been");
                }
                print("Authentication failed for " + username);
                return;
            }

            try {
                session.getRootNode().addNode(UUID.randomUUID().toString());
                if (!expectSuccessfulAuthorizaton) {
                    throw new IllegalStateException(username + " was authorized, even though he shouldn't have been");
                }
                print("Successfully authorized " + username);
            } catch (AccessDeniedException e) {
                if (expectSuccessfulAuthorizaton) {
                    throw new IllegalStateException(username + " was not authorized, even though he should've been");
                }
                print("Authorization failed for " + username);
            }
        } finally {
            if (session != null) {
                session.logout();
            }
        }
    }
}
