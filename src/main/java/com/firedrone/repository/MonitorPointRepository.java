package com.firedrone.repository;

import com.firedrone.entity.MonitorPointEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface MonitorPointRepository extends JpaRepository<MonitorPointEntity, Integer> {

    List<MonitorPointEntity> findAllByOrderByIdDesc();

    @Query("SELECT m FROM MonitorPointEntity m ORDER BY "
         + "CASE m.riskLevel WHEN '紧急' THEN 1 WHEN '危险' THEN 2 WHEN '预警' THEN 3 ELSE 4 END, "
         + "m.updateTime DESC")
    List<MonitorPointEntity> findTop12OrderByRisk();

    long countByRiskLevel(String riskLevel);

    @Modifying
    @Transactional
    @Query("UPDATE MonitorPointEntity m SET m.riskLevel = :level, m.updateTime = CURRENT_TIMESTAMP WHERE m.id = :id")
    void updateRiskLevel(@Param("id") Integer id, @Param("level") String level);
}
