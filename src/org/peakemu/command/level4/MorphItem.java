/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command.level4;

import org.peakemu.command.Command;
import org.peakemu.command.CommandPerformer;
import org.peakemu.world.ItemTemplate;
import org.peakemu.world.World;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class MorphItem implements Command {

    @Override
    public String name() {
        return null; //a return type of a created method
    }

    @Override
    public String shortDescription() {
        return null; //a return type of a created method
    }

    @Override
    public String help() {
        return null; //a return type of a created method
    }

    @Override
    public int minLevel() {
        return 0; //a return type of a created method
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        //ID du skin
        int id_apparance = Integer.parseInt(args[0]);
        //ID de l'item de des stats
        int id_item_jet = Integer.parseInt(args[1]);
        ItemTemplate T = World.getObjTemplate(id_apparance);

        /*Item morphitem = T.createNewMorphItem(id_apparance, id_item_jet);
        if (_perso.addObjet(morphitem, true)) {
            World.addObjet(morphitem, true);
        }*/

        String str = "Morphitem gÃ©nÃ©rÃ©.";

        //SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(_out, str);
        //SocketManager.GAME_SEND_Ow_PACKET(_perso);
    }

}
