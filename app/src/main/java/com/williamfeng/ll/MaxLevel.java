package com.williamfeng.ll;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by williamfeng on 8/01/15.
 */
public class MaxLevel {
    public static final Map<Rarity, Integer> maxLevelMap = createMap();

    private static Map<Rarity, Integer> createMap() {
        Map<Rarity, Integer> map = new HashMap<Rarity, Integer>();
        map.put(Rarity.N, 30);
        map.put(Rarity.N_PLUS, 40);
        map.put(Rarity.R, 40);
        map.put(Rarity.R_PLUS, 60);
        map.put(Rarity.SR, 60);
        map.put(Rarity.SR_PLUS, 80);
        map.put(Rarity.UR, 80);
        map.put(Rarity.UR_PLUS, 100);
        return Collections.unmodifiableMap(map);
    }
}
