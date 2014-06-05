/*
 * ModeShape (http://www.modeshape.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.modeshape.example.ddl;

import static org.modeshape.sequencer.ddl.StandardDdlLexicon.DATATYPE_LENGTH;
import static org.modeshape.sequencer.ddl.StandardDdlLexicon.DATATYPE_NAME;
import static org.modeshape.sequencer.ddl.StandardDdlLexicon.DATATYPE_PRECISION;
import static org.modeshape.sequencer.ddl.StandardDdlLexicon.DATATYPE_SCALE;
import static org.modeshape.sequencer.ddl.StandardDdlLexicon.DDL_EXPRESSION;
import static org.modeshape.sequencer.ddl.StandardDdlLexicon.DEFAULT_VALUE;
import static org.modeshape.sequencer.ddl.StandardDdlLexicon.TYPE_ADD_COLUMN_DEFINITION;
import static org.modeshape.sequencer.ddl.StandardDdlLexicon.TYPE_ALTER_COLUMN_DEFINITION;
import static org.modeshape.sequencer.ddl.StandardDdlLexicon.TYPE_ALTER_TABLE_STATEMENT;
import static org.modeshape.sequencer.ddl.StandardDdlLexicon.TYPE_COLUMN_DEFINITION;
import static org.modeshape.sequencer.ddl.StandardDdlLexicon.TYPE_CREATE_SCHEMA_STATEMENT;
import static org.modeshape.sequencer.ddl.StandardDdlLexicon.TYPE_CREATE_TABLE_STATEMENT;
import static org.modeshape.sequencer.ddl.StandardDdlLexicon.TYPE_CREATE_VIEW_STATEMENT;
import static org.modeshape.sequencer.ddl.StandardDdlLexicon.TYPE_DROP_SCHEMA_STATEMENT;
import static org.modeshape.sequencer.ddl.StandardDdlLexicon.TYPE_DROP_TABLE_STATEMENT;
import static org.modeshape.sequencer.ddl.StandardDdlLexicon.TYPE_DROP_VIEW_STATEMENT;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedHashMap;
import java.util.Map;
import org.modeshape.sequencer.ddl.DdlParsers;
import org.modeshape.sequencer.ddl.StandardDdlLexicon;
import org.modeshape.sequencer.ddl.node.AstNode;

/**
 * A simple example application that demonstrates how to parse DDL files to create an Abstract Syntax Tree (AST) as a ModeShape
 * graph and to then post-processes that graph.
 * <p>
 * The {@link #parse(File)} method returns a {@link Database} object that contains a final representation of the structure of an
 * empty database after the supplied (simple) schema is applied. After all, DDL statements are actually operations against a
 * database, so a DDL file with some statements (like "DROP TABLE foo") don't make sense when applied to an empty database.
 * </p>
 * <p>
 * However, this example works really well when processing DDL statements that create tables, views, and schemas.
 * </p>
 * <p>
 * Note: this example does not process all of the available DDL statements, so many statements (e.g., foreign keys, primary keys,
 * etc.) are ignored. Support for these are left to the reader. The actual structure of the files is described by the following
 * JCR CND node type definition files contained within the "modeshape-sequencer-ddl" library:
 * <ul>
 * <li>org.modeshape.sequencer.ddl.StandardDdl.cnd</li>
 * <li>org.modeshape.sequencer.ddl.dialect.derby.DerbyDdl.cnd</li>
 * <li>org.modeshape.sequencer.ddl.dialect.oracle.OracleDdl.cnd</li>
 * <li>org.modeshape.sequencer.ddl.dialect.postgres.PostgresDdl.cnd</li>
 * </ul>
 * </p>
 */
public class Parser {

