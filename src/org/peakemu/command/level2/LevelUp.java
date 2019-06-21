/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command.level2;

import org.peakemu.command.Command;
import org.peakemu.command.CommandPerformer;
import org.peakemu.database.dao.PlayerDAO;
import org.peakemu.objects.player.Player;
import org.peakemu.world.handler.PlayerHandler;
import org.peakemu.world.handler.SessionHandler;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class LevelUp implements Command {

    private final SessionHandler session;
    final private PlayerDAO playerDAO;
    final private PlayerHandler playerHandler;

    public LevelUp(SessionHandler session, PlayerDAO playerDAO, PlayerHandler playerHandler) {
        this.session = session;
        this.playerDAO = playerDAO;
        this.playerHandler = playerHandler;
    }

    @Override
    public String name() {
        return "LEVEL"; //a return type of a created method
    }

    @Override
    public String shortDescription() {
        return "Augmenter le niveau d'un joueur"; //a return type of a created method
    }

    @Override
    public String help() {
        return "LEVEL [niveau] {qui}"; //a return type of a created method
    }

    @Override
    public int minLevel() {
        return 2; //a return type of a created method
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        int newLevel = 0;
        try {
            newLevel = Integer.parseInt(args[0]);
        } catch (Exception e) {
            performer.displayError("Commande invalide");
            return;
        }

        if (newLevel < 1) {
            newLevel = 1;
        }
        
        Player target = performer.getPlayer();

        if (args.length > 1)//Si le nom du perso est spÃ©cifiÃ©
        {
            target = session.searchPlayer(args[1]);
            if (target == null) {
                performer.displayError("Cible non trouvée");
                return;
            }
        }
        
        playerHandler.setLevel(target, newLevel);
        playerDAO.save(target);
        
        String mess = "Vous avez fixé le niveau de " + target.getName() + " a " + newLevel;
        performer.displayMessage(mess);

    }
}
