/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.basic;

import org.peakemu.world.Sprite;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class ChatSmileySent {
    final private Sprite sprite;
    final private int smiley;

    public ChatSmileySent(Sprite sprite, int smiley) {
        this.sprite = sprite;
        this.smiley = smiley;
    }


    @Override
    public String toString() {
        return "cS" + sprite.getSpriteId() + "|" + smiley;
    }
    
    
}
