<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>火情智能监测系统登录</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>

<div class="login-box">
    <h2>火情智能监测与无人机配送系统</h2>

    <form action="login" method="post">
        <input type="text" name="username" placeholder="请输入账号">
        <input type="password" name="password" placeholder="请输入密码">
        <button class="btn" type="submit" style="width: 100%;">登录</button>
    </form>

    <p style="color:red;">${msg}</p>
</div>

</body>
</html>
