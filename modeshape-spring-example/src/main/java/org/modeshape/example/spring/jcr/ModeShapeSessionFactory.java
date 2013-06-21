package org.modeshape.example.spring.jcr;

import javax.annotation.PreDestroy;
import javax.jcr.Repository;
import javax.jcr.Session;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * {@link org.springframework.beans.factory.FactoryBean} which uses {@link ModeShapeRepositoryFactory} to provide
 * different {@link Session} instances each time this bean is used.
 *
 * @author Horia Chiorean (hchiorea@redhat.com)
 */
@Component
public class ModeShapeSessionFactory implements FactoryBean<Session> {

    @Autowired
    private Repository repository;
    private Session session;

    @Override
    public Session getObject() throws Exception {
        if (session == null) {
            session = repository.login();
        }
        return session;
    }

    @Override
    public Class<?> getObjectType() {
        return Session.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @PreDestroy
    public void logout() throws Exception {
        session.logout();
        session = null;
    }
}
