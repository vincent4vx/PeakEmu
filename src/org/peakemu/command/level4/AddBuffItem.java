/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command.level4;

import org.peakemu.command.Command;
import org.peakemu.command.CommandPerformer;
import org.peakemu.common.SocketManager;
import org.peakemu.objects.item.BuffItem;
import org.peakemu.world.ItemTemplate;
import org.peakemu.world.enums.ItemType;
import org.peakemu.world.handler.ItemHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class AddBuffItem implements Command{
    final private ItemHandler itemHandler;

    public AddBuffItem(ItemHandler itemHandler) {
        this.itemHandler = itemHandler;
    }

    @Override
    public String name() {
        return "ADDBUFFITEM";
    }

    @Override
    public String shortDescription() {
        return "Ajoute un item de buff (test)";
    }

    @Override
    public String help() {
        return "ADDBUFFITEM [itemid]";
    }

    @Override
    public int minLevel() {
        return 4;
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        if(args.length < 1){
            performer.displayError("Commande invalide");
            return;
        }
        
        ItemTemplate template;
        
        try{
            template = itemHandler.getTemplateById(Integer.parseInt(args[0]));
        }catch(NumberFormatException e){
            performer.displayError("Commande invalide");
            return;
        }
        
        if(template == null){
            performer.displayError("Item introuvable");
            return;
        }
        
        if(!ItemType.isBuff(template.getType())){
            performer.displayError("Cet objet n'est pas un objet de Buff");
            return;
        }
        
        BuffItem item = (BuffItem)itemHandler.createNewItem(template, 1, false);
        if(!performer.getPlayer().getItems().addBuffItem(item)){
            performer.displayError("Ajout du buff impossible");
        }else{
            performer.getPlayer().refreshStats();
            SocketManager.GAME_SEND_STATS_PACKET(performer.getPlayer());
            performer.displayMessage("Le buff " + item.getTemplate().getName() + " a était ajouté !");
        }
    }
    
}
