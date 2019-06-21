/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.conquest;

import org.peakemu.common.Constants;
import org.peakemu.game.GameClient;
import org.peakemu.game.out.conquest.ConquestWorldData;
import org.peakemu.network.InputPacket;
import org.peakemu.world.handler.AlignementHandler;
import org.peakemu.world.handler.AreaHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class ConquestInfoWorld implements InputPacket<GameClient>{
    final private AlignementHandler alignementHandler;
    final private AreaHandler areaHandler;

    public ConquestInfoWorld(AlignementHandler alignementHandler, AreaHandler areaHandler) {
        this.alignementHandler = alignementHandler;
        this.areaHandler = areaHandler;
    }

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null)
            return;
        
        client.send(new ConquestWorldData(
            alignementHandler.getConquestedAreasCount(client.getPlayer().getAlignement()),
            alignementHandler.getConquestableAreasCount(),
            alignementHandler.getFreeAreasCount(), 
            areaHandler.getSubAreas()
        ));
    }

    @Override
    public String header() {
        return "CWJ";
    }
    
}
