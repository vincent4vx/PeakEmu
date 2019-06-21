/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world;

import java.util.HashSet;
import java.util.Set;
import org.peakemu.objects.player.Player;
import org.peakemu.world.action.Action;
import org.peakemu.world.handler.ActionHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class NPCResponse {
    final private int id;
    final private Set<Action> actions = new HashSet<>();

    public NPCResponse(int id) {
        this.id = id;
    }

    public int get_id() {
        return id;
    }

    public void addAction(Action act) {
        actions.add(act);
    }

    public boolean applyActions(ActionHandler actionHandler, Player player){
        boolean quit = true;
        for(Action action : actions){
            if(action.getId() == 1) //do not leave dialog
                quit = false;
            
            actionHandler.apply(action, player, null, null, null);
        }
        return quit;
    }
    
    public boolean isAnotherDialog() {
        for (Action curAct : actions) {
            if (curAct.getId() == 1) //1 = Discours NPC
            {
                return true;
            }
        }

        return false;
    }
}
