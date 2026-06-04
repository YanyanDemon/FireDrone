package com.firedrone.dao;

import com.firedrone.model.Alarm;
import com.firedrone.util.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlarmDao {

    private static final Logger logger = LoggerFactory.getLogger(AlarmDao.class);

    public List<Alarm> findAll() {
        List<Alarm> list = new ArrayList<>();

        String sql = """
                SELECT a.*, m.name AS monitor_name
                FROM alarm a
                LEFT JOIN monitor_point m ON a.monitor_id = m.id
                ORDER BY a.id DESC
                """;

        try (
                Connection connection = DBUtil.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                Alarm alarm = new Alarm();
                alarm.setId(rs.getInt("id"));
                alarm.setMonitorId(rs.getInt("monitor_id"));
                alarm.setMonitorName(rs.getString("monitor_name"));
                alarm.setLevel(rs.getString("level"));
                alarm.setContent(rs.getString("content"));
                alarm.setStatus(rs.getString("status"));

                Timestamp ts = rs.getTimestamp("create_time");
                if (ts != null) {
                    alarm.setCreateTime(ts.toLocalDateTime());
                }

                list.add(alarm);
            }
        } catch (Exception e) {
            logger.error("查询告警失败", e);
        }

        return list;
    }

    public int addAlarm(int monitorId, String level, String content) {
        String sql = "INSERT INTO alarm(monitor_id, level, content, status, create_time) VALUES (?, ?, ?, '未处理', NOW())";

        try (
                Connection connection = DBUtil.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setInt(1, monitorId);
            ps.setString(2, level);
            ps.setString(3, content);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            logger.error("创建告警失败", e);
        }

        return -1;
    }

    public Alarm findById(int id) {
        String sql = """
                SELECT a.*, m.name AS monitor_name, m.area AS monitor_area
                FROM alarm a
                LEFT JOIN monitor_point m ON a.monitor_id = m.id
                WHERE a.id = ?
                """;

        try (
                Connection connection = DBUtil.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Alarm alarm = new Alarm();
                alarm.setId(rs.getInt("id"));
                alarm.setMonitorId(rs.getInt("monitor_id"));
                alarm.setMonitorName(rs.getString("monitor_name"));
                alarm.setLevel(rs.getString("level"));
                alarm.setContent(rs.getString("content"));
                alarm.setStatus(rs.getString("status"));

                Timestamp ts = rs.getTimestamp("create_time");
                if (ts != null) {
                    alarm.setCreateTime(ts.toLocalDateTime());
                }

                return alarm;
            }
        } catch (Exception e) {
            logger.error("查询告警失败", e);
        }

        return null;
    }

    public int count() {
        String sql = "SELECT COUNT(*) FROM alarm";

        try (
                Connection connection = DBUtil.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            logger.error("统计告警失败", e);
        }

        return 0;
    }
}