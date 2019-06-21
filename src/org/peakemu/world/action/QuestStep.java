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
public class QuestStep implements ActionPerformer {

    @Override
    public int actionId() {
        return 42;
    }

    @Override
    public boolean apply(Action action, Player caster, Player target, MapCell cell, Item item) {
        //try catch à mettre par la suite
        int quest = Integer.parseInt(action.getArgs());
//        caster.upgradeQuest(quest);
        return false;
    }

}
