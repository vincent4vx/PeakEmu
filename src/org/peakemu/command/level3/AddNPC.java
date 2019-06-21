/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command.level3;

import org.peakemu.command.Command;
import org.peakemu.command.CommandPerformer;
import org.peakemu.common.SocketManager;
import org.peakemu.database.Database;
import org.peakemu.database.dao.NPCTemplateDAO;
import org.peakemu.database.dao.NpcDAO;
import org.peakemu.objects.NPC;
import org.peakemu.world.NPCTemplate;
import org.peakemu.objects.player.Player;
import org.peakemu.world.Sprite;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class AddNPC implements Command {
    final private NPCTemplateDAO npcTemplateDAO;
    final private NpcDAO npcDAO;

    public AddNPC(NPCTemplateDAO npcTemplateDAO, NpcDAO npcDAO) {
        this.npcTemplateDAO = npcTemplateDAO;
        this.npcDAO = npcDAO;
    }

    @Override
    public String name() {
        return "ADDNPC";
    }

    @Override
    public String shortDescription() {
        return "Ajoute un pnj sur votre position";
    }

    @Override
    public String help() {
        return "ADDNPC [id]";
    }

    @Override
    public int minLevel() {
        return 3;
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        Player target = performer.getPlayer();
        int id = 0;
        try {
            id = Integer.parseInt(args[0]);
        } catch (Exception e) {
            performer.displayError("Commande invalide");
            return;
        }
        
        NPCTemplate template = npcTemplateDAO.getTemplateById(id);
        
        if (template == null) {
            String str = "NpcID invalide";
            performer.displayError(str);
            return;
        }
        
        for(Sprite sprite : performer.getPlayer().getMap().getNonPlayerSprites()){
            if(sprite instanceof NPC && sprite.getCell().equals(performer.getPlayer().getCell())){
                performer.displayError("PnJ déjà présent sur cette case");
                return;
            }
        }
        
        NPC npc = new NPC(template, performer.getPlayer().getCell(), (byte) target.getOrientation());
        int spriteId = performer.getPlayer().getMap().getNextSpriteId();
        npc.setSpriteId(spriteId);
        performer.getPlayer().getMap().addSprite(npc);
        
        String str = "Le PNJ a ete ajoute";
        if (target.getOrientation() == 0
                || target.getOrientation() == 2
                || target.getOrientation() == 4
                || target.getOrientation() == 6) {
            str += " mais est invisible (orientation diagonale invalide).";
        }

        performer.displayMessage(str);
        
        if(!npcDAO.insert(npc))
            performer.displayError("Une erreur est survenue lors de l'enregistrement en base de données");
    }

}
