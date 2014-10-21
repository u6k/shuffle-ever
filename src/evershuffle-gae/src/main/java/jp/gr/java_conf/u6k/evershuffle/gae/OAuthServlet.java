
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
import org.scribe.oauth.OAuthService;

import com.evernote.auth.EvernoteService;

@SuppressWarnings("serial")
public class OAuthServlet extends HttpServlet {
    
    private static final Logger LOG=Logger.getLogger(OAuthServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        OAuthProperties oauthProp = new OAuthProperties(getServletContext());
        
        EvernoteService evernoteService=EvernoteService.SANDBOX;
        String callbackUrl="oauth.do?action=callbackReturn";
        
        HttpSession session=req.getSession();
        String accessToken=(String) session.getAttribute("accessToken");
        String requestToken=(String) session.getAttribute("requestToken");
        String requestTokenSecret=(String) session.getAttribute("requestTokenSecret");
        String verifier=(String) session.getAttribute("verifier");
        String noteStoreUrl=(String) session.getAttribute("noteStoreUrl");
        
        String action=req.getParameter("action");
        
        String thisUrl=req.getRequestURL().toString();
        String cbUrl=thisUrl.substring(0, thisUrl.lastIndexOf('/')+1)+callbackUrl;
        Class<? extends EvernoteApi> providerClass=EvernoteApi.Sandbox.class;
        if(evernoteService==EvernoteService.PRODUCTION){
            providerClass=EvernoteApi.class;
        }
        
        OAuthService oauthService=new ServiceBuilder().provider(providerClass).apiKey(oauthProp.getConsumerKey()).apiSecret(oauthProp.getConsumerSecret()).callback(cbUrl).build();
        
        try{
            if("reset".equals(action)){
                LOG.severe("Resetting");
                
                for(Enumeration<?> names=session.getAttributeNames();names.hasMoreElements();){
                    session.removeAttribute((String)names.nextElement());
                }
                accessToken=null;
                requestToken=null;
                requestTokenSecret=null;
                verifier=null;
                noteStoreUrl=null;
                
                LOG.info("Removed all attributes from user session");
            }else if("getRequestToken".equals(action)){
                Token scribeRequestToken=oauthService.getRequestToken();
                LOG.info("Reply: "+scribeRequestToken.getRawResponse());
                requestToken=scribeRequestToken.getToken();
                requestTokenSecret=scribeRequestToken.getSecret();
                session.setAttribute("requestToken", requestToken);
                session.setAttribute("requestTokenSecret", requestTokenSecret);
            }else if("getAccessToken".equals(action)){
                
            }
        }
    }

}