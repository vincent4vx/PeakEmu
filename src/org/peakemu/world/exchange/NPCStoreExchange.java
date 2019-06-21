/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.exchange;

import org.peakemu.game.out.InfoMessage;
import org.peakemu.game.out.exchange.BuyDone;
import org.peakemu.game.out.exchange.ExchangeLeaved;
import org.peakemu.game.out.exchange.SellDone;
import org.peakemu.objects.item.Item;
import org.peakemu.objects.NPC;
import org.peakemu.objects.player.Player;
import org.peakemu.world.ItemTemplate;
import org.peakemu.world.enums.ExchangeType;
import org.peakemu.world.handler.ItemHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class NPCStoreExchange implements BuyExchange, SellExchange{
    final private ItemHandler itemHandler;
    final private NPC npc;
    final private Player player;

    public NPCStoreExchange(ItemHandler itemHandler, NPC npc, Player player) {
        this.itemHandler = itemHandler;
        this.npc = npc;
        this.player = player;
    }

    @Override
    public void buy(int itemId, int qte) {
        ItemTemplate tpl = npc.getTemplate().getSellItem(itemId);
        
        if(tpl == null)
            return;
        
        long price = qte * tpl.getPrix();
        
        if(price > player.getKamas()){
            player.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(128, price));
            return;
        }
        
        player.removeKamas(price);
        Item item = itemHandler.createNewItem(tpl, qte, false);
        player.getItems().addItem(item);
        player.send(new BuyDone(true));
    }

    @Override
    public void sell(int itemId, int qte) {
        Item item = player.getItems().get(itemId);
        
        if(item == null || qte < 1){
            player.send(new SellDone(false));
            return;
        }
        
        if(qte > item.getQuantity())
            qte = item.getQuantity();
        
        long price = (item.getTemplate().getPrix() * qte) / 10;
        
        player.getItems().changeQuantity(item, item.getQuantity() - qte);
        player.addKamas(price);
        player.send(new SellDone(true));
    }

    @Override
    public void cancel() {
        player.send(new ExchangeLeaved());
    }

    @Override
    public ExchangeType getType() {
        return ExchangeType.NPC_STORE;
    }
    
}
