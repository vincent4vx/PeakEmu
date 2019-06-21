/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.exchange;

import org.peakemu.objects.Bank;
import org.peakemu.objects.Collector;
import org.peakemu.objects.item.Item;
import org.peakemu.world.ItemStorage;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class ExchangeList {
    private ItemStorage storage;
    private long kamas;

    public ExchangeList(ItemStorage storage, long kamas) {
        this.storage = storage;
        this.kamas = kamas;
    }

    public ItemStorage getStorage() {
        return storage;
    }

    public void setStorage(ItemStorage storage) {
        this.storage = storage;
    }

    public long getKamas() {
        return kamas;
    }

    public void setKamas(long kamas) {
        this.kamas = kamas;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(storage.getItems().size() * 128);
        
        sb.append("EL");
        
        for(Item item : storage){
            sb.append('O').append(item.getPacket()).append(';');
        }
        
        if(kamas > 0)
            sb.append('G').append(kamas);
        
        return sb.toString();
    }
    
    static public ExchangeList fromBank(Bank bank){
        return new ExchangeList(bank.getItems(), bank.getKamas());
    }
    
    static public ExchangeList fromCollector(Collector collector){
        return new ExchangeList(collector.getItems(), collector.getKamas());
    }
}
