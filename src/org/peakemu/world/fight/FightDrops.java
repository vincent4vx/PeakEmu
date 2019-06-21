/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight;

import java.util.Collection;
import java.util.Collections;
import org.peakemu.world.World;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class FightDrops {
    final static public FightDrops EMPTY_DROPS = new FightDrops(0, 0, 0, Collections.EMPTY_LIST);
    
    final private int minKamas;
    final private int maxKamas;
    final private long totalXP;
    final private Collection<World.Drop> drops;

    public FightDrops(int minKamas, int maxKamas, long totalXP, Collection<World.Drop> drops) {
        this.minKamas = minKamas;
        this.maxKamas = maxKamas;
        this.totalXP = totalXP;
        this.drops = drops;
    }

    public int getMinKamas() {
        return minKamas;
    }

    public int getMaxKamas() {
        return maxKamas;
    }

    public Collection<World.Drop> getDrops() {
        return drops;
    }

    public long getTotalXP() {
        return totalXP;
    }
}
