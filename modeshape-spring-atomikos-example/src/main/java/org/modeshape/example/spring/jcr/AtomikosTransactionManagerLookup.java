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

package org.modeshape.example.spring.jcr;

import javax.transaction.TransactionManager;
import org.apache.log4j.Logger;
import org.modeshape.jcr.api.txn.TransactionManagerLookup;

/**
 * {@link TransactionManagerLookup} implementation needed to make ModeShape use a custom transaction manager. We're using
 * Spring here to inject the actual Atomikos instance which we'll simply return to ModeShape.
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
        }
        throw new IllegalStateException("Spring has not injected the Atomikos TM as expected. Check the configurations...");
    }
}
