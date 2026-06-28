package com.firedrone.config;

import com.firedrone.entity.*;
import com.firedrone.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
@Profile("demo")
public class DemoDataConfig {

    @Bean
    CommandLineRunner demoData(UserRepository users,
                               MonitorPointRepository monitors,
                               AlarmRepository alarms,
                               DroneRepository drones,
                               DeliveryTaskRepository tasks,
                               PasswordEncoder encoder) {
        return args -> {
            UserEntity admin = new UserEntity();
            admin.setUsername("admin");
            admin.setPassword(encoder.encode("admin123"));
            admin.setRole("管理员");
            users.save(admin);

            UserEntity operator = new UserEntity();
            operator.setUsername("operator");
            operator.setPassword(encoder.encode("operator123"));
            operator.setRole("操作员");
            users.save(operator);

            MonitorPointEntity north = monitor("北山林区监测站", "北山林区", 67.2, 720.0, 168.0, 1, "紧急");
            MonitorPointEntity warehouse = monitor("物资仓库东侧", "消防园区", 48.6, 350.0, 42.0, 0, "预警");
            MonitorPointEntity park = monitor("城市公园A区", "高新区", 31.8, 82.0, 18.0, 0, "正常");
            MonitorPointEntity south = monitor("南部防火瞭望点", "南山片区", 55.4, 510.0, 102.0, 0, "危险");
            monitors.save(north);
            monitors.save(warehouse);
            monitors.save(park);
            monitors.save(south);

            DroneEntity d1 = drone("FD-01 鹰眼", "北山林区", 86, 8.0, 20, "任务中");
            DroneEntity d2 = drone("FD-02 迅捷", "南山片区", 74, 6.5, 15, "空闲");
            DroneEntity d3 = drone("FD-03 守望", "消防园区", 42, 5.0, 10, "空闲");
            DroneEntity d4 = drone("FD-04 先锋", "高新区", 18, 4.5, 8, "空闲");
            drones.save(d1);
            drones.save(d2);
            drones.save(d3);
            drones.save(d4);

            AlarmEntity a1 = alarm(north.getId(), "紧急", "北山林区 北山林区监测站 出现火情风险，当前等级：紧急", "未处理");
            AlarmEntity a2 = alarm(south.getId(), "危险", "南山片区 南部防火瞭望点 出现火情风险，当前等级：危险", "未处理");
            alarms.save(a1);
            alarms.save(a2);

            DeliveryTaskEntity task = new DeliveryTaskEntity();
            task.setAlarmId(a1.getId());
            task.setDroneId(d1.getId());
            task.setTargetArea("北山林区");
            task.setMaskCount(12);
            task.setStatus("配送中");
            task.setCreateTime(LocalDateTime.now().minusMinutes(18));
            tasks.save(task);

            DeliveryTaskEntity done = new DeliveryTaskEntity();
            done.setAlarmId(a2.getId());
            done.setDroneId(d2.getId());
            done.setTargetArea("南山片区");
            done.setMaskCount(8);
            done.setStatus("已送达");
            done.setCreateTime(LocalDateTime.now().minusHours(2));
            done.setFinishTime(LocalDateTime.now().minusHours(1));
            tasks.save(done);
        };
    }

    private static MonitorPointEntity monitor(String name, String area, double temp,
                                               double smoke, double co, int flame, String risk) {
        MonitorPointEntity m = new MonitorPointEntity();
        m.setName(name);
        m.setArea(area);
        m.setTemperature(temp);
        m.setSmoke(smoke);
        m.setCo(co);
        m.setFlame(flame);
        m.setRiskLevel(risk);
        m.setUpdateTime(LocalDateTime.now().minusMinutes(5));
        return m;
    }

    private static DroneEntity drone(String name, String area, int battery,
                                     double load, int capacity, String status) {
        DroneEntity d = new DroneEntity();
        d.setName(name);
        d.setArea(area);
        d.setBattery(battery);
        d.setMaxLoad(load);
        d.setMaskCapacity(capacity);
        d.setStatus(status);
        return d;
    }

    private static AlarmEntity alarm(int monitorId, String level, String content, String status) {
        AlarmEntity a = new AlarmEntity();
        a.setMonitorId(monitorId);
        a.setLevel(level);
        a.setContent(content);
        a.setStatus(status);
        a.setCreateTime(LocalDateTime.now().minusMinutes(25));
        return a;
    }
}
