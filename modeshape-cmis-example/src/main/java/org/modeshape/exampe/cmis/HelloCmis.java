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
package org.modeshape.exampe.cmis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.client.bindings.spi.StandardAuthenticationProvider;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.w3c.dom.Element;

/**
 *
 * @author kulikov
 */
public class HelloCmis {

    public static void main(String[] args) {
// default factory implementation
        SessionFactoryImpl factory = SessionFactoryImpl.newInstance();
        Map<String, String> parameter = new HashMap<String, String>();

// user credentials
        //parameter.put(SessionParameter.USER, "");
        //parameter.put(SessionParameter.PASSWORD, "");

// connection settings
        parameter.put(SessionParameter.BINDING_TYPE, BindingType.WEBSERVICES.value());
        parameter.put(SessionParameter.WEBSERVICES_ACL_SERVICE, "http://localhost:8080/modeshape-cmis/services/ACLService?wsdl");
        parameter.put(SessionParameter.WEBSERVICES_DISCOVERY_SERVICE, "http://localhost:8080/modeshape-cmis/services/DiscoveryService?wsdl");
        parameter.put(SessionParameter.WEBSERVICES_MULTIFILING_SERVICE, "http://localhost:8080/modeshape-cmis/services/MultiFilingService?wsdl");
        parameter.put(SessionParameter.WEBSERVICES_NAVIGATION_SERVICE, "http://localhost:8080/modeshape-cmis/services/NavigationService?wsdl");
        parameter.put(SessionParameter.WEBSERVICES_OBJECT_SERVICE, "http://localhost:8080/modeshape-cmis/services/ObjectService?wsdl");
        parameter.put(SessionParameter.WEBSERVICES_POLICY_SERVICE, "http://localhost:8080/modeshape-cmis/services/PolicyService?wsdl");
        parameter.put(SessionParameter.WEBSERVICES_RELATIONSHIP_SERVICE, "http://localhost:8080/modeshape-cmis/services/RelationshipService?wsdl");
        parameter.put(SessionParameter.WEBSERVICES_REPOSITORY_SERVICE, "http://localhost:8080/modeshape-cmis/services/RepositoryService?wsdl");
        parameter.put(SessionParameter.WEBSERVICES_VERSIONING_SERVICE, "http://localhost:8080/modeshape-cmis/services/VersioningService?wsdl");
        parameter.put(SessionParameter.REPOSITORY_ID, "default");

        Session session = factory.createSession(parameter, null, new StandardAuthenticationProvider() {

            @Override
            public Element getSOAPHeaders(Object portObject) {
                //Place headers here
                return super.getSOAPHeaders(portObject);
            }
         ;
        }, null);

        Folder root = session.getRootFolder();
        System.out.println("Root foler: " + root);
        System.out.println("---------- ");
        System.out.println("OBJECT TYPEI ID =" + root.getProperty(PropertyIds.OBJECT_TYPE_ID).getValue());
        System.out.println("OBJECT ID =" + root.getProperty(PropertyIds.OBJECT_ID).getValue());

        System.out.println("Childrens: ");
        System.out.println("---------- ");
        ItemIterable<CmisObject> children = root.getChildren();
        for (CmisObject child : children) {
            System.out.println("CHILD OBJECT TYPE ID = " + child.getProperty(PropertyIds.OBJECT_TYPE_ID).getValue());
            System.out.println("CHILD OBJECT ID = " + child.getProperty(PropertyIds.OBJECT_ID).getValue());
            System.out.println("CHILD NAME = " + child.getProperty(PropertyIds.NAME).getValue());
        }

        System.out.println("---------- ");

        
        Map params = new HashMap();
        params.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
        params.put(PropertyIds.NAME,  "child");

        root.createFolder(params);

        List props = root.getProperties();
        for (Object p : props) {
            System.out.println(p);
        }
        System.out.println(root.getProperties());
    }
}
