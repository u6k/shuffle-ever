<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
    <head>
        <meta charset="UTF-8" />
        <title>ShuffleEver</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0, minimal-scale=1.0, maximum-scale=1.0, user-scalable=no" />
        <meta name="format-detection" content="telephone=no" />
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap-theme.min.css">
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
        <style type="text/css">
            body {
                padding-top: 70px;
            }
        </style>
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
                        <li><a href="<%=request.getContextPath()%>/index.jsp">Home</a></li>
                        <li class="active"><a href="<%=request.getContextPath()%>/about.jsp">About</a></li>
                    </ul>
                </div>
            </div>
        </nav>
        <div class="container-fluid">
            <p>Twitter: <a href="https://twitter.com/u6k_yu1">@u6k_yu1</a></p>
            <p>Blog: <a href="http://u6k-apps.blogspot.jp/">u6k Apps開発ログ</a></p>
            <p>GitHub: <a href="https://github.com/u6k/shuffle-ever">u6k / shuffle-ever</a></p>
            <hr />
            <p>Version 0.2.1</p>
            <hr />
            <p>(C) 2014 @u6k_yu1</p>
        </div>
    </body>
</html>