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
 * Verifies the example app is deployed correctly
 * 
 * @author Horia Chiorean (hchiorea@redhat.com)
 */
public class JSFAndCDIExampleIT {

    @Test
    public void shouldAccessInitialPage() throws Exception {
        URL url = new URL("http://localhost:8080/modeshape-tomcat-jsf-example/");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        assertEquals(200, connection.getResponseCode());
    }
}
