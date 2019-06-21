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
import org.peakemu.world.World;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class WeddingCell implements ActionPerformer {

    @Override
    public int actionId() {
        return 101;
    }

    @Override
    public boolean apply(Action action, Player caster, Player target, MapCell cell, Item item) {
//        if ((caster.get_sexe() == 0 && caster.get_curCell().getID() == 282 && caster.hasEquiped(6660) && caster.hasEquiped(1501)) || (caster.get_sexe() == 1 && caster.get_curCell().getID() == 297 && caster.hasEquiped(6662) && caster.hasEquiped(1501))) {
//            World.AddMarried(caster.get_sexe(), caster);
//        } else {
//            SocketManager.GAME_SEND_Im_PACKET(caster, "1102");
//            //SocketManager.GAME_SEND_MESSAGE(caster, "Erreur: vous devez avoir un chapeau de la mari�e et une alliance pour la mari�e. Un chapeau du mari� et une alliance sont requis pour le mari" , "000000");
//        }
        return false;
    }

}
