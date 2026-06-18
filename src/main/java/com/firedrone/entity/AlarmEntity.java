package com.firedrone.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "alarm")
public class AlarmEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "monitor_id", nullable = false)
    private Integer monitorId;

    @Column(nullable = false, length = 20)
    private String level;

    @Column(nullable = false, length = 500)
    private String content;

    @Column(nullable = false, length = 20)
    private String status = "未处理";

    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "monitor_id", insertable = false, updatable = false)
    private MonitorPointEntity monitorPoint;

    public AlarmEntity() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getMonitorId() { return monitorId; }
    public void setMonitorId(Integer monitorId) { this.monitorId = monitorId; }
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public MonitorPointEntity getMonitorPoint() { return monitorPoint; }
}
