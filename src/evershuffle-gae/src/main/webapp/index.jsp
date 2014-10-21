<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="true" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8" />
        <title>EverShuffle</title>
    </head>
    <body>
        <%
            if (session.getAttribute("accessToken") == null) {
        %>
        <a href="oauth.do">Login Evernote</a>
        <%
            } else {
        %>
        <p>Logged in Evernote</p>
        <%
            }
        %>
    </body>
</html>