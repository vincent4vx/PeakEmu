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
public class ExchangeRequestOk {
    private int requester;
    private int target;
    private ExchangeType type;

    public ExchangeRequestOk(int requester, int target, ExchangeType type) {
        this.requester = requester;
        this.target = target;
        this.type = type;
    }

    public int getRequester() {
        return requester;
    }

    public void setRequester(int requester) {
        this.requester = requester;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public ExchangeType getType() {
        return type;
    }

    public void setType(ExchangeType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ERK" + requester + "|" + target + "|" + type.getId();
    }
    
    
}
