package com.firedrone.dao;

import com.firedrone.model.MonitorPoint;
import com.firedrone.util.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MonitorDao {

    private static final Logger logger = LoggerFactory.getLogger(MonitorDao.class);

    public List<MonitorPoint> findAll() {
        List<MonitorPoint> list = new ArrayList<>();
        String sql = "SELECT * FROM monitor_point ORDER BY id DESC";

        try (
                Connection connection = DBUtil.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                MonitorPoint point = new MonitorPoint();
                point.setId(rs.getInt("id"));
                point.setName(rs.getString("name"));
                point.setArea(rs.getString("area"));
                point.setTemperature(rs.getDouble("temperature"));
                point.setSmoke(rs.getDouble("smoke"));
                point.setCo(rs.getDouble("co"));
                point.setFlame(rs.getInt("flame"));
                point.setRiskLevel(rs.getString("risk_level"));

                Timestamp ts = rs.getTimestamp("update_time");
                if (ts != null) {
                    point.setUpdateTime(ts.toLocalDateTime());
                }

                list.add(point);
            }
        } catch (Exception e) {
            logger.error("查询监控点失败", e);
        }

        return list;
    }

    public MonitorPoint findById(int id) {
        String sql = "SELECT * FROM monitor_point WHERE id = ?";

        try (
                Connection connection = DBUtil.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                MonitorPoint point = new MonitorPoint();
                point.setId(rs.getInt("id"));
                point.setName(rs.getString("name"));
                point.setArea(rs.getString("area"));
                point.setTemperature(rs.getDouble("temperature"));
                point.setSmoke(rs.getDouble("smoke"));
                point.setCo(rs.getDouble("co"));
                point.setFlame(rs.getInt("flame"));
                point.setRiskLevel(rs.getString("risk_level"));

                Timestamp ts = rs.getTimestamp("update_time");
                if (ts != null) {
                    point.setUpdateTime(ts.toLocalDateTime());
                }

                return point;
            }
        } catch (Exception e) {
            logger.error("查询监控点失败", e);
        }

        return null;
    }

    public void updateRiskLevel(int id, String riskLevel) {
        String sql = "UPDATE monitor_point SET risk_level = ?, update_time = NOW() WHERE id = ?";

        try (
                Connection connection = DBUtil.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setString(1, riskLevel);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (Exception e) {
            logger.error("更新风险等级失败", e);
        }
    }

    public int count() {
        String sql = "SELECT COUNT(*) FROM monitor_point";

        try (
                Connection connection = DBUtil.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            logger.error("统计监控点失败", e);
        }

        return 0;
    }
}