<!--
~ ModeShape (http://www.modeshape.org)
~ See the COPYRIGHT.txt file distributed with this work for information
~ regarding copyright ownership. Some portions may be licensed
~ to Red Hat, Inc. under one or more contributor license agreements.
~ See the AUTHORS.txt file in the distribution for a full listing of
~ individual contributors.
~
~ ModeShape is free software. Unless otherwise indicated, all code in ModeShape
~ is licensed to you under the terms of the GNU Lesser General Public License as
~ published by the Free Software Foundation; either version 2.1 of
~ the License, or (at your option) any later version.
~
~ ModeShape is distributed in the hope that it will be useful,
~ but WITHOUT ANY WARRANTY; without even the implied warranty of
~ MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
~ Lesser General Public License for more details.
~
~ You should have received a copy of the GNU Lesser General Public
~ License along with this software; if not, write to the Free
~ Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
~ 02110-1301 USA, or see the FSF site: http://www.fsf.org.
-->
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:form="http://www.springframework.org/tags/form">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
    <title>ModeShape Spring Example</title>
    <style type="text/css">
        .error {
            color: #D8000C;
        }
    </style>
</head>

<body>

<h1>ModeShape Spring Example</h1>

<div id="container">
    <form:form id="mainForm" commandName="repositoryModel">
        <table>
            <tr>
                <td colspan="3"><div class="error"><c:out value="${globalError}"/></div></td>
            </tr>
            <tr>
                <td>Repository Name:</td>
                <td><c:out value="${repositoryModel.repositoryName}"/></td>
                <td>&nbsp;<form:hidden path="repositoryName"/></td>
            </tr>
            <tr>
                <td>Parent Absolute Path:</td>
                <td><form:input path="parentPath"/></td>
                <td><form:errors path="parentPath" cssClass="error"/></td>
            </tr>
            <tr>
                <td>New Node Name:</td>
                <td><form:input path="newNodeName"/></td>
                <td><form:errors path="newNodeName" cssClass="error"/></td>
            </tr>

            <tr>
                <td><input value="Load nodes" name="loadChildren" type="submit" id="btnLoad"/></td>
                <td><input value="New node" name="addNode" type="submit" id="btnNew"/></td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td>Children:</td>
                <td><form:textarea path="children" rows="5" cols="30" readonly="true"/></td>
                <td>&nbsp;</td>
            </tr>
        </table>
    </form:form>
</div>

</body>
</html>