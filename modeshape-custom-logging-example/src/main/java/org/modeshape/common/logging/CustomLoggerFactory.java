package org.modeshape.common.logging;

/**
 * To create a custom ModeShape logger, create a class in the <code>org.modeshape.common.logging</code> package that is named
 * {@link CustomLoggerFactory} and extends {@link LogFactory}. It should create your own implementation of {@link Logger}.
 */
public class CustomLoggerFactory extends LogFactory {

    @Override
    protected Logger getLogger( String name ) {
        return new CustomLogger(name);
    }

}
