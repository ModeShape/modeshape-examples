/*
 * ModeShape (http://www.modeshape.org)
 * See the COPYRIGHT.txt file distributed with this work for information
 * regarding copyright ownership.  Some portions may be licensed
 * to Red Hat, Inc. under one or more contributor license agreements.
 * See the AUTHORS.txt file in the distribution for a full listing of 
 * individual contributors.
 *
 * ModeShape is free software. Unless otherwise indicated, all code in ModeShape
 * is licensed to you under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 * 
 * ModeShape is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.modeshape.example.ddl;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import java.io.File;
import java.util.Iterator;
import org.junit.Before;
import org.junit.Test;
import org.modeshape.example.ddl.Parser.Column;
import org.modeshape.example.ddl.Parser.Database;
import org.modeshape.example.ddl.Parser.Schema;
import org.modeshape.example.ddl.Parser.Table;
import org.modeshape.example.ddl.Parser.View;

/**
 * 
 */
public class ParserTest {

    private Parser parser;
    private Database database;

    @Before
    public void beforeEach() {
        parser = new Parser();
    }

    @Test
    public void shouldParseSql92CreateSchema() throws Exception {
        database = parser.parse(testFile("create_schema.ddl"));
        assertThat(database.getName(), is("POSTGRES"));
        Schema schema = database.getSchema("hollywood", false);
        assertThat(schema.getName(), is("hollywood"));
        Table table = schema.getTable("films", false);
        assertThat(table.getName(), is("films"));
        Iterator<Column> columns = table.getColumns().values().iterator();
        assertColumn(columns.next(), "title", "VARCHAR", null, 255, 0, 0);
        assertColumn(columns.next(), "release", "DATE", null, 0, 0, 0);
        assertColumn(columns.next(), "producerName", "VARCHAR", null, 255, 0, 0);
        assertThat(columns.hasNext(), is(false));

        View view = schema.getView("winners", false);
        assertThat(view.getName(), is("winners"));
        assertThat(view.expression, is("CREATE VIEW winners AS SELECT title, release FROM films WHERE producerName IS NOT NULL;"));
    }

    protected File testFile( String path ) {
        return new File("src/test/resources/" + path);
    }

    protected Column assertColumn( Column actual,
                                   String name,
                                   String datatype,
                                   String defaultValue,
                                   int length,
                                   int precision,
                                   int scale ) {
        assertThat(actual.getName(), is(name));
        assertThat(actual.datatypeName, is(datatype));
        assertThat(actual.defaultValue, is(defaultValue));
        assertThat(actual.length, is(length));
        assertThat(actual.precision, is(precision));
        assertThat(actual.precision, is(scale));
        return actual;
    }

}
