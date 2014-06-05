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

package org.modeshape.example.jsf;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import org.apache.log4j.Logger;

/**
 * A simple managed bean, that provides clients with {@link javax.jcr.Session} instances.
 *
 * @author Horia Chiorean (hchiorea@redhat.com)
 */
@Singleton
public class SessionProducer {

    private static final Logger LOGGER = Logger.getLogger(SessionProducer.class);

    private Repository sampleRepository;

    @PostConstruct
    public void initializeRepository() {
        LOGGER.info("Getting sample repository...");
        sampleRepository = RepositoryProvider.getRepository("file:/repository-config.json");
    }

    /**
     * Producer method which returns a default session to the preconfigured repository.
     *
     * @return a {@link Session} instance
     * @throws RepositoryException if anything goes wrong
     */
    @RequestScoped
    @Produces
    public Session getSession() throws RepositoryException {
        LOGGER.debug("Creating new session...");
        return sampleRepository.login();
    }

    /**
     * Disposer method for {@link SessionProducer#getSession()}
     *
     * @param session the {@link Session} created by the producer method
     */
    public void logoutSession( @Disposes final Session session ) {
        LOGGER.debug("Closing session...");
        session.logout();
    }
}
