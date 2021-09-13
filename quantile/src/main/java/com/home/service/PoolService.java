package com.home.service;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@Service
@Scope("singleton")
public class PoolService {
    Map<Integer, ConcurrentSkipListSet> pool = new ConcurrentHashMap<Integer, ConcurrentSkipListSet>();


    private static ConcurrentSkipListSet<Integer> InitValueSet() {
        // the quick workaround to make set become multiset(allowing duplicated values)
        ConcurrentSkipListSet set = new ConcurrentSkipListSet<Integer>((o1, o2) -> {
            if (o1 <= o2)
                return -1;
            return 1;
        });
        return set;
    }

    public Integer addValues(Integer poolId, Collection<Integer> c) {
        //Preventing concurrent initialization by using lock, also using "Double-checked locking" to minimize locking time.
        ConcurrentSkipListSet valueSet = pool.get(poolId);
        if (valueSet == null) {
            synchronized (pool) {
                valueSet = pool.get(poolId);
                if (valueSet == null) {
                    valueSet = InitValueSet();
                    if (valueSet.addAll(c)) {
                        pool.put(poolId, valueSet);
                        return 1;
                    }
                }
                if (valueSet.addAll(c))
                    return 2;
            }
        } else if (valueSet.addAll(c))
            return 2;
        return 0;
    }

    public Integer[] getQuantile(Integer poolId, float percentage) {
        ConcurrentSkipListSet valueSet = pool.get(poolId);
        if(valueSet==null)
            return null;
        Object[] valArr = valueSet.toArray();
        double index = Math.floor((percentage / 100) * (valArr.length + 1));
        Integer result[] = {(Integer) valArr[(int) index - 1],valArr.length  };
        return result;

    }


}
