/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.network;

import java.util.HashMap;
import java.util.Map;
import org.peakemu.common.Logger;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class InputPacketRegistry {
    private int minHeaderLength = 2;
    private int maxHeaderLength = 2;
    
    final private Map<String, InputPacket> packets = new HashMap<>();
    
    public void addPacket(InputPacket packet){
        packets.put(packet.header(), packet);
        
        if(packet.header().length() < minHeaderLength)
            minHeaderLength = packet.header().length();
        
        if(packet.header().length() > maxHeaderLength)
            maxHeaderLength = packet.header().length();
    }
    
    public void parsePacket(String packet, DofusClient client) throws PacketNotFound{
        for(int len = maxHeaderLength > packet.length() ? packet.length() : maxHeaderLength; len >= minHeaderLength; --len){
            String header = packet.substring(0, len);
            
            if(packets.containsKey(header)){
                InputPacket inputPacket = packets.get(header);
                String args = packet.substring(len);
                client.log().addToLog(Logger.Level.INFO, "Recv << %s (%s) args : %s", inputPacket.getClass().getSimpleName(), header, args);
                inputPacket.parse(args, client);
                return;
            }
        }
        
        throw new PacketNotFound(packet);
    }
}
