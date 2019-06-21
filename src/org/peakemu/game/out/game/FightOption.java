/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.game;

import org.peakemu.world.fight.team.FightTeam;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class FightOption {
    final static public char HELP = 'H';
    final static public char SPECTATOR = 'S';
    final static public char LOCK = 'A';
    final static public char ONLY_GROUP = 'P';
    
    private FightTeam team;
    private boolean isAdd;
    private char option;

    public FightOption(FightTeam team, boolean isAdd, char option) {
        this.team = team;
        this.isAdd = isAdd;
        this.option = option;
    }

    public FightTeam getTeam() {
        return team;
    }

    public void setTeam(FightTeam team) {
        this.team = team;
    }

    public boolean isIsAdd() {
        return isAdd;
    }

    public void setIsAdd(boolean isAdd) {
        this.isAdd = isAdd;
    }

    public char getOption() {
        return option;
    }

    public void setOption(char option) {
        this.option = option;
    }

    @Override
    public String toString() {
        return "Go" + (isAdd ? "+" : "-") + option + team.getId();
    }
    
    
}
