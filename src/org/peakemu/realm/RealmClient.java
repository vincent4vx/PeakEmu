package org.peakemu.realm;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import org.peakemu.Peak;
import org.peakemu.common.Logger;
import org.peakemu.common.util.StringUtil;


import org.peakemu.network.DofusClient;
import org.peakemu.network.InputPacket;
import org.peakemu.network.InputPacketRegistry;
import org.peakemu.realm.out.HelloConnection;
import org.peakemu.world.handler.SessionHandler;

public class RealmClient extends DofusClient {
    final private List<InputPacket> packetsByPacketNumber;

    private String hashKey;

    public RealmClient(Socket sock, InputPacketRegistry packetRegistry, SessionHandler sessionHandler, List<InputPacket> packetsByPacketNumber) throws IOException {
        super(sock, packetRegistry, sessionHandler);
        this.packetsByPacketNumber = packetsByPacketNumber;
    }

    @Override
    public Logger log() {
        return Peak.realmLog;
    }

    @Override
    public void onConnect() {
        hashKey = StringUtil.generateRandString(32);
        send(new HelloConnection(hashKey));
    }

    public String getHashKey() {
        return hashKey;
    }

    @Override
    protected void parsePacket(String packet) {
        if(getPacketNumber() >= packetsByPacketNumber.size())
            super.parsePacket(packet);
        else
            packetsByPacketNumber.get(getPacketNumber()).parse(packet, this);
    }
}
