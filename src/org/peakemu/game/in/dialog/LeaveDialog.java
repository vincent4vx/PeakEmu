/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.dialog;

import org.peakemu.game.GameClient;
import org.peakemu.game.out.dialog.DialogLeaved;
import org.peakemu.network.InputPacket;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class LeaveDialog implements InputPacket<GameClient>{

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null)
            return;
        
        client.getPlayer().setCurQuestion(null);
        client.send(new DialogLeaved());
    }

    @Override
    public String header() {
        return "DV";
    }
    
}
