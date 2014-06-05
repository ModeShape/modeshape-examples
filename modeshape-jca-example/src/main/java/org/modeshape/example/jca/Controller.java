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
package org.modeshape.example.jca;

import java.io.IOException;
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

/**
 * Servlet for accessing Repository via JCA adapter.
 *
 * @author kulikov
 */
public class Controller extends HttpServlet {
    
    private static final String FACTORY_JNDI_NAME = "repositoryURL";
    private static final String NODE_PATH = "path";
    private static final String ERROR_MESSAGE = "errorMessage";
    
    private Session session;

    private Logger logger = Logger.getLogger(Controller.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        cleanSession(request);

        //read user specified parameters
        String location = getParameter(request, FACTORY_JNDI_NAME);
        String path = getParameter(request, NODE_PATH);

        try {
            //lookup repository
            logger.info("Obtaining repository");
            InitialContext ic = new InitialContext();
            Object obj = ic.lookup(location);

            logger.info("Repository class: " + obj);
            logger.info("Cheking instance: " + (obj instanceof Repository));

            Repository repository = (Repository)ic.lookup(location);

            //Lookup may silently return null if location is recognized
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

}
