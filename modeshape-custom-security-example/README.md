# ModeShape Custom Security Example

This is a self-contained Maven project that shows how to implement and plug into ModeShape a custom security provider which 
performs both authorization and authentication. 
There are two important classes for this example:
- The [PicketBoxSecurityProvider](src/main/java/org/modeshape/example/security/PicketBoxSecurityProvider.java)
class is a simple implementation of a [PicketBox](http://picketbox.jboss.org/) authorization and authentication provider which 
plugs into the [ModeShape configuration](src/main/resources/my-repository-config.json) allowing
custom logic whenever an authentication operation is performed (via `session.login`) or whenever authorization checks are performed
for various repository operations (for example `node.add`). This is essentially an example very similar to the one described 
by the [ModeShape documentation](https://docs.jboss.org/author/display/MODE40/Authentication+and+authorization).

- The [ModeShapeExample](src/main/java/org/modeshape/example/security/ModeShapeExample.java) 
class has a 'main(...)' method that loads a ModeShape configuration as a resource on the classpath, uses that configuration to build a 
ModeShapeEngine instance, starts the engine, attempts to authenticate and then performs various repository operations which 
require authorization on a number of different users as defined by the [Picketbox configuration](src/main/resources/security.conf.xml)

This project is self-contained and can be built at the top level 
of your local clone of the Git repository, or by simply building 
this project using Maven 3:

    $ mvn clean install

See [this ModeShape community article](http://community.jboss.org/wiki/ModeShapeandMaven) 
for help on how to install Maven 3.

# The ModeShape project

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

# Need help?

ModeShape is open source software with a dedicated community. If you have 
any questions or problems, post a question in our 
[user forum](http://community.jboss.org/en/modeshape) or hop into our 
[IRC chat room](http://www.jboss.org/modeshape/chat) and talk our 
community of contributors and users.
