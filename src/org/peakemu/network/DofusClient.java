/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.peakemu.common.Logger;
import org.peakemu.objects.Account;
import org.peakemu.world.handler.SessionHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
abstract public class DofusClient implements Runnable {
    final private BufferedReader in;
    final private PrintWriter out;
    final private Socket socket;
    final private InputPacketRegistry packetRegistry;
    final private SessionHandler sessionHandler;
    final private Map<Class, Object> registry = new HashMap<>();
    private int packetNumber = 0;
    private long lastPacketTime = System.currentTimeMillis();
    private boolean running = false;
    
    private Account account;

    public DofusClient(Socket sock, InputPacketRegistry packetRegistry, SessionHandler sessionHandler) throws IOException {
        this.socket = sock;
        this.packetRegistry = packetRegistry;
        this.sessionHandler = sessionHandler;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream());
    }
    
    public void send(Object packet){
        if(isClosed())
            return;
        
        log().addToLog(Logger.Level.INFO, "Send >> %s", packet);
        out.print(packet);
        out.print('\0');
        out.flush();
    }

    public int getPacketNumber() {
        return packetNumber;
    }
    
    protected void parsePacket(String packet){
        try {
            packetRegistry.parsePacket(packet, this);
        } catch (PacketNotFound ex) {
            log().addToLog(Logger.Level.INFO, "Recv << (Packet not found) %s", packet);
        }
    }
    
    public void close(){
        
        try{
            if(!socket.isClosed())
                socket.close();
        }catch(Exception e){}
        
        sessionHandler.removeSession(this);
    }
    
    abstract public Logger log();
    abstract public void onConnect();

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public void run() {
        running = true;
        try {
            onConnect();
            StringBuilder packet = new StringBuilder(256);
            char charCur[] = new char[1];

            while (!isClosed() && in.read(charCur, 0, 1) != -1) {
                if (charCur[0] != '\0') {
                    packet.append(charCur[0]);
                    continue;
                }
                
                String realPacket = packet.toString().trim();
                
                if (realPacket.length() > 0) {
                    lastPacketTime = System.currentTimeMillis();
                    
                    try{
                        parsePacket(realPacket);
                    }catch(Exception e){
                        log().addToLog(e);
                    }
                    
                    packetNumber++;
                    packet = new StringBuilder(256);
                }
            }
        }catch(IOException e){
        } catch (Throwable e) {
            log().addToLog(e);
        } finally {
            close();
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.socket);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DofusClient other = (DofusClient) obj;
        if (!Objects.equals(this.socket, other.socket)) {
            return false;
        }
        return true;
    }

    public boolean isClosed() {
        return socket.isClosed() && !running;
    }

    protected Socket getSocket() {
        return socket;
    }
    
    public String getIP(){
        return socket.getInetAddress().getHostAddress();
    }

    public long getLastPacketTime() {
        return lastPacketTime;
    }
    
    public void addRegistry(Object o){
        registry.put(o.getClass(), o);
    }
    
    public<T> T getRegistry(Class<T> clazz){
        return (T)registry.get(clazz);
    }
    
    public<T> T deleteRegistry(Class<T> clazz){
        return (T)registry.remove(clazz);
    }
    
    public boolean registryContains(Class clazz){
        return registry.containsKey(clazz);
    }
}
