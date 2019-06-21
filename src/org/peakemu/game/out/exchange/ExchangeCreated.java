/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.exchange;

import org.peakemu.world.enums.ExchangeType;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class ExchangeCreated {
    private boolean success = true;
    private ExchangeType exchangeType;

    public ExchangeCreated(ExchangeType exchangeType) {
        this.exchangeType = exchangeType;
        success = true;
    }

    public ExchangeCreated(boolean success) {
        this.success = success;
    }

    public ExchangeType getExchangeType() {
        return exchangeType;
    }

    public void setExchangeType(ExchangeType exchangeType) {
        this.exchangeType = exchangeType;
    }

    @Override
    public String toString() {
        if(!success)
            return "ECE";
        
        return "ECK" + exchangeType.getId();
    }
    
}
