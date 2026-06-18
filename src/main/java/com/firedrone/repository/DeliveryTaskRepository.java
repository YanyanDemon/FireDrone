package com.firedrone.repository;

import com.firedrone.entity.DeliveryTaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface DeliveryTaskRepository extends JpaRepository<DeliveryTaskEntity, Integer> {

    @Query("SELECT t FROM DeliveryTaskEntity t JOIN FETCH t.drone ORDER BY t.id DESC")
    List<DeliveryTaskEntity> findAllWithDrone();

    long countByStatus(String status);

    @Modifying
    @Transactional
    @Query("UPDATE DeliveryTaskEntity t SET t.status = '已送达', t.finishTime = CURRENT_TIMESTAMP WHERE t.id = :id")
    void finishTask(@Param("id") Integer id);
}
