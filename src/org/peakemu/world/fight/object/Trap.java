/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight.object;

import java.util.ArrayList;
import java.util.Collection;
import org.peakemu.game.out.game.GameActionResponse;
import org.peakemu.world.MapCell;
import org.peakemu.world.SpellEffect;
import org.peakemu.world.SpellLevel;
import org.peakemu.world.fight.Fight;
import org.peakemu.world.fight.team.FightTeam;
import org.peakemu.world.fight.fighter.Fighter;
import org.peakemu.world.fight.OutputFilter;
import org.peakemu.world.handler.fight.FightHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class Trap extends FightObject{
    final private SpellLevel triggerSpell;
    final private Collection<FightTeam> visibility = new ArrayList<>();

    public Trap(SpellLevel triggerSpell, MapCell cell, int size, Fighter caster, int color, SpellLevel layingSpell) {
        super(cell, size, caster, color, layingSpell);
        this.triggerSpell = triggerSpell;
        visibility.add(caster.getTeam());
    }

    public SpellLevel getTriggerSpell() {
        return triggerSpell;
    }

    @Override
    public String getCellData(){
        return "Haaaaaaaaz3005";
    }
    
    public void trigger(Fighter fighter, Fight fight, FightHandler fightHandler){
        fight.sendToFight(GameActionResponse.triggerTrap(fighter, this));
        
        for(SpellEffect spellEffect : triggerSpell.getSpellEffects()){
            fightHandler.applyEffectToFight(fight, spellEffect, getCell(), getCaster());
        }
    }

    @Override
    public OutputFilter getOutputFilter() {
        return new OutputFilter() {
            @Override
            public boolean canSendToFighter(Fighter fighter) {
                return visibility.contains(fighter.getTeam());
            }

            @Override
            public boolean canSendToSpectator() {
                return false;
            }
        };
    }

    
}
