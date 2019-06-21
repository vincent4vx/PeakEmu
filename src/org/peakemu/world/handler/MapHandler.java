/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.handler;

import java.util.Collection;
import org.peakemu.database.dao.MapDAO;
import org.peakemu.database.dao.TriggerDAO;
import org.peakemu.world.GameMap;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class MapHandler {
    final private MapDAO mapDAO;
    final private TriggerDAO triggerDAO;

    public MapHandler(MapDAO mapDAO, TriggerDAO triggerDAO) {
        this.mapDAO = mapDAO;
        this.triggerDAO = triggerDAO;
    }
    
    public void load(){
        System.out.println("Chargement des triggers: ");
        System.out.println("triggers chargées pour " + triggerDAO.getAll().size() + " maps");
        
        System.out.print("Chargement des maps: ");
        System.out.println(mapDAO.getAll().size() + " maps ont ete chargées");
    }
    
    public Collection<GameMap> getMaps(){
        return mapDAO.getAll();
    }
    
    public GameMap getMap(short id){
        return mapDAO.getMapById(id);
    }
    
    public GameMap getMapByPos(int mapX, int mapY, int contID) {
        for (GameMap map : getMaps()) {
            if(map.getSubArea() == null)
                continue;
            
            if (map.getX() == mapX
                    && map.getY() == mapY
                    && map.getSubArea().getArea().getSuperArea().get_id() == contID) {
                return map;
            }
        }
        return null;
    }
}
