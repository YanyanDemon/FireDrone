# FireDroneSystem

火情智能监测与无人机配送系统 v2.0.0 — 基于 Spring Boot 3 的全栈重构版本。

## 项目概述

FireDrone 是一套面向森林与城市消防场景的智能监测调度平台。系统通过部署在各地的监测点实时采集温度、烟雾浓度、一氧化碳及火焰传感器数据，由内置的风险评估算法自动判定火情等级。一旦触发"危险"或"紧急"级别，系统自动生成告警并智能调度闲置无人机执行防毒面具配送任务，形成「监测 → 评估 → 告警 → 调度 → 配送 → 闭环」的完整应急响应链路。

### 核心业务流程

```
传感器数据采集 → FireRiskService 风险评估 → 自动生成告警 (危险/紧急)
                                                    ↓
                                          DispatchService 智能派单
                                                    ↓
                                    匹配最优无人机 (同区域优先 + 电量筛选)
                                                    ↓
                                    生成配送任务 → 无人机状态变更 → 闭环确认
```

## 技术栈

| 层面 | 技术选型 |
|---|---|
| 框架 | Spring Boot 3.4.1 |
| Java 版本 | 17 |
| 构建工具 | Maven |
| 模板引擎 | Thymeleaf + Layout Dialect + Spring Security 6 扩展 |
| 安全框架 | Spring Security (表单登录 + BCrypt 12 轮加密) |
| 持久层 | Spring Data JPA + Hibernate |
| 数据库 | MySQL 8 + HikariCP 连接池 |
| 打包方式 | Executable JAR |

## 数据模型

```text
┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│  user        │     │  drone       │     │  delivery_   │
│──────────────│     │──────────────│     │  task        │
│ id (PK)      │     │ id (PK)      │◄────│ drone_id(FK) │
│ username     │     │ name         │     │ alarm_id(FK) │
│ password     │     │ area         │     │ target_area  │
│ role         │     │ battery      │     │ mask_count   │
│ (管理员/操作员)│     │ max_load     │     │ status       │
└──────────────┘     │ mask_capacity│     │ create_time  │
                     │ status       │     │ finish_time  │
┌──────────────┐     │ (空闲/任务中) │     │ (配送中/已送达)│
│ monitor_point│     └──────────────┘     └──────────────┘
│──────────────│
│ id (PK)      │     ┌──────────────┐
│ name         │     │  alarm       │
│ area         │     │──────────────│
│ temperature  │     │ id (PK)      │
│ smoke        │     │ monitor_id   │──► monitor_point
│ co           │     │ level        │
│ flame        │     │ content      │
│ risk_level   │     │ status       │
│ update_time  │     │ create_time  │
└──────────────┘     │ (未处理/已处理)│
                     └──────────────┘
```

## 风险评估算法 (FireRiskService)

基于多传感器加权评分机制，对每个监测点的四维数据计算综合风险分值：

| 传感器 | 阈值一 | 阈值二 | 分值 |
|---|---|---|---|
| 温度 (°C) | > 45 | > 60 | +2 / +3 |
| 烟雾 (ppm) | > 300 | > 600 | +2 / +3 |
| CO (ppm) | > 80 | > 150 | +2 / +3 |
| 火焰检测 | 检测到 | — | +5 |

**等级映射：** 总分 < 3 → 正常 · < 6 → 预警 · < 9 → 危险 · ≥ 9 → 紧急

触发"危险"或"紧急"时，系统自动创建告警记录，供后续无人机调度使用。

## 无人机调度策略 (DispatchService)

收到告警派单请求后，调度服务按以下优先级从无人机机队中选取最优单元：

1. **状态筛选** — 仅考虑"空闲"无人机
2. **电量门槛** — 电量 ≥ 30% 方可执行任务
3. **载重校验** — 防毒面具容量 ≥ 任务需求量
4. **地域优先** — 与目标区域相同的无人机优先
5. **电量降序** — 同区域中电量最高的排在最前

调度成功后自动更新无人机状态为"任务中"，生成配送任务记录。

## 角色与权限

| 角色 | 权限范围 |
|---|---|
| **管理员** (ROLE_ADMIN) | 全局仪表盘、监测点管理、告警派单、无人机管理、配送任务闭环 |
| **操作员** (ROLE_OPERATOR) | 工作台、监测点查看、告警查看与派单、配送任务闭环 |

管理员注册需邀请码 `FIREDRONE2026`，操作员注册无限制。密码采用 BCrypt(strength=12) 加密存储。

## 项目结构

```text
src/main/java/com/firedrone/
├── FireDroneApplication.java          # 启动入口
├── config/
│   └── SecurityConfig.java            # Spring Security 配置
├── entity/
│   ├── UserEntity.java                # 用户实体
│   ├── DroneEntity.java               # 无人机实体
│   ├── MonitorPointEntity.java        # 监测点实体
│   ├── AlarmEntity.java               # 告警实体
│   └── DeliveryTaskEntity.java        # 配送任务实体
├── repository/
│   ├── UserRepository.java
│   ├── DroneRepository.java
│   ├── MonitorPointRepository.java
│   ├── AlarmRepository.java
│   └── DeliveryTaskRepository.java
├── service/
│   ├── CustomUserDetailsService.java  # 认证服务
│   ├── DashboardService.java          # 仪表盘聚合
│   ├── FireRiskService.java           # 风险评估
│   └── DispatchService.java           # 无人机调度
├── controller/
│   ├── LoginController.java           # GET /login
│   ├── RegisterController.java        # GET/POST /register
│   ├── HomeController.java            # /home, /dashboard
│   ├── MonitorController.java         # /monitor
│   ├── DroneController.java           # /drone (admin)
│   ├── AlarmController.java           # /alarm
│   └── DeliveryController.java        # /delivery
└── resources/
    ├── application.yml                # 应用配置
    └── templates/                     # Thymeleaf 模板
        ├── layout/default.html        # 布局母版
        ├── login.html                 # 登录页
        ├── register.html              # 注册页
        ├── dashboard.html             # 指挥中心仪表盘
        ├── monitor.html               # 监测点管理
        ├── drone.html                 # 无人机管理
        ├── alarm.html                 # 告警与派单
        └── delivery.html              # 配送任务
```

## 快速启动

### 环境要求

- JDK 17+
- Maven 3.8+
- MySQL 8.0+

### 数据库初始化

```sql
CREATE DATABASE IF NOT EXISTS FDdb
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;
```

JPA 配置为 `ddl-auto: validate`，需确保表结构与实体映射一致。首次部署时可临时改为 `update` 让 Hibernate 自动建表。

### 配置文件

`application.yml` 中的数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/FDdb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai&characterEncoding=UTF-8
    username: root
    password: 2005
```

### 构建与运行

```bash
# 编译打包
mvn clean package -DskipTests

# 启动应用
java -jar target/FireDroneSystem-2.0.0.jar
```

应用默认监听 `http://localhost:8080`。

### 初始账号

首次启动后通过 `/register` 页面注册管理员账号（需邀请码 `FIREDRONE2026`），或注册普通操作员账号后手动在数据库中将 role 字段改为"管理员"。

## 页面路由

| 路径 | 权限 | 说明 |
|---|---|---|
| `/login` | 公开 | 登录页 |
| `/register` | 公开 | 注册页 |
| `/dashboard` | 已认证 | 管理员指挥中心 |
| `/home` | 已认证 | 操作员工作台 |
| `/monitor` | 已认证 | 监测点管理 |
| `/drone` | 管理员 | 无人机编队管理 |
| `/alarm` | 已认证 | 告警列表与派单 |
| `/delivery` | 已认证 | 配送任务管理 |

## License

本项目为课程/毕业设计项目，仅供学习参考。
