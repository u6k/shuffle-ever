
package jp.gr.java_conf.u6k.evershuffle.gae;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.evernote.auth.EvernoteAuth;
import com.evernote.auth.EvernoteService;
import com.evernote.clients.ClientFactory;
import com.evernote.clients.NoteStoreClient;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.type.Notebook;
import com.evernote.thrift.TException;

@SuppressWarnings("serial")
public class HelloServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            EvernoteService evernoteService = EvernoteService.SANDBOX;
            String accessToken = (String) req.getSession().getAttribute("accessToken");

            resp.setContentType("text/plain");
            PrintWriter w = resp.getWriter();

            EvernoteAuth evernoteAuth = new EvernoteAuth(evernoteService, accessToken);
            NoteStoreClient noteStoreClient = new ClientFactory(evernoteAuth).createNoteStoreClient();

            List<Notebook> notebookList = noteStoreClient.listNotebooks();
            for (Notebook notebook : notebookList) {
                w.write("Notebook: " + notebook.getName() + "\n");
            }
        } catch (EDAMUserException e) {
            throw new ServletException(e);
        } catch (EDAMSystemException e) {
            throw new ServletException(e);
        } catch (TException e) {
            throw new ServletException(e);
        }
    }

}
