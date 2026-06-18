package com.firedrone.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "monitor_point")
public class MonitorPointEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String area;

    @Column(nullable = false)
    private Double temperature = 0.0;

    @Column(nullable = false)
    private Double smoke = 0.0;

    @Column(nullable = false)
    private Double co = 0.0;

    @Column(nullable = false, columnDefinition = "TINYINT")
    private Integer flame = 0;

    @Column(name = "risk_level", nullable = false, length = 20)
    private String riskLevel = "正常";

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    public MonitorPointEntity() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }
    public Double getTemperature() { return temperature; }
    public void setTemperature(Double temperature) { this.temperature = temperature; }
    public Double getSmoke() { return smoke; }
    public void setSmoke(Double smoke) { this.smoke = smoke; }
    public Double getCo() { return co; }
    public void setCo(Double co) { this.co = co; }
    public Integer getFlame() { return flame; }
    public void setFlame(Integer flame) { this.flame = flame; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}
