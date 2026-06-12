-- ============================================
-- FireDrone 火情智能监测与无人机配送系统
-- 数据库初始化脚本 (MySQL 8.0+)
-- ============================================

CREATE DATABASE IF NOT EXISTS FDdb
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE FDdb;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id`       INT          AUTO_INCREMENT PRIMARY KEY,
    `username` VARCHAR(50)  NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `role`     VARCHAR(20)  NOT NULL DEFAULT '操作员'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 管理员账号: admin / admin123
-- 请先运行 BCryptHashGenerator.java 生成真实哈希后替换下面这行
INSERT INTO `user` (`username`, `password`, `role`) VALUES
('admin', '$2a$12$2YlaZLNzFrtNpYoHHaA5heb6PkQ99Lf5JtP2FZCsuksRGQCsE50dq', '管理员')
ON DUPLICATE KEY UPDATE `username` = `username`;

-- 监测点表
CREATE TABLE IF NOT EXISTS `monitor_point` (
    `id`          INT           AUTO_INCREMENT PRIMARY KEY,
    `name`        VARCHAR(100)  NOT NULL,
    `area`        VARCHAR(100)  NOT NULL,
    `temperature` DOUBLE        NOT NULL DEFAULT 0,
    `smoke`       DOUBLE        NOT NULL DEFAULT 0,
    `co`          DOUBLE        NOT NULL DEFAULT 0,
    `flame`       TINYINT       NOT NULL DEFAULT 0 COMMENT '0=无火焰 1=检测到火焰',
    `risk_level`  VARCHAR(20)   NOT NULL DEFAULT '正常',
    `update_time` DATETIME      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 告警表
CREATE TABLE IF NOT EXISTS `alarm` (
    `id`          INT           AUTO_INCREMENT PRIMARY KEY,
    `monitor_id`  INT           NOT NULL,
    `level`       VARCHAR(20)   NOT NULL COMMENT '预警/危险/紧急',
    `content`     VARCHAR(500)  NOT NULL,
    `status`      VARCHAR(20)   NOT NULL DEFAULT '未处理',
    `create_time` DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`monitor_id`) REFERENCES `monitor_point`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 无人机表
CREATE TABLE IF NOT EXISTS `drone` (
    `id`            INT           AUTO_INCREMENT PRIMARY KEY,
    `name`          VARCHAR(100)  NOT NULL,
    `area`          VARCHAR(100)  NOT NULL,
    `battery`       INT           NOT NULL DEFAULT 100,
    `max_load`      DOUBLE        NOT NULL DEFAULT 5.0,
    `mask_capacity` INT           NOT NULL DEFAULT 10,
    `status`        VARCHAR(20)   NOT NULL DEFAULT '空闲'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 配送任务表
CREATE TABLE IF NOT EXISTS `delivery_task` (
    `id`          INT           AUTO_INCREMENT PRIMARY KEY,
    `alarm_id`    INT           NOT NULL,
    `drone_id`    INT           NOT NULL,
    `target_area` VARCHAR(100)  NOT NULL,
    `mask_count`  INT           NOT NULL,
    `status`      VARCHAR(20)   NOT NULL DEFAULT '配送中',
    `create_time` DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `finish_time` DATETIME      DEFAULT NULL,
    FOREIGN KEY (`alarm_id`) REFERENCES `alarm`(`id`),
    FOREIGN KEY (`drone_id`) REFERENCES `drone`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
