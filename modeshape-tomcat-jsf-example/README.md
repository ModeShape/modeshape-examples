Example Using ModeShape from a JSF application via CDI
=========================================================

What is it?
-----------

This is a self-contained and deployable Maven 3 project that shows how to get access and use a repository from
a JSF 2 application, using only CDI.

System requirements
-------------------

All you need to build this project is Java 8.0 (Java SDK 1.8) or better, Maven 3.0 or better.
The application this project produces is designed to be run on Tomcat 7.x or greater.

Build and Deploy the Example
-------------------------
_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must use the `settings.xml`
file from the root of this project. See [this ModeShape community article](http://community.jboss.org/wiki/ModeShapeandMaven)
for help on how to install and configure Maven 3._

This project is self-contained and can be built at the top level of your local clone of the Git repository, or by simply building this project
using Maven 3.

1. Type this command to build the archive:

        mvn clean package cargo:run

2. This will produce a WAR file named `target/modeshape-tomcat-jsf-example.war`, download a Tomcat 7.x instance (via the Maven Cargo plugin),
deploy & start this web application in the Tomcat instance

Build and Deploy the Example manually into an existing Tomcat instance
----------------------------------------------------------------------
_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must use the `settings.xml`
file from the root of this project. See [this ModeShape community article](http://community.jboss.org/wiki/ModeShapeandMaven)
for help on how to install and configure Maven 3._

This project is self-contained and can be built at the top level of your local clone of the Git repository, or by simply building this project
using Maven 3.

1. Type this command to build the archive:

        mvn clean package

2. This will produce a WAR file named `target/modeshape-tomcat-jsf-example.war`
3. Copy the above WAR into your local `%TOMCAT_HOME%/webapps` folder
4. Start the Tomcat server

Accessing the application
-------------------------

The application will be running by default at the following URL: <http://localhost:8080/modeshape-tomcat-jsf-example/>.
The user is presented with a form where he can input one of the following:

1. Parent Absolute Path - an absolute node path
2. New Node Name - a simple string which represents the name of new node that can be added

based on which one of the following actions can be performed

1. Show children - displays the children of node located at "Parent Absolute Path"
2. Add Node - add a new child with the given name under the node located at "Parent Absolute Path"

The ModeShape project
---------------------
ModeShape is an open source implementation of the JCR 2.0 
([JSR-283](http://www.jcp.org/en/jsr/detail?id=283])) specification and 
standard API. To your applications, ModeShape looks and behaves like a 
regular JCR repository. Applications can search, query, navigate, change, 
version, listen for changes, etc. But ModeShape can store that content 
in a variety of back-end stores or it can access and update existing content 
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
