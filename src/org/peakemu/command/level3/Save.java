/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command.level3;

import org.peakemu.Ancestra;
import org.peakemu.command.Command;
import org.peakemu.command.CommandPerformer;
import org.peakemu.world.World;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class Save implements Command {
    final private World world;

    public Save(World world) {
        this.world = world;
    }

    @Override
    public String name() {
        return "SAVE";
    }

    @Override
    public String shortDescription() {
        return "Sauvegarde le serveur";
    }

    @Override
    public String help() {
        return "";
    }

    @Override
    public int minLevel() {
        return 3;
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        if (!Ancestra.isSaving) {
            String mess = "Sauvegarde lancee!";
            performer.displayMessage(mess);
            
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    world.saveAll();
                }
            });
            
            thread.start();
        }

    }

}
