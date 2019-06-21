package org.peakemu.realm;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.peakemu.Ancestra;
import org.peakemu.Peak;
import org.peakemu.common.Logger;
import fr.quatrevieux.crisis.config.RootConfig;
import org.peakemu.game.GameServerConfig;
import org.peakemu.network.DofusClient;
import org.peakemu.network.DofusServer;
import org.peakemu.network.InputPacket;
import org.peakemu.network.InputPacketRegistry;
import org.peakemu.realm.in.CheckVersion;
import org.peakemu.realm.in.Login;
import org.peakemu.realm.in.SelectServer;
import org.peakemu.realm.in.ServerList;

public class RealmServer extends DofusServer {
    final private RealmServerConfig config;
    final private InputPacketRegistry inputPacketRegistry = new InputPacketRegistry();
    final private List<InputPacket> packetsByPacketNumber = new ArrayList<>();
    final private Peak peak;

    public RealmServer(RootConfig rootConfig, Peak peak) throws Exception{
        super(rootConfig.getPackage(RealmServerConfig.class).getPort(), peak.getWorld().getSessionHandler());
        config = rootConfig.getPackage(RealmServerConfig.class);
        this.peak = peak;
        registerPackets();
    }
    
    private void registerPackets(){
        try{
            packetsByPacketNumber.add(new CheckVersion(config));
            packetsByPacketNumber.add(new Login(peak.getWorld().getSessionHandler(), peak.getDao().getAccountDAO(), peak.getWorld()));

            inputPacketRegistry.addPacket(new ServerList(peak.getDao().getPlayerDAO()));
            inputPacketRegistry.addPacket(new SelectServer(config, peak.getConfig().getPackage(GameServerConfig.class), peak.getWorld().getSessionHandler()));
        }catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    protected DofusClient createClient(Socket socket) throws IOException {
        return new RealmClient(socket, inputPacketRegistry, peak.getWorld().getSessionHandler(), packetsByPacketNumber);
    }

    public synchronized static void addToLog(String str) {
        System.out.println(str);
        if (Ancestra.canLog) {
            Peak.realmLog.addToLog(Logger.Level.INFO, str);
        }
    }

    public synchronized static void addToSockLog(String str) {
        if (Ancestra.CONFIG_DEBUG) {
            System.out.println(str);
        }
        if (Ancestra.canLog) {
            Peak.realmLog.addToLog(Logger.Level.INFO, str);
        }
    }
}
