/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.action;

import org.peakemu.common.SocketManager;
import org.peakemu.objects.item.Item;
import org.peakemu.objects.player.Player;
import org.peakemu.world.MapCell;
import org.peakemu.world.enums.PlayerRace;
import org.peakemu.world.handler.PlayerHandler;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class TeleportationIncarnamToAstrub implements ActionPerformer {
    final private PlayerHandler playerHandler;

    public TeleportationIncarnamToAstrub(PlayerHandler playerHandler) {
        this.playerHandler = playerHandler;
    }

    @Override
    public int actionId() {
        return 69;
    }

    @Override
    public boolean apply(Action action, Player caster, Player target, MapCell cell, Item item) {
        SocketManager.GAME_SEND_GA_PACKET(caster.getAccount().getGameThread(), "", "2", caster.getSpriteId() + "", "7");
        if (caster.getRace() == PlayerRace.FECA) {
            playerHandler.teleport(caster, (short) 7398, (int) 284);
            caster.setSavePos("7398,284");
        }
        if (caster.getRace() == PlayerRace.OSAMODAS) {
            playerHandler.teleport(caster, (short) 7545, (int) 297);
            caster.setSavePos("7545,297");
        }
        if (caster.getRace() == PlayerRace.ENUTROF) {
            playerHandler.teleport(caster, (short) 7442, (int) 255);
            caster.setSavePos("7442,255");
        }
        if (caster.getRace() == PlayerRace.SRAM) {
            playerHandler.teleport(caster, (short) 7392, (int) 282);
            caster.setSavePos("7392,282");
        }
        if (caster.getRace() == PlayerRace.XELOR) {
            playerHandler.teleport(caster, (short) 7332, (int) 312);
            caster.setSavePos("7332,312");
        }
        if (caster.getRace() == PlayerRace.ECAFLIP) {
            playerHandler.teleport(caster, (short) 7446, (int) 284);
            caster.setSavePos("7446,284");
        }
        if (caster.getRace() == PlayerRace.ENIRIPSA) {
            playerHandler.teleport(caster, (short) 7361, (int) 192);
            caster.setSavePos("7361,192");
        }
        if (caster.getRace() == PlayerRace.IOP) {
            playerHandler.teleport(caster, (short) 7427, (int) 267);
            caster.setSavePos("7427,267");
        }
        if (caster.getRace() == PlayerRace.CRA) {
            playerHandler.teleport(caster, (short) 7378, (int) 324);
            caster.setSavePos("7378,324");
        }
        if (caster.getRace() == PlayerRace.SADIDA) {
            playerHandler.teleport(caster, (short) 7395, (int) 357);
            caster.setSavePos("7395,357");
        }
        if (caster.getRace() == PlayerRace.SACRIEUR) {
            playerHandler.teleport(caster, (short) 7336, (int) 198);
            caster.setSavePos("7336,198");
        }
        if (caster.getRace() == PlayerRace.PANDAWA) {
            playerHandler.teleport(caster, (short) 8035, (int) 340);
            caster.setSavePos("8035,340");
        }
				//caster.setSavePos();
        //SQLManager.SAVE_PERSONNAGE(caster,true);
        //SocketManager.GAME_SEND_GA_PACKET(caster.getAccount().getGameThread().get_out(), "", "2", caster.getSpriteId()+"", "7");
        SocketManager.GAME_SEND_Im_PACKET(caster, "06");
        return true;
    }

}
