/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.exchange;

import org.peakemu.Peak;
import org.peakemu.common.Logger;
import org.peakemu.common.util.StringUtil;
import org.peakemu.game.GameClient;
import org.peakemu.game.out.exchange.ExchangeRequestError;
import org.peakemu.network.InputPacket;
import org.peakemu.world.enums.ExchangeType;
import org.peakemu.world.handler.ExchangeHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class ExchangeRequest implements InputPacket<GameClient> {
    final private ExchangeHandler exchangeHandler;

    public ExchangeRequest(ExchangeHandler exchangeHandler) {
        this.exchangeHandler = exchangeHandler;
    }

    @Override
    public void parse(String args, GameClient client) {
        String[] data = StringUtil.split(args, "|");
        ExchangeType exchangeType = ExchangeType.valueOf(Integer.parseInt(data[0]));

        if (exchangeType == null) {
            Peak.gameLog.addToLog(Logger.Level.DEBUG, "ExchangeType %s not found", data[0]);
            client.send(new ExchangeRequestError(ExchangeRequestError.CANT_EXCHANGE));
            return;
        }

        if (client.getPlayer().getRequest() != null || client.getPlayer().getCurExchange() != null) {
            client.send(new ExchangeRequestError(ExchangeRequestError.CANT_EXCHANGE));
            return;
        }

        exchangeHandler.request(exchangeType, client.getPlayer(), data);
    }

    @Override
    public String header() {
        return "ER";
    }
}
