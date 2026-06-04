package com.firedrone.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtil {

    private static final HikariDataSource dataSource;
    private static final ThreadLocal<Connection> TX_CONNECTION = new ThreadLocal<>();

    static {
        try {
            Properties properties = new Properties();
            InputStream inputStream = DBUtil.class.getClassLoader().getResourceAsStream("db.properties");

            if (inputStream == null) {
                throw new RuntimeException("未找到 db.properties 配置文件");
            }

            properties.load(inputStream);

            HikariConfig config = new HikariConfig();
            config.setDriverClassName(properties.getProperty("driver"));
            config.setJdbcUrl(properties.getProperty("url"));
            config.setUsername(properties.getProperty("username"));
            config.setPassword(properties.getProperty("password"));

            config.setMinimumIdle(Integer.parseInt(properties.getProperty("pool.minIdle", "2")));
            config.setMaximumPoolSize(Integer.parseInt(properties.getProperty("pool.maxPoolSize", "10")));
            config.setConnectionTimeout(Long.parseLong(properties.getProperty("pool.connectionTimeout", "30000")));
            config.setIdleTimeout(Long.parseLong(properties.getProperty("pool.idleTimeout", "600000")));
            config.setMaxLifetime(Long.parseLong(properties.getProperty("pool.maxLifetime", "1800000")));

            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            config.addDataSourceProperty("useServerPrepStmts", "true");

            dataSource = new HikariDataSource(config);
        } catch (Exception e) {
            throw new RuntimeException("数据库连接池初始化失败", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        Connection txConn = TX_CONNECTION.get();
        if (txConn != null) {
            return new SharedConnection(txConn);
        }
        return dataSource.getConnection();
    }

    public static void beginTransaction() throws SQLException {
        if (TX_CONNECTION.get() != null) {
            throw new IllegalStateException("当前线程已存在未提交的事务");
        }
        Connection conn = dataSource.getConnection();
        conn.setAutoCommit(false);
        TX_CONNECTION.set(conn);
    }

    public static void commitTransaction() throws SQLException {
        Connection conn = TX_CONNECTION.get();
        if (conn == null) {
            throw new IllegalStateException("当前线程没有活跃的事务");
        }
        try {
            conn.commit();
        } finally {
            TX_CONNECTION.remove();
            conn.setAutoCommit(true);
            conn.close();
        }
    }

    public static void rollbackTransaction() {
        Connection conn = TX_CONNECTION.get();
        if (conn == null) {
            return;
        }
        try {
            conn.rollback();
        } catch (SQLException ignored) {
        } finally {
            TX_CONNECTION.remove();
            try {
                conn.setAutoCommit(true);
            } catch (SQLException ignored) {
            }
            try {
                conn.close();
            } catch (SQLException ignored) {
            }
        }
    }

    public static void shutdown() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }

    private static class SharedConnection implements Connection {
        private final Connection delegate;

        SharedConnection(Connection delegate) {
            this.delegate = delegate;
        }

        @Override
        public void close() {
            // 事务连接不在 DAO 层关闭，由 commitTransaction/rollbackTransaction 统一管理
        }

        // 以下方法全部委托给真正的连接
        @Override
        public java.sql.Statement createStatement() throws SQLException { return delegate.createStatement(); }
        @Override
        public java.sql.PreparedStatement prepareStatement(String sql) throws SQLException { return delegate.prepareStatement(sql); }
        @Override
        public java.sql.CallableStatement prepareCall(String sql) throws SQLException { return delegate.prepareCall(sql); }
        @Override
        public String nativeSQL(String sql) throws SQLException { return delegate.nativeSQL(sql); }
        @Override
        public void setAutoCommit(boolean autoCommit) throws SQLException { /* no-op */ }
        @Override
        public boolean getAutoCommit() throws SQLException { return delegate.getAutoCommit(); }
        @Override
        public void commit() throws SQLException { /* no-op */ }
        @Override
        public void rollback() throws SQLException { /* no-op */ }
        @Override
        public boolean isClosed() throws SQLException { return delegate.isClosed(); }
        @Override
        public java.sql.DatabaseMetaData getMetaData() throws SQLException { return delegate.getMetaData(); }
        @Override
        public void setReadOnly(boolean readOnly) throws SQLException { delegate.setReadOnly(readOnly); }
        @Override
        public boolean isReadOnly() throws SQLException { return delegate.isReadOnly(); }
        @Override
        public void setCatalog(String catalog) throws SQLException { delegate.setCatalog(catalog); }
        @Override
        public String getCatalog() throws SQLException { return delegate.getCatalog(); }
        @Override
        public void setTransactionIsolation(int level) throws SQLException { delegate.setTransactionIsolation(level); }
        @Override
        public int getTransactionIsolation() throws SQLException { return delegate.getTransactionIsolation(); }
        @Override
        public java.sql.SQLWarning getWarnings() throws SQLException { return delegate.getWarnings(); }
        @Override
        public void clearWarnings() throws SQLException { delegate.clearWarnings(); }
        @Override
        public java.sql.Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException { return delegate.createStatement(resultSetType, resultSetConcurrency); }
        @Override
        public java.sql.PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException { return delegate.prepareStatement(sql, resultSetType, resultSetConcurrency); }
        @Override
        public java.sql.CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException { return delegate.prepareCall(sql, resultSetType, resultSetConcurrency); }
        @Override
        public java.util.Map<String, Class<?>> getTypeMap() throws SQLException { return delegate.getTypeMap(); }
        @Override
        public void setTypeMap(java.util.Map<String, Class<?>> map) throws SQLException { delegate.setTypeMap(map); }
        @Override
        public void setHoldability(int holdability) throws SQLException { delegate.setHoldability(holdability); }
        @Override
        public int getHoldability() throws SQLException { return delegate.getHoldability(); }
        @Override
        public java.sql.Savepoint setSavepoint() throws SQLException { return delegate.setSavepoint(); }
        @Override
        public java.sql.Savepoint setSavepoint(String name) throws SQLException { return delegate.setSavepoint(name); }
        @Override
        public void rollback(java.sql.Savepoint savepoint) throws SQLException { delegate.rollback(savepoint); }
        @Override
        public void releaseSavepoint(java.sql.Savepoint savepoint) throws SQLException { delegate.releaseSavepoint(savepoint); }
        @Override
        public java.sql.Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException { return delegate.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability); }
        @Override
        public java.sql.PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException { return delegate.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability); }
        @Override
        public java.sql.CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException { return delegate.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability); }
        @Override
        public java.sql.PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException { return delegate.prepareStatement(sql, autoGeneratedKeys); }
        @Override
        public java.sql.PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException { return delegate.prepareStatement(sql, columnIndexes); }
        @Override
        public java.sql.PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException { return delegate.prepareStatement(sql, columnNames); }
        @Override
        public java.sql.Clob createClob() throws SQLException { return delegate.createClob(); }
        @Override
        public java.sql.Blob createBlob() throws SQLException { return delegate.createBlob(); }
        @Override
        public java.sql.NClob createNClob() throws SQLException { return delegate.createNClob(); }
        @Override
        public java.sql.SQLXML createSQLXML() throws SQLException { return delegate.createSQLXML(); }
        @Override
        public boolean isValid(int timeout) throws SQLException { return delegate.isValid(timeout); }
        @Override
        public void setClientInfo(String name, String value) throws java.sql.SQLClientInfoException { delegate.setClientInfo(name, value); }
        @Override
        public void setClientInfo(java.util.Properties properties) throws java.sql.SQLClientInfoException { delegate.setClientInfo(properties); }
        @Override
        public String getClientInfo(String name) throws SQLException { return delegate.getClientInfo(name); }
        @Override
        public java.util.Properties getClientInfo() throws SQLException { return delegate.getClientInfo(); }
        @Override
        public java.sql.Array createArrayOf(String typeName, Object[] elements) throws SQLException { return delegate.createArrayOf(typeName, elements); }
        @Override
        public java.sql.Struct createStruct(String typeName, Object[] attributes) throws SQLException { return delegate.createStruct(typeName, attributes); }
        @Override
        public void setSchema(String schema) throws SQLException { delegate.setSchema(schema); }
        @Override
        public String getSchema() throws SQLException { return delegate.getSchema(); }
        @Override
        public void abort(java.util.concurrent.Executor executor) throws SQLException { delegate.abort(executor); }
        @Override
        public void setNetworkTimeout(java.util.concurrent.Executor executor, int milliseconds) throws SQLException { delegate.setNetworkTimeout(executor, milliseconds); }
        @Override
        public int getNetworkTimeout() throws SQLException { return delegate.getNetworkTimeout(); }
        @Override
        public <T> T unwrap(Class<T> iface) throws SQLException { return delegate.unwrap(iface); }
        @Override
        public boolean isWrapperFor(Class<?> iface) throws SQLException { return delegate.isWrapperFor(iface); }
    }
}