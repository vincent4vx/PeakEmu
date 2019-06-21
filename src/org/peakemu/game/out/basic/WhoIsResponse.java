/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.basic;

import org.peakemu.objects.player.Player;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class WhoIsResponse {
    final private Player player;

    public WhoIsResponse(Player player) {
        this.player = player;
    }

    @Override
    public String toString() {
        return "BWK" + player.getAccount().get_pseudo() + "|1|" + player.getName() + "|-1";
    }
    
    
}
