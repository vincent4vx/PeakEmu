/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight;

import org.peakemu.world.fight.fighter.Fighter;
import java.util.Collection;
import java.util.Collections;
import org.peakemu.common.util.Pair;
import org.peakemu.world.enums.EndFightTeam;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class FightRewards {
    final private EndFightTeam team;
    final private Fighter fighter;
    final private long winXp;
    final private long guildXp;
    final private long mountXp;
    final private long winKamas;
    final private int winHonour;
    final private int winDisgrace;
    final private Collection<Pair<Integer, Integer>> drops;

    public FightRewards(EndFightTeam team, Fighter fighter, long winXp, long guildXp, long mountXp, long winKamas, int winHonour, int winDisgrace, Collection<Pair<Integer, Integer>> drops) {
        this.team = team;
        this.fighter = fighter;
        this.winXp = winXp;
        this.guildXp = guildXp;
        this.mountXp = mountXp;
        this.winKamas = winKamas;
        this.winHonour = winHonour;
        this.winDisgrace = winDisgrace;
        this.drops = drops;
    }

    public EndFightTeam getTeam() {
        return team;
    }

    public Fighter getFighter() {
        return fighter;
    }

    public long getWinXp() {
        return winXp;
    }

    public long getGuildXp() {
        return guildXp;
    }

    public long getMountXp() {
        return mountXp;
    }

    public long getWinKamas() {
        return winKamas;
    }

    public int getWinHonour() {
        return winHonour;
    }

    public int getWinDisgrace() {
        return winDisgrace;
    }

    public Collection<Pair<Integer, Integer>> getDrops() {
        return drops;
    }
    
    static public FightRewards emptyRewards(EndFightTeam team, Fighter fighter){
        return new FightRewards(team, fighter, 0, 0, 0, 0, 0, 0, Collections.EMPTY_LIST);
    }
}
