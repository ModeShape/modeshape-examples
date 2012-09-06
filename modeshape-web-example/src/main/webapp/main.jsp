<%@ page import="javax.jcr.*" %>

<html>
  <head>
	<title>Welcome to ModeShape</title>
	<style>
		.box={margin-left:150px; background-color:#A9F5F2; border:1px solid navy;}
	</style>
  </head>
  <body style="background-image:url(dblue106.gif)">
	<div style="background-image:url(dblue106.gif)">
		<div style="margin:5%;background-color:white; border:1px solid black">
			<div style="margin:5%">
				<table border="0" cellpadding="5px">
					<tr>
						<td><img border="0" align="left" height="64" width="64" src="modeshape_icon_64px_med.png"></td>
						<td><p><h1>MODESHAPE DEMO WEB APPLICATION</h1></p></td>
					</tr>
				</table>
				<hr>
				<p style="font: 10pt sans-sherif;">
					This is a demo which demonstrates two approaches of how to access Modeshape repository bound to JNDI from the web application.
					The first approach uses standard way to get a reference to the repository via JNDI API, the second way demonstrates how to utilize
					native JCR API and tread path to the same repository. 
				</p
				<p style="font: 10pt sans-sherif;">
					Before run this demo make sure that you have installed Modeshape repository and the repository is bound to some JNDI name. The simplest way to do it is use JBoss AS7 and install 
					Modeshape following to intructions provided by the <a href="https://docs.jboss.org/author/display/MODE/Installing+ModeShape+into+AS7"> wiki page</a>
					which explains how to start ModeShape engine as AS7 subsystem with two preconfigured sample repositories: <b>sample and artifacts</b> (see conf/standalone-modeshape.xml).
					Both repositories by default are bound to the JNDI under name <i>jcr/sample</i> and <i>jcr/artifacts</i> respectively so we don't need to do anything more with that configuration.
				</p>
				<p style="font: 10pt sans-sherif">
				    Using the form bellow it is possible to specify repository JNDI name and an absolute path to the node. Into the response this application will print
				    childs of the node pointed by the specified path. The format of  <i>Repository location</i> parameter defines which approach will be used 
				    to get the  access to the repository. If Repository location looks like URI  with 'jndi:' prefix (i.e. <i>jndi:/jcr/artifacts</i>) the native JCR API will be used
				    and otherwise (i.e. <i>jcr/artifacts</i>) the Initial context will be used to lookup repository in JNDI 
				</p>
				<div style="padding:2%; border:1px solid black; font: 10pt sans-sherif;background-image:url(lgrey029.jpg)">
					<p style="font: 14pt sans-sherif"><b>Repository parameters</b><hr></p>
					<form action="session.do">
						<table>
						   <tr>
							<td>
								<p style="font: 10pt sans-sherif"><b>Repository location</b></br>(jndi-name or URL jndi:jndi-name where jndi-name point to the repository in JNDI)</p>
							</td>
							<td style="vertical-align:bottom">
								<input type="text" name="repositoryURL" value="<%= session.getAttribute("repositoryURL") == null ? "" : session.getAttribute("repositoryURL") %>"  size="50"/>
							</td>
						   </tr>
						   <tr>
							<td>
								<p style="font: 10pt sans-sherif"><b>Node path</b></br>Absolute path to node. If you don't know path to node start exploring from root("/")</p>
							</td>
							<td style="vertical-align:bottom"><input type="text" name="path" value="<%= session.getAttribute("path") == null ?  "" : session.getAttribute("path") %>" size="50"/></td>
						   </tr>
						   <tr><td colspan="2" align="center"><p><input type="submit"/></p></td></tr>
						</table>
					</form>
				</div>
				<% if (session.getAttribute("errorMessage") != null) { %>
				<div style="border:1px solid red; color:red; margin-top:30px; padding:5%"><%= session.getAttribute("errorMessage") %></div>
				<%} else if (session.getAttribute("childs") != null){%>
				<!-- div style="border:1px solid black; margin-top:30px; padding:2%; font:10pt sans-serif" -->
				    <p style="font: 14pt sans-serif"><b>Selected node: <%= session.getAttribute("path") %></b></p>
				    <p style="font: 14pt sans-serif"><b>Childs:</b></p>
				    <ul style="font: 10pt sans-serif">
					<% 
					    NodeIterator it = (NodeIterator)session.getAttribute("childs");
					    while (it.hasNext()) {
						Node n = it.nextNode(); %>
						<li><%= n.getName() %></li>
					<% } %>
				    </ul>
				<!-- /div -->
				<% } %>
			</div>
		</div>
	</div>
    </body>
</html> 
