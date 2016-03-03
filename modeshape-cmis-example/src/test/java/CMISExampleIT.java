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

import static org.junit.Assert.assertEquals;
import java.net.HttpURLConnection;
import java.net.URL;
import org.junit.Test;

/**
 * Test that the deployed webapp returns 200 when pinged
 * 
 * @author Horia Chiorean (hchiorea@redhat.com)
 */
public class CMISExampleIT {

    @Test
    public void shouldAccessInitialPage() throws Exception {
        assertResponseCode("http://localhost:8090/modeshape-cmis-example/services", 200);
        assertResponseCode("http://localhost:8090/modeshape-cmis-example/services11", 200);
        assertResponseCode("http://localhost:8090/modeshape-cmis-example/atom", 200);
        assertResponseCode("http://localhost:8090/modeshape-cmis-example/atom11", 200);
        assertResponseCode("http://localhost:8090/modeshape-cmis-example/browser", 200);
    }
    
    private void assertResponseCode(String urlString, int code) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        assertEquals(code, connection.getResponseCode());
    }
}
