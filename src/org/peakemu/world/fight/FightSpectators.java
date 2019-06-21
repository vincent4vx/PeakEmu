/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight;

import org.peakemu.world.fight.team.FightTeam;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.peakemu.common.Constants;
import org.peakemu.game.out.InfoMessage;
import org.peakemu.game.out.game.AddSprites;
import org.peakemu.game.out.game.FightLeaved;
import org.peakemu.game.out.game.FightOption;
import org.peakemu.game.out.game.FightStarted;
import org.peakemu.game.out.game.FightTurnList;
import org.peakemu.game.out.game.FightTurnStarted;
import org.peakemu.game.out.game.JoinFightOk;
import org.peakemu.objects.player.Player;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class FightSpectators {
    final private Fight fight;
    
    final private Set<Player> players = Collections.synchronizedSet(new HashSet<Player>());
    private boolean canJoin = true;

    public FightSpectators(Fight fight) {
        this.fight = fight;
    }
    
    public void sendToSpectators(Object packet){
        String sPacket = packet.toString();
        
        for (Player player : players) {
            player.send(sPacket);
        }
    }
    
    public void join(Player player){
        if(player.isAway() || player.isOnFight() || !canJoin || fight.getState() != Constants.FIGHT_STATE_ACTIVE){
            player.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(57));
            return;
        }
        
        player.getMap().removePlayer(player);
        player.setFight(fight);
        players.add(player);
        
        player.send(new JoinFightOk(fight, true));
        player.send(new AddSprites(fight.getAllFighters()));
        player.send(new FightStarted());
        player.send(new FightTurnList(fight.get_ordreJeu()));
        player.send(new FightTurnStarted(fight.getCurFighter(), Constants.TIME_BY_TURN)); //TODO: remaining turn time
        
        fight.sendToFight(new InfoMessage(InfoMessage.Type.INFO).addMessage(36, player.getName()));
    }

    public boolean canJoin() {
        return canJoin;
    }

    public void setCanJoin(boolean canJoin) {
        this.canJoin = canJoin;
    }
    
    public void clear(){
        players.clear();
    }
    
    public void unsetFight(){
        for (Player player : players) {
            player.setFight(null);
        }
    }
    
    public void leave(Player player){
        if(!players.remove(player))
            return;
        
        player.setFight(null);
        player.send(new FightLeaved());
    }    
    
    public void toggleCanJoin() {
        canJoin = !canJoin;

        for (FightTeam team : fight.getTeams()) {
            fight.getOldMap().sendToMap(new FightOption(team, canJoin, FightOption.SPECTATOR));
        }

        fight.sendToFight(new InfoMessage(InfoMessage.Type.INFO).addMessage(canJoin ? 39 : 40));
    }
}
