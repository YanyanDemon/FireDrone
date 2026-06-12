package com.firedrone.service;

import com.firedrone.dao.DeliveryDao;
import com.firedrone.dao.DroneDao;
import com.firedrone.model.Drone;
import com.firedrone.util.DBUtil;

public class DispatchService {

    private final DroneDao droneDao = new DroneDao();
    private final DeliveryDao deliveryDao = new DeliveryDao();

    public boolean dispatchMask(int alarmId, String targetArea, int maskCount) {
        boolean committed = false;
        try {
            DBUtil.beginTransaction();

            Drone drone = droneDao.findBestDrone(targetArea, maskCount);

            if (drone == null) {
                return false;
            }

            deliveryDao.addTask(alarmId, drone.getId(), targetArea, maskCount);
            droneDao.updateStatus(drone.getId(), "执行任务中");

            DBUtil.commitTransaction();
            committed = true;
            return true;

        } catch (Exception e) {
            throw new RuntimeException("无人机调度失败", e);
        } finally {
            if (!committed) {
                DBUtil.rollbackTransaction();
            }
        }
    }
}
