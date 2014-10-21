
package jp.gr.java_conf.u6k.evershuffle.gae;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.evernote.auth.EvernoteAuth;
import com.evernote.auth.EvernoteService;
import com.evernote.clients.ClientFactory;
import com.evernote.clients.NoteStoreClient;
import com.evernote.edam.limits.Constants;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteMetadata;
import com.evernote.edam.notestore.NotesMetadataList;
import com.evernote.edam.notestore.NotesMetadataResultSpec;
import com.evernote.edam.type.NoteSortOrder;

@SuppressWarnings("serial")
public class HelloServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            EvernoteService evernoteService = EvernoteService.SANDBOX;
            String accessToken = (String) req.getSession().getAttribute("accessToken");
            EvernoteAuth evernoteAuth = new EvernoteAuth(evernoteService, accessToken);
            NoteStoreClient noteStoreClient = new ClientFactory(evernoteAuth).createNoteStoreClient();

            resp.setContentType("text/plain");
            PrintWriter w = resp.getWriter();

            NoteFilter filter = new NoteFilter();
            filter.setOrder(NoteSortOrder.UPDATED.getValue());
            NotesMetadataResultSpec spec = new NotesMetadataResultSpec();
            spec.setIncludeTitle(true);
            NotesMetadataList noteMetaList = noteStoreClient.findNotesMetadata(filter, 0, Constants.EDAM_USER_NOTES_MAX, spec);

            w.write("EDAM_USER_NOTES_MAX: " + Constants.EDAM_USER_NOTES_MAX + "\n");
            w.write("Note count: " + noteMetaList.getNotesSize() + "\n");

            for (NoteMetadata noteMeta : noteMetaList.getNotes()) {
                w.write("Note: guid=" + noteMeta.getGuid() + ", title=" + noteMeta.getTitle() + "\n");
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

}
