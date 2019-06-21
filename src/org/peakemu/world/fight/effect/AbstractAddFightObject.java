/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight.effect;

import org.peakemu.Peak;
import org.peakemu.common.CryptManager;
import org.peakemu.common.Logger;
import org.peakemu.world.MapCell;
import org.peakemu.world.Spell;
import org.peakemu.world.SpellEffect;
import org.peakemu.world.SpellLevel;
import org.peakemu.world.enums.EffectType;
import org.peakemu.world.fight.Fight;
import org.peakemu.world.fight.fighter.Fighter;
import org.peakemu.world.fight.object.FightObject;
import org.peakemu.world.handler.SpellHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
abstract public class AbstractAddFightObject implements FightEffect{
    final private SpellHandler spellHandler;

    public AbstractAddFightObject(SpellHandler spellHandler) {
        this.spellHandler = spellHandler;
    }

    @Override
    public EffectType getType() {
        return EffectType.TRAP;
    }

    @Override
    public void applyToFight(Fight fight, SpellEffect spellEffect, MapCell cell, Fighter caster) {
        if(fight.cellContainsObject(cell)){
            Peak.worldLog.addToLog(Logger.Level.DEBUG, "The cell %d already contains an object", cell.getID());
            return;
        }
        
        Spell spellTpl = spellHandler.getSpellById(spellEffect.getArg1());
        
        if(spellTpl == null){
            Peak.worldLog.addToLog(Logger.Level.ERROR, "Undefined trigger spell %d", spellEffect.getArg1());
            return;
        }
        
        SpellLevel spell = spellTpl.getLevel(spellEffect.getArg2());
        
        if(spell == null){
            Peak.worldLog.addToLog(Logger.Level.ERROR, "Undefined level %d on trigger spell %d", spellEffect.getArg2(), spellEffect.getArg1());
            return;
        }
        
        int size = CryptManager.getIntByHashedValue(spellEffect.getArea().charAt(1));
        
        FightObject fightObject = createObject(spellEffect, cell, caster, spell, size, spellEffect.getArg3(), spellEffect.getTurns());
        fight.addFightObject(fightObject);
    }
    
    abstract protected FightObject createObject(SpellEffect spellEffect, MapCell cell, Fighter caster, SpellLevel triggerSpell, int size, int color, int duration);

    @Override
    public void applyBuffOnStartTurn(Buff buff) {}

    @Override
    public void applyBuffOnEndTurn(Buff buff) {}

    @Override
    public void applyBuffOnDammages(Buff buff, EffectScope scope) {}

    @Override
    public void onBuffEnd(Buff buff) {}
    
}
