/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.info;

import org.peakemu.objects.player.Player;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class HarvestQuantity {
    private Player player;
    private int qte;

    public HarvestQuantity(Player player, int qte) {
        this.player = player;
        this.qte = qte;
    }

    @Override
    public String toString() {
        return "IQ" + player.getId() + "|" + qte;
    }
    
    
}
