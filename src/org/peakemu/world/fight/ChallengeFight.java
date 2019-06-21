/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight;

import org.peakemu.world.fight.fighter.Fighter;
import org.peakemu.world.fight.team.ChallengeTeam;
import org.peakemu.world.fight.team.FightTeam;
import java.util.Collection;
import org.peakemu.common.SocketManager;
import org.peakemu.objects.player.Player;
import org.peakemu.world.GameMap;
import org.peakemu.world.enums.FightType;
import org.peakemu.world.handler.fight.FightHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class ChallengeFight extends DropFight{
    public ChallengeFight(FightHandler fightHandler, int id, GameMap map, Player init1, Player init2) {
        super(fightHandler, id, FightType.CHALLENGE, map);
        
        init1.stopAllRegen();
        init2.stopAllRegen();
        //on desactive le timer de regen coté client
        SocketManager.GAME_SEND_ILF_PACKET(init1, 0);
        SocketManager.GAME_SEND_ILF_PACKET(init2, 0);
        
        FightTeam team0 = new ChallengeTeam(init1, parsePlaces(0), 0);
        FightTeam team1 = new ChallengeTeam(init2, parsePlaces(1), 1);
        
        addTeam(team0);
        addTeam(team1);
        
        init1.getMap().removePlayer(init1);
        init2.getMap().removePlayer(init2);
        
        initFight();
    }

    @Override
    protected FightDrops getDrops(Collection<Fighter> looseFighters, Collection<Fighter> winFighters) {
        return FightDrops.EMPTY_DROPS; //no drops
    }

    @Override
    public boolean canCancel() {
        return true;
    }

    @Override
    public int getPlacementTime() {
        return 0;
    }

    @Override
    protected void applyDefaultEndFightActionsToLoosers(Collection<Fighter> loosers) {}

    @Override
    protected void applyDefaultEndFightActionsToWinners(Collection<Fighter> winners) {}

    @Override
    protected void applyDefaultEndFightActionsToAll(Collection<Fighter> fighters) {}

    
}
