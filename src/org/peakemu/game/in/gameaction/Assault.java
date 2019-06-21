/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.gameaction;

import org.peakemu.Peak;
import org.peakemu.common.Constants;
import org.peakemu.common.SocketManager;
import org.peakemu.objects.player.Player;
import org.peakemu.world.handler.fight.FightHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class Assault implements GameAction{
    final static public int ACTION_ID = 906;
    
    final private FightHandler fightHandler;

    public Assault(FightHandler fightHandler) {
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
            int id = Integer.parseInt(arg.getArg());
            Player target = player.getMap().getPlayer(id);
            if (target == null || !target.isOnline() || target.getFight() != null
                    || target.getMap().getId() != player.getMap().getId()
                    || target.getAlignement() == player.getAlignement()
                    || player.getMap().get_placesStr().equalsIgnoreCase("|")
                    || !target.canAggro()) {
                return;
            }

            if (target.getAlignement() == 0) {
                SocketManager.GAME_SEND_Im_PACKET(player, "084;1");
            }
            
            if (player.get_lastPersoAgro() == target.getSpriteId()) {
                SocketManager.send(player, "cC+i");
                SocketManager.GAME_SEND_MESSAGE(player, "<b>Erreur action impossible:</b> vous avez déjà agressé cette personne! C'est à elle de vous agresser maintenant.", "000000");
                return;
            }
            
            player.set_lastPersoAgro(target.getSpriteId());
            player.toggleWings('+');
            SocketManager.GAME_SEND_GA_PACKET_TO_MAP(player.getMap(), "", 906, player.getSpriteId() + "", id + "");
            fightHandler.startAgressionFight(player.getMap(), player, target);
        } catch (Exception e) {
            Peak.errorLog.addToLog(e);
        };
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
