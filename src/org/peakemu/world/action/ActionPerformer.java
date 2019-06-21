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
 * @author Ludo
 */
public interface ActionPerformer {
    int actionId();
    boolean apply(Action action, Player caster, Player target, MapCell cell, Item item);
    
}
