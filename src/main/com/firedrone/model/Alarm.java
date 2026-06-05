package main.com.firedrone.model;

import java.time.LocalDateTime;

public class Alarm {
    private int id;
    private int monitorId;
    private String monitorName;
    private String level;
    private String content;
    private String status;
    private LocalDateTime createTime;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getMonitorId() { return monitorId; }
    public void setMonitorId(int monitorId) { this.monitorId = monitorId; }
    public String getMonitorName() { return monitorName; }
    public void setMonitorName(String monitorName) { this.monitorName = monitorName; }
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}