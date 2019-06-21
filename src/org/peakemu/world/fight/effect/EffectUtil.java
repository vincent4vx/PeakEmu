/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight.effect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.PriorityQueue;
import org.peakemu.Peak;
import org.peakemu.common.Logger;
import org.peakemu.common.util.Util;
import org.peakemu.world.SpellEffect;
import org.peakemu.world.Stats;
import org.peakemu.world.enums.Effect;
import org.peakemu.world.enums.Element;
import org.peakemu.world.fight.fighter.Fighter;
import org.peakemu.world.fight.fighter.PlayerFighter;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class EffectUtil {
    static public double applyPerDom(double dmg, Stats stats){
        double value = (double)stats.getEffect(Effect.ADD_PERDOM) / 100;
        return dmg * (1 + value);
    }
    
    static public double applyBoostStats(double dmg, Stats stats, Element element){
        return dmg * element.getBoostCoef(stats);
    }
    
    static public double applyPercentBoost(double value, int boost){
        double d = (double)boost / 100;
        double coef;
        
        if(d < 0){
            d = -d;
            coef = 1 / (1 + d);
        }else{
            coef = 1 + value;
        }
        
        return value * coef;
    }
    
    static public double applyResistances(double dmg, Element element, Fighter caster, Fighter target){
        Stats stats = target.getTotalStats();
        
        boolean pvp = (caster instanceof PlayerFighter) && (target instanceof PlayerFighter);
        int resFix = element.getResFix(stats, pvp);
        double resCoef = element.getResCoef(stats, pvp);
        
        dmg -= resFix;
        dmg *= resCoef;
        
        if(dmg < 0)
            dmg = 0;
        
        return dmg;
    }
    
    static public int computeRemovedPoints(int value, Effect points, Effect resis, Fighter caster, Fighter target){
        Stats casterStats = caster.getBaseStats();
        Stats targetStats = target.getTotalStats();
        
        int totalPoints = targetStats.getEffect(points);
        int curPoints = points == Effect.REM_PA ? target.getCurPA() : target.getCurPM();
        int removed = 0;
        
        double resCoef = (double)(casterStats.getEffect(resis) + 1) / (double)(targetStats.getEffect(resis) + 1);
        
        for(;value > 0 && curPoints > 0; --value){
            double ptsCoef = (double)curPoints / (double)totalPoints;
            
            int chance = (int)(resCoef * ptsCoef * 50);
            
            if(Util.randBool(chance)){
                ++removed;
                --curPoints;
            }
        }
        
        return removed;
    }
    
    static public Collection<SpellEffect> selectConditionalEffects(Collection<SpellEffect> effects){
        Collection<SpellEffect> selected = new ArrayList<>(effects.size());
        PriorityQueue<SpellEffect> conditionals = new PriorityQueue<>((SpellEffect e1, SpellEffect e2) -> {
            return e2.getChance() - e1.getChance();
        });
        
        for (SpellEffect se : effects) {
            if(se.getChance() == 0)
                selected.add(se);
            else
                conditionals.add(se);
        }
        
        if(conditionals.isEmpty())
            return selected;
        
        int jet = Util.randInt(100);
        Peak.worldLog.addToLog(Logger.Level.DEBUG, "Condition effect : jet = %d", jet);
        
        SpellEffect se = null;
        int i = 0;
        
        while((se = conditionals.poll()) != null){
            if(jet < se.getChance())
                break;
            
            jet -= se.getChance();
            ++i;
        }
        
        if(se != null){
            Peak.worldLog.addToLog(Logger.Level.DEBUG, "Found effect : %s, chance = %d", se.getEffect(), se.getChance());
            selected.add(se);
        }else{
            Peak.worldLog.addToLog(Logger.Level.DEBUG, "No effect found after %d effects", i);
        }
        
        return selected;
    }
}
