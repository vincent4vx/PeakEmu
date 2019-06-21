/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world;

import org.peakemu.objects.player.Player;
import org.peakemu.world.action.Action;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class LiveAction {
    final private int id;
    final private Player target;
    final private Action action;

    public LiveAction(int id, Player target, Action action) {
        this.id = id;
        this.target = target;
        this.action = action;
    }

    public int getId() {
        return id;
    }

    public Player getTarget() {
        return target;
    }

    public Action getAction() {
        return action;
    }
}
