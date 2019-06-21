/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.action;

import org.peakemu.objects.item.Item;
import org.peakemu.objects.player.Player;
import org.peakemu.world.MapCell;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class SpawnMob implements ActionPerformer {

    @Override
    public int actionId() {
        return 12;
    }

    @Override
    public boolean apply(Action action, Player caster, Player target, MapCell cell, Item item) {
        return false;
//        try {
//            boolean delObj = action.getArgs().split(",")[0].equals("true");
//            boolean inArena = action.getArgs().split(",")[1].equals("true");
//
//            if (inArena && !World.isArenaMap(caster.getMap().getId())) {
//                return;	//Si la map du casternnage n'est pas class� comme �tant dans l'ar�ne
//            }
//            PierreAme pierrePleine = (PierreAme) caster.getItems().get(item.getGuid());
//
//            String groupData = pierrePleine.parseGroupData();
//            String condition = "MiS = " + caster.getSpriteId();	//Condition pour que le groupe ne soit lan�able que par le casternnage qui � utiliser l'objet
//            caster.getMap().spawnNewGroup(true, caster.getCell().getID(), groupData, condition);
//
//            if (delObj) {
//                caster.getItems().remove(item);
//                //caster.removeItem(itemID, 1, true, true);
//            }
//        } catch (Exception e) {
//            GameServer.addToLog(e.getMessage());
//        }
    }

}
