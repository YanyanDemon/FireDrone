package com.firedrone.model;

import java.time.LocalDateTime;

public class DeliveryTask {
    private int id;
    private int alarmId;
    private int droneId;
    private String droneName;
    private String targetArea;
    private int maskCount;
    private String status;
    private LocalDateTime createTime;
    private LocalDateTime finishTime;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getAlarmId() { return alarmId; }
    public void setAlarmId(int alarmId) { this.alarmId = alarmId; }
    public int getDroneId() { return droneId; }
    public void setDroneId(int droneId) { this.droneId = droneId; }
    public String getDroneName() { return droneName; }
    public void setDroneName(String droneName) { this.droneName = droneName; }
    public String getTargetArea() { return targetArea; }
    public void setTargetArea(String targetArea) { this.targetArea = targetArea; }
    public int getMaskCount() { return maskCount; }
    public void setMaskCount(int maskCount) { this.maskCount = maskCount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getFinishTime() { return finishTime; }
    public void setFinishTime(LocalDateTime finishTime) { this.finishTime = finishTime; }
}
