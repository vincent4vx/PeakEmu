/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command.level3;

import org.peakemu.command.Command;
import org.peakemu.command.CommandPerformer;
import org.peakemu.common.SocketManager;
import org.peakemu.game.out.game.AlterSprite;
import org.peakemu.objects.player.Player;
import org.peakemu.world.handler.SessionHandler;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class Demorph implements Command {

    private final SessionHandler session;

    public Demorph(SessionHandler session) {
        this.session = session;
    }

    @Override
    public String name() {
        return "DEMORPH";
    }

    @Override
    public String shortDescription() {
        return "Demorphe un joueur";
    }

    @Override
    public String help() {
        return "DEMORPH [qui]";
    }

    @Override
    public int minLevel() {
        return 3;
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        Player target = performer.getPlayer();
        if (args.length > 1)//Si un nom de perso est spÃ©cifiÃ©
        {
            target = session.searchPlayer(args[0]);
            if (target == null) {
                performer.displayError("Le personnage n'a pas ete trouve");
                return;
            }
        }
        int morphID = target.getRace().ordinal() * 10 + target.getGender();
        target.setGfxID(morphID);
        target.getMap().sendToMap(new AlterSprite(target));
        performer.displayMessage("Le joueur a ete transforme");
    }

}
