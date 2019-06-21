/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.peakemu.command.level3;

import org.peakemu.command.Command;
import org.peakemu.command.CommandPerformer;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class Exit implements Command{

    @Override
    public String name() {
        return "EXIT";
    }

    @Override
    public String shortDescription() {
        return "Arrête le système";
    }

    @Override
    public String help() {
        return "EXIT";
    }

    @Override
    public int minLevel() {
        return 3;
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        System.exit(0);
    }

}
