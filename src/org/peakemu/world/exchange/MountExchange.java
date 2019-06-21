/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.exchange;

import org.peakemu.game.out.InfoMessage;
import org.peakemu.game.out.exchange.ExchangeLeaved;
import org.peakemu.game.out.exchange.MountPods;
import org.peakemu.objects.item.Item;
import org.peakemu.objects.Mount;
import org.peakemu.objects.player.Player;
import org.peakemu.world.enums.ExchangeType;
import org.peakemu.world.listener.ExchangeInventoryListener;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class MountExchange implements ExchangeItem{
    final private Player player;
    final private Mount mount;
    
    final private ExchangeInventoryListener listener;

    public MountExchange(Player player, Mount mount) {
        this.player = player;
        this.mount = mount;
        
        listener = new ExchangeInventoryListener(player.getAccount().getGameThread());
        mount.getItems().addListener(listener);
    }

    @Override
    public void addItem(int itemId, int qte) {
        Item item = player.getItems().get(itemId);
        
        if(item == null)
            return;
        
        if(qte > item.getQuantity())
            qte = item.getQuantity();
        
        int freePods = mount.getMaxPod() - mount.getItems().getUsedPods();
        
        if(freePods < item.getTemplate().getPod() * qte){ //inventory full
            player.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(10));
            return;
        }
        
        player.getItems().changeQuantity(item, item.getQuantity() - qte);
        
        Item mountItem = item.cloneItem(qte);
        mount.getItems().addItem(mountItem);
        player.send(new MountPods(mount));
    }

    @Override
    public void removeItem(int itemId, int qte) {
        Item item = mount.getItems().get(itemId);
        
        if(item == null)
            return;
        
        if(qte > item.getQuantity())
            qte = item.getQuantity();
        
        mount.getItems().changeQuantity(item, item.getQuantity() - qte);
        
        Item playerItem = item.cloneItem(qte);
        player.getItems().addItem(playerItem);
        player.send(new MountPods(mount));
    }

    @Override
    public void cancel() {
        player.send(new ExchangeLeaved());
        mount.getItems().removeListener(listener);
    }

    @Override
    public ExchangeType getType() {
        return ExchangeType.MOUNT;
    }
    
}
