/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command.level3;

import org.peakemu.command.Command;
import org.peakemu.command.CommandPerformer;
import org.peakemu.game.out.game.FightStartPlaces;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class ShowFightPos implements Command {

    @Override
    public String name() {
        return "SHOWFIGHTPOS";
    }

    @Override
    public String shortDescription() {
        return "Affiche les cellules de combat de chaque team";
    }

    @Override
    public String help() {
        return "SHOWFIGHTPOS\nVoir aussi HIDEFIGHTPOS";
    }

    @Override
    public int minLevel() {
        return 3;
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        if(performer.getPlayer().getFight() != null){
            performer.displayError("Commande invalide en combat");
            return;
        }
        
        String places = performer.getPlayer().getMap().get_placesStr();
        if (places.indexOf('|') == -1 || places.length() < 2) {
            performer.displayError("Les places n'ont pas ete definies");
            return;
        }
        
        performer.getClient().send(new FightStartPlaces(places, 0));
    }

}
