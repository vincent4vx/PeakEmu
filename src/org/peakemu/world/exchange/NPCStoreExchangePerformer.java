/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.exchange;

import org.peakemu.database.dao.PlayerDAO;
import org.peakemu.game.out.exchange.ExchangeCreated;
import org.peakemu.game.out.exchange.ExchangeLeaved;
import org.peakemu.game.out.exchange.NPCStoreList;
import org.peakemu.objects.NPC;
import org.peakemu.objects.player.Player;
import org.peakemu.world.Sprite;
import org.peakemu.world.enums.ExchangeType;
import org.peakemu.world.handler.ItemHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class NPCStoreExchangePerformer implements ExchangePerformer{
    final private PlayerDAO playerDAO;
    final private ItemHandler itemHandler;

    public NPCStoreExchangePerformer(PlayerDAO playerDAO, ItemHandler itemHandler) {
        this.playerDAO = playerDAO;
        this.itemHandler = itemHandler;
    }

    @Override
    public ExchangeType getExchangeType() {
        return ExchangeType.NPC_STORE;
    }

    @Override
    public void request(Player player, String[] args) {
        int npcId;
        
        try{
            npcId = Integer.parseInt(args[1]);
        }catch(NumberFormatException e){
            return;
        }
        
        Sprite sprite = player.getMap().getSprite(npcId);
        
        if(sprite == null || !(sprite instanceof NPC))
            return;
        
        NPC npc = (NPC)sprite;
        
        player.setCurExchange(new NPCStoreExchange(itemHandler, npc, player));
        player.send(new ExchangeCreated(getExchangeType()));
        player.send(new NPCStoreList(npc));
    }

    @Override
    public void leave(Player player, ExchangeBase exchange) {
        exchange.cancel();
        playerDAO.save(player);
    }

    @Override
    public void validate(Player player, ExchangeBase exchange) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
