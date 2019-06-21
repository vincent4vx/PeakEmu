/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class Alias implements Command{
    final private String name;
    final private Command command;

    public Alias(String name, Command command) {
        this.name = name;
        this.command = command;
    }

    @Override
    public String help() {
        return command.help();
    }

    @Override
    public int minLevel() {
        return command.minLevel();
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        command.perform(performer, args);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String shortDescription() {
        return "Alias de " + command.name();
    }
    
    
}
