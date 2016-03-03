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

package org.modeshape.example.spring.web;

import java.util.Set;
import org.springframework.validation.BindingResult;

/**
 * Model used by {@link RepositoryController}
 *
 * @author Horia Chiorean (hchiorea@redhat.com)
 */
public class RepositoryModel {
    private Set<String> children;

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

    public Set<String> getChildren() {
        return children;
    }

    public RepositoryModel setChildren(Set<String> childrenSet) {
        this.children = childrenSet;
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
