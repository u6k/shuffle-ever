<%@ page import="jp.gr.java_conf.u6k.evershuffle.gae.EvernoteUtil"%>
<%@ page import="java.util.Random"%>
<%@ page import="com.evernote.edam.notestore.NoteMetadata"%>
<%@ page import="com.evernote.edam.limits.Constants"%>
<%@ page import="com.evernote.edam.notestore.NotesMetadataList"%>
<%@ page import="com.evernote.edam.notestore.NotesMetadataResultSpec"%>
<%@ page import="com.evernote.edam.type.NoteSortOrder"%>
<%@ page import="com.evernote.edam.notestore.NoteFilter"%>
<%@ page import="com.evernote.clients.ClientFactory"%>
<%@ page import="com.evernote.clients.NoteStoreClient"%>
<%@ page import="com.evernote.auth.EvernoteAuth"%>
<%@ page import="com.evernote.auth.EvernoteService"%>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="true" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8" />
        <title>EverShuffle</title>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap-theme.min.css">
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
        <%
            session.setAttribute("mode", "pc");
        %>
    </head>
    <body>
        <nav class="navbar navbar-default" role="navigation">
            <div class="container-fluid">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                        <span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
                    </button>
                    <a class="navbar-brand" href="<%=request.getContextPath()%>/index.jsp">EverShuffle</a>
                </div>
                <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                    <ul class="nav navbar-nav">
                        <li><a href="<%=request.getContextPath()%>/about.jsp">About</a></li>
                    </ul>
                    <!--
                    <ul class="nav navbar-nav navbar-right">
                        <li><a href="#">Logout</a></li>
                    </ul>
                    -->
                </div>
            </div>
        </nav>
        <div class="container-fluid">
            <%
                if (session.getAttribute("accessToken") == null) {
            %>
            <a href="<%= request.getContextPath() %>/oauth.do">Login Evernote</a>
            <%
                } else {
                    EvernoteService evernoteService = new EvernoteUtil().getEvernoteService();
                    EvernoteAuth evernoteAuth = new EvernoteAuth(evernoteService, (String) session.getAttribute("accessToken"));
                    NoteStoreClient noteStoreClient = new ClientFactory(evernoteAuth).createNoteStoreClient();
                    
                    NoteFilter filter = new NoteFilter();
                    filter.setOrder(NoteSortOrder.UPDATED.getValue());
                    NotesMetadataResultSpec spec = new NotesMetadataResultSpec();
                    spec.setIncludeTitle(true);
                    NotesMetadataList noteMetaList = noteStoreClient.findNotesMetadata(filter, 0, Constants.EDAM_USER_NOTES_MAX, spec);
                    Random r = new Random();
                    int index = r.nextInt(noteMetaList.getTotalNotes());
                    NoteMetadata noteMeta = noteMetaList.getNotes().get(index);
                    String noteContent = noteStoreClient.getNoteContent(noteMeta.getGuid());
            %>
            <p>
                <a href="<%= request.getContextPath() %>/index.jsp">Next Note</a>
            </p>
            <p>Title: <%= noteMeta.getTitle() %></p>
            <p><%= noteContent %></p>
            <%
                }
            %>
        </div>
    </body>
</html>