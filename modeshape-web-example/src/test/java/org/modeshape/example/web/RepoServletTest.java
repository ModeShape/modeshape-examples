package org.modeshape.example.web;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.Assert.*;
import org.junit.*;
import org.junit.runner.RunWith;
import sun.net.www.protocol.http.HttpURLConnection;

@RunWith(Arquillian.class)
@RunAsClient
public class RepoServletTest {

    @Deployment
    public static WebArchive createDeployment() {

        return ShrinkWrap.create(WebArchive.class, "modeshape-example-web.war").addClass(RepoServlet.class)
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/jboss-web.xml"))
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/jboss-deployment-structure.xml"))
                .addAsWebResource(new File("src/main/webapp/dblue106.gif"))
                .addAsWebResource(new File("src/main/webapp/lgrey029.jpg"))
                .addAsWebResource(new File("src/main/webapp/main.jsp"))
                .addAsWebResource(new File("src/main/webapp/modeshape_icon_64px_med.png"))
                .setWebXML(new File("src/main/webapp/WEB-INF/web.xml"));

    }

    @Test
    public void shouldExecuteQuery() throws Exception {

        URL url = new URL("http://localhost:8080/modeshape/session.do?repositoryURL=jcr/sample&path=/");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible )");
        connection.setRequestProperty("Accept","*/*");
        connection.setDoOutput(true);
        connection.setDoInput(true);

        connection.connect();

        int responseCode = connection.getResponseCode();
        Assert.assertEquals(200, responseCode);

    }

}

