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
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class SetSeller implements Command{

    @Override
    public String name() {
        return "SETSELLER";
    }

    @Override
    public String shortDescription() {
        return "Transforme un joueur en marchand";
    }

    @Override
    public String help() {
        return "SETSELLER";
    }

    @Override
    public int minLevel() {
        return 4;
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        performer.getPlayer().setSeller();
    }
    
}
