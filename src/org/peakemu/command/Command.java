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
public interface Command {
    public String name();
    public String shortDescription();
    public String help();
    public int minLevel();
    public void perform(CommandPerformer performer, String[] args);
}
