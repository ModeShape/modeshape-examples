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

package org.modeshape.example.jdbc;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jcr.RepositoryException;

/**
 * A simple JSF controller, that uses CDI.
 *
 * @author Horia Chiorean (hchiorea@redhat.com)
 */
@Named( "repositoryController" )
@RequestScoped
public class RepositoryController implements Serializable {

    @Inject
    private RepositoryService repositoryService;

    private int foldersCount;
    private int filesCount;
    private List<String> folders;

    public int getFoldersCount() {
        return foldersCount;
    }

    public void setFoldersCount( int foldersCount ) {
        this.foldersCount = foldersCount;
    }

    public int getFilesCount() {
        return filesCount;
    }

    public void setFilesCount( int filesCount ) {
        this.filesCount = filesCount;
    }

    public void storeData() throws IOException, RepositoryException {
        if (foldersCount <= 0 || filesCount <= 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The folders and files count must be positive"));
            return;
        }
        repositoryService.storeFilesAndFolders(foldersCount, filesCount);
        if (folders != null) {
            folders = null;
        }
    }

   public List<String> foldersAndFiles() throws Exception {
       if (folders == null) {
           folders = repositoryService.listFolders();
       }
       return folders;
   }

    public String getRepositoryName() {
        return repositoryService.repositoryName();
    }
}
