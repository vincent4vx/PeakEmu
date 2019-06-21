/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.dialog;

import org.peakemu.world.Sprite;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class DialogCreated {
    private Sprite sprite;

    public DialogCreated(Sprite sprite) {
        this.sprite = sprite;
    }

    @Override
    public String toString() {
        return "DCK" + sprite.getSpriteId();
    }
    
}
