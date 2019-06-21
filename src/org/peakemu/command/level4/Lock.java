/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command.level4;

import org.peakemu.command.Command;
import org.peakemu.command.CommandPerformer;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class Lock implements Command {

    @Override
    public String name() {
        return "LOCK"; //a return type of a created method
    }

    @Override
    public String shortDescription() {
        return "Bloque le serveur"; //a return type of a created method
    }

    @Override
    public String help() {
        return "LOCK [id]"; //a return type of a created method
    }

    @Override
    public int minLevel() {
        return 4; //a return type of a created method
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        byte lockValue = 1;//Accessible
        try {
            lockValue = Byte.parseByte(args[0]);
        } catch (Exception e) {
            performer.displayError("Commande invalide");
            return;
        }

        if (lockValue > 2) {
            lockValue = 2;
        }
        if (lockValue < 0) {
            lockValue = 0;
        }
        //World.set_state((short)LockValue);
        switch(lockValue){
            case 0:
                performer.displayMessage("Serveur inaccessible");
                break;
            case 1:
                performer.displayMessage("Serveur accessible");
                break;
            case 2:
                performer.displayMessage("Serveur en sauvegarde");
                break;
        }
    }

}
