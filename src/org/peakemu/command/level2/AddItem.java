/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command.level2;

import org.peakemu.command.Command;
import org.peakemu.command.CommandPerformer;
import org.peakemu.common.SocketManager;
import org.peakemu.objects.item.Item;
import org.peakemu.world.ItemTemplate;
import org.peakemu.world.handler.ItemHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class AddItem implements Command {

    final private ItemHandler itemHandler;

    public AddItem(ItemHandler itemHandler) {
        this.itemHandler = itemHandler;
    }

    @Override
    public String name() {
        return "ADDITEM";
    }

    @Override
    public String shortDescription() {
        return "Crée et ajoute un item à l'inventaire";
    }

    @Override
    public String help() {
        return "ADDITEM [template] {qte} {max}";
    }

    @Override
    public int minLevel() {
        return 2;
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        int tID = 0;
        try {
            tID = Integer.parseInt(args[0]);
        } catch (Exception e) {
            performer.displayError("Commande invalide");
            return;
        }
        
        int qua = 1;
        if (args.length >= 2)//Si une quantitÃ© est spÃ©cifiÃ©e
        {
            try {
                qua = Integer.parseInt(args[1]);
            } catch (Exception e) {
            }
        }
        
        boolean useMax = false;
        if (args.length >= 3 && args[2].equalsIgnoreCase("MAX"))//Si un jet est spÃ©cifiÃ©
        {
            useMax = true;
        }
        
        ItemTemplate t = itemHandler.getTemplateById(tID);
        
        if (t == null) {
            String mess = "Le template " + tID + " n'existe pas ";
            performer.displayError(mess);
            return;
        }
        if (qua < 1) {
            qua = 1;
        }
        Item obj = itemHandler.createNewItem(t, qua, useMax);
        
        performer.getPlayer().getItems().addItem(obj);
        
        String str = "Creation de l'item " + tID + " reussie";
        if (useMax) {
            str += " avec des stats maximums";
        }
        performer.displayMessage(str);
        SocketManager.GAME_SEND_Ow_PACKET(performer.getPlayer());
    }

}
