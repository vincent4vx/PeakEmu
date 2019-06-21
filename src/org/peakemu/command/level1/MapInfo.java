/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command.level1;

import org.peakemu.command.Command;
import org.peakemu.command.CommandPerformer;
import org.peakemu.world.GameMap;
import org.peakemu.objects.MonsterGroup;
import org.peakemu.objects.NPC;
import org.peakemu.world.Sprite;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class MapInfo implements Command {

    @Override
    public String name() {
        return "MAPINFO";
    }

    @Override
    public String shortDescription() {
        return "Récupère les informations sur la map";
    }

    @Override
    public String help() {
        return "";
    }

    @Override
    public int minLevel() {
        return 1;
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        GameMap map = performer.getPlayer().getMap();
        
        performer.displayMessage("==========");
        performer.displayMessage("MapID : " + map.getId());
        performer.displayMessage("SubArea : " + map.getSubArea().getId() + " (" + map.getSubArea().getId() + ")");
        performer.displayMessage("Area : " + map.getSubArea().getArea().getId() + " (" + map.getSubArea().getArea().getName() + ")");
        performer.displayMessage("SuperArea : " + map.getSubArea().getArea().getSuperArea().get_id());
        
        String mess = "==========\n"
            + "Liste des Npcs de la carte:";
        
        performer.displayMessage(mess);
        for (Sprite sprite : map.getNonPlayerSprites()) {
            if(!(sprite instanceof NPC))
                continue;
            
            NPC npc = (NPC)sprite;
            mess = npc.getSpriteId() + " " + npc.getTemplate().get_id() + " " + npc.getCell().getID();
            performer.displayMessage(mess);
        }
        
        mess = "Liste des groupes de monstres:";
        performer.displayMessage(mess);
        
        for (MonsterGroup group : map.getMonsterGroups()) {
            mess = group.getSpriteId() + " " + group.getCell().getID() + " " + group.getAlignement();
            performer.displayMessage(mess);
        }
        
        performer.displayMessage("Autres sprites :");
        
        for(Sprite sprite : map.getNonPlayerSprites()){
            if((sprite instanceof MonsterGroup) || (sprite instanceof NPC))
                continue;
            
            performer.displayMessage(sprite.getSpriteId() + " " + sprite.getCell().getID() + " (" + sprite.getClass().getSimpleName() + ")");
        }
        
        mess = "==========";
        performer.displayMessage(mess);
    }

}
