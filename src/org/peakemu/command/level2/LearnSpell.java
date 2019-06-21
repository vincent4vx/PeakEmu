/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command.level2;

import org.peakemu.command.Command;
import org.peakemu.command.CommandPerformer;
import org.peakemu.game.out.InfoMessage;
import org.peakemu.objects.player.Player;
import org.peakemu.world.Spell;
import org.peakemu.world.handler.SessionHandler;
import org.peakemu.world.handler.SpellHandler;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class LearnSpell implements Command {

    final private SessionHandler sessionHandler;
    final private SpellHandler spellHandler;

    public LearnSpell(SessionHandler sessionHandler, SpellHandler spellHandler) {
        this.sessionHandler = sessionHandler;
        this.spellHandler = spellHandler;
    }

    @Override
    public String name() {
        return "LEARNSPELL";
    }

    @Override
    public String shortDescription() {
        return "Apprendre un nouveau sort";
    }

    @Override
    public String help() {
        return "LEARNSPELL [idSort] {qui}";
    }

    @Override
    public int minLevel() {
        return 2;
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        Spell spell;
        try {
            spell = spellHandler.getSpellById(Integer.parseInt(args[0]));
        } catch (Exception e) {
            performer.displayError("Sort invalide");
            return;
        }
        
        Player target = performer.getPlayer();
        
        if (args.length > 1)//Si un nom de perso est spécifié
        {
            target = sessionHandler.searchPlayer(args[1]);
            if (target == null) {
                String str = "Le personnage n'a pas ete trouve";
                performer.displayError(str);
                return;
            }
        }

        if(target.learnSpell(spell)){
            target.send(new InfoMessage(InfoMessage.Type.INFO).addMessage(3, spell.getSpellID()));
            performer.displayMessage("Le sort a ete appris");
        }else{
            performer.displayError("Le personnage ne peut pas apprendre ce sort");
        }
    }

}
