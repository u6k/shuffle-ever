
package jp.gr.java_conf.u6k.evershuffle.gae;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class HelloServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Properties oauthProperties;

        String oauthPropertiesPath = getServletContext().getRealPath("WEB-INF/oauth.properties");
        FileInputStream fin = new FileInputStream(oauthPropertiesPath);
        try {
            oauthProperties = new Properties();
            oauthProperties.load(fin);
        } finally {
            fin.close();
        }

        resp.setContentType("text/plain");

        PrintWriter w = resp.getWriter();
        try {
            w.write("consumer_key=" + oauthProperties.getProperty("oauth.consumer_key") + "\n");
            w.write("consumer_secret=" + oauthProperties.getProperty("oauth.consumer_secret"));
        } finally {
            w.flush();
        }
    }

}
