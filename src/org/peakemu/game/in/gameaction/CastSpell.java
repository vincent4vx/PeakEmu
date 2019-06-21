/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.gameaction;

import org.peakemu.common.SocketManager;
import org.peakemu.objects.player.Player;
import org.peakemu.world.SpellLevel;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class CastSpell implements GameAction{
    final public static int ACTION_ID = 300;

    @Override
    public void start(GameActionArg arg) {
        Player player = arg.getClient().getPlayer();
        
        try {
            String[] splt = arg.getArg().split(";");
            int spellID = Integer.parseInt(splt[0]);
            int caseID = Integer.parseInt(splt[1]);
            if (player.getFight() != null) {

                SpellLevel spell = player.getSpellById(spellID);
                if (spell == null || spell.getLevel() == 0) {
                    SocketManager.GAME_SEND_GA_CLEAR_PACKET_TO_FIGHT(player.getFight(), 7);
                    SocketManager.GAME_SEND_Im_PACKET(player, "1169");
                    return;
                }
                player.getFight().tryCastSpell(player.getFight().getFighterByPerso(player), spell, caseID);
            }
        } catch (NumberFormatException e) {
            return;
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
