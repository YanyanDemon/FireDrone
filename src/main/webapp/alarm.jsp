<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>告警管理</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>

<div class="header">火情告警管理</div>

<div class="nav">
    <a href="dashboard">首页</a>
    <a href="monitor">火情监测</a>
    <a href="alarm">告警管理</a>
    <a href="drone">无人机管理</a>
    <a href="delivery">配送任务</a>
</div>

<div class="container">
    <h2>告警列表</h2>

    <p style="color:red;">${sessionScope.msg}</p>
    <c:remove var="msg" scope="session"/>

    <table>
        <tr>
            <th>ID</th>
            <th>监测点</th>
            <th>等级</th>
            <th>内容</th>
            <th>状态</th>
            <th>时间</th>
            <th>派发防毒面具</th>
        </tr>

        <c:forEach items="${list}" var="a">
            <tr>
                <td>${a.id}</td>
                <td>${a.monitorName}</td>
                <td>${a.level}</td>
                <td>${a.content}</td>
                <td>${a.status}</td>
                <td>${a.createTime}</td>
                <td>
                    <form action="alarm" method="post">
                        <input type="hidden" name="alarmId" value="${a.id}">
                        <input type="text" name="targetArea" placeholder="目标区域" style="width:80px;">
                        <input type="number" name="maskCount" value="5" min="1" style="width:60px;">
                        <button class="btn btn-blue" type="submit">生成任务</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>

</body>
</html>
