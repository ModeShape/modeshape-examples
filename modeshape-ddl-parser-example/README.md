# ModeShape DDL Parsing Example

The [ModeShape DDL Sequencer library](http://docs.jboss.org/modeshape/latest/manuals/reference/html_single/reference-guide-en.html#ddl-file-sequencer) 
contains a DDL parser capable of parsing many common DDL statements 
conforming to SQL-92, MySQL, Oracle, PostgreSQL, Sybase, DB2, and 
Derby. The sequencer uses this parser to build an internal AST 
representation of the DDL statements, and then the sequencer turns 
that AST representation into a graph representation (which ModeShape 
then persists as part of the sequencing service).

However, this DDL parser can be used on its own, as long as one 
understands the parsers limitations and is willing to process the 
very generic AST representation for DDL statements that can vary 
wildly. 

It is also important to understand that DDL statements actually 
describe operations to be performed against a database schema. 
Many of us are familiar with DDL that creates a set of tables and 
views, and transforming such statements into a model of a database 
is relatively straightforward. But this transformation becomes more 
complicated when the DDL contain ALTER, DROP, ADD COLUMN and other 
such statements. 

This example shows how to use the DDL parser to produce a simplistic 
model of a database schema. It generally works against the more common 
create-type DDL statements, although some ALTER statements are supported. 
The example also does not process primary keys, indexes, foreign keys, 
and other more detailed statements; these are left to the reader, but 
we'd welcome pull-requests with improvements or changes.

# Building

This project is self-contained and can be built at the top level of your 
local clone of the Git repository, or by simply building this project 
using Maven 3:

    $ mvn clean install

See [this ModeShape community article](http://community.jboss.org/wiki/ModeShapeandMaven) 
for help on how to install Maven 3.

# The ModeShape project

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

# Need help?

ModeShape is open source software with a dedicated community. If you have 
any questions or problems, post a question in our 
[user forum](http://community.jboss.org/en/modeshape) or hop into our 
[IRC chat room](http://www.jboss.org/modeshape/chat) and talk our 
community of contributors and users.
