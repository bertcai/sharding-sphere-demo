package io.shardingsphere.example.jdbc.algorithm;

import io.shardingsphere.core.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.core.api.algorithm.sharding.standard.PreciseShardingAlgorithm;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

public class ModuloShardingTableAlgorithm2 implements PreciseShardingAlgorithm<Long> {

    // @Override
    // public String doSharding(final Collection<String> tableNames, final PreciseShardingValue<Long> shardingValue) {
    //     for (String each : tableNames) {
    //         if (each.endsWith(shardingValue.getValue() % 2 + "")) {
    //             return each;
    //         }
    //     }
    //     throw new UnsupportedOperationException();
    // }
    private static SortedMap<Integer, String> sortedMap = new TreeMap<Integer, String>();


    @Override
    public String doSharding(Collection<String> tableNames, PreciseShardingValue<Long> shardingValue) {
        for (String each : tableNames) {
            int hash = getHash(each);
            sortedMap.put(hash, each);
			if (each.endsWith(getHash(shardingValue.getValue()+"") % 2 + "")) {
				System.out.println(each);
				return each;
			}
        }
		throw new UnsupportedOperationException();


//        int hash = getHash(shardingValue.getValue() + " ");
//        int key = sortedMap.tailMap(hash).isEmpty() ? sortedMap.firstKey() : sortedMap.tailMap(hash).firstKey();
//        System.out.println(sortedMap.get(key));
//        return sortedMap.get(key);
//		if(subMap.isEmpty()){
//			return sortedMap.get(sortedMap.firstKey());
//		}
//		Integer i = subMap.firstKey();
//		return subMap.get(i);
    }


    private static int getHash(String str) {
        final int p = 16777619;
        int hash = (int) 2166136261L;

        for (int i = 0; i < str.length(); i++) {
            hash = (hash ^ str.charAt(i)) * p;
        }

        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash >> 3;
        hash ^= hash >> 17;
        hash += hash << 5;

        if (hash < 0)
            hash = Math.abs(hash);
        return hash;
    }
}