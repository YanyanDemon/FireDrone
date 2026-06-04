package com.firedrone.model;

import java.time.LocalDateTime;

public class MonitorPoint {
    private int id;
    private String name;
    private String area;
    private double temperature;
    private double smoke;
    private double co;
    private int flame;
    private String riskLevel;
    private LocalDateTime updateTime;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }
    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }
    public double getSmoke() { return smoke; }
    public void setSmoke(double smoke) { this.smoke = smoke; }
    public double getCo() { return co; }
    public void setCo(double co) { this.co = co; }
    public int getFlame() { return flame; }
    public void setFlame(int flame) { this.flame = flame; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}