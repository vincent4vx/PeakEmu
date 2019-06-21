/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.exchange;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class ExchangeLeaved {
    private boolean exchangeOk = false;

    public ExchangeLeaved() {
    }

    public ExchangeLeaved(boolean exchangeOk) {
        this.exchangeOk = exchangeOk;
    }

    @Override
    public String toString() {
        return "EV" + (exchangeOk ? "a" : "");
    }
    
}
