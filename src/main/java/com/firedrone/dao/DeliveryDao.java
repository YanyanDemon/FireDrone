package com.firedrone.dao;

import com.firedrone.model.DeliveryTask;
import com.firedrone.util.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DeliveryDao {

    private static final Logger logger = LoggerFactory.getLogger(DeliveryDao.class);

    public List<DeliveryTask> findAll() {
        List<DeliveryTask> list = new ArrayList<>();

        String sql = """
                SELECT t.*, d.name AS drone_name
                FROM delivery_task t
                LEFT JOIN drone d ON t.drone_id = d.id
                ORDER BY t.id DESC
                """;

        try (
                Connection connection = DBUtil.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                DeliveryTask task = new DeliveryTask();
                task.setId(rs.getInt("id"));
                task.setAlarmId(rs.getInt("alarm_id"));
                task.setDroneId(rs.getInt("drone_id"));
                task.setDroneName(rs.getString("drone_name"));
                task.setTargetArea(rs.getString("target_area"));
                task.setMaskCount(rs.getInt("mask_count"));
                task.setStatus(rs.getString("status"));

                Timestamp createTs = rs.getTimestamp("create_time");
                if (createTs != null) {
                    task.setCreateTime(createTs.toLocalDateTime());
                }

                Timestamp finishTs = rs.getTimestamp("finish_time");
                if (finishTs != null) {
                    task.setFinishTime(finishTs.toLocalDateTime());
                }

                list.add(task);
            }
        } catch (Exception e) {
            logger.error("查询配送任务失败", e);
        }

        return list;
    }

    public void addTask(int alarmId, int droneId, String targetArea, int maskCount) {
        String sql = """
                INSERT INTO delivery_task(alarm_id, drone_id, target_area, mask_count, status, create_time)
                VALUES (?, ?, ?, ?, '配送中', NOW())
                """;

        try (
                Connection connection = DBUtil.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, alarmId);
            ps.setInt(2, droneId);
            ps.setString(3, targetArea);
            ps.setInt(4, maskCount);
            ps.executeUpdate();
        } catch (Exception e) {
            logger.error("创建配送任务失败", e);
        }
    }

    public void finishTask(int taskId) {
        String sql = "UPDATE delivery_task SET status = '已送达', finish_time = NOW() WHERE id = ?";

        try (
                Connection connection = DBUtil.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, taskId);
            ps.executeUpdate();
        } catch (Exception e) {
            logger.error("完成任务失败", e);
        }
    }

    public int count() {
        String sql = "SELECT COUNT(*) FROM delivery_task";

        try (
                Connection connection = DBUtil.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            logger.error("统计配送任务失败", e);
        }

        return 0;
    }
}