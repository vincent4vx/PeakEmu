/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.peakemu.Ancestra;
import org.peakemu.common.util.Util;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class Logger implements Runnable{
    final private BlockingQueue<String> queue = new LinkedBlockingQueue<>();
    private BufferedWriter br;
    final private Thread thread;
    private boolean running = true;
    
    public enum Level{
        ERROR, INFO, DEBUG
    }
    
    public Logger(File folder) {
        if(!folder.isDirectory()){
            folder.mkdirs();
        }
        
        try{
            br = new BufferedWriter(new FileWriter(folder.getAbsolutePath() + "/" + Util.getStringDate(), true));
            br.newLine();
            br.newLine();
            br.write("====> Starting <====");
            br.newLine();
            br.newLine();
        }catch(IOException e){
            e.printStackTrace();
            System.exit(1);
        }
        
        thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void run() {
        while(running){
            try{
                String line = queue.poll(100, TimeUnit.MILLISECONDS);
                
                if(line != null){
                    br.write(line);
                    br.newLine();
                }
                
                br.flush();
            }catch(Exception e){}
        }
    }
    
    public void addToLog(Level level, String message, Object... data){
        String realMessage = (data.length > 0 ? String.format(message, data) : message);
        
        switch(level){
            case ERROR:
                System.err.println(realMessage);
                queue.add("[" + Util.getStringTime() + "] " + level + " : " + realMessage);
                break;
            case INFO:
                queue.add("[" + Util.getStringTime() + "] " + level + " : " + realMessage);
                if(Ancestra.CONFIG_DEBUG){
                    System.out.println(realMessage);
                }
                break;
            case DEBUG:
                if(Ancestra.CONFIG_DEBUG){
                    System.out.println(realMessage);
                    queue.add("[" + Util.getStringTime() + "] " + level + " : " + realMessage);
                }
        }
    }
    
    public void addToLog(Throwable e){
        StringBuilder msg = new StringBuilder();
        msg.append(e).append('\n');
        msg.append("===>StackTrace<===\n");
        
        for(StackTraceElement ste : e.getStackTrace()){
            msg.append(">> ").append(ste).append('\n');
        }
        
        if(e.getCause() != null){
            msg.append("===>Caused By<===").append('\n');
            msg.append(">> ").append(e.getCause()).append('\n');
            for(StackTraceElement ste : e.getCause().getStackTrace()){
                msg.append(">>>> ").append(ste).append('\n');
            }
        }
        
        addToLog(Level.ERROR, msg.toString());
    }
    
    public void close(){
        try{
            running = false;
            thread.stop();
            br.flush();
        }catch(Exception e){}
    }
}
