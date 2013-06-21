package org.modeshape.example.spring.jcr;

import javax.transaction.TransactionManager;
import org.apache.log4j.Logger;
import org.infinispan.transaction.lookup.TransactionManagerLookup;
import org.infinispan.transaction.tm.DummyTransactionManager;

/**
 * {@link TransactionManagerLookup} implementation needed to make Infinispan use a custom transaction manager. Even though
 * normally Infinispan will create a new instance of this class, we use a static member and control bean initialization order
 * to make sure that Atomikos transaction manager has been injected.
 *
 * @author Horia Chiorean (hchiorea@redhat.com)
 */
public class AtomikosTransactionManagerLookup implements TransactionManagerLookup {

    private static final Logger LOGGER = Logger.getLogger(AtomikosTransactionManagerLookup.class);

    private static TransactionManager atomikosTransactionManager;

    public void setAtomikosTransactionManager( TransactionManager atomikosTransactionManager ) {
        AtomikosTransactionManagerLookup.atomikosTransactionManager = atomikosTransactionManager;
    }

    @Override
    public TransactionManager getTransactionManager() {
        if (atomikosTransactionManager != null) {
            LOGGER.info("Using Atomikos transaction manager...");
            return atomikosTransactionManager;
        } else {
            LOGGER.warn("Atomikos transaction manager has not been injected by Spring, defaulting to the Dummy Transaction Manager");
            return new DummyTransactionManager();
        }
    }
}