    public static void main( String[] argv ) {
        // Process the arguments ...
        String filename = null;
        for (String arg : argv) {
            // First non-flag parameter is the filename ...
            if (filename == null) filename = arg;
        }

        // Figure out if the arguments are valid ...
        if (filename == null) {
            printUsage(System.out);
            System.exit(1);
        }
        File file = new File(filename);
        if (!file.exists()) printError(-1, "The file \"" + filename + "\" does not exist.");
        if (!file.isFile()) printError(-2, "File could not be found at \"" + file + "\"");
        if (!file.canRead()) printError(-3, "Unable to read file \"" + filename + "\".");

        // Now parse the file ...
        try {
            Parser parser = new Parser();
            parser.parse(file);
        } catch (Throwable t) {
            printError(-10, t.getMessage());
        }
    }

    protected static void printError( int exitCode,
                                      String message ) {
        System.err.println("Error: " + message);
        System.err.println();
        printUsage(System.out);
        System.exit(exitCode);
    }

    protected static void printUsage( PrintStream stream ) {
        stream.println("Usage:   " + Parser.class.getCanonicalName() + " filename");
        stream.println();
        stream.println("   where");
        stream.println("     filename         is the name of the DDL file to be parsed");
        stream.println();
    }

    protected static String readFile( File file ) throws IOException {
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        try {
            char[] buf = new char[1024];
            int numRead = 0;
            while ((numRead = reader.read(buf)) != -1) {
                String readData = String.valueOf(buf, 0, numRead);
                fileData.append(readData);
                buf = new char[1024];
            }
            return fileData.toString();
        } finally {
            reader.close();
        }
    }

    protected boolean print = true;

    public Database parse( File file ) throws IOException {
        // Read the file into a single string ...
        String ddl = readFile(file);

        // Create the object that will parse the file ...
        DdlParsers parsers = new DdlParsers();
        AstNode node = parsers.parse(ddl, file.getName());

        // Now process the AST ...
        System.out.println(node.toString());
        return processStatements(node);
    }

    /**
     * Process the top-level 'ddl:statements' node, which contains information about the parsed content.
     * 
     * @param node the node; may not be null
     * @return the database
     */
    protected Database processStatements( AstNode node ) {
        assert node.getName().equals(StandardDdlLexicon.STATEMENTS_CONTAINER);

        // Get the dialect that we've inferred ...
        String dialectId = string(node.getProperty(StandardDdlLexicon.PARSER_ID));

        // Now process the children of the statements node ...
        Database database = new Database(dialectId);
        processChildrenOf(node, database);
        return database;
    }

    protected void processChildrenOf( AstNode node,
                                      NamedComponent parent ) {
        for (AstNode child : node) {
            process(child, parent);
        }
    }

    protected void process( AstNode node,
                            NamedComponent parent ) {
        String mixin = string(node.getProperty("jcr:mixinTypes"));

        // There are lots of different types of AST nodes, but we're only going to process a few ...

        if (TYPE_CREATE_SCHEMA_STATEMENT.equals(mixin)) processCreateSchema(node, (SchemaContainer)parent);
        else if (TYPE_DROP_SCHEMA_STATEMENT.equals(mixin)) processDropSchema(node, (SchemaContainer)parent);

        else if (TYPE_CREATE_TABLE_STATEMENT.equals(mixin)) processCreateTable(node, (TableContainer)parent);
        else if (TYPE_ALTER_TABLE_STATEMENT.equals(mixin)) processAlterTable(node, (TableContainer)parent);
        else if (TYPE_DROP_TABLE_STATEMENT.equals(mixin)) processDropTable(node, (TableContainer)parent);

        else if (TYPE_CREATE_VIEW_STATEMENT.equals(mixin)) processCreateView(node, (ViewContainer)parent);
        else if (TYPE_DROP_VIEW_STATEMENT.equals(mixin)) processDropView(node, (ViewContainer)parent);

        else if (TYPE_COLUMN_DEFINITION.equals(mixin)) processColumnDefinition(node, (ColumnContainer)parent);
        else if (TYPE_ADD_COLUMN_DEFINITION.equals(mixin)) processColumnDefinition(node, (ColumnContainer)parent);
        else if (TYPE_ALTER_COLUMN_DEFINITION.equals(mixin)) processColumnDefinition(node, (ColumnContainer)parent);
    }

