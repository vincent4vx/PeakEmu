/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command.level3;

import java.util.Arrays;
import org.peakemu.command.Command;
import org.peakemu.command.CommandPerformer;
import org.peakemu.common.SocketManager;
import org.peakemu.common.util.StringUtil;
import org.peakemu.database.Database;
import org.peakemu.database.dao.AccountDAO;
import org.peakemu.objects.player.Player;
import org.peakemu.world.handler.SessionHandler;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class Ban implements Command {

    private final SessionHandler session;
    final private AccountDAO accountDAO;

    public Ban(SessionHandler session, AccountDAO accountDAO) {
        this.session = session;
        this.accountDAO = accountDAO;
    }

    @Override
    public String name() {
        return "BAN";
    }

    @Override
    public String shortDescription() {
        return "Banni un joueur";
    }

    @Override
    public String help() {
        return "BAN [qui] {raison}";
    }

    @Override
    public int minLevel() {
        return 3;
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        Player target = null;
        String reason = "";
        try {
            target = session.searchPlayer(args[0]);
            try {
                reason = "\n" + StringUtil.join(Arrays.copyOfRange(args, 1, args.length), " ");
            } catch (Exception e) {
                reason = "";
            }

        } catch (Exception e) {
        }

        if (target == null) {
            performer.displayError("Personnage non trouve");
            return;
        }
        if (target.getAccount()== null) {
            performer.displayError("Erreur, compte non trouvé.");
            return;
        }
        
        accountDAO.save(target.getAccount());
        
        if (target.getAccount().getGameThread() != null) {
            SocketManager.REALM_SEND_MESSAGE(target, 18, target.getName() + ";" + "Vous avez été banni pour la raison suivante :\n" + reason);
            target.getAccount().getGameThread().kick();
        }
        performer.displayMessage("Vous avez banni " + target.getName());
    }

}
