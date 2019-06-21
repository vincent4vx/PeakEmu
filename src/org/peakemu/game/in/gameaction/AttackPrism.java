/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.gameaction;

import org.peakemu.Peak;
import org.peakemu.common.Constants;
import org.peakemu.common.SocketManager;
import org.peakemu.game.out.game.GameActionResponse;
import org.peakemu.objects.Prism;
import org.peakemu.objects.player.Player;
import org.peakemu.world.Sprite;
import org.peakemu.world.handler.fight.FightHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class AttackPrism implements GameAction{
    final static public int ACTION_ID = 912;
    
    final private FightHandler fightHandler;

    public AttackPrism(FightHandler fightHandler) {
        this.fightHandler = fightHandler;
    }

    @Override
    public void start(GameActionArg arg) {
        Player player = arg.getClient().getPlayer();
        
        try {
            if (player == null) {
                return;
            }
            
            if (player.isOnFight()) {
                return;
            }
            
            if(player.getAlignement() != Constants.ALIGNEMENT_BONTARIEN && player.getAlignement() != Constants.ALIGNEMENT_BRAKMARIEN)
                return;
            
            if (player.isAway()) {
                return;
            }
            
            int id;
            
            try{
                id = Integer.parseInt(arg.getArg());
            }catch(NumberFormatException e){
                return;
            }
            
            Sprite sprite = player.getMap().getSprite(id);
            
            if(sprite == null || !(sprite instanceof Prism)){
                return;
            }
            
            Prism prism = (Prism)sprite;
            
            if(prism.getAlignement() == player.getAlignement())
                return;
            
            player.getMap().sendToMap(new GameActionResponse(ACTION_ID, player.getSpriteId(), id));
            fightHandler.startConquestFight(player.getMap(), player, prism);
        } catch (Exception e) {
            Peak.errorLog.addToLog(e);
        }
    }

    @Override
    public void end(GameActionArg arg, boolean success, String args) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int actionId() {
        return ACTION_ID;
    }
    
    
}
