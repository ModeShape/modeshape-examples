# ModeShape Custom Logging Example

This is a simple self-contained Maven project that shows how to make ModeShape send log messages to a logging framework ([LogBack](http://logback.qos.ch)) 
that ModeShape is not normally aware of. 

Out of the box, ModeShape is able to discover and use several common logging frameworks:

1. [SLF4J](http://slf4j.org)
1. [Log4J](http://logging.apache.org/log4j)
1. A custom logging framework
1. [Java Util Logging](http://docs.oracle.com/javase/6/docs/technotes/guides/logging/index.html)

ModeShape first looks on the classpath to find the SLF4J API, and if so uses that. If it is not available, ModeShape then looks
on the classpath for the Log4J library, and if found ModeShape uses that. If neither SLF4J nor Log4J are found, then 
ModeShape will look on the classpath for the `org.modeshape.common.logging.CustomLoggerFactory` class (see below), 
and if found uses this. If none of these are found, the ModeShape falls back to the using the JDK logging facility.

## CustomLoggerFactory

Not all applications use one of the logging frameworks listed above, and so ModeShape provides a way for you to 
tell ModeShape how to use a completely different logging framework. To do this, you must provide the 
`org.modeshape.common.logging.CustomLoggerFactory` class (with that name and in that package) that extends ModeShape's
[org.modeshape.common.logging.LogFactory](https://github.com/ModeShape/modeshape/blob/master/modeshape-common/src/main/java/org/modeshape/common/logging/LogFactory.java)
abstract class and that implements the `getLogger(String)` method to return your own custom implementation of the
[org.modeshape.common.logging.Logger](https://github.com/ModeShape/modeshape/blob/master/modeshape-common/src/main/java/org/modeshape/common/logging/Logger.java)
class (which can be named anything an in any package). The `Logger` class is responsible for forwarding the ModeShape
log requests to the your logging framework.

This mechanism is actually quite simple, and doesn't require any special classloaders or extension APIs: simply provide 
the `CustomLoggerFactory` and a custom `Logger` implementation on the classpath, and ModeShape will use it.
(Be sure that SLF4J and Log4J are not on the classpath, or else ModeShape will use them instead.)


## Creating your own implementations

You can use this example project to create and test your own `CustomLoggerFactory` implementation. 
Simply change the project to use the desired logging framework, change the 
[org.modeshape.common.logging.CustomLogger](modeshape-custom-logging-example/src/main/java/org/modeshape/common/logging/CustomLogger.java)
class to use that framework, and then build and run the tests (see below). Once it works as you expect, put the 
[org.modeshape.common.logging.CustomLoggerFactory](modeshape-custom-logging-example/src/main/java/org/modeshape/common/logging/CustomLoggerFactory.java)
and [org.modeshape.common.logging.CustomLogger](modeshape-custom-logging-example/src/main/java/org/modeshape/common/logging/CustomLogger.java)
classes into your application's classpath. (For Java SE applications, you can simply include these classes in one of your application's JAR files.)


## About this example

The example provides an implementation of the 
[org.modeshape.common.logging.CustomLoggerFactory](modeshape-custom-logging-example/src/main/java/org/modeshape/common/logging/CustomLoggerFactory.java) class and then
sets up ModeShape as normal to run embedded within a simple J2SE application. The 
[ModeShapeExample](modeshape-custom-logging-example/src/main/java/org/modeshape/example/embedded/ModeShapeExample.java) class has a 'main(...)' method
that loads a ModeShape configuration as a resource on the classpath, uses that configuration to build a 
ModeShape JcrEngine instance, starts the engine, and obtains a JCR Session to the repository.

This project is self-contained and can be built at the top level of your local clone of the Git repository,
or by simply building this project using Maven 3:

    $ mvn clean install

See [this ModeShape community article](http://community.jboss.org/wiki/ModeShapeandMaven) for help on how to install Maven 3.

# The ModeShape project

ModeShape is an open source implementation of the JCR 2.0 ([JSR-283](http://www.jcp.org/en/jsr/detail?id=283])) specification and standard API.
To your applications, ModeShape looks and behaves like a regular JCR repository. Applications can search, query, navigate, change, version, listen for changes, etc.
But ModeShape can store that content in a variety of back-end stores (including relational databases, Infinispan data grids, JBoss Cache, etc.), or it can
access and update existing content from *other* kinds of systems (including file systems, SVN repositories, JDBC database metadata, and other JCR repositories).
ModeShape's connector architecture means that you can write custom connectors to access any kind of system. And ModeShape can even federate multiple back-end systems
into a single, unified virtual repository.

For more information on ModeShape, including getting started guides, reference guides, and downloadable binaries, visit the project's website at [http://www.modeshape.org]()
or follow us on our [blog](http://modeshape.wordpress.org) or on [Twitter](http://twitter.com/modeshape). Or hop into our [IRC chat room](http://www.jboss.org/modeshape/chat)
and talk our community of contributors and users.

The official Git repository for the project is also on GitHub at [http://github.com/ModeShape/modeshape]().

# Need help?

ModeShape is open source software with a dedicated community. If you have any questions or problems, post a question in our 
[user forum](http://community.jboss.org/en/modeshape) or hop into our [IRC chat room](http://www.jboss.org/modeshape/chat) and talk our community of contributors and users.
