/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.peakemu.Ancestra;
import org.peakemu.Peak;
import org.peakemu.common.Logger;
import org.peakemu.network.out.Message;
import org.peakemu.world.handler.SessionHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
abstract public class DofusServer<C extends DofusClient> implements Runnable{
    final private ServerSocket ss;
    final private SessionHandler sessionHandler;
    private boolean running = true;
    
    final private ExecutorService service = Executors.newCachedThreadPool();

    public DofusServer(int port, SessionHandler sessionHandler) throws Exception{
        ss = new ServerSocket(port);
        this.sessionHandler = sessionHandler;
        service.submit(this);
    }

    @Override
    public void run() {
        while (running){
            DofusClient client = null;
            try{
                client = createClient(ss.accept());
                
                if(sessionHandler.isFull()){
                    client.send(new Message(true, 16, new String[]{}));
                    client.close();
                    Peak.errorLog.addToLog(Logger.Level.INFO, "Reach max clients limit : %d", sessionHandler.getClientCount());
                    continue;
                }
                
                int ipCount = sessionHandler.getIpCount(client.getIP());
                
                if(ipCount >= sessionHandler.getMaxIpPerClient()){
                    client.send(new Message(true, 34, ipCount, client.getIP()));
                    client.close();
                    continue;
                }
                
                sessionHandler.addSession(client);
                service.submit(client);
            }catch(Throwable e){
                Peak.errorLog.addToLog(e);
                if(client != null)
                    client.close();
            }
        }
    }
    
    abstract protected C createClient(Socket socket) throws IOException;

    public void close() {
        try {
            running = false;
            ss.close();
        } catch (IOException e) {
        }
    }
}
