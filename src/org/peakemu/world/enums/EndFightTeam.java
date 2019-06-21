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
public enum EndFightTeam {
    LOOSER(0),
    WINNER(2),
    COLLECTOR(5),
    FIGHT_DROP(6), //?
    ;
    final private int id;

    private EndFightTeam(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
