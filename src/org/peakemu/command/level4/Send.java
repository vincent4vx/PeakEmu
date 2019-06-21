/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command.level4;

import org.peakemu.command.Command;
import org.peakemu.command.CommandPerformer;
import org.peakemu.common.util.StringUtil;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class Send implements Command{

    @Override
    public String name() {
        return "SEND";
    }

    @Override
    public String shortDescription() {
        return "Envoie un packet";
    }

    @Override
    public String help() {
        return "SEND [packet]";
    }

    @Override
    public int minLevel() {
        return 4;
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        performer.getClient().send(StringUtil.join(args, " "));
    }
    
}