    protected void processCreateSchema( AstNode node,
                                        SchemaContainer parent ) {
        String name = string(node.getName());
        print("Create schema \"" + name + "\"");
        Schema schema = parent.getSchema(name, true);
        processChildrenOf(node, schema);
    }

    protected void processDropSchema( AstNode node,
                                      SchemaContainer parent ) {
        String name = string(node.getName());
        print("Drop schema \"" + name + "\"");
        parent.getSchemas().remove(name);
    }

    protected void processCreateTable( AstNode node,
                                       TableContainer parent ) {
        String name = string(node.getName());
        print("Create table \"" + name + "\"");
        Table table = parent.getTable(name, true);
        processChildrenOf(node, table);
    }

    protected void processAlterTable( AstNode node,
                                      TableContainer parent ) {
        String name = string(node.getName());
        print("Alter table \"" + name + "\"");
        Table table = parent.getTable(name, true);
        processChildrenOf(node, table);
    }

    protected void processDropTable( AstNode node,
                                     TableContainer parent ) {
        String name = string(node.getName());
        print("Drop table \"" + name + "\"");
        parent.getTables().remove(name);
    }

    protected void processCreateView( AstNode node,
                                      ViewContainer parent ) {
        String name = string(node.getName());
        print("Create view \"" + name + "\"");
        View view = parent.getView(name, true);

        Object prop = node.getProperty(DDL_EXPRESSION);
        if (prop != null) view.expression = string(prop);

        processChildrenOf(node, view);
    }

    protected void processDropView( AstNode node,
                                    ViewContainer parent ) {
        String name = string(node.getName());
        print("Drop view \"" + name + "\"");
        parent.getViews().remove(name);
    }

    protected void processColumnDefinition( AstNode node,
                                            ColumnContainer parent ) {
        String name = string(node.getName());
        print("Column column \"" + name + "\"");
        Column column = parent.getColumn(name, true);

        Object prop = node.getProperty(DATATYPE_LENGTH);
        if (prop != null) column.length = (int)number(prop);

        prop = node.getProperty(DATATYPE_NAME);
        if (prop != null) column.datatypeName = string(prop);

        prop = node.getProperty(DATATYPE_PRECISION);
        if (prop != null) column.precision = (int)number(prop);

        prop = node.getProperty(DATATYPE_SCALE);
        if (prop != null) column.scale = (int)number(prop);

        prop = node.getProperty(DEFAULT_VALUE);
        if (prop != null) column.defaultValue = string(prop);
    }

    /**
     * Convenience method to transform a property value into a string representation. Property values are often able to be
     * tranformed into multiple types, so the ModeShape Graph API is designed so that you always convert the values into the
     * desired type.
     * 
     * @param value the property value; may be null
     * @return the string representation of the value, or null if the value was null
     */
    protected String string( Object value ) {
        return value == null ? null : value.toString();
    }

    protected long number( Object value ) {
        return value == null ? null : (Long)value;
    }

    protected void print( String message ) {
        if (print) System.out.println(message);
    }

    public static abstract class NamedComponent {
        protected final String name;

        protected NamedComponent( String name ) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    public static class Table extends NamedComponent implements ColumnContainer {
        protected final Map<String, Column> columns = new LinkedHashMap<String, Column>();

        public Table( String name ) {
            super(name);
        }

        @Override
        public Column getColumn( String name,
                                 boolean createIfMissing ) {
            Column column = columns.get(name);
            if (column == null && createIfMissing) {
                column = new Column(name);
                columns.put(name, column);
            }
            return column;
        }

        @Override
        public Map<String, Column> getColumns() {
            return columns;
        }
    }

    public static class View extends NamedComponent {
        protected String expression;

