/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.action;

import org.peakemu.Peak;
import org.peakemu.common.SocketManager;
import org.peakemu.game.GameServer;
import org.peakemu.objects.item.Item;
import org.peakemu.objects.player.Player;
import org.peakemu.world.MapCell;
import org.peakemu.world.handler.PlayerHandler;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class TeleportationDungeon implements ActionPerformer {
    final private PlayerHandler playerHandler;

    public TeleportationDungeon(PlayerHandler playerHandler) {
        this.playerHandler = playerHandler;
    }

    @Override
    public int actionId() {
        return 15;
    }

    @Override
    public boolean apply(Action action, Player caster, Player target, MapCell cell, Item item) {
        try {
            short newMapID = Short.parseShort(action.getArgs().split(",")[0]);
            int newCellID = Integer.parseInt(action.getArgs().split(",")[1]);
            int ObjetNeed = Integer.parseInt(action.getArgs().split(",")[2]);
            int MapNeed = Integer.parseInt(action.getArgs().split(",")[3]);
            if (ObjetNeed == 0) {
                //T�l�portation sans objets
                playerHandler.teleport(caster, newMapID, newCellID);
            } else if (ObjetNeed > 0) {
                if (MapNeed == 0) {
                    //T�l�portation sans map
                    playerHandler.teleport(caster, newMapID, newCellID);
                } else if (MapNeed > 0) {
                    if (caster.hasItemTemplate(ObjetNeed, 1) && caster.getMap().getId() == MapNeed) {
								//Le caster a l'item
                        //Le caster est sur la bonne map
                        //On t�l�porte, on supprime apr�s
                        playerHandler.teleport(caster, newMapID, newCellID);
                        caster.removeByTemplateID(ObjetNeed, 1);
                        SocketManager.GAME_SEND_Ow_PACKET(caster);
                        SocketManager.GAME_SEND_Im_PACKET(caster, "022;1~" + ObjetNeed);
                    } else if (caster.getMap().getId() != MapNeed) {
                        //Le caster n'est pas sur la bonne map
                        SocketManager.GAME_SEND_MESSAGE(caster, "Vous n'etes pas sur la bonne map du donjon pour etre teleporter.", "009900");
                        return false;
                    } else {
                        //Le caster ne poss�de pas l'item
                        SocketManager.GAME_SEND_MESSAGE(caster, "Vous ne possedez pas la clef necessaire.", "009900");
                        return false;
                    }
                }
            }
            
            return true;
        } catch (Exception e) {
            Peak.errorLog.addToLog(e);
            return false;
        }
    }

}
