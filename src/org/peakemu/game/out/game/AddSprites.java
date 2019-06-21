/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.game;

import java.util.ArrayList;
import java.util.Collection;
import org.peakemu.world.Sprite;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class AddSprites {
    private Collection<Sprite> sprites = new ArrayList<>();

    public AddSprites(Collection<? extends Sprite> sprites) {
        this.sprites.addAll(sprites);
    }

    public AddSprites() {}
    
    public AddSprites(Sprite sprite){
        sprites = new ArrayList<>();
        sprites.add(sprite);
    }
    
    public void add(Sprite sprite){
        sprites.add(sprite);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(64 * sprites.size());
        
        sb.append("GM");
        
        for(Sprite sprite : sprites){
            sb.append("|+").append(sprite.getSpritePacket());
        }
        
        return sb.toString();
    }
}
