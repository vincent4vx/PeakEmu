/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.action;

import org.peakemu.game.out.dialog.DialogLeaved;
import org.peakemu.objects.item.Item;
import org.peakemu.objects.player.Player;
import org.peakemu.world.MapCell;
import org.peakemu.world.NPCQuestion;
import org.peakemu.world.handler.NPCHandler;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class SpeechNPC implements ActionPerformer {
    final private NPCHandler npcHandler;

    public SpeechNPC(NPCHandler npcHandler) {
        this.npcHandler = npcHandler;
    }

    @Override
    public int actionId() {
        return 1;
    }

    @Override
    public boolean apply(Action action, Player caster, Player target, MapCell cell, Item item) {
        if (action.getArgs().equalsIgnoreCase("DV")) {
            caster.setCurQuestion(null);
            caster.send(new DialogLeaved());
        } else {
            int qID = -1;
            try {
                qID = Integer.parseInt(action.getArgs());
            } catch (NumberFormatException e) {}

            NPCQuestion question = npcHandler.getRealQuestion(qID, caster);
            npcHandler.newQuestion(question, caster);
        }
        
        return true;
    }

}
