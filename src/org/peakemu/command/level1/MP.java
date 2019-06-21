/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command.level1;

import java.util.Arrays;
import org.peakemu.Ancestra;
import org.peakemu.command.Command;
import org.peakemu.command.CommandPerformer;
import org.peakemu.common.SocketManager;
import org.peakemu.common.util.StringUtil;
import org.peakemu.game.out.basic.ServerMessage;
import org.peakemu.objects.player.Player;
import org.peakemu.world.handler.SessionHandler;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class MP implements Command {
    
    final private SessionHandler sessionHandler;

    public MP(SessionHandler sessionHandler) {
        this.sessionHandler = sessionHandler;
    }

    @Override
    public String name() {
        return "MP";
    }

    @Override
    public String shortDescription() {
        return "Envoie un MP à un joueur façon modérateur";
    }

    @Override
    public String help() {
        return "MP [qui] [quoi]";
    }

    @Override
    public int minLevel() {
        return 1;
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        if(args.length < 2){
            performer.displayError("Commande invalide");
            return;
        }
        Player target = null; //cible
        String name = args[0];
        
        //args = msg.split(" ", 3);
        /*if (this._compte.get_gmLvl() < 1) {
            SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this._out, "Erreur : Commande non reconue (spe)");
            return;
        }*/
        //Player P = World.getPersoByName(args[1]);
        
        target = sessionHandler.searchPlayer(name);
        
        if ((target == null) || (!target.isOnline()) || (target.getName() == null ? performer.getPlayer().getName() == null : target.getName().equals(performer.getPlayer().getName()))) {
            String str = "Erreur : Impossible de parler a ce personnage";
            performer.displayError(str);
            //SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this._out, str);
            return;
        }
//        if (infos.length > 3) {
//            String str = "Erreur : La commande est incomplete.";
//            SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this._out, str);
//            return;
//        }

        String message = StringUtil.join(Arrays.copyOfRange(args, 1, args.length), " ");
        String prefix1 = "<i>de</i> [<b><a href='asfunction:onHref,ShowPlayerPopupMenu," + performer.getPlayer().getName() + "'>" + performer.getPlayer().getName() + "</a></b>] : ";
        String prefix2 = "Message à \"" + target.getName() + "\" ";
        
        SocketManager.GAME_SEND_MESSAGE(target, prefix1 + message, Ancestra.CONFIG_MOTD_COLOR);
        target.send(ServerMessage.unsafeColoredMessage(ServerMessage.MEETIC_CHAT_COLOR, message));
        performer.displayMessage(prefix2 + message);
    }

}
