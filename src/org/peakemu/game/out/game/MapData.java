/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.game;

import org.peakemu.world.GameMap;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class MapData {
    final private GameMap map;

    public MapData(GameMap map) {
        this.map = map;
    }

    @Override
    public String toString() {
        return "GDM|" + map.getId() + "|" + map.get_date() + "|" + map.get_key();
    }
    
    
}
