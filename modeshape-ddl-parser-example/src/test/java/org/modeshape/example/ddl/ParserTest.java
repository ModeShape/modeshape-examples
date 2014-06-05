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
