package io.shardingsphere.example.jdbc;

import io.shardingsphere.core.api.MasterSlaveDataSourceFactory;
import io.shardingsphere.core.api.config.MasterSlaveRuleConfiguration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TestShardingSphere {
    public static void main(String[] args) throws SQLException {
//        Map<String, DataSource> dataSourceMap = new HashMap<>();
//
//        //master
//        DataSource masterDateSource = DataSourceUtil.createDataSource("master");
//        dataSourceMap.put("ds_master", masterDateSource);
//
//        //slave_1
//        DataSource slaveDateSource1 = DataSourceUtil.createDataSource("slave_1");
//        dataSourceMap.put("ds_slave1", slaveDateSource1);
//
//        //slave_2
//        DataSource slaveDateSource2 = DataSourceUtil.createDataSource("slave_2");
//        dataSourceMap.put("ds_slave2", slaveDateSource2);
//
//        //配置读写分离规则
//        MasterSlaveRuleConfiguration masterSlaveRuleConfig = new MasterSlaveRuleConfiguration(
//                "ds_master_slave", "ds_master", Arrays.asList("ds_slave0", "ds_slave1"));
//
//        //获取数据源对象
////        try {
////            DataSource dataSource = MasterSlaveDataSourceFactory.createDataSource(
////                    dataSourceMap, masterSlaveRuleConfig, new HashMap<String, Object>());
////        } catch (SQLException e) {
////            e.printStackTrace();
////        }
//
//
//        String sql1 = "select * from slave_1";
//        String sql2 = "insert into t_order (order_id,user_id,business_id) values (1,1,200)";
//
//        try {
//            DataSource dataSource = MasterSlaveDataSourceFactory.createDataSource(
//                    dataSourceMap, masterSlaveRuleConfig, new HashMap<String, Object>());
//            Connection connection = dataSource.getConnection();
//            Statement s = connection.createStatement();
//            s.execute(sql1);
//            ResultSet rs = s.getResultSet();
//            while (rs.next()) {
//                int orderId = rs.getInt(1);
//                int userId = rs.getInt(2);
//                int businessId = rs.getInt(3);
//                System.out.println("" + orderId + " " + userId + " " + businessId);
//            }
//            s.execute(sql2);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        new DataRepository(getDataSource()).demo();
    }

    private static DataSource getDataSource() throws SQLException {
        MasterSlaveRuleConfiguration masterSlaveRuleConfig = new MasterSlaveRuleConfiguration("demo_ds_master_slave", "demo_ds_master", Arrays.asList("demo_ds_slave_0", "demo_ds_slave_1"));
        return MasterSlaveDataSourceFactory.createDataSource(createDataSourceMap(), masterSlaveRuleConfig, new ConcurrentHashMap<String, Object>());
    }

    private static Map<String, DataSource> createDataSourceMap() {
        Map<String, DataSource> result = new HashMap<>();
        result.put("demo_ds_master", DataSourceUtil.createDataSource("demo_ds_master",3300,"root","admin"));
        result.put("demo_ds_slave_0", DataSourceUtil.createDataSource("demo_ds_slave_0",3301,"root",""));
        result.put("demo_ds_slave_1", DataSourceUtil.createDataSource("demo_ds_slave_1",3302,"root",""));
        return result;
    }
}
