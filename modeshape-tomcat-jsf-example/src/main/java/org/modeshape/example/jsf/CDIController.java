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

package org.modeshape.example.jsf;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

/**
 * A simple JSF controller, that uses CDI to obtain a {@link SessionProducer} instance with which it performs some operations.
 *
 * @author Horia Chiorean (hchiorea@redhat.com)
 */
@Named( "cdiController" )
@RequestScoped
public class CDIController implements Serializable {

    @Inject
    private Session repositorySession;

    private String parentPath = "/";
    private String newNodeName;

    private Set<String> children = Collections.emptySet();

    /**
     * Sets a name for a new node to create.
     *
     * @param newNodeName a {@code non-null} string
     */
    public void setNewNodeName( String newNodeName ) {
        this.newNodeName = newNodeName;
    }

    /**
     * Returns the name of a new node which can be created.
     *
     * @return a {@code non-null} string
     */
    public String getNewNodeName() {
        return newNodeName;
    }

    /**
     * Returns the absolute path of a parent node
     *
     * @return a {@code non-null} string
     */
    public String getParentPath() {
        return parentPath;
    }

    /**
     * Sets the absolute path of a parent node.
     *
     * @param parentPath a {@code non-null} string
     */
    public void setParentPath( String parentPath ) {
        this.parentPath = parentPath;
    }

    /**
     * Returns the children nodes of the node located at {@link CDIController#parentPath}
     *
     * @return a Set<String> containing the paths of the children.
     */
    public Set<String> getChildren() {
        return children;
    }

    /**
     * Loads the children nodes of the node located at {@link CDIController#parentPath}
     */
    public void loadChildren() {
        children = new TreeSet<String>();
        if (parentPath == null || parentPath.trim().length() == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    "The absolute path of the parent node is required"));
        } else {
            try {
                Node parentNode = repositorySession.getNode(parentPath);
                for (NodeIterator nodeIterator = parentNode.getNodes(); nodeIterator.hasNext(); ) {
                    children.add(nodeIterator.nextNode().getPath());
                }
            } catch (RepositoryException e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
            }
        }
    }

    /**
     * Adds a child node at {@link CDIController#parentPath} which has the name {@link CDIController#newNodeName}
     */
    public void addChildNode() {
        if (newNodeName == null || newNodeName.trim().length() == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The name of the new node is required"));
        } else {
            try {
                Node parentNode = repositorySession.getNode(parentPath);
                parentNode.addNode(newNodeName);
                repositorySession.save();
            } catch (RepositoryException e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
            }
        }
        loadChildren();
    }

    /**
     * Returns the name of the repository to which the session is bound.
     *
     * @return a non-null string.
     */
    public String getRepositoryName() {
        return repositorySession.getRepository().getDescriptor(org.modeshape.jcr.api.Repository.REPOSITORY_NAME);
    }
}
