/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.mount;

import org.peakemu.common.util.StringUtil;
import org.peakemu.database.dao.MountDAO;
import org.peakemu.game.GameClient;
import org.peakemu.game.out.InfoMessage;
import org.peakemu.game.out.mount.MountDescriptionResponse;
import org.peakemu.network.InputPacket;
import org.peakemu.objects.Mount;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class MountDescription implements InputPacket<GameClient>{
    final private MountDAO mountDAO;

    public MountDescription(MountDAO mountDAO) {
        this.mountDAO = mountDAO;
    }

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null)
            return;
        
        Mount mount;
        
        try{
            String[] data = StringUtil.split(args, "|", 2);
            mount = mountDAO.getMountById(Integer.parseInt(data[0]));
        }catch(Exception e){
            return;
        }
        
        if(mount == null){
            client.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(108));
            return;
        }
        
        client.send(new MountDescriptionResponse(mount));
    }

    @Override
    public String header() {
        return "Rd";
    }
    
}
