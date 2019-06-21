/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command.level3;

import org.peakemu.command.Command;
import org.peakemu.command.CommandPerformer;
import org.peakemu.database.Database;
import org.peakemu.database.dao.NpcDAO;
import org.peakemu.objects.NPC;
import org.peakemu.world.NPCTemplate;
import org.peakemu.objects.player.Player;
import org.peakemu.world.Sprite;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class DeleteNPC implements Command {
    final private NpcDAO npcDAO;

    public DeleteNPC(NpcDAO npcDAO) {
        this.npcDAO = npcDAO;
    }

    @Override
    public String name() {
        return "DELNPC";
    }

    @Override
    public String shortDescription() {
        return "Supprime un PNJ";
    }

    @Override
    public String help() {
        return "DELNPC [id]";
    }

    @Override
    public int minLevel() {
        return 3;
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        int id;
        try {
            id = Integer.parseInt(args[0]);
        } catch (Exception e) {
            performer.displayError("Commande invalide");
            return;
        }
        
        Sprite sprite = performer.getPlayer().getMap().getSprite(id);
        
        if(sprite == null || !(sprite instanceof NPC)){
            performer.displayError("PnJ inexistant");
            return;
        }
            
        performer.getPlayer().getMap().removeSprite(sprite);
        
        NPC npc = (NPC)sprite;
        if(!npcDAO.delete(npc)){
            performer.displayError("Suppression en base de données impossible");
        }
    }

}
