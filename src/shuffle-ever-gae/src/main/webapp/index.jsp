<%@ page import="jp.gr.java_conf.u6k.shuffle_ever.gae.EvernoteUtil"%>
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
        <title>ShuffleEver</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0, minimal-scale=1.0, maximum-scale=1.0, user-scalable=no" />
        <meta name="format-detection" content="telephone=no" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge"></meta>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css"></link>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap-theme.min.css"></link>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
        <style type="text/css">
            body {
                padding-top: 70px;
                padding-bottom: 70px;
            }
        </style>
        <%
            session.setAttribute("mode", "pc");
        %>
    </head>
    <body>
        <nav class="navbar navbar-default navbar-fixed-top" role="navigation">
            <div class="container-fluid">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a class="navbar-brand" href="<%=request.getContextPath()%>/index.jsp">ShuffleEver</a>
                </div>
                <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                    <ul class="nav navbar-nav">
                        <li class="active"><a href="<%=request.getContextPath()%>/index.jsp">Home</a></li>
                        <li><a href="<%=request.getContextPath()%>/about.jsp">About</a></li>
                    </ul>
                </div>
            </div>
        </nav>
        <%
            if (session.getAttribute("accessToken") != null) {
        %>
        <nav class="navbar navbar-default navbar-fixed-bottom" role="navigation">
            <div class="container-fluid">
                <div class="collapse navbar-collapse">
                    <a href="<%= request.getContextPath() %>/index.jsp" class="btn btn-default navbar-btn navbar-right">Next &rarr;</a>
                </div>
            </div>
        </nav>
        <%
            }
        %>
        <div class="container-fluid">
            <%
                if (session.getAttribute("accessToken") == null) {
            %>
            <div class="row">
                <div class="col-md-3"></div>
                <div class="col-md-6">
                    <h1>ShuffleEverへようこそ</h1>
                    <p style="padding-top: 30px; padding-bottom: 30px">あなたのノートを"シャッフル"して、埋もれていたノートから新しい発見をしましょう。</p>
                    <p><a href="<%= request.getContextPath() %>/oauth.do" class="btn btn-success btn-lg"><img src="<%= request.getContextPath() %>/img/evernote-lg.png" /> Sign in Evernote</a></p>
                </div>
                <div class="col-md-3"></div>
            </div>
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
            <p><%= noteMeta.getTitle() %></p>
            <hr />
            <p><%= noteContent %></p>
            <%
                }
            %>
        </div>
    </body>
</html>