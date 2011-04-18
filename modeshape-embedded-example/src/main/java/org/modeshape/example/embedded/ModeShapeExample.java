package org.modeshape.example.embedded;

import java.io.IOException;
import java.net.URL;
import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import org.modeshape.jcr.JcrConfiguration;
import org.modeshape.jcr.JcrEngine;
import org.xml.sax.SAXException;

public class ModeShapeExample {
	
	public static void main( String[] argv ) {
		
		// Load the configuration via the classloader (can also use path to a file)...
		JcrConfiguration config = new JcrConfiguration();
        try {
            URL url = ModeShapeExample.class.getClassLoader().getResource("modeshape-config.xml");       
		    config.loadFrom(url);
        } catch ( SAXException e ) {
            System.err.println("Failed to read the configuration file");
        } catch ( IOException e ) {
            System.err.println("Failed to load the configuration file");
        }
		
		// Create and start the engine ...
        JcrEngine engine = config.build();
        if ( engine.getProblems().hasErrors() ) {
            System.err.println("Problems starting the engine.");
            System.err.println(engine.getProblems());
            System.exit(-1);
        }

        // Start the engine
        engine.start();

        Repository repository = null;
        Session session = null;
        try {
	        // Get the repository
            repository = engine.getRepository("My repository");

		    // Create a session ...
            session = repository.login("default");

            // Get the root node ...
            Node root = session.getRootNode();
		
            System.out.println("Found the root node in the \"" + session.getWorkspace().getName() + "\" workspace");
        } catch ( RepositoryException e ) {
            System.err.println("Failed to load the configuration file");
        } finally {
            if ( session != null ) session.logout();
            if ( engine != null ) engine.shutdown();
        }

	}
}
