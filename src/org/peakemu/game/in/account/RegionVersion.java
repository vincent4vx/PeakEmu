/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.account;

import org.peakemu.game.GameServerConfig;
import org.peakemu.game.GameClient;
import org.peakemu.game.out.account.RegionalVersion;
import org.peakemu.network.InputPacket;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class RegionVersion implements InputPacket<GameClient>{
    final private GameServerConfig config;

    public RegionVersion(GameServerConfig config) {
        this.config = config;
    }

    @Override
    public void parse(String args, GameClient client) {
        client.send(new RegionalVersion(config.getRegionalVersion()));
    }

    @Override
    public String header() {
        return "AV";
    }
    
}
