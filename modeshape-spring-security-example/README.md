ModeShape Integration with Spring Security
=========================================================

What is it?
-----------

This is a self-contained and deployable Maven 3 project that shows how to integrate Modeshape with Spring Security for authentication and role-based authorization.


The example contains the following implementations:

1. `SpringSecurityProvider` implements `org.modeshape.jcr.security.AuthenticationProvider`
2. `SpringSecurityContext` implements `org.modeshape.jcr.security.SecurityContext`
3. `SpringSecurityCredentials` implements `javax.jcr.Credentials`

System requirements
-------------------

All you need to build this project is Java 7.0 (Java SDK 1.7) or better, Maven 3.0 or better.
The application this project produces is designed to be run on Tomcat 7.x or greater.

Build and Deploy the Example
-------------------------
_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must use the `settings.xml`
file from the root of this project. See [this ModeShape community article](http://community.jboss.org/wiki/ModeShapeandMaven)
for help on how to install and configure Maven 3._

This project is self-contained and can be built at the top level of your local clone of the Git repository, or by simply building this project
using Maven 3.

1. Type this command to build the archive:

        mvn clean package

2. This will produce a WAR file named `target/modeshape-spring-security-example.war`
3. Copy the above WAR into your local `%TOMCAT_HOME%/webapps` folder
4. Start the Tomcat server


Accessing the application
-------------------------

Open the following URL: <http://localhost:8080/modeshape-spring-security-example/jcr/> in your web browser.
You will be presented with a login form.<br/>The following users are pre-configured in spring security:


Username    | Password
----------- | --------
admin       | 123
user1       | 123
user2       | 123


After logging in using one of the above users, a sample web page will be displayed showing the following information:

1. Logged in user name.
2. Workspace name in the form "repository_name.workspace_name".
3. Granted roles for the current user.

The ModeShape project
---------------------
ModeShape is an open source implementation of the JCR 2.0 
([JSR-283](http://www.jcp.org/en/jsr/detail?id=283])) specification and 
standard API. To your applications, ModeShape looks and behaves like a 
regular JCR repository. Applications can search, query, navigate, change, 
version, listen for changes, etc. But ModeShape can store that content 
in a variety of back-end stores (including relational databases, Infinispan 
data grids, JBoss Cache, etc.), or it can access and update existing content 
from *other* kinds of systems (including file systems, SVN repositories, 
JDBC database metadata, and other JCR repositories). ModeShape's connector 
architecture means that you can write custom connectors to access any 
kind of system. And ModeShape can even federate multiple back-end systems 
into a single, unified virtual repository.

For more information on ModeShape, including getting started guides, 
reference guides, and downloadable binaries, visit the project's website 
at [http://www.modeshape.org]() or follow us on our [blog](http://modeshape.wordpress.org) 
or on [Twitter](http://twitter.com/modeshape). Or hop into our 
[IRC chat room](http://www.jboss.org/modeshape/chat) and talk our community 
of contributors and users.

The official Git repository for the project is also on GitHub at 
[http://github.com/ModeShape/modeshape]().

Need help ?
-----------

ModeShape is open source software with a dedicated community. If you have 
any questions or problems, post a question in our 
[user forum](http://community.jboss.org/en/modeshape) or hop into our 
[IRC chat room](http://www.jboss.org/modeshape/chat) and talk our 
community of contributors and users.
