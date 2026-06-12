<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>配送任务</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>

<div class="header">防毒面具配送任务</div>

<div class="nav">
    <a href="dashboard">首页</a>
    <a href="monitor">火情监测</a>
    <a href="alarm">告警管理</a>
    <a href="drone">无人机管理</a>
    <a href="delivery">配送任务</a>
</div>

<div class="container">
    <h2>配送任务列表</h2>

    <table>
        <tr>
            <th>ID</th>
            <th>告警ID</th>
            <th>无人机</th>
            <th>目标区域</th>
            <th>面具数量</th>
            <th>状态</th>
            <th>创建时间</th>
            <th>完成时间</th>
            <th>操作</th>
        </tr>

        <c:forEach items="${list}" var="t">
            <tr>
                <td>${t.id}</td>
                <td>${t.alarmId}</td>
                <td>${t.droneName}</td>
                <td>${t.targetArea}</td>
                <td>${t.maskCount}</td>
                <td>${t.status}</td>
                <td>${t.createTime}</td>
                <td>${t.finishTime}</td>
                <td>
                    <c:if test="${t.status != '已送达'}">
                        <form action="delivery" method="post">
                            <input type="hidden" name="taskId" value="${t.id}">
                            <button class="btn" type="submit">确认送达</button>
                        </form>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>

</body>
</html>
