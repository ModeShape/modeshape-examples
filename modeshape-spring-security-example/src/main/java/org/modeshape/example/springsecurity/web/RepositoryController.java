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

package org.modeshape.example.springsecurity.web;

import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import org.modeshape.example.springsecurity.jcr.security.SpringSecurityCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author M.Sarhan
 */

@Controller
@RequestMapping("/jcr")
public class RepositoryController {

    final static Logger logger = LoggerFactory.getLogger(RepositoryController.class);

    @Autowired
    private Repository repository;

    @RequestMapping(value = "/", method = {RequestMethod.GET}, produces = "text/html; charset=utf-8")
    public
    @ResponseBody
    String jcrLogin(Authentication auth) {
        String html = "";
        try {
            Session session = repository.login(new SpringSecurityCredentials(auth));

            String repoName = session.getRepository().getDescriptor(org.modeshape.jcr.api.Repository.REPOSITORY_NAME);
            String wsName = session.getWorkspace().getName();

            html = "<html>" +
                    "<body>" +
                    "<h3>Welcome " + (auth.getName()) + "!</h3>" +
                    "<p>You have successfully logged in to [" + (repoName + "." + wsName) + "]</p>" +
                    "<p>You have granted " + auth.getAuthorities().toString() + " roles.</p>" +
                    "<p><a href='/login?logout'>logout</a></p>" +
                    "</body>" +
                    "</html>";
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
        return html;
    }
}
