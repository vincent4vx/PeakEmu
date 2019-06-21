/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.enums;

import org.peakemu.world.Stats;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public enum PlayerRace {
    NO_CLASS(10300, 308),
	FECA(10300, 308),
	OSAMODAS(10284, 372),
	ENUTROF(10299, 286){
        @Override
        public Stats getStartStats(int level) {
            Stats stats = super.getStartStats(level);
            stats.addOneStat(Effect.ADD_PROS, 20);
            return stats;
        }
    },
	SRAM(10285, 234),
	XELOR(10298, 300),
	ECAFLIP(10276, 250),
	ENIRIPSA(10283, 270),
	IOP(10294, 278),
	CRA(10292, 300),
	SADIDA(10279, 270),
	SACRIEUR(10296, 244),
	PANDAWA(10289, 263),
    ;
    
    final private int startMap;
    final private int startCell;

    private PlayerRace(int startMap, int startCell) {
        this.startMap = startMap;
        this.startCell = startCell;
    }
    
    public Stats getStartStats(int level){
        Stats stats = new Stats();
        
        stats.addOneStat(Effect.ADD_PA, 6);
        stats.addOneStat(Effect.ADD_PM, 3);
        stats.addOneStat(Effect.ADD_PROS, 100);
        stats.addOneStat(Effect.ADD_PODS, 1000);
        stats.addOneStat(Effect.CREATURE, 1);
        stats.addOneStat(Effect.ADD_INIT, 1);
        stats.addOneStat(Effect.ADD_VITA, 50 + (level - 1) * 5);
        
        return stats;
    }

    public short getStartMap() {
        return (short) startMap;
    }

    public short getStartCell() {
        return (short) startCell;
    }
    
    public int getGfxId(int gender){
        return 10 * ordinal() + gender;
    }
}
