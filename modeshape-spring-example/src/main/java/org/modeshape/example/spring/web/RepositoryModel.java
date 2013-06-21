package org.modeshape.example.spring.web;

import java.util.Set;
import org.springframework.validation.BindingResult;

/**
 * Model used by {@link RepositoryController}
 *
 * @author Horia Chiorean (hchiorea@redhat.com)
 */
public class RepositoryModel {
    private String children;

    private String repositoryName;
    private String parentPath;
    private String newNodeName;

    public String getNewNodeName() {
        return newNodeName;
    }

    public void setNewNodeName( String newNodeName ) {
        this.newNodeName = newNodeName;
    }

    public String getParentPath() {
        return parentPath;
    }

    public RepositoryModel setParentPath( String parentPath ) {
        this.parentPath = parentPath;
        return this;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public RepositoryModel setRepositoryName( String repositoryName ) {
        this.repositoryName = repositoryName;
        return this;
    }

    public String getChildren() {
        return children;
    }

    public RepositoryModel setChildren(Set<String> childrenSet) {
        this.children = childrenSet.toString();
        return this;
    }

    boolean validateParentPath(BindingResult bindingResult) {
        if (parentPath == null || parentPath.trim().length() == 0) {
            bindingResult.rejectValue("parentPath", "parentPath.empty", "Parent path must not be empty");
            return false;
        }
        return true;
    }

    boolean validateNodeName(BindingResult bindingResult) {
        if (newNodeName == null || newNodeName.trim().length() == 0) {
            bindingResult.rejectValue("newNodeName", "newNodeName.empty", "New node name must not be empty");
            return false;
        }
        return true;
    }
}
