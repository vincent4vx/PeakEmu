/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight.effect;

import org.peakemu.Peak;
import org.peakemu.common.Logger;
import org.peakemu.common.SocketManager;
import org.peakemu.database.dao.MonsterDAO;
import org.peakemu.game.out.game.FightTurnList;
import org.peakemu.game.out.game.GameActionResponse;
import org.peakemu.objects.Monster;
import org.peakemu.world.MapCell;
import org.peakemu.world.MonsterTemplate;
import org.peakemu.world.SpellEffect;
import org.peakemu.world.enums.Effect;
import org.peakemu.world.enums.EffectType;
import org.peakemu.world.fight.Fight;
import org.peakemu.world.fight.fighter.Fighter;
import org.peakemu.world.fight.fighter.MonsterFighter;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class InvocEffect implements FightEffect{
    final private MonsterDAO monsterDAO;

    public InvocEffect(MonsterDAO monsterDAO) {
        this.monsterDAO = monsterDAO;
    }

    @Override
    public void applyToFight(Fight fight, SpellEffect spellEffect, MapCell cell, Fighter caster) {
        if(!fight.isFreeCell(cell))
            return;
        
        MonsterTemplate tpl = monsterDAO.getMonsterById(spellEffect.getMin());
        
        if(tpl == null){
            Peak.worldLog.addToLog(Logger.Level.ERROR, "Undefined invocation %d", spellEffect.getMin());
            return;
        }
        
        Monster monster = tpl.getGradeByLevel(spellEffect.getMax());
        
        if(monster == null){
            Peak.worldLog.addToLog(Logger.Level.ERROR, "Undefined invocation level %d for %d", spellEffect.getMax(), spellEffect.getMin());
            return;
        }
        
        int id = fight.getNextLowerFighterGuid() - caster._nbInvoc;
        monster = monster.modifStatByInvocator(caster);
        
        Fighter fighter = new MonsterFighter(id, monster);
        fighter.setInvocator(caster);
        
        fighter.setTeam(caster.getTeam());
        fight.addFighterInTeam(fighter, caster.getTeam());
        
        fighter.set_fightCell(cell);
        cell.addFighter(fighter);
        
        fight.get_ordreJeu().add((fight.get_ordreJeu().indexOf(caster) + 1), fighter);
        
        fight.sendToFight(GameActionResponse.invocation(getEffect(), caster, fighter));
        fight.sendToFight(GameActionResponse.fakePacket(caster, new FightTurnList(fight.get_ordreJeu())));
        
        caster._nbInvoc++;
    }

    @Override
    public void applyBuffOnStartTurn(Buff buff) {}

    @Override
    public void applyBuffOnEndTurn(Buff buff) {}

    @Override
    public void applyBuffOnDammages(Buff buff, EffectScope scope) {}

    @Override
    public void onBuffEnd(Buff buff) {}

    @Override
    public Effect getEffect() {
        return Effect.INVOC;
    }

    @Override
    public EffectType getType() {
        return EffectType.INVOC;
    }
    
}
