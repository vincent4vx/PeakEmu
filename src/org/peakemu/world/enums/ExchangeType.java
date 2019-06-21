/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.enums;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public enum ExchangeType {
    NPC_STORE(0),
    PLAYER_EXCHANGE(1),
    NPC_EXCHANGE(2),
    FACTORY_TABLE(3),
    PLAYER_STORE_BUY(4),
    BANK(5),
    PLAYER_STORE_SELL(6),
    COLLECTOR(8),
    HDV_SELL(10),
    HDV_BUY(11),
    MOUNT(15),
    MOUNT_PARK(16),
    ;
    
    final private int id;

    private ExchangeType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
    
    static public ExchangeType valueOf(int id){
        for(ExchangeType et : values()){
            if(et.getId() == id)
                return et;
        }
        
        return null;
    }
}
