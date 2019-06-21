/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.conquest;

import org.peakemu.common.Constants;
import org.peakemu.game.GameClient;
import org.peakemu.game.in.game.InitialiseMap;
import org.peakemu.network.InputPacket;
import org.peakemu.objects.Prism;
import org.peakemu.world.fight.team.FightTeam;
import org.peakemu.world.handler.PlayerHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class ConquestJoinFight implements InputPacket<GameClient>{
    final private PlayerHandler playerHandler;

    public ConquestJoinFight(PlayerHandler playerHandler) {
        this.playerHandler = playerHandler;
    }
    

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null || client.getPlayer().getAlignement() == Constants.ALIGNEMENT_NEUTRE)
            return;
        
        
        final Prism prism = client.getPlayer().getMap().getSubArea().getPrism();
        
        if(prism == null || prism.getFight() == null || prism.getFight().getState() != Constants.FIGHT_STATE_PLACE)
            return;
        
        if(prism.getAlignement() != client.getPlayer().getAlignement())
            return;
        
        final FightTeam team = prism.getFight().getDefendersTeam();
        
        if(!team.canAddFighter(client.getPlayer()))
            return;
        
        //teleport
        if(!client.getPlayer().getMap().equals(prism.getMap())){
            client.attach( InitialiseMap.CUSTOM_ACTION_KEY, new InitialiseMap.CustomAction() {
                @Override
                public boolean doAction(GameClient client) {
                    return !prism.getFight().joinTeam(client.getPlayer(), team);
                }
            });
            
            playerHandler.teleport(client.getPlayer(), prism.getMap(), prism.getCell());
        }else{
            prism.getFight().joinTeam(client.getPlayer(), team);
        }
    }

    @Override
    public String header() {
        return "CFJ";
    }
    
}
