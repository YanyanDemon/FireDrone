package com.firedrone.repository;

import com.firedrone.entity.AlarmEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface AlarmRepository extends JpaRepository<AlarmEntity, Integer> {

    @Query("SELECT a FROM AlarmEntity a JOIN FETCH a.monitorPoint ORDER BY a.id DESC")
    List<AlarmEntity> findAllWithMonitorPoint();

    long countByLevel(String level);
    long countByStatus(String status);

    @Modifying
    @Transactional
    @Query("UPDATE AlarmEntity a SET a.status = :status WHERE a.id = :id")
    void updateStatus(@Param("id") Integer id, @Param("status") String status);
}
