//package io.shardingsphere.example.jdbc;
//
//import com.google.common.collect.Lists;
//import io.shardingsphere.core.api.ShardingDataSourceFactory;
//import io.shardingsphere.core.api.config.MasterSlaveRuleConfiguration;
//import io.shardingsphere.core.api.config.ShardingRuleConfiguration;
//import io.shardingsphere.core.api.config.TableRuleConfiguration;
//import io.shardingsphere.core.api.config.strategy.StandardShardingStrategyConfiguration;
//import io.shardingsphere.example.jdbc.algorithm.ModuloShardingDatabaseAlgorithm;
//import io.shardingsphere.example.jdbc.algorithm.ModuloShardingTableAlgorithm;
//
//import javax.sql.DataSource;
//import java.sql.SQLException;
//import java.util.*;
//
//public class ShardingAndMasterSlave {
//    public static void main(final String[] args) throws SQLException {
//        new DataRepository(getDataSource()).demo();
//    }
//
//    private static DataSource getDataSource() throws SQLException {
//        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
//        shardingRuleConfig.getTableRuleConfigs().add(getOrderTableRuleConfiguration());
//        shardingRuleConfig.getTableRuleConfigs().add(getOrderItemTableRuleConfiguration());
//        shardingRuleConfig.getBindingTableGroups().add("t_order, t_order_item");
//        shardingRuleConfig.setDefaultDatabaseShardingStrategyConfig(new StandardShardingStrategyConfiguration("user_id", new ModuloShardingDatabaseAlgorithm()));
//        shardingRuleConfig.setDefaultTableShardingStrategyConfig(new StandardShardingStrategyConfiguration("order_id", new ModuloShardingTableAlgorithm()));
//        shardingRuleConfig.setMasterSlaveRuleConfigs(getMasterSlaveRuleConfigurations());
//        return ShardingDataSourceFactory.createDataSource(createDataSourceMap(), shardingRuleConfig, new HashMap<String, Object>(), new Properties());
//    }
//
//    private static TableRuleConfiguration getOrderTableRuleConfiguration() {
//        TableRuleConfiguration result = new TableRuleConfiguration();
//        result.setLogicTable("t_order");
//        result.setActualDataNodes("ds_${0..1}.t_order_${[0, 1]}");
//        result.setKeyGeneratorColumnName("order_id");
//        return result;
//    }
//
//    private static TableRuleConfiguration getOrderItemTableRuleConfiguration() {
//        TableRuleConfiguration result = new TableRuleConfiguration();
//        result.setLogicTable("t_order_item");
//        result.setActualDataNodes("ds_${0..1}.t_order_item_${[0, 1]}");
//        return result;
//    }
//
//    private static List<MasterSlaveRuleConfiguration> getMasterSlaveRuleConfigurations() {
//        MasterSlaveRuleConfiguration masterSlaveRuleConfig1 = new MasterSlaveRuleConfiguration("ds_0", "demo_ds_master_0", Arrays.asList("demo_ds_master_0_slave_0", "demo_ds_master_0_slave_1"));
//        MasterSlaveRuleConfiguration masterSlaveRuleConfig2 = new MasterSlaveRuleConfiguration("ds_1", "demo_ds_master_1", Arrays.asList("demo_ds_master_1_slave_0", "demo_ds_master_1_slave_1"));
//        return Lists.newArrayList(masterSlaveRuleConfig1, masterSlaveRuleConfig2);
//    }
//
//    private static Map<String, DataSource> createDataSourceMap() {
//        final Map<String, DataSource> result = new HashMap<>();
//        result.put("demo_ds_master_0", DataSourceUtil.createDataSource("demo_ds_master_0"));
//        result.put("demo_ds_master_0_slave_0", DataSourceUtil.createDataSource("demo_ds_master_0_slave_0"));
//        result.put("demo_ds_master_0_slave_1", DataSourceUtil.createDataSource("demo_ds_master_0_slave_1"));
//        result.put("demo_ds_master_1", DataSourceUtil.createDataSource("demo_ds_master_1"));
//        result.put("demo_ds_master_1_slave_0", DataSourceUtil.createDataSource("demo_ds_master_1_slave_0"));
//        result.put("demo_ds_master_1_slave_1", DataSourceUtil.createDataSource("demo_ds_master_1_slave_1"));
//        return result;
//    }
//}
