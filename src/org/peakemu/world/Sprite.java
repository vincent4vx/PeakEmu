/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world;

import org.peakemu.world.enums.SpriteTypeEnum;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public interface Sprite {
    public SpriteTypeEnum getSpriteType();
    public String getSpritePacket();
    public int getSpriteId();
    public MapCell getCell();
}
