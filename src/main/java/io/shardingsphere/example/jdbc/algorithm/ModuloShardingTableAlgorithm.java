package io.shardingsphere.example.jdbc.algorithm;

import io.shardingsphere.core.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.core.api.algorithm.sharding.standard.PreciseShardingAlgorithm;

import java.util.Collection;

public class ModuloShardingTableAlgorithm implements PreciseShardingAlgorithm<Long> {

    @Override
    public String doSharding(final Collection<String> tableNames, final PreciseShardingValue<Long> shardingValue) {
        for (String each : tableNames) {
            if (each.endsWith(shardingValue.getValue() % 2 + "")) {
                return each;
            }
        }
        throw new UnsupportedOperationException();
    }
}