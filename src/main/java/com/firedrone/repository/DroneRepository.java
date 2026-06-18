package com.firedrone.repository;

import com.firedrone.entity.DroneEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

public interface DroneRepository extends JpaRepository<DroneEntity, Integer> {

    List<DroneEntity> findAllByOrderByIdDesc();

    long countByStatus(String status);
    long countByBatteryLessThan(Integer battery);

    @Query("SELECT d FROM DroneEntity d WHERE d.status = '空闲' AND d.battery >= 30 AND d.maskCapacity >= :maskCount "
         + "ORDER BY CASE WHEN d.area = :targetArea THEN 0 ELSE 1 END, d.battery DESC")
    List<DroneEntity> findBestDroneTop(@Param("targetArea") String targetArea,
                                       @Param("maskCount") Integer maskCount);

    @Modifying
    @Transactional
    @Query("UPDATE DroneEntity d SET d.status = :status WHERE d.id = :id")
    void updateStatus(@Param("id") Integer id, @Param("status") String status);
}
