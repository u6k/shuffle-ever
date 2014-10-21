
package jp.gr.java_conf.u6k.evershuffle.gae;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContext;

public class OAuthProperties {

    private String _consumerKey;

    private String _consumerSecret;

    public OAuthProperties(ServletContext ctx) throws IOException {
        String oauthPropertiesPath = ctx.getRealPath("WEB-INF/oauth.properties");
        FileInputStream fin = new FileInputStream(oauthPropertiesPath);
        try {
            Properties oauthProperties = new Properties();
            oauthProperties.load(fin);

            _consumerKey = oauthProperties.getProperty("oauth.consumer_key");
            _consumerSecret = oauthProperties.getProperty("oauth.consumer_secret");
        } finally {
            fin.close();
        }
    }

    public String getConsumerKey() {
        return _consumerKey;
    }

    public String getConsumerSecret() {
        return _consumerSecret;
    }

}
