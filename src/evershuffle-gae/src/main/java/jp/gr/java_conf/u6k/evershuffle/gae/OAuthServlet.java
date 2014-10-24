
package jp.gr.java_conf.u6k.evershuffle.gae;

import java.io.IOException;
import java.util.Enumeration;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.EvernoteApi;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import com.evernote.auth.EvernoteAuth;
import com.evernote.auth.EvernoteService;

@SuppressWarnings("serial")
public class OAuthServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(OAuthServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        OAuthProperties oauthProp = new OAuthProperties(getServletContext());

        EvernoteService evernoteService = new EvernoteUtil().getEvernoteService();
        String callbackUrl = "oauth.do?action=callbackReturn";

        HttpSession session = req.getSession();
        String accessToken = (String) session.getAttribute("accessToken");
        String requestToken = (String) session.getAttribute("requestToken");
        String requestTokenSecret = (String) session.getAttribute("requestTokenSecret");
        String verifier = (String) session.getAttribute("verifier");
        String noteStoreUrl = (String) session.getAttribute("noteStoreUrl");

        String action = req.getParameter("action");

        String thisUrl = req.getRequestURL().toString();
        String cbUrl = thisUrl.substring(0, thisUrl.lastIndexOf('/') + 1) + callbackUrl;
        Class<? extends EvernoteApi> providerClass = EvernoteApi.Sandbox.class;
        if (evernoteService == EvernoteService.PRODUCTION) {
            providerClass = EvernoteApi.class;
        }

        OAuthService oauthService = new ServiceBuilder().provider(providerClass).apiKey(oauthProp.getConsumerKey()).apiSecret(oauthProp.getConsumerSecret()).callback(cbUrl).build();

        if ("reset".equals(action)) {
            LOG.severe("Resetting");

            for (Enumeration<?> names = session.getAttributeNames(); names.hasMoreElements();) {
                session.removeAttribute((String) names.nextElement());
            }
            accessToken = null;
            requestToken = null;
            requestTokenSecret = null;
            verifier = null;
            noteStoreUrl = null;

            LOG.info("Removed all attributes from user session");
        } else if ("getRequestToken".equals(action)) {
            Token scribeRequestToken = oauthService.getRequestToken();
            LOG.info("Reply: " + scribeRequestToken.getRawResponse());
            requestToken = scribeRequestToken.getToken();
            requestTokenSecret = scribeRequestToken.getSecret();
            session.setAttribute("requestToken", requestToken);
            session.setAttribute("requestTokenSecret", requestTokenSecret);
        } else if ("getAccessToken".equals(action)) {
            Token scribeRequestToken = new Token(requestToken, requestTokenSecret);
            Verifier scribeVerifier = new Verifier(verifier);
            Token scribeAccessToken = oauthService.getAccessToken(scribeRequestToken, scribeVerifier);
            EvernoteAuth evernoteAuth = EvernoteAuth.parseOAuthResponse(evernoteService, scribeAccessToken.getRawResponse());
            LOG.info("Reply: " + scribeAccessToken.getRawResponse());

            accessToken = evernoteAuth.getToken();
            noteStoreUrl = evernoteAuth.getNoteStoreUrl();
            session.setAttribute("accessToken", accessToken);
            session.setAttribute("noteStoreUrl", noteStoreUrl);
        } else if ("callbackReturn".equals(action)) {
            requestToken = req.getParameter("oauth_token");
            verifier = req.getParameter("oauth_verifier");
            session.setAttribute("verifier", verifier);
        }

        LOG.info("Evernote EDAM API Web Test State");
        LOG.info("Consumer key: " + oauthProp.getConsumerKey());
        LOG.info("Request token URL: " + evernoteService.getRequestTokenEndpoint());
        LOG.info("Access token URL: " + evernoteService.getAccessTokenEndpoint());
        LOG.info("Authorization URL Base: " + evernoteService.getHost());
        LOG.info("User request token: " + requestToken);
        LOG.info("User request token secret: " + requestTokenSecret);
        LOG.info("User oauth verifier: " + verifier);
        LOG.info("User access token: " + accessToken);
        LOG.info("User NoteStore URL: " + noteStoreUrl);

        if (requestToken == null && accessToken == null) {
            LOG.info("Step 1 in OAuth authorization: obtain an unauthorized request token from the provider");
            resp.sendRedirect(req.getContextPath() + "/oauth.do?action=getRequestToken");
        } else if (requestToken != null && verifier == null && accessToken == null) {
            LOG.info("Step 2 in OAuth authorization: redirect the user to the provider to authorize the request token");
            resp.sendRedirect(evernoteService.getAuthorizationUrl(requestToken));
        } else if (requestToken != null && verifier != null && accessToken == null) {
            LOG.info("Step 3 in OAuth authorization: exchange the authorized request token for an access token");
            resp.sendRedirect(req.getContextPath() + "/oauth.do?action=getAccessToken");
        } else if (accessToken != null) {
            LOG.info("Step 4 in OAuth authorization: use the access token that you obtained");
            if (!"mobile".equals(session.getAttribute("mode"))) {
                resp.sendRedirect(req.getContextPath() + "/index.jsp");
            } else {
                resp.sendRedirect(req.getContextPath() + "/m/index.jsp");
            }
        } else {
            LOG.severe("Unknown Step");
        }
    }

}
