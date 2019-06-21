/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.game;

import org.peakemu.world.Sprite;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class AlterSprite {
    private Sprite spritable;

    public AlterSprite(Sprite spritable) {
        this.spritable = spritable;
    }

    public Sprite getSpritable() {
        return spritable;
    }

    public void setSpritable(Sprite spritable) {
        this.spritable = spritable;
    }

    @Override
    public String toString() {
        return "GM|~" + spritable.getSpritePacket();
    }
    
    
}
