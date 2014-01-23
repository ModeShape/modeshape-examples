package org.modeshape.example.leveldbstore;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Random;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Repository;
import javax.jcr.Session;
import org.modeshape.common.collection.Problems;
import org.modeshape.jcr.ModeShapeEngine;
import org.modeshape.jcr.RepositoryConfiguration;

public class ModeShapeExample {

    public static void main( String[] argv ) {

        Random rnd = new Random();

        // Create and start the engine ...
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
            repository = engine.getRepository(repositoryName);

            // Create a session ...
            session = repository.login("default");

            // Get the root node ...
            Node root = session.getRootNode();
            assert root != null;

            System.out.println("Found the root node in the \"" + session.getWorkspace().getName() + "\" workspace");

            Node n = root.addNode("Node" + rnd.nextInt());

            n.setProperty("key", "value");
            n.setProperty("content", session.getValueFactory().createBinary(new ByteArrayInputStream(new byte[1000])));

            session.save();

            System.out.println("Added one node under root");
            System.out.println("+ Root childs");

            NodeIterator it = root.getNodes();
            while (it.hasNext()) {
                System.out.println("+---> " + it.nextNode().getName());
            }
        } catch (Exception e) {
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
