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
public class WeddingRequest implements ActionPerformer{

    @Override
    public int actionId() {
        return 102;
    }

    @Override
    public boolean apply(Action action, Player caster, Player target, MapCell cell, Item item) {
//        World.PriestRequest(caster, caster.getMap(), caster.get_isTalkingWith());
        return false;
    }

}
