package com.firedrone.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "drone")
public class DroneEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String area;

    @Column(nullable = false)
    private Integer battery = 100;

    @Column(name = "max_load", nullable = false)
    private Double maxLoad = 5.0;

    @Column(name = "mask_capacity", nullable = false)
    private Integer maskCapacity = 10;

    @Column(nullable = false, length = 20)
    private String status = "空闲";

    public DroneEntity() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }
    public Integer getBattery() { return battery; }
    public void setBattery(Integer battery) { this.battery = battery; }
    public Double getMaxLoad() { return maxLoad; }
    public void setMaxLoad(Double maxLoad) { this.maxLoad = maxLoad; }
    public Integer getMaskCapacity() { return maskCapacity; }
    public void setMaskCapacity(Integer maskCapacity) { this.maskCapacity = maskCapacity; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
