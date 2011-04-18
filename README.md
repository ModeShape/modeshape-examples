# The ModeShape project

ModeShape is an open source implementation of the JCR 2.0 ([JSR-283](http://www.jcp.org/en/jsr/detail?id=283])) (aka, 'JCR') specification and standard API.
To your applications, ModeShape looks and behaves like a regular JCR repository. Applications can search, query, navigate, change, version, listen for changes, etc.
But ModeShape can store that content in a variety of back-end stores (including relational databases, Infinispan data grids, JBoss Cache, etc.), or it can
access and update existing content from *other* kinds of systems (including file systems, SVN repositories, JDBC database metadata, and other JCR repositories).
ModeShape's connector architecture means that you can write custom connectors to access any kind of system. And ModeShape can even federate multiple back-end systems
into a single, unified virtual repository.

For more information on ModeShape, including getting started guides, reference guides, and downloadable binaries, visit the project's website at [http://www.modeshape.org]()
or follow us on our [blog](http://modeshape.wordpress.org) or on [Twitter](http://twitter.com/modeshape). Or hop into our [IRC chat room](http://www.jboss.org/modeshape/chat)
and talk our community of contributors and users.

The official Git repository for the project is also on GitHub at [http://github.com/ModeShape/modeshape]().

# Examples

This Git repository contains examples showing how to use ModeShape within your applications. Each example is a self-contained Maven project
that is ready to use.

To run the examples, simply clone the repository:

    $ git clone git://github.com/ModeShape/modeshape-examples.git
    $ cd modeshape-examples

Then use Maven to build all of the examples (and run any unit tests):

    $ mvn clean install

or you can build an individual example. For instance:

    $ cd modeshape-embedded-example
    $ mvn clean install

See [this ModeShape community article](http://community.jboss.org/wiki/ModeShapeandMaven) for help on how to install Maven 3.

# Need help?

ModeShape is open source software with a dedicated community. If you have any questions or problems, post a question in our 
[user forum](http://community.jboss.org/en/modeshape) or hop into our [IRC chat room](http://www.jboss.org/modeshape/chat) and talk our community of contributors and users.

# Contribute an example

We're always looking for good, easy to follow examples. If you've written one and would like to help, simply use GitHub's Fork and Pull-Request techniques.

1. Use the "Fork" button at the top of [this page](https://github.com/ModeShape/modeshape-examples) on GitHub to create your own fork.
2. Clone your fork:
    $ git clone git@github.com:<you>/modeshape-examples.git
    $ cd modeshape-examples
    $ git remote add upstream git://github.com/ModeShape/modeshape-examples.git
3. Create a topic branch
    $ git checkout -b <branch-name>
4. Make your changes
5. When all of the examples (including yours) build, commit to that branch
    $ git commit .
6. Push your commit(s) to your fork on GitHub
    $ git push origin <branch-name>
7. On GitHub.com, go to your fork and switch branches to your topic branch, press the 'Pull Request' button, and fill out the form with the details.

We'll then review your submission and, if it works and builds, merge it into the examples repository.