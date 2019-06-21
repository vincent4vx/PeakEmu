/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight.object;

import org.peakemu.Peak;
import org.peakemu.common.Logger;
import org.peakemu.game.out.game.GameActionResponse;
import org.peakemu.world.MapCell;
import org.peakemu.world.SpellEffect;
import org.peakemu.world.SpellLevel;
import org.peakemu.world.fight.Fight;
import org.peakemu.world.fight.fighter.Fighter;
import org.peakemu.world.fight.OutputFilter;
import org.peakemu.world.handler.fight.FightHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class Glyphe extends FightObject{
    final private SpellLevel triggerSpell;
    private int duration;

    public Glyphe(SpellLevel triggerSpell, int duration, MapCell cell, int size, Fighter caster, int color, SpellLevel layingSpell) {
        super(cell, size, caster, color, layingSpell);
        this.triggerSpell = triggerSpell;
        this.duration = duration;
    }

    @Override
    public String getCellData() {
        return "Haaaaaaaaa3005";
    }

    @Override
    public OutputFilter getOutputFilter() {
        return OutputFilter.ALLOW_ALL;
    }

    public SpellLevel getTriggerSpell() {
        return triggerSpell;
    }

    public int getDuration() {
        return duration;
    }
    
    public void decrementDuration(){
        --duration;
    }
    
    public void trigger(Fighter fighter, Fight fight, FightHandler fightHandler){
        Peak.worldLog.addToLog(Logger.Level.DEBUG, "Trigger glyphe on cell %s", getCell());
        fight.sendToFight(GameActionResponse.triggerGlyphe(fighter, this));
        
        for(SpellEffect spellEffect : triggerSpell.getSpellEffects()){
            fightHandler.applyEffectToFight(fight, spellEffect, fighter.getCell(), getCaster());
        }
    }
}
