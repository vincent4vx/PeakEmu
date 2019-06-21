/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.action;

import org.peakemu.objects.item.Item;
import org.peakemu.objects.player.Player;
import org.peakemu.world.MapCell;
import org.peakemu.world.handler.AlignementHandler;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class AddPrism implements ActionPerformer {
    final private AlignementHandler alignementHandler;

    public AddPrism(AlignementHandler alignementHandler) {
        this.alignementHandler = alignementHandler;
    }

    @Override
    public int actionId() {
        return 201;
    }

    @Override
    public boolean apply(Action action, Player caster, Player target, MapCell cell, Item item) {
        if(!alignementHandler.canAddPrism(caster, caster.getMap()))
            return false;
        
        alignementHandler.addPrism(caster.getAlignement(), caster.getMap(), caster.getCell());
        return true;
    }

}
