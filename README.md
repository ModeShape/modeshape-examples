[![License](http://img.shields.io/:license-apache%202.0-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.modeshape/modeshape-parent/badge.svg)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.modeshape%22)
[![Build Status](https://travis-ci.org/ModeShape/modeshape-examples.svg?branch=master)](https://travis-ci.org/ModeShape/modeshape-examples)

# The ModeShape project

ModeShape is an open source implementation of the JCR 2.0 
([JSR-283](http://www.jcp.org/en/jsr/detail?id=283])) 
(aka, 'JCR') specification and standard API. To your applications, 
ModeShape looks and behaves like a regular JCR repository. Applications 
can search, query, navigate, change, version, listen for changes, etc. 
But ModeShape can store that content in a variety of back-end stores 
or it can access and update existing content from *other* kinds 
of systems (including file systems, SVN repositories, JDBC database 
metadata, and other JCR repositories). ModeShape's connector architecture 
means that you can write custom connectors to access any kind of system. 
And ModeShape can even federate multiple back-end systems into a single, 
unified virtual repository.

For more information on ModeShape, including getting started guides, 
reference guides, and downloadable binaries, visit the project's 
website at [http://www.modeshape.org]() or follow us on our 
[blog](http://modeshape.wordpress.org) or on 
[Twitter](http://twitter.com/modeshape). Or hop into our 
[IRC chat room](http://www.jboss.org/modeshape/chat)
and talk our community of contributors and users.

The official Git repository for the project is also on GitHub 
at [http://github.com/ModeShape/modeshape]().

# Examples

This Git repository contains examples showing how to use ModeShape 
within your applications. Each example is a self-contained Maven project
that is ready to use.

To run the examples, simply clone the repository:

    $ git clone git://github.com/ModeShape/modeshape-examples.git
    $ cd modeshape-examples

Then use Maven to build all of the examples (and run any unit tests):

    $ mvn clean install -s settings.xml

or you can build an individual example. For instance:

    $ cd modeshape-embedded-example
    $ mvn clean install -s settings.xml

See [this ModeShape community article](http://community.jboss.org/wiki/ModeShapeandMaven) 
for help on how to install Maven 3.

# Looking for ModeShape 4.x or 3.x examples?

The code on the 'master' branch works against the latest ModeShape 5.x 
release, but examples for ModeShape 4.x and 3.x are on different
branches. To get to those, use the Git `checkout` command to 
switch branches:

    $ git checkout 4.x
    
or

    $ git checkout 3.x

and then use the Maven command to build the examples (same as above).

# Need help?

ModeShape is open source software with a dedicated community. If you 
have any questions or problems, post a question in our 
[user forum](http://community.jboss.org/en/modeshape) or hop into our 
[IRC chat room](http://www.jboss.org/modeshape/chat) and talk our 
community of contributors and users.

# Contribute an example

We're always looking for good, easy to follow examples. If you've written 
one and would like to help, simply use GitHub's Fork and Pull-Request 
techniques.

1. Use the "Fork" button at the top of [this page](https://github.com/ModeShape/modeshape-examples) 
on GitHub to create your own fork.
2. Clone your fork:
<pre>
    $ git clone git@github.com:&lt;you>/modeshape-examples.git
    $ cd modeshape-examples
    $ git remote add upstream git://github.com/ModeShape/modeshape-examples.git
</pre>
3. Create a topic branch
<pre>
    $ git checkout -b &lt;branch-name>
</pre>
4. Make your changes
5. When all of the examples (including yours) build, commit to that branch
<pre>
    $ git commit .
</pre>
6. Push your commit(s) to your fork on GitHub
<pre>
    $ git push origin &lt;branch-name>
</pre>
7. On GitHub.com, go to your fork and switch branches to your topic branch, press the 'Pull Request' button, and fill out the form with the details.

We'll then review your submission and, if it works and builds, merge it into the examples repository.