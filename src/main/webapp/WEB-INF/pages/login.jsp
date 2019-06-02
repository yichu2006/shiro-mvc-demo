<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
    <head>
        <title>title-login</title>
        <meta charset="UTF-8">
    </head>
    <body>
        <form action="/login.html" method="post">
            用户名：<input type="text" name="username" /><br/>
            密码：<input type="password" name="password" /><br/>
            <input type="submit" value="登录" />${error}
        </form>

    </body>

</html>