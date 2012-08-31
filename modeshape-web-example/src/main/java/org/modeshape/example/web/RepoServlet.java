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
import java.util.Collections;
import java.util.Map;
import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.modeshape.jcr.JcrRepositoryFactory;

/**
 * Demo for using JCR from web application.
 *
 * @author kulikov
 */
public class RepoServlet extends HttpServlet {
    private static final String JNDI_PREFIX = "jndi:";
    private static final String ERROR_MESSAGE = "errorMessage";
    
    private static final String REPOSITORY_URL = "repositoryURL";
    private static final String NODE_PATH = "path";

    private Session session;
    private static final Logger logger = Logger.getLogger("Web-Demo");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        cleanSession(request);

        //read user specified parameters
        String location = getParameter(request, REPOSITORY_URL);
        String path = getParameter(request, NODE_PATH);

        try {
            //lookup repository
            Repository repository = this.isURI(location) ?
                    lookupUsingFactory(location) : lookupUsingJndi(location);

            //Factory may silently return null if location is recognized
            //as uri but uri format is wrong, so double check
            if (repository == null) {
                throw new Exception("Unable to lookup repository under that name. Please check location format");
            }

            //establish session and query node by path
            session = repository.login();
            logger.info("Session established successfuly");

            //display childs
            Node node = session.getNode(path);
            request.getSession().setAttribute("childs", node.getNodes());
        } catch (Exception e) {
            logger.error("Could not complete request", e);
            displayError(request, e);
        } finally {
            //logout
            if (this.isLoggedIn(session)) {
                session.logout();
                logger.info("Logout");
            }
        }
        
        response.sendRedirect("main.jsp");
    }

    /**
     * Tests parameter's format.
     *
     * @param location parameter for testing
     * @return true if parameter matches to URI format and false otherwise.
     */
    private boolean isURI(String location) {
        return location.startsWith(JNDI_PREFIX);
    }

    /**
     * Test session's state.
     *
     * @param session session to be tested.
     * @return true if session was established and false otherwise
     */
    private boolean isLoggedIn(Session session) {
        return session != null;
    }

    /**
     * Cleans errors stored in session scope.
     *
     * @param request http request
     */
    private void cleanSession(HttpServletRequest request) {
        request.getSession(true).removeAttribute(ERROR_MESSAGE);
        request.getSession().removeAttribute("childs");
    }

    /**
     * Stores error in session scope.
     *
     * @param request http request
     * @param e exception representing error
     */
    private void displayError(HttpServletRequest request, Exception e) {
        request.getSession().setAttribute(ERROR_MESSAGE, e.getMessage());
    }

    /**
     * Reads parameter from request and stores in session.
     *
     * @param request received request
     * @param name name of the parameter to read.
     * @return parameter's value
     */
    private String getParameter(HttpServletRequest request, String name) {
        String v = request.getParameter(name);
        request.getSession(true).setAttribute(name, v);
        return v;
    }

    /**
     * Gets access to Repository through JNDI API.
     *
     * @param jndiName the JNDI name
     * @return Repository instance
     * @throws NamingException if repository not bound
     */
    private Repository lookupUsingJndi(String jndiName) throws NamingException {
        logger.info(String.format("Location %s recognized as JNDI name", jndiName));
        InitialContext ic = new InitialContext();
        return (Repository) ic.lookup(jndiName);
    }

    /**
     * Gets access to Repository through JCR API.
     *
     * @param url the repository URL.
     * @return Repository instance
     * @throws Exception
     */
    private Repository lookupUsingFactory(String url) throws Exception {
        logger.info(String.format("Location %s recognized as URL", url));
        JcrRepositoryFactory factory = (JcrRepositoryFactory) Class.forName("org.modeshape.jcr.JcrRepositoryFactory").newInstance();
        Map params = Collections.singletonMap(JcrRepositoryFactory.URL, url);
        return factory.getRepository(params);
    }
}
