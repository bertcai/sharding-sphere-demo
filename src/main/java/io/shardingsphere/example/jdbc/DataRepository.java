package io.shardingsphere.example.jdbc;

import io.shardingsphere.core.api.HintManager;

import javax.sql.DataSource;
import java.sql.*;

public class DataRepository {
    private final DataSource dataSource;

    public DataRepository(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void demo() throws SQLException {
        createTable();
        insertData();
        System.out.println("1.Query with EQUAL--------------");
        queryWithEqual();
        System.out.println("2.Query with IN--------------");
        queryWithIn();
        System.out.println("3.Query with Hint--------------");
        queryWithHint();
        System.out.println("4.Drop tables--------------");
        dropTable();
        System.out.println("5.All done-----------");
    }

    private void createTable() throws SQLException {
        execute("CREATE TABLE IF NOT EXISTS t_order (order_id BIGINT NOT NULL AUTO_INCREMENT, user_id INT NOT NULL, status VARCHAR(50), PRIMARY KEY (order_id))");
//        syncData("CREATE TABLE IF NOT EXISTS t_order (order_id BIGINT NOT NULL AUTO_INCREMENT, user_id INT NOT NULL, status VARCHAR(50), PRIMARY KEY (order_id))");
        execute("CREATE TABLE IF NOT EXISTS t_order_item (order_item_id BIGINT NOT NULL AUTO_INCREMENT, order_id BIGINT NOT NULL, user_id INT NOT NULL, PRIMARY KEY (order_item_id))");
//        syncData("CREATE TABLE IF NOT EXISTS t_order_item (order_item_id BIGINT NOT NULL AUTO_INCREMENT, order_id BIGINT NOT NULL, user_id INT NOT NULL, PRIMARY KEY (order_item_id))");
    }

    private void insertData() throws SQLException {
        for (int i = 1; i < 10; i++) {
            long orderId = insertAndGetGeneratedKey("INSERT INTO t_order (user_id, status) VALUES (10, 'INIT')");
            execute(String.format("INSERT INTO t_order_item (order_id, user_id) VALUES (%d, 10)", orderId));
//            syncData(String.format("INSERT INTO t_order_item (order_id, user_id) VALUES (%d, 10)", orderId));
            orderId = insertAndGetGeneratedKey("INSERT INTO t_order (user_id, status) VALUES (11, 'INIT')");
            execute(String.format("INSERT INTO t_order_item (order_id, user_id) VALUES (%d, 11)", orderId));
//            syncData(String.format("INSERT INTO t_order_item (order_id, user_id) VALUES (%d, 11)", orderId));
        }
    }

    private long insertAndGetGeneratedKey(final String sql) throws SQLException {
        long result = -1;
        try (
                Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
//            syncUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    result = resultSet.getLong(1);
                }
            }
        }
        return result;
    }

    private void queryWithEqual() throws SQLException {
        String sql = "SELECT i.* FROM t_order o JOIN t_order_item i ON o.order_id=i.order_id WHERE o.user_id=?";
        String sql2 = "SELECT * FROM t_order";
        try (
                Connection connection = dataSource.getConnection();
                Statement s = connection.createStatement();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, 10);
            printQuery(preparedStatement);
//            s.execute(sql2);
//            ResultSet rs = s.getResultSet();
//            while (rs.next()) {
//                System.out.println(rs.getLong(1));
//            }
        }
    }

    private void queryWithIn() throws SQLException {
        String sql = "SELECT i.* FROM t_order o JOIN t_order_item i ON o.order_id=i.order_id WHERE o.user_id IN (?, ?)";
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, 10);
            preparedStatement.setInt(2, 11);
            printQuery(preparedStatement);
        }
    }

    private void queryWithHint() throws SQLException {
        String sql = "SELECT i.* FROM t_order o JOIN t_order_item i ON o.order_id=i.order_id";
        try (
                HintManager hintManager = HintManager.getInstance();
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            hintManager.addDatabaseShardingValue("t_order", "user_id", 11);
            printQuery(preparedStatement);
        }
    }

    private void printQuery(final PreparedStatement preparedStatement) throws SQLException {
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
//            System.out.println(resultSet.getLong(1));
            while (resultSet.next()) {
                System.out.print("order_item_id:" + resultSet.getLong(1) + ", ");
                System.out.print("order_id:" + resultSet.getLong(2) + ", ");
                System.out.print("user_id:" + resultSet.getInt(3));
                System.out.println();
            }
        }
    }

    private void dropTable() throws SQLException {
        execute("DROP TABLE t_order_item");
//        syncData("DROP TABLE t_order_item");
        execute("DROP TABLE t_order");
//        syncData("DROP TABLE t_order");
    }

    private void execute(final String sql) throws SQLException {
        try (
                Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

//    private void syncData(String sql) {
//        DataSource slave0 = DataSourceUtil.createDataSource("demo_ds_slave_0");
//        DataSource slave1 = DataSourceUtil.createDataSource("demo_ds_slave_1");
//        try (
//                Connection c0 = slave0.getConnection();
//                Statement s0 = c0.createStatement();
//                Connection c1 = slave1.getConnection();
//                Statement s1 = c1.createStatement()
//        ) {
//            s0.execute(sql);
//            s1.execute(sql);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void syncUpdate(String sql, int i) {
//        DataSource slave0 = DataSourceUtil.createDataSource("demo_ds_slave_0");
//        DataSource slave1 = DataSourceUtil.createDataSource("demo_ds_slave_1");
//        try (
//                Connection c0 = slave0.getConnection();
//                Statement s0 = c0.createStatement();
//                Connection c1 = slave1.getConnection();
//                Statement s1 = c1.createStatement()
//        ) {
//            s0.executeUpdate(sql, i);
//            s1.executeUpdate(sql, i);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void syncMasterSlave() {
//        DataSource master = DataSourceUtil.createDataSource("demo_ds_master");
//        DataSource slave0 = DataSourceUtil.createDataSource("demo_ds_slave_0");
//        DataSource slave1 = DataSourceUtil.createDataSource("demo_ds_slave_1");
//
//        try (
//                Connection masterConnection = master.getConnection();
//                Connection slaveConnection0 = slave0.getConnection();
//                Connection slaveConnection1 = slave1.getConnection();
//                Statement masterStatement = masterConnection.createStatement();
//                Statement slaveStatement0 = slaveConnection0.createStatement();
//                Statement slaveStatement1 = slaveConnection1.createStatement()
//        ) {
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
}
