package main.com.firedrone.dao;

import com.firedrone.model.Drone;
import com.firedrone.util.DBUtil;
import main.com.firedrone.model.Drone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DroneDao {

    private static final Logger logger = LoggerFactory.getLogger(DroneDao.class);

    public List<Drone> findAll() {
        List<Drone> list = new ArrayList<>();
        String sql = "SELECT * FROM drone ORDER BY id DESC";

        try (
                Connection connection = DBUtil.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                Drone drone = new Drone();
                drone.setId(rs.getInt("id"));
                drone.setName(rs.getString("name"));
                drone.setArea(rs.getString("area"));
                drone.setBattery(rs.getInt("battery"));
                drone.setMaxLoad(rs.getDouble("max_load"));
                drone.setMaskCapacity(rs.getInt("mask_capacity"));
                drone.setStatus(rs.getString("status"));
                list.add(drone);
            }
        } catch (Exception e) {
            logger.error("数据库操作失败", e);
        }

        return list;
    }

    public Drone findBestDrone(String targetArea, int maskCount) {
        String sql = """
                SELECT *
                FROM drone
                WHERE status = '绌洪棽'
                  AND battery >= 30
                  AND mask_capacity >= ?
                ORDER BY 
                    CASE WHEN area = ? THEN 0 ELSE 1 END,
                    battery DESC
                LIMIT 1
                """;

        try (
                Connection connection = DBUtil.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, maskCount);
            ps.setString(2, targetArea);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Drone drone = new Drone();
                drone.setId(rs.getInt("id"));
                drone.setName(rs.getString("name"));
                drone.setArea(rs.getString("area"));
                drone.setBattery(rs.getInt("battery"));
                drone.setMaxLoad(rs.getDouble("max_load"));
                drone.setMaskCapacity(rs.getInt("mask_capacity"));
                drone.setStatus(rs.getString("status"));
                return drone;
            }

        } catch (Exception e) {
            logger.error("数据库操作失败", e);
        }

        return null;
    }

    public void updateStatus(int droneId, String status) {
        String sql = "UPDATE drone SET status = ? WHERE id = ?";

        try (
                Connection connection = DBUtil.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setString(1, status);
            ps.setInt(2, droneId);
            ps.executeUpdate();
        } catch (Exception e) {
            logger.error("数据库操作失败", e);
        }
    }

    public int count() {
        String sql = "SELECT COUNT(*) FROM drone";

        try (
                Connection connection = DBUtil.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            logger.error("数据库操作失败", e);
        }

        return 0;
    }
}