/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.object;

import org.peakemu.common.ConditionParser;
import org.peakemu.common.SocketManager;
import org.peakemu.common.util.StringUtil;
import org.peakemu.game.GameClient;
import org.peakemu.game.out.InfoMessage;
import org.peakemu.network.InputPacket;
import org.peakemu.objects.item.Item;
import org.peakemu.objects.player.Player;
import org.peakemu.world.MapCell;
import org.peakemu.world.enums.ItemType;
import org.peakemu.world.handler.ActionHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class UseObject implements InputPacket<GameClient>{
    final private ActionHandler actionHandler;

    public UseObject(ActionHandler actionHandler) {
        this.actionHandler = actionHandler;
    }

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null)
            return;
        
        int guid = -1;
        int targetGuid = -1;
        short cellId = -1;
        try {
            String[] infos = StringUtil.split(args, "|", 3);
            guid = Integer.parseInt(infos[0]);
            try {
                targetGuid = Integer.parseInt(infos[1]);
            } catch (Exception e) {
                targetGuid = -1;
            };
            try {
                cellId = Short.parseShort(infos[2]);
            } catch (Exception e) {
                cellId = -1;
            }
        } catch (Exception e) {
            return;
        }
        
        Player target = client.getPlayer().getMap().getPlayer(targetGuid);
        Item item = client.getPlayer().getItems().get(guid);
        MapCell cell = client.getPlayer().getMap().getCell(cellId);
        
        if (item == null || client.getPlayer().getFight() != null || client.getPlayer().isAway()) {
            return;
        }
        
        if (target != null && (target.getFight() != null || target.isAway())) {
            return;
        }
        
        if(!ItemType.isUsable(item.getType())){
            return;
        }
        
        if(item.getQuantity() <= 0){
            return;
        }
        
        if (!ConditionParser.validConditions(client.getPlayer(), item.getTemplate().getConditions())) {
            client.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(19).addMessage(43));
            return;
        }
        
        if(!actionHandler.applyAll(item.getTemplate().getUseActions(), client.getPlayer(), target, cell, item)){
            return;
        }

        // Objectif quête : Utiliser l'objet x
//        client.getPlayer().confirmObjective(8, item.getTemplate().getID() + "", null);

        if (item.getTemplate().getType() == ItemType.PAIN || item.getTemplate().getType() == ItemType.VIANDE_COMESTIBLE) {
            SocketManager.GAME_SEND_eUK_PACKET_TO_MAP(client.getPlayer().getMap(), client.getPlayer().getSpriteId(), 17);
        } else if (item.getTemplate().getType() == ItemType.BIERE) {
            SocketManager.GAME_SEND_eUK_PACKET_TO_MAP(client.getPlayer().getMap(), client.getPlayer().getSpriteId(), 18);
        }
        
        client.getPlayer().getItems().changeQuantity(item, item.getQuantity() - 1);
    }

    @Override
    public String header() {
        return "OU";
    }
    
}
