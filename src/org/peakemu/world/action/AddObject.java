/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.action;

import org.peakemu.Peak;
import org.peakemu.common.SocketManager;
import org.peakemu.common.util.StringUtil;
import org.peakemu.objects.item.Item;
import org.peakemu.objects.player.Player;
import org.peakemu.world.ItemTemplate;
import org.peakemu.world.MapCell;
import org.peakemu.world.handler.ItemHandler;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class AddObject implements ActionPerformer {
    final private ItemHandler itemHandler;

    public AddObject(ItemHandler itemHandler) {
        this.itemHandler = itemHandler;
    }

    @Override
    public int actionId() {
        return 5;
    }

    @Override
    public boolean apply(Action action, Player caster, Player target, MapCell cell, Item item) {
        try {
            String[] args = StringUtil.split(action.getArgs(), ",");
            int tID = Integer.parseInt(args[0]);
            int count = Integer.parseInt(args[1]);
            
            boolean send = true;
            if (args.length > 2) {
                send = action.getArgs().split(",")[2].equals("1");
            }
            
            boolean max = false;
            if(args.length > 3){
                max = args[2].equals("1");
            }

            //Si on ajoute
            if (count > 0) {
                ItemTemplate T = itemHandler.getTemplateById(tID);
                
                if (T == null) {
                    return false;
                }
                
                Item O = itemHandler.createNewItem(T, count, max);
                caster.getItems().addItem(O);
            } else {
                if(item == null)
                    caster.removeByTemplateID(tID, -count);
                else
                    caster.getItems().changeQuantity(item, item.getQuantity() + count);
            }
            //Si en ligne (normalement oui)
            if (caster.isOnline())//on envoie le packet qui indique l'ajout//retrait d'un item
            {
                SocketManager.GAME_SEND_Ow_PACKET(caster);
                if (send) {
                    if (count >= 0) {
                        SocketManager.GAME_SEND_Im_PACKET(caster, "021;" + count + "~" + tID);
                    } else if (count < 0) {
                        SocketManager.GAME_SEND_Im_PACKET(caster, "022;" + -count + "~" + tID);
                    }
                }
            }
            return true;
        } catch (Exception e) {
            Peak.errorLog.addToLog(e);
            return false;
        }
    }

}