        public View( String name ) {
            super(name);
        }
    }

    public static class Column extends NamedComponent {
        protected int length;
        protected String datatypeName;
        protected int precision;
        protected int scale;
        protected String defaultValue;

        public Column( String name ) {
            super(name);
        }
    }

    public static interface SchemaContainer {
        Schema getSchema( String name,
                          boolean createIfMissing );

        Map<String, Schema> getSchemas();
    }

    public static interface TableContainer {
        Table getTable( String name,
                        boolean createIfMissing );

        Map<String, Table> getTables();
    }

    public static interface ViewContainer {
        View getView( String name,
                      boolean createIfMissing );

        Map<String, View> getViews();
    }

    public static interface ColumnContainer {
        Column getColumn( String name,
                          boolean createIfMissing );

        Map<String, Column> getColumns();
    }

    public static class Schema extends NamedComponent implements TableContainer, ViewContainer {
        protected final Map<String, Table> tables = new LinkedHashMap<String, Table>();
        protected final Map<String, View> views = new LinkedHashMap<String, View>();

        public Schema( String name ) {
            super(name);
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.modeshape.example.ddl.Parser.TableContainer#getTable(java.lang.String, boolean)
         */
        @Override
        public Table getTable( String name,
                               boolean createIfMissing ) {
            Table table = tables.get(name);
            if (table == null && createIfMissing) {
                table = new Table(name);
                tables.put(name, table);
            }
            return table;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.modeshape.example.ddl.Parser.TableContainer#getTables()
         */
        @Override
        public Map<String, Table> getTables() {
            return tables;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.modeshape.example.ddl.Parser.ViewContainer#getView(java.lang.String, boolean)
         */
        @Override
        public View getView( String name,
                             boolean createIfMissing ) {
            View view = views.get(name);
            if (view == null && createIfMissing) {
                view = new View(name);
                views.put(name, view);
            }
            return view;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.modeshape.example.ddl.Parser.ViewContainer#getViews()
         */
        @Override
        public Map<String, View> getViews() {
            return views;
        }
    }

    public static class Database extends NamedComponent implements TableContainer, ViewContainer, SchemaContainer {
        protected final Map<String, Table> tables = new LinkedHashMap<String, Table>();
        protected final Map<String, View> views = new LinkedHashMap<String, View>();
        protected final Map<String, Schema> schemas = new LinkedHashMap<String, Schema>();

        public Database( String name ) {
            super(name);
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.modeshape.example.ddl.Parser.TableContainer#getTable(java.lang.String, boolean)
         */
        @Override
        public Table getTable( String name,
                               boolean createIfMissing ) {
            Table table = tables.get(name);
            if (table == null && createIfMissing) {
                table = new Table(name);
                tables.put(name, table);
            }
            return table;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.modeshape.example.ddl.Parser.TableContainer#getTables()
         */
        @Override
        public Map<String, Table> getTables() {
            return tables;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.modeshape.example.ddl.Parser.ViewContainer#getView(java.lang.String, boolean)
         */
        @Override
        public View getView( String name,
                             boolean createIfMissing ) {
            View view = views.get(name);
            if (view == null && createIfMissing) {
                view = new View(name);
                views.put(name, view);
            }
            return view;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.modeshape.example.ddl.Parser.ViewContainer#getViews()
         */
        @Override
        public Map<String, View> getViews() {
            return views;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.modeshape.example.ddl.Parser.SchemaContainer#getSchema(java.lang.String, boolean)
         */
        @Override
        public Schema getSchema( String name,
                                 boolean createIfMissing ) {
            Schema schema = schemas.get(name);
            if (schema == null && createIfMissing) {
                schema = new Schema(name);
                schemas.put(name, schema);
            }
            return schema;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.modeshape.example.ddl.Parser.SchemaContainer#getSchemas()
         */
        @Override
        public Map<String, Schema> getSchemas() {
            return schemas;
        }
    }

}
