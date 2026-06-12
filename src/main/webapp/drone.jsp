<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>无人机管理</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>

<div class="header">无人机管理</div>

<div class="nav">
    <a href="dashboard">首页</a>
    <a href="monitor">火情监测</a>
    <a href="alarm">告警管理</a>
    <a href="drone">无人机管理</a>
    <a href="delivery">配送任务</a>
</div>

<div class="container">
    <h2>无人机列表</h2>

    <table>
        <tr>
            <th>ID</th>
            <th>名称</th>
            <th>区域</th>
            <th>电量</th>
            <th>最大载重</th>
            <th>面具容量</th>
            <th>状态</th>
        </tr>

        <c:forEach items="${list}" var="d">
            <tr>
                <td>${d.id}</td>
                <td>${d.name}</td>
                <td>${d.area}</td>
                <td>${d.battery}%</td>
                <td>${d.maxLoad} kg</td>
                <td>${d.maskCapacity}</td>
                <td>${d.status}</td>
            </tr>
        </c:forEach>
    </table>
</div>

</body>
</html>
