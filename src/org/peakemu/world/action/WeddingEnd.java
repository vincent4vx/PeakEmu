/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.action;

import org.peakemu.objects.item.Item;
import org.peakemu.objects.player.Player;
import org.peakemu.world.MapCell;
import org.peakemu.world.World;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class WeddingEnd implements ActionPerformer {

    @Override
    public int actionId() {
        return 103;
    }

    @Override
    public boolean apply(Action action, Player caster, Player target, MapCell cell, Item item) {
//        if (caster.getKamas() >= 50000) {
//            caster.setKamas(caster.getKamas() - 50000);
//            Player wife = World.getPersonnage(caster.getWife());
//            wife.Divorce();
//            caster.Divorce();
//        } /*else {
//           
//        }*/
        return false;
    }

}
