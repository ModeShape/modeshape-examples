# ModeShape Web application example

This is a simple self-contained Maven project that shows how to get 
access repository bound in JNDI either via native JCR API or using 
JNDI API.

Before run this demo make sure that you have installed Modeshape 
repository and the repository is bound to some JNDI name. The 
simplest way to do it is use JBoss AS7 and install Modeshape following 
to intructions provided by our 
[wiki page](https://docs.jboss.org/author/display/MODE/Installing+ModeShape+into+AS7) 
which explains how to start ModeShape engine as AS7 subsystem with 
two preconfigured sample repositories: sample and artifacts (see 
`conf/standalone-modeshape.xml`). Both repositories by default 
are bound to the JNDI under name jcr/sample and jcr/artifacts 
respectively so we don't need to do anything more with that 
configuration.

User is able to specify repository JNDI name and an absolute path 
to the node. Into the response this application will show childs 
of the node pointed by the specified path. The format of Repository 
location parameter defines which approach will be used to get the 
access to the repository. If Repository location looks like URI with 
'jndi:' prefix (i.e. jndi:/jcr/artifacts) the native JCR API will be 
used and otherwise (i.e. jcr/artifacts) the Initial context will be 
used to lookup repository in JNDI 

This project is self-contained and can be built at the top level of 
your local clone of the Git repository,
or by simply building this project using Maven 3:

    $ mvn clean install -s ../settings.xml

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
