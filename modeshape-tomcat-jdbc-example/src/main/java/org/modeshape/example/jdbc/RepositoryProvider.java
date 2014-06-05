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

package org.modeshape.example.jdbc;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.TimeUnit;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.log4j.Logger;
import org.modeshape.jcr.api.RepositoriesContainer;

/**
 * Simple {@link ServletContextListener} implementation which will initialize & return a repository
 *
 * @author Horia Chiorean (hchiorea@redhat.com)
 */
public class RepositoryProvider implements ServletContextListener {
    private static final Logger LOGGER = Logger.getLogger(RepositoryProvider.class);

    private static RepositoriesContainer repositoriesContainer;
    private static Repository repository;

    @Override
    public void contextInitialized( ServletContextEvent sce ) {
        LOGGER.info("Initializing repositories container...");
        Iterator<RepositoriesContainer> it = ServiceLoader.load(RepositoriesContainer.class).iterator();
        if (!it.hasNext()) {
            throw new IllegalArgumentException("No repositories container located in the classpath. Make sure modeshape-jcr.jar is present");
        }
        repositoriesContainer = it.next();
        Map<String, String> params = new HashMap<String, String>();
        params.put(RepositoriesContainer.URL, "file:repository-config.json");

        try {
            repository = repositoriesContainer.getRepository("JDBCRepository", params);
        } catch (RepositoryException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void contextDestroyed( ServletContextEvent sce ) {
        LOGGER.info("Shutting down the repositories container...");
        try {
            repositoriesContainer.shutdown().get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            LOGGER.error("Unexpected exception while shutting down the repository", e);
        }
    }

    public static Repository getRepository() {
        return repository;
    }
}
