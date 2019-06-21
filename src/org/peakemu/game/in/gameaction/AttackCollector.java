/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.gameaction;

import org.peakemu.Peak;
import org.peakemu.game.out.InfoMessage;
import org.peakemu.game.out.game.GameActionResponse;
import org.peakemu.objects.Collector;
import org.peakemu.objects.player.Player;
import org.peakemu.world.Sprite;
import org.peakemu.world.handler.fight.FightHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class AttackCollector implements GameAction{
    final static public int ACTION_ID = 909;
    
    final private FightHandler fightHandler;

    public AttackCollector(FightHandler fightHandler) {
        this.fightHandler = fightHandler;
    }

    @Override
    public void start(GameActionArg arg) {
        Player player = arg.getClient().getPlayer();
        
        try {
            if (player == null) {
                return;
            }
            if (player.getFight() != null) {
                return;
            }
            if (player.get_isTradingWith() != 0
                    || player.isDead() > 0
                    || player.getCurExchange() != null
                    || player.isAway()) {
                return;
            }
            int id = Integer.parseInt(arg.getArg());
            Sprite sprite = player.getMap().getSprite(id);
            
            if(sprite == null || !(sprite instanceof Collector))
                return;
            
            Collector collector = (Collector) sprite;
            if (collector.get_inFight() > 0) {
                return;
            }
            
            if (collector.isInExchange()) {
                player.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(180));
                return;
            }
            
            if(collector.getGuild() == player.getGuild())
                return;
            
            player.getMap().sendToMap(new GameActionResponse(ACTION_ID, player.getSpriteId(), id));
            fightHandler.startPvTFight(player.getMap(), player, collector);
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
