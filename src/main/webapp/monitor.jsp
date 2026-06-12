<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>火情监测</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>

<div class="header">火情监测数据</div>

<div class="nav">
    <a href="dashboard">首页</a>
    <a href="monitor">火情监测</a>
    <a href="alarm">告警管理</a>
    <a href="drone">无人机管理</a>
    <a href="delivery">配送任务</a>
</div>

<div class="container">
    <h2>监测点列表</h2>

    <table>
        <tr>
            <th>ID</th>
            <th>监测点</th>
            <th>区域</th>
            <th>温度</th>
            <th>烟雾</th>
            <th>CO</th>
            <th>火焰</th>
            <th>风险等级</th>
            <th>操作</th>
        </tr>

        <c:forEach items="${list}" var="p">
            <tr>
                <td>${p.id}</td>
                <td>${p.name}</td>
                <td>${p.area}</td>
                <td>${p.temperature}</td>
                <td>${p.smoke}</td>
                <td>${p.co}</td>
                <td>
                    <c:choose>
                        <c:when test="${p.flame == 1}">检测到</c:when>
                        <c:otherwise>无</c:otherwise>
                    </c:choose>
                </td>
                <td>${p.riskLevel}</td>
                <td>
                    <form action="monitor" method="post">
                        <input type="hidden" name="id" value="${p.id}">
                        <button class="btn" type="submit">智能判断</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>

</body>
</html>
