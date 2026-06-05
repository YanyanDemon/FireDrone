package main.com.firedrone.service;

import main.com.firedrone.dao.DeliveryDao;
import main.com.firedrone.dao.DroneDao;
import main.com.firedrone.model.Drone;
import com.firedrone.util.DBUtil;

public class DispatchService {

    private final DroneDao droneDao = new DroneDao();
    private final DeliveryDao deliveryDao = new DeliveryDao();

    public boolean dispatchMask(int alarmId, String targetArea, int maskCount) {
        try {
            DBUtil.beginTransaction();

            Drone drone = droneDao.findBestDrone(targetArea, maskCount);

            if (drone == null) {
                DBUtil.rollbackTransaction();
                return false;
            }

            deliveryDao.addTask(alarmId, drone.getId(), targetArea, maskCount);
            droneDao.updateStatus(drone.getId(), "执行任务中");

            DBUtil.commitTransaction();
            return true;

        } catch (Exception e) {
            DBUtil.rollbackTransaction();
            throw new RuntimeException("无人机调度失败", e);
        }
    }
}