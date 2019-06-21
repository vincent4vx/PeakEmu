/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight;

import org.peakemu.world.fight.fighter.Fighter;
import org.peakemu.world.fight.team.FightTeam;
import org.peakemu.world.fight.team.AgressionTeam;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.peakemu.common.Constants;
import org.peakemu.common.Formulas;
import org.peakemu.common.SocketManager;
import org.peakemu.objects.player.Player;
import org.peakemu.world.GameMap;
import org.peakemu.world.enums.EndFightTeam;
import org.peakemu.world.enums.FightType;
import org.peakemu.world.handler.ExpHandler;
import org.peakemu.world.handler.fight.FightHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class AgressionFight extends HonourFight{
    final private boolean disgraceFight;
    
    public AgressionFight(ExpHandler expHandler, FightHandler fightHandler, int id, GameMap map, Player init1, Player init2){
        super(expHandler, fightHandler, id, FightType.AGRESSION, map);
        
        disgraceFight = init1.getAlignement() == Constants.ALIGNEMENT_NEUTRE || init2.getAlignement() == Constants.ALIGNEMENT_NEUTRE;
        
        init1.stopAllRegen();
        init2.stopAllRegen();
        //on desactive le timer de regen coté client
        SocketManager.GAME_SEND_ILF_PACKET(init1, 0);
        SocketManager.GAME_SEND_ILF_PACKET(init2, 0);
        
        FightTeam team0 = new AgressionTeam(init1, parsePlaces(0), 0);
        FightTeam team1 = new AgressionTeam(init2, parsePlaces(1), 1);
        
        addTeam(team0);
        addTeam(team1);
        
        init1.getMap().removePlayer(init1);
        init2.getMap().removePlayer(init2);
        
        initFight();
    }

    @Override
    protected Collection<FightRewards> getAllRewards(Collection<Fighter> winners, Collection<Fighter> loosers) {
        Collection<FightRewards> rewards = new ArrayList<>();
        
        for(Fighter fighter : winners){
            if(fighter.getPlayer() == null) //only players
                continue;
            
            Player player = fighter.getPlayer();
            
            //no rewards for neutral
            if(player.getAlignement() == Constants.ALIGNEMENT_NEUTRE){
                rewards.add(FightRewards.emptyRewards(EndFightTeam.WINNER, fighter));
                continue;
            }
            
            //for disgrace fight, add disgrace
            if(disgraceFight){
                player.setDisgrace(player.getDeshonor() + 1);
                rewards.add(new FightRewards(EndFightTeam.WINNER, fighter, 0, 0, 0, 0, 0, 1, Collections.EMPTY_LIST));
                continue;
            }
            
            //for loyal fight
            if(player.getDeshonor() > 0){
                player.setDisgrace(player.getDeshonor() - 1); //remove disgrace
                rewards.add(new FightRewards(EndFightTeam.WINNER, fighter, 0, 0, 0, 0, 0, -1, Collections.EMPTY_LIST));
            }else{ //or add honour
                int honour = Formulas.calculHonorWin(winners, loosers, fighter);
                player.setHonor(player.getHonor() + honour);
                SocketManager.GAME_SEND_Im_PACKET(player, "074;" + honour);
                rewards.add(new FightRewards(EndFightTeam.WINNER, fighter, 0, 0, 0, 0, honour, 0, Collections.EMPTY_LIST));
            }
        }
        
        for(Fighter fighter : loosers){
            if(fighter.getPlayer() == null) //only players
                continue;
            
            Player player = fighter.getPlayer();
            
            //no rewards for neutral
            if(player.getAlignement() == Constants.ALIGNEMENT_NEUTRE){
                rewards.add(FightRewards.emptyRewards(EndFightTeam.LOOSER, fighter));
                continue;
            }
            
            //for disgrace fight, add disgrace
            if(disgraceFight){
                player.setDisgrace(player.getDeshonor() + 1);
                rewards.add(new FightRewards(EndFightTeam.LOOSER, fighter, 0, 0, 0, 0, 0, 1, Collections.EMPTY_LIST));
                continue;
            }
            
            int honour = Formulas.calculHonorWin(winners, loosers, fighter);
            
            if(player.getHonor() + honour < 0)
                honour = -player.getHonor();
            
            player.setHonor(player.getHonor() + honour);
            rewards.add(new FightRewards(EndFightTeam.LOOSER, fighter, 0, 0, 0, 0, honour, 0, Collections.EMPTY_LIST));
        }
        
        return rewards;
    }

    @Override
    public int getPlacementTime() {
        return fightHandler.getConfig().getDefaultPlacementTime();
    }
}
