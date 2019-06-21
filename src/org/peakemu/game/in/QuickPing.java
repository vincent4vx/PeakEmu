/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in;

import org.peakemu.game.GameClient;
import org.peakemu.game.out.QuickPong;
import org.peakemu.network.InputPacket;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class QuickPing implements InputPacket<GameClient>{

    @Override
    public void parse(String args, GameClient client) {
        client.send(new QuickPong());
    }

    @Override
    public String header() {
        return "qping";
    }
    
}
