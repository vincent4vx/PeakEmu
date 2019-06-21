/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.enums;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public enum PlayerStat {
    FORCE(10, Effect.ADD_FORC),
    VITALITE(11, Effect.ADD_VITA),
    SAGESSE(12, Effect.ADD_SAGE),
    CHANCE(13, Effect.ADD_CHAN),
    AGILITE(14, Effect.ADD_AGIL),
    INTELLIGENCE(15, Effect.ADD_INTE);
    final private int id;
    final private Effect effect;

    final static private Map<Integer, PlayerStat> statsById = new HashMap<>();

    static {
        for (PlayerStat bs : PlayerStat.values()) {
            statsById.put(bs.id, bs);
        }
    }

    private PlayerStat(int id, Effect realStat) {
        this.id = id;
        this.effect = realStat;
    }

    public int getId() {
        return id;
    }

    public Effect getEffect() {
        return effect;
    }

    static public PlayerStat valueOf(int id) {
        return statsById.get(id);
    }
}
