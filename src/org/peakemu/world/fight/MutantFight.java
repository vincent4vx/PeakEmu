/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.peakemu.common.Formulas;
import org.peakemu.common.SocketManager;
import org.peakemu.objects.Monster;
import org.peakemu.objects.player.Player;
import org.peakemu.world.GameMap;
import org.peakemu.world.World;
import org.peakemu.world.enums.FightType;
import org.peakemu.world.fight.fighter.Fighter;
import org.peakemu.world.fight.fighter.PlayerFighter;
import org.peakemu.world.fight.team.ChallengeTeam;
import org.peakemu.world.fight.team.FightTeam;
import org.peakemu.world.fight.team.MutantTeam;
import org.peakemu.world.handler.fight.FightHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class MutantFight extends DropFight{

    public MutantFight(FightHandler fightHandler, int id, GameMap map, Player mutant, Player other) {
        super(fightHandler, id, FightType.PVMU, map);
        
        mutant.stopAllRegen();
        SocketManager.GAME_SEND_ILF_PACKET(mutant, 0);
        map.removePlayer(mutant);
        
        other.stopAllRegen();
        SocketManager.GAME_SEND_ILF_PACKET(other, 0);
        map.removePlayer(other);
        
        FightTeam team0 = new MutantTeam(mutant, parsePlaces(0), 0);
        FightTeam team1 = new ChallengeTeam(other, parsePlaces(1), 1);
        
        addTeam(team0);
        addTeam(team1);
        
        initFight();
    }

    @Override
    protected FightDrops getDrops(Collection<Fighter> looseFighters, Collection<Fighter> winFighters) {
        int minKamas = 0;
        int maxKamas = 0;
        long xp = 0;
        int totalLevel = 0;
        Collection<World.Drop> drops = new ArrayList<>();
        
        boolean mutantLoose = false;
        
        for(Fighter fighter : looseFighters){
            if(!(fighter instanceof PlayerFighter))
                continue;
            
            totalLevel += fighter.getPlayer().getLevel();
            
            if(!fighter.getPlayer().isMutant())
                continue;
            
            Monster monster = fighter.getPlayer().getMutant().getGrade();
            
            minKamas += monster.getTemplate().getMinKamas();
            maxKamas += monster.getTemplate().getMaxKamas();
            xp += monster.getBaseXp();
            drops.addAll(monster.getDrops());
            mutantLoose = true;
        }
        
        if(!mutantLoose){
            return new FightDrops(0, 0, Formulas.getTraqueXP(totalLevel), Collections.EMPTY_LIST);
        }
        
        return new FightDrops(minKamas, maxKamas, xp, drops);
    }

    @Override
    protected void applyDefaultEndFightActionsToLoosers(Collection<Fighter> loosers) {
        fightHandler.teleportLoosers(loosers);
        fightHandler.removeEnergy(this, loosers);
    }

    @Override
    protected void applyDefaultEndFightActionsToWinners(Collection<Fighter> winners) {}

    @Override
    protected void applyDefaultEndFightActionsToAll(Collection<Fighter> fighters) {
        fightHandler.setLifePoints(fighters);
        fightHandler.inventoryDammages(fighters);
    }

    @Override
    public boolean canCancel() {
        return false;
    }

    @Override
    public int getPlacementTime() {
        return fightHandler.getConfig().getDefaultPlacementTime();
    }

}
