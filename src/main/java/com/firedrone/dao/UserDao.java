package com.firedrone.dao;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.firedrone.model.User;
import com.firedrone.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDao {

    private static final BCrypt.Verifyer VERIFYER = BCrypt.verifyer();
    private static final BCrypt.Hasher HASHER = BCrypt.withDefaults();
    private static final int BCRYPT_COST = 12;

    public User login(String username, String password) {
        String sql = "SELECT id, username, password, role FROM user WHERE username = ?";

        try (
                Connection connection = DBUtil.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return null;
            }

            int id = rs.getInt("id");
            String storedPassword = rs.getString("password");
            String role = rs.getString("role");

            boolean matched = false;
            boolean needsMigration = false;

            if (storedPassword != null && storedPassword.startsWith("$2")) {
                BCrypt.Result result = VERIFYER.verify(password.toCharArray(), storedPassword);
                matched = result.verified;
            } else {
                if (password.equals(storedPassword)) {
                    matched = true;
                    needsMigration = true;
                }
            }

            if (!matched) {
                return null;
            }

            if (needsMigration) {
                migratePassword(connection, id, password);
            }

            User user = new User();
            user.setId(id);
            user.setUsername(username);
            user.setRole(role);
            return user;

        } catch (Exception e) {
            throw new RuntimeException("µ«¬º≤È—Ø ß∞‹", e);
        }
    }

    private void migratePassword(Connection connection, int userId, String plainPassword) {
        String sql = "UPDATE user SET password = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            String hash = HASHER.hashToString(BCRYPT_COST, plainPassword.toCharArray());
            ps.setString(1, hash);
            ps.setInt(2, userId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("√‹¬Î«®“∆ ß∞‹", e);
        }
    }
}