package org.modeshape.example.springsecurity.jcr;

import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jcr.Repository;
import org.apache.log4j.Logger;
import org.modeshape.jcr.ModeShapeEngine;
import org.modeshape.jcr.RepositoryConfiguration;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.Resource;

/**
 * {@link FactoryBean} which uses ModeShape to provide {@link Repository} instances
 *
 * @author Horia Chiorean (hchiorea@redhat.com)
 */
public class ModeShapeRepositoryFactory implements FactoryBean<Repository> {

    private static final Logger LOG = Logger.getLogger(ModeShapeRepositoryFactory.class);
    private static final ModeShapeEngine ENGINE = new ModeShapeEngine();

    private Resource configuration;
    private Repository repository;

    @PostConstruct
    public void start() throws Exception {
        if (configuration == null) {
            throw new IllegalArgumentException("The repository configuration file is required");
        }
        ENGINE.start();
        RepositoryConfiguration repositoryConfiguration = RepositoryConfiguration.read(configuration.getURL());
        repository = ENGINE.deploy(repositoryConfiguration);
        ENGINE.startRepository(repositoryConfiguration.getName());
    }

    @PreDestroy
    public void stop() throws Exception {
        try {
            ENGINE.shutdown().get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            LOG.error("Error while waiting for the ModeShape engine to shutdown", e);
        }
    }

    @Override
    public Repository getObject() throws Exception {
        return repository;
    }

    @Override
    public Class<?> getObjectType() {
        return javax.jcr.Repository.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setConfiguration( Resource configuration ) {
        this.configuration = configuration;
    }
}
