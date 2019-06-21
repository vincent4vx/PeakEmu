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
public enum Element {
    NONE(null, null, null, null, null, null),
	NEUTRAL(Effect.ADD_FORC, Effect.ADD_R_NEU, Effect.ADD_RP_NEU, Effect.ADD_R_PVP_NEU, Effect.ADD_RP_PVP_NEU, Effect.RES_PHY),
	EARTH(Effect.ADD_FORC, Effect.ADD_R_TER, Effect.ADD_RP_TER, Effect.ADD_R_PVP_TER, Effect.ADD_RP_PVP_TER, Effect.RES_PHY),
	WATER(Effect.ADD_CHAN, Effect.ADD_R_EAU, Effect.ADD_RP_EAU, Effect.ADD_R_PVP_EAU, Effect.ADD_RP_PVP_EAU, Effect.RES_MAG),
	FIRE(Effect.ADD_INTE, Effect.ADD_R_FEU, Effect.ADD_RP_FEU, Effect.ADD_R_PVP_FEU, Effect.ADD_RP_PVP_FEU, Effect.RES_MAG),
	AIR(Effect.ADD_AGIL, Effect.ADD_R_AIR, Effect.ADD_RP_AIR, Effect.ADD_R_PVP_AIR, Effect.ADD_RP_PVP_AIR, Effect.RES_MAG),
    ;
    
    final private Effect boost;
    
    final private Effect resFix;
    final private Effect resPer;
    final private Effect resFixPvp;
    final private Effect resPerPvp;
    
    final private Effect resSpec;

    private Element(Effect boost, Effect resFix, Effect resPer, Effect resFixPvp, Effect resPerPvp, Effect resSpec) {
        this.boost = boost;
        this.resFix = resFix;
        this.resPer = resPer;
        this.resFixPvp = resFixPvp;
        this.resPerPvp = resPerPvp;
        this.resSpec = resSpec;
    }

    public Effect getBoost() {
        return boost;
    }

    public Effect getResFix() {
        return resFix;
    }

    public Effect getResPer() {
        return resPer;
    }

    public Effect getResFixPvp() {
        return resFixPvp;
    }

    public Effect getResPerPvp() {
        return resPerPvp;
    }

    public Effect getResSpec() {
        return resSpec;
    }
    
    public double getBoostCoef(Stats stats){
        double value = 0;
        
        if(boost != null){
            value = (double)stats.getEffect(boost) / 100;
        }
        
        if(value >= 0){
            return 1 + value;
        }
        
        value = -value;
        
        return 1 / (1 + value);
    }
    
    public int getResFix(Stats stats, boolean pvp){
        int res = 0;
        
        if(resFix != null)
            res += stats.getEffect(resFix);
        
        if(resSpec != null)
            res += stats.getEffect(resSpec);
        
        if(pvp && resFixPvp != null)
            res += stats.getEffect(resFixPvp);
        
        return res;
    }
    
    public double getResCoef(Stats stats, boolean pvp){
        double res = 0;
        
        if(resPer != null)
            res += stats.getEffect(resPer);
        
        if(pvp && resPerPvp != null)
            res += stats.getEffect(resPerPvp);
        
        res = 1 - res / 100;
        return res;
    }
    
    public int getId(){
        return ordinal() - 1;
    }
}
