package com.firedrone.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "delivery_task")
public class DeliveryTaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "alarm_id", nullable = false)
    private Integer alarmId;

    @Column(name = "drone_id", nullable = false)
    private Integer droneId;

    @Column(name = "target_area", nullable = false, length = 100)
    private String targetArea;

    @Column(name = "mask_count", nullable = false)
    private Integer maskCount;

    @Column(nullable = false, length = 20)
    private String status = "配送中";

    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    @Column(name = "finish_time")
    private LocalDateTime finishTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "drone_id", insertable = false, updatable = false)
    private DroneEntity drone;

    public DeliveryTaskEntity() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getAlarmId() { return alarmId; }
    public void setAlarmId(Integer alarmId) { this.alarmId = alarmId; }
    public Integer getDroneId() { return droneId; }
    public void setDroneId(Integer droneId) { this.droneId = droneId; }
    public String getTargetArea() { return targetArea; }
    public void setTargetArea(String targetArea) { this.targetArea = targetArea; }
    public Integer getMaskCount() { return maskCount; }
    public void setMaskCount(Integer maskCount) { this.maskCount = maskCount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getFinishTime() { return finishTime; }
    public void setFinishTime(LocalDateTime finishTime) { this.finishTime = finishTime; }
    public DroneEntity getDrone() { return drone; }
}
