/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.enums;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public enum SpriteTypeEnum {
    PLAYER(0),
    CREATURE(-1), //?
    MONSTER(-2),
    MONSTER_GROUP(-3),
    NPC(-4),
    OFFLINE_CHARACTER(-5), //seller ?
    COLLECTOR(-6),
    MUTANT(-7),
    MUTANT_PLAYER(-8),
    MOUNT_PARK(-9),
    PRISM(-10),
    ;
    final private int type;

    private SpriteTypeEnum(int type) {
        this.type = type;
    }

    public int toInt() {
        return type;
    }
}
