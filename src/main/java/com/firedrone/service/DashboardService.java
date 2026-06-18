package com.firedrone.service;

import com.firedrone.entity.*;
import com.firedrone.repository.*;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final MonitorPointRepository monitorPointRepository;
    private final AlarmRepository alarmRepository;
    private final DroneRepository droneRepository;
    private final DeliveryTaskRepository deliveryTaskRepository;

    public DashboardService(MonitorPointRepository monitorPointRepository,
                            AlarmRepository alarmRepository,
                            DroneRepository droneRepository,
                            DeliveryTaskRepository deliveryTaskRepository) {
        this.monitorPointRepository = monitorPointRepository;
        this.alarmRepository = alarmRepository;
        this.droneRepository = droneRepository;
        this.deliveryTaskRepository = deliveryTaskRepository;
    }

    public Map<String, Object> getDashboardData() {
        Map<String, Object> data = new LinkedHashMap<>();

        data.put("monitorCount", monitorPointRepository.count());
        data.put("alarmCount", alarmRepository.count());
        data.put("droneCount", droneRepository.count());
        data.put("taskCount", deliveryTaskRepository.count());

        data.put("monitorNormal", monitorPointRepository.countByRiskLevel("正常"));
        data.put("monitorWarning", monitorPointRepository.countByRiskLevel("预警"));
        data.put("monitorDanger", monitorPointRepository.countByRiskLevel("危险"));
        data.put("monitorUrgent", monitorPointRepository.countByRiskLevel("紧急"));

        data.put("alarmWarning", alarmRepository.countByLevel("预警"));
        data.put("alarmDanger", alarmRepository.countByLevel("危险"));
        data.put("alarmUrgent", alarmRepository.countByLevel("紧急"));
        data.put("alarmUnprocessed", alarmRepository.countByStatus("未处理"));

        data.put("droneIdle", droneRepository.countByStatus("空闲"));
        data.put("droneMission", droneRepository.countByStatus("任务中"));
        data.put("droneLowBattery", droneRepository.countByBatteryLessThan(20));

        data.put("taskDelivering", deliveryTaskRepository.countByStatus("配送中"));
        data.put("taskDelivered", deliveryTaskRepository.countByStatus("已送达"));

        data.put("monitorList", toMonitorMaps(monitorPointRepository.findTop12OrderByRisk()));
        data.put("alarmList", toAlarmMaps(alarmRepository.findAllWithMonitorPoint()));
        data.put("droneList", toDroneMaps(droneRepository.findAllByOrderByIdDesc()));
        data.put("taskList", toTaskMaps(deliveryTaskRepository.findAllWithDrone()));

        return data;
    }

    private List<Map<String, Object>> toMonitorMaps(List<MonitorPointEntity> list) {
        return list.stream().map(m -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("name", m.getName());
            map.put("area", m.getArea());
            map.put("temperature", m.getTemperature());
            map.put("smoke", m.getSmoke());
            map.put("co", m.getCo());
            map.put("flame", m.getFlame());
            map.put("riskLevel", m.getRiskLevel());
            return map;
        }).collect(Collectors.toList());
    }

    private List<Map<String, Object>> toAlarmMaps(List<AlarmEntity> list) {
        return list.stream().limit(10).map(a -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("level", a.getLevel());
            map.put("status", a.getStatus());
            map.put("createTime", a.getCreateTime());
            map.put("monitorName", a.getMonitorPoint() != null ? a.getMonitorPoint().getName() : "未知");
            return map;
        }).collect(Collectors.toList());
    }

    private List<Map<String, Object>> toDroneMaps(List<DroneEntity> list) {
        return list.stream().map(d -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("name", d.getName());
            map.put("area", d.getArea());
            map.put("battery", d.getBattery());
            map.put("status", d.getStatus());
            map.put("maskCapacity", d.getMaskCapacity());
            return map;
        }).collect(Collectors.toList());
    }

    private List<Map<String, Object>> toTaskMaps(List<DeliveryTaskEntity> list) {
        return list.stream().limit(10).map(t -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("targetArea", t.getTargetArea());
            map.put("maskCount", t.getMaskCount());
            map.put("status", t.getStatus());
            map.put("createTime", t.getCreateTime());
            map.put("droneName", t.getDrone() != null ? t.getDrone().getName() : "未知");
            return map;
        }).collect(Collectors.toList());
    }
}
