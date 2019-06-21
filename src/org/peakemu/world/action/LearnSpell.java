/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.action;

import org.peakemu.Peak;
import org.peakemu.game.GameServer;
import org.peakemu.game.out.InfoMessage;
import org.peakemu.objects.item.Item;
import org.peakemu.objects.player.Player;
import org.peakemu.world.MapCell;
import org.peakemu.world.Spell;
import org.peakemu.world.handler.SpellHandler;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class LearnSpell implements ActionPerformer {
    final private SpellHandler spellHandler;

    public LearnSpell(SpellHandler spellHandler) {
        this.spellHandler = spellHandler;
    }

    @Override
    public int actionId() {
        return 9;
    }

    @Override
    public boolean apply(Action action, Player caster, Player target, MapCell cell, Item item) {
        try {
            int sID = Integer.parseInt(action.getArgs());
            Spell spell = spellHandler.getSpellById(sID);
            
            if (spell == null) {
                return false;
            }
            
            caster.learnSpell(spell);
            caster.send(new InfoMessage(InfoMessage.Type.INFO).addMessage(3, sID));
            return true;
        } catch (Exception e) {
            Peak.errorLog.addToLog(e);
            return false;
        }
    }

}
