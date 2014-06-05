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

package org.modeshape.example.logging.jdk;

import java.net.URL;
import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import org.modeshape.common.collection.Problems;
import org.modeshape.common.logging.Logger;
import org.modeshape.jcr.ModeShapeEngine;
import org.modeshape.jcr.RepositoryConfiguration;

public class ModeShapeExample {

    public static void main( String[] argv ) {

        Logger logger = Logger.getLogger(ModeShapeExample.class);
        logger.debug("Starting the custom logging example application");

        // Create and start the engine ...
        logger.debug("Starting the ModeShape engine ...");
        ModeShapeEngine engine = new ModeShapeEngine();
        engine.start();

        // Load the configuration for a repository via the classloader (can also use path to a file)...
        Repository repository = null;
        String repositoryName = null;
        try {
            URL url = ModeShapeExample.class.getClassLoader().getResource("my-repository-config.json");
            RepositoryConfiguration config = RepositoryConfiguration.read(url);

            // We could change the name of the repository programmatically ...
            // config = config.withName("Some Other Repository");

            // Verify the configuration for the repository ...
            Problems problems = config.validate();
            if (problems.hasErrors()) {
                System.err.println("Problems starting the engine.");
                System.err.println(problems);
                System.exit(-1);
            }

            // Deploy the repository ...
            logger.debug("Deploying the '{0}' repository ...", config.getName());
            repository = engine.deploy(config);
            repositoryName = config.getName();
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(-1);
            return;
        }

        Session session = null;
        try {
            // Get the repository
            logger.debug("Getting the '{0}' repository and creating a Session ...", repositoryName);
            repository = engine.getRepository(repositoryName);

            // Create a session ...
            session = repository.login("default");

            // Get the root node ...
            Node root = session.getRootNode();
            assert root != null;

            System.out.println("Found the root node in the \"" + session.getWorkspace().getName() + "\" workspace");
        } catch (RepositoryException e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.logout();
            System.out.println("Shutting down engine ...");
            try {
                engine.shutdown().get();
                System.out.println("Success!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
