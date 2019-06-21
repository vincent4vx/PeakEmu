/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.game;

import java.util.HashMap;
import java.util.Map;
import org.peakemu.game.GameClient;
import org.peakemu.game.in.gameaction.GameAction;
import org.peakemu.game.in.gameaction.GameActionArg;
import org.peakemu.network.InputPacket;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class StartGameAction implements InputPacket<GameClient>{
    final private Map<Integer, GameAction> gameActions = new HashMap<>();
    
    private void registerGameAction(GameAction ga){
        gameActions.put(ga.actionId(), ga);
    }

    @Override
    public void parse(String args, GameClient client) {
        int actionId;
        String gaArg = args.substring(3);
        
        try{
            actionId = Integer.parseInt(args.substring(0, 3));
        }catch(NumberFormatException e){
            return;
        }
        
        GameActionArg gameActionArg = new GameActionArg(client, actionId, gaArg);
        client.getGameActionHandler().handleAction(gameActionArg);
    }

    @Override
    public String header() {
        return "GA";
    }
    
}
