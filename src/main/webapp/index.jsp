<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>系统首页</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>

<div class="header">火情智能监测与防毒面具无人机快速配送系统</div>

<div class="nav">
    <a href="dashboard">首页</a>
    <a href="monitor">火情监测</a>
    <a href="alarm">告警管理</a>
    <a href="drone">无人机管理</a>
    <a href="delivery">配送任务</a>
</div>

<div class="container">
    <h2>系统仪表盘</h2>

    <div class="card-box">
        <div class="card">
            <div>监测点数量</div>
            <div class="num">${monitorCount}</div>
        </div>

        <div class="card">
            <div>告警数量</div>
            <div class="num">${alarmCount}</div>
        </div>

        <div class="card">
            <div>无人机数量</div>
            <div class="num">${droneCount}</div>
        </div>

        <div class="card">
            <div>配送任务</div>
            <div class="num">${taskCount}</div>
        </div>
    </div>

    <h3 style="margin-top: 30px;">系统说明</h3>
    <p>
        本系统用于模拟火情智能监测、火灾风险识别、告警生成以及防毒面具无人机快速配送流程。
        当监测点温度、烟雾、一氧化碳或火焰数据异常时，系统会自动计算风险等级，并支持快速派发无人机配送防毒面具。
    </p>
</div>

</body>
</html>