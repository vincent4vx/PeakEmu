/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.object;

import org.peakemu.Peak;
import org.peakemu.common.ConditionParser;
import org.peakemu.common.Constants;
import org.peakemu.common.Logger;
import org.peakemu.common.SocketManager;
import org.peakemu.common.util.StringUtil;
import org.peakemu.game.GameClient;
import org.peakemu.game.out.InfoMessage;
import org.peakemu.game.out.game.SpriteParser;
import org.peakemu.game.out.mount.MountEquiped;
import org.peakemu.network.InputPacket;
import org.peakemu.objects.Mount;
import org.peakemu.objects.item.Item;
import org.peakemu.objects.player.JobStats;
import org.peakemu.world.ItemTemplate;
import org.peakemu.world.enums.InventoryPosition;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class MoveItem implements InputPacket<GameClient> {

    @Override
    public void parse(String args, GameClient client) {
        if (client.getPlayer() == null) {
            return;
        }

        String[] infos = StringUtil.split(args, "|", 3);

        int qua;
        int guid = Integer.parseInt(infos[0]);
        InventoryPosition pos = null;
        
        try {
            int ipos = Integer.parseInt(infos[1]);
            pos = InventoryPosition.fromId(ipos);
            
            qua = Integer.parseInt(infos[2]);
        } catch (Exception e) {
            qua = 1;
        }
           
        if(pos == null){
            Peak.worldLog.addToLog(Logger.Level.ERROR, "Undefined position");
            return;
        }

        // VERIFS
        if (!client.getPlayer().getItems().containsItemId(guid)) {
            return;
        }
        
        if(pos.isEquipment()) //can equip more than 1 item
            qua = 1;

        final Item toMove = client.getPlayer().getItems().get(guid);
        
        if(toMove == null)
            return;

        if (qua > toMove.getQuantity()) {
            qua = toMove.getQuantity();
        }

        if (client.getPlayer().getFight() != null) {
            if (client.getPlayer().getFight().getState() > Constants.FIGHT_STATE_ACTIVE) {
                return;
            }
        }

        if (!client.getPlayer().showWings() && pos == InventoryPosition.BOUCLIER) // Besoin des ailes pour bouclier
        {
            //TODO: Envoi le Im
            return;
        }

        if (!pos.isValidPosition(toMove.getType())) {
            return;
        }

        if (!toMove.getTemplate().getConditions().isEmpty() && !ConditionParser.validConditions(client.getPlayer(), toMove.getTemplate().getConditions())) {
            SocketManager.GAME_SEND_Im_PACKET(client.getPlayer(), "119|43");
            return;
        }

        if (toMove.getTemplate().getLevel() > client.getPlayer().getLevel()) {
            SocketManager.GAME_SEND_OAEL_PACKET(client);
            return;
        }

        //On ne peut équiper 2 items identiques
        if (pos != InventoryPosition.NO_EQUIPED && client.getPlayer().hasEquiped(toMove.getTemplate())) {
            return;
        }

        // FIN DES VERIFS
//        Item exObj = client.getPlayer().getItems().getItemByPos(pos);//Objet a l'ancienne position
//
//        int objGUID = toMove.getTemplate().getID();
//        // CODE OBVI
//        if ((objGUID == 9234) || (objGUID == 9233) || (objGUID == 9255) || (objGUID == 9256)) {
//            // LES VERFIS
//            if (exObj == null) {// si on place l'obvi sur un emplacement vide
//                SocketManager.send(client.getPlayer(), "Im1161");
//                return;
//            }
//            // Sac à dos : on n'associe pas d'obvi a un sac a dos
//            if (exObj.getTemplate().getType() == ItemType.SAC_DOS) {
//                return;
//            }
//            if (exObj.getObvijevanPos() != 0) {// si il y a dï¿½jï¿½ un obvi
//                SocketManager.GAME_SEND_BN(client.getPlayer());
//                return;
//            }
//            // FIN DES VERIFS
//
//            exObj.setObvijevanPos(toMove.getObvijevanPos()); // L'objet qui ï¿½tait en place a maintenant un obvi
//
//            client.getPlayer().removeItem(toMove.getGuid(), 1, false, false); // on enlï¿½ve l'existance de l'obvi en lui-mï¿½me
//            SocketManager.send(client.getPlayer(), "OR" + toMove.getGuid()); // on le prï¿½cise au client
//
////            StringBuilder cibleNewStats = new StringBuilder();
////            cibleNewStats.append(toMove.parseStatsStringSansUserObvi()).append(",").append(exObj.parseStatsStringSansUserObvi());
////            cibleNewStats.append(",3ca#").append(Integer.toHexString(objGUID)).append("#0#0#0d0+").append(objGUID);
////            exObj.clearStats();
////            exObj.parseStringToStats(cibleNewStats.toString());
//
//            SocketManager.send(client.getPlayer(), exObj.obvijevanOCO_Packet(pos));
//
//            if ((objGUID == 9233) || (objGUID == 9234)) {
//                SocketManager.GAME_SEND_ON_EQUIP_ITEM(client.getPlayer().getMap(), client.getPlayer()); // Si l'obvi ï¿½tait cape ou coiffe : packet au client
//            }					// S'il y avait plusieurs objets
//            if (toMove.getQuantity() > 1) {
//                if (qua > toMove.getQuantity()) {
//                    qua = toMove.getQuantity();
//                }
//
//                if (toMove.getQuantity() - qua > 0)//Si il en reste
//                {
//                    int newItemQua = toMove.getQuantity() - qua;
//                    Item newItem = Item.getCloneObjet(toMove, newItemQua);
//                    client.getPlayer().addItem(newItem, false);
//                    toMove.setQuantity(qua);
//                    SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(client.getPlayer(), toMove);
//                }
//            }
//
//            return; // on s'arrï¿½te lï¿½ pour l'obvi
//        } // FIN DU CODE OBVI

        //player already have an equiped item
        //he should unequip before - Dofus client automatically do this
        if (client.getPlayer().getItems().getItemByPos(pos) != null) {
            return;
        }
        
        //Equip two handed weapon, with shield
        if(pos == InventoryPosition.ARME && toMove.getTemplate().isTwoHanded()){
            if(client.getPlayer().getItems().unequipItem(InventoryPosition.BOUCLIER) != null){
                client.send(new InfoMessage(InfoMessage.Type.INFO).addMessage(79));
            }
        }
        
        //Equip shield with two handed weapon
        if(pos == InventoryPosition.BOUCLIER){
            Item weapon = client.getPlayer().getItemByPos(InventoryPosition.ARME);
            
            if(weapon != null && weapon.getTemplate().isTwoHanded()){
                client.getPlayer().getItems().unequipItem(InventoryPosition.ARME);
                client.send(new InfoMessage(InfoMessage.Type.INFO).addMessage(78));
            }
        }
        
        if(pos == InventoryPosition.DRAGODINDE){
            Mount mount = client.getPlayer().getMount();
            if(mount == null){
                client.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(104));
                return;
            }
            
            int totalwin = qua * 10;
            int winEnergie = mount.get_energie() + totalwin;
            mount.setEnergie(winEnergie);
            client.send(new MountEquiped(mount));
            client.getPlayer().getItems().remove(toMove);
            client.send(new InfoMessage(InfoMessage.Type.INFO).addMessage(105));
            return;
        }
        
        //Si familier
        if (pos == InventoryPosition.FAMILIER && client.getPlayer().isOnMount()) {
            client.getPlayer().toggleOnMount();
        }
        
        final InventoryPosition lastPos = toMove.getPosition();
        
        if(!client.getPlayer().getItems().moveItem(toMove, pos, qua))
            return;
        
        client.getPlayer().refreshStats();
        SocketManager.GAME_SEND_STATS_PACKET(client.getPlayer());
        
        if(SpriteParser.isVisibleStuffPosition(lastPos) || SpriteParser.isVisibleStuffPosition(pos)){
            if (client.getPlayer().getGroup() != null) {
                SocketManager.GAME_SEND_PM_MOD_PACKET_TO_GROUP(client.getPlayer().getGroup(), client.getPlayer());
            }
            SocketManager.GAME_SEND_ON_EQUIP_ITEM(client.getPlayer().getMap(), client.getPlayer());
        }
        
        //Verif pour les outils de métier
        if (pos == InventoryPosition.NO_EQUIPED && client.getPlayer().getItemByPos(InventoryPosition.ARME) == null) {
            SocketManager.GAME_SEND_OT_PACKET(client, -1);
        }

        if (pos == InventoryPosition.ARME && client.getPlayer().getItemByPos(InventoryPosition.ARME) != null) {
            ItemTemplate tpl = client.getPlayer().getItemByPos(InventoryPosition.ARME).getTemplate();

            for (JobStats jobStats : client.getPlayer().getJobs()) {
                if (jobStats.getJob().isValidTool(tpl)) {
                    SocketManager.GAME_SEND_OT_PACKET(client, jobStats.getJob().getId());
                    break;
                }
            }
        }
        
        //Si en combat
        if (client.getPlayer().getFight() != null) {
            SocketManager.GAME_SEND_ON_EQUIP_ITEM_FIGHT(client.getPlayer(), client.getPlayer().getFight().getFighterByPerso(client.getPlayer()), client.getPlayer().getFight());
        }
    }

    @Override
    public String header() {
        return "OM";
    }

}
