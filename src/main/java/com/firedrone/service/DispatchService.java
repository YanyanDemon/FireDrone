package com.firedrone.service;

import com.firedrone.entity.DeliveryTaskEntity;
import com.firedrone.entity.DroneEntity;
import com.firedrone.repository.DeliveryTaskRepository;
import com.firedrone.repository.DroneRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DispatchService {

    private final DroneRepository droneRepository;
    private final DeliveryTaskRepository deliveryTaskRepository;

    public DispatchService(DroneRepository droneRepository,
                           DeliveryTaskRepository deliveryTaskRepository) {
        this.droneRepository = droneRepository;
        this.deliveryTaskRepository = deliveryTaskRepository;
    }

    @Transactional
    public boolean dispatchMask(int alarmId, String targetArea, int maskCount) {
        List<DroneEntity> candidates = droneRepository.findBestDroneTop(targetArea, maskCount);
        DroneEntity drone = candidates.isEmpty() ? null : candidates.get(0);

        if (drone == null) {
            return false;
        }

        DeliveryTaskEntity task = new DeliveryTaskEntity();
        task.setAlarmId(alarmId);
        task.setDroneId(drone.getId());
        task.setTargetArea(targetArea != null && !targetArea.isBlank() ? targetArea : drone.getArea());
        task.setMaskCount(maskCount);
        task.setStatus("配送中");
        task.setCreateTime(LocalDateTime.now());
        deliveryTaskRepository.save(task);

        droneRepository.updateStatus(drone.getId(), "任务中");

        return true;
    }
}
