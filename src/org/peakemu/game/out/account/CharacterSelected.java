/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.account;

import org.peakemu.objects.player.Player;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class CharacterSelected {
    final private Player player;

    public CharacterSelected(Player player) {
        this.player = player;
    }

    @Override
    public String toString() {
        StringBuilder packet = new StringBuilder();
        packet.append("ASK|").append(player.getSpriteId()).append("|").append(player.getName()).append("|");
        packet.append(player.getLevel()).append("|").append(player.getRace().ordinal()).append("|").append(player.getGender());
        packet.append("|").append(player.getGfxID()).append("|").append((player.getColor1() == -1 ? "-1" : Integer.toHexString(player.getColor1())));
        packet.append("|").append((player.getColor2() == -1 ? "-1" : Integer.toHexString(player.getColor2()))).append("|");
        packet.append((player.getColor3() == -1 ? "-1" : Integer.toHexString(player.getColor3()))).append("|");
        packet.append(player.parseItemToASK());
        return packet.toString();
    }
    
    
}
