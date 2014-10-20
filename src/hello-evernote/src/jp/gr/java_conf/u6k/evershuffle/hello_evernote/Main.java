
package jp.gr.java_conf.u6k.evershuffle.hello_evernote;

import java.util.Iterator;

import com.evernote.auth.EvernoteAuth;
import com.evernote.auth.EvernoteService;
import com.evernote.clients.ClientFactory;
import com.evernote.clients.NoteStoreClient;
import com.evernote.clients.UserStoreClient;
import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.NoteSortOrder;
import com.evernote.edam.userstore.Constants;
import com.evernote.thrift.TException;

public class Main {

    public static void main(String[] args) throws TException, EDAMUserException, EDAMSystemException, EDAMNotFoundException {
        String token = System.getProperty("token");
        System.out.println("token: " + token);
        EvernoteAuth evernoteAuth = new EvernoteAuth(EvernoteService.SANDBOX, token);
        ClientFactory factory = new ClientFactory(evernoteAuth);
        UserStoreClient userStore = factory.createUserStoreClient();
        boolean isCheckVersion = userStore.checkVersion("Evershuffle", Constants.EDAM_VERSION_MAJOR, Constants.EDAM_VERSION_MINOR);
        if (!isCheckVersion) {
            throw new RuntimeException("Incompatible Evernote client protocol version");
        }
        NoteStoreClient noteStore = factory.createNoteStoreClient();

        NoteFilter filter = new NoteFilter();
        filter.setOrder(NoteSortOrder.UPDATED.getValue());
        filter.setAscending(false);

        NoteList notes = noteStore.findNotes(filter, 0, 100);
        System.out.println("Found " + notes.getTotalNotes() + " matching notes.");

        Iterator<Note> ite = notes.getNotesIterator();
        while (ite.hasNext()) {
            Note note = ite.next();
            System.out.println("Note: " + note.getTitle());
        }
    }

}
