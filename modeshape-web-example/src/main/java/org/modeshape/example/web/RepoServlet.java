/*
 * ModeShape (http://www.modeshape.org)
 * See the COPYRIGHT.txt file distributed with this work for information
 * regarding copyright ownership.  Some portions may be licensed
 * to Red Hat, Inc. under one or more contributor license agreements.
 * See the AUTHORS.txt file in the distribution for a full listing of
 * individual contributors.
 *
 * ModeShape is free software. Unless otherwise indicated, all code in ModeShape
 * is licensed to you under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * ModeShape is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.modeshape.example.web;

import java.io.IOException;
import java.util.Map;
import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.RepositoryFactory;
import javax.jcr.Session;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 * Demo for using JCR from web application. This example application accepts a "repositoryUrl" parameter and a "path" parameter,
 * while most practical applications will know where to find the repository URL (e.g. via configuration, perhaps via the web.xml),
 * and will determine the path from the requests' URL.
 * 
 * @author kulikov
 */
public class RepoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * A constant that determines whether this code will look up the repository using JCR's RepositoryFactory approach or directly
     * looking up in JNDI.
     */
    private static final boolean USE_JNDI_DIRECTLY = true;

    private static final String JNDI_PREFIX = "jndi:";
    private static final String ERROR_MESSAGE = "errorMessage";

    private static final String REPOSITORY_URL = "repositoryURL";
    private static final String NODE_PATH = "path";

    private Session session;
    private static final Logger logger = Logger.getLogger("Web-Demo");

    @Override
    protected void doGet( HttpServletRequest request,
                          HttpServletResponse response ) throws IOException {
        cleanSession(request);

        // read user specified parameters
        String location = getParameter(request, REPOSITORY_URL);
        String path = getParameter(request, NODE_PATH);

        try {
            // Lookup repository ...
            Repository repository = getRepository(location);

            // establish session ...
            session = repository.login();
            logger.info("Session established successfuly");

            // display childs
            Node node = session.getNode(path);
            request.getSession().setAttribute("childs", node.getNodes());
        } catch (Exception e) {
            logger.error("Could not complete request", e);
            displayError(request, e);
        } finally {
            // Always log out of the session ...
            if (session != null) {
                session.logout();
                logger.info("Logout");
            }
        }

        response.sendRedirect("main.jsp");
    }

    /**
     * Normally applications will do this with <emphasis>either</emphasis> JCR's RepositoryFactory <emphasis>or</emphasis> with
     * JNDI. However, in this example we'll show how both are done. (Really, there's no benefit to doing both.) Additionally, many
     * web applications will get the repository location as a property via the application's "web.xml"; in such cases, it's often
     * easier to always use JCR's RepositoryFactory, since that can handle JNDI URIs and other URIs.
     * 
     * @param location the location of the repository, either "<path-in-JNDI>" or a valid URI (e.g.,
     *        "jndi:<jndi-name-for-repository>" or "file://<path-to-configuration-file>" or other URL to a configuration file).
     * @return the repository; never null
     * @throws Exception if there is a problem getting the repository
     */
    private Repository getRepository( String location ) throws Exception {
        Repository repository = null;
        if (USE_JNDI_DIRECTLY) {
            // The repository instance should be in JNDI, so look it up using the supplied location.
            // Again, normally applications will "know" this location (from configuration). But this is a generic
            // example, so we get it from the request. Note that the location might not point to a registered Repository instance.
            logger.info(String.format("Location %s recognized as JNDI name", location));
            InitialContext ic = new InitialContext();
            repository = (Repository)ic.lookup(location);
        } else {
            assert location.startsWith("jndi:") || location.startsWith("file:") || location.startsWith("http");

            // Look up the repository using JCR's RepositoryFactory. This seems like a lot more work, but it actually
            // is far more capable and can handle a much wider range of "location" formats. This code even works in
            // a Java SE application, so this approach is great for libraries that can be used in any kind of application.
            // The "parameters" map could actually be obtained by reading in the entries from a properties file or via
            // properties in the web.xml; this way, the code contains no specific settings for ModeShape.
            Map<String, String> parameters = java.util.Collections.singletonMap("org.modeshape.jcr.URL", location);
            for (RepositoryFactory factory : java.util.ServiceLoader.load(RepositoryFactory.class)) {
                repository = factory.getRepository(parameters);
                if (repository != null) break;
            }
        }
        if (repository == null) {
            throw new Exception("Unable to lookup repository at '" + location + "'. Please check the format.");
        }
        return repository;
    }

    /**
     * Cleans errors stored in session scope.
     * 
     * @param request http request
     */
    private void cleanSession( HttpServletRequest request ) {
        request.getSession(true).removeAttribute(ERROR_MESSAGE);
        request.getSession().removeAttribute("childs");
    }

    /**
     * Stores error in session scope.
     * 
     * @param request http request
     * @param e exception representing error
     */
    private void displayError( HttpServletRequest request,
                               Exception e ) {
        request.getSession().setAttribute(ERROR_MESSAGE, e.getMessage());
    }

    /**
     * Reads parameter from request and stores in session.
     * 
     * @param request received request
     * @param name name of the parameter to read.
     * @return parameter's value
     */
    private String getParameter( HttpServletRequest request,
                                 String name ) {
        String v = request.getParameter(name);
        request.getSession(true).setAttribute(name, v);
        return v;
    }
}
