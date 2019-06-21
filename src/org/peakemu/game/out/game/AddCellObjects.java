/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import org.peakemu.common.util.Pair;
import org.peakemu.objects.item.Item;
import org.peakemu.world.GameMap;
import org.peakemu.world.MapCell;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class AddCellObjects {
    final private Collection<Pair<MapCell, Item>> objects = new ArrayList<>();
    
    public AddCellObjects addObject(MapCell cell, Item item){
        objects.add(new Pair<>(cell, item));
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(16 * objects.size());
        
        sb.append("GDO+");
        
        boolean b = false;
        
        for(Pair<MapCell, Item> pair : objects){
            if(b)
                sb.append('|');
            else 
                b = true;
            
            sb.append(pair.getFirst().getID()).append(';')
              .append(pair.getSecond().getTemplate().getID()).append(';')
              .append("0"); //TODO: handle this value (Game.as L1020)
        }
        
        return sb.toString();
    }
    
    static public AddCellObjects fromMap(GameMap map){
        AddCellObjects packet = new AddCellObjects();
        
        for(Map.Entry<MapCell, Item> entry : map.getDropItems().entrySet()){
            packet.addObject(entry.getKey(), entry.getValue());
        }
        
        return packet;
    }
}
