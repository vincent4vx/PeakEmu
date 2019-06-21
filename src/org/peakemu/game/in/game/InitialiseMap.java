/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.game;

import org.peakemu.common.Constants;
import org.peakemu.common.SocketManager;
import org.peakemu.database.dao.MountParkDAO;
import org.peakemu.game.GameClient;
import org.peakemu.game.out.basic.NoOperation;
import org.peakemu.game.out.game.AddCellObjects;
import org.peakemu.game.out.game.AddSprites;
import org.peakemu.game.out.game.FightCreated;
import org.peakemu.game.out.game.TeamFighters;
import org.peakemu.game.out.mount.AddMountPark;
import org.peakemu.network.InputPacket;
import org.peakemu.objects.MountPark;
import org.peakemu.world.fight.Fight;
import org.peakemu.world.fight.team.FightTeam;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class InitialiseMap implements InputPacket<GameClient>{
    final static public String CUSTOM_ACTION_KEY = "GI_CUSTOM_ACTION";
    
    final private MountParkDAO mountParkDAO;
    
    public interface CustomAction{
        public boolean doAction(GameClient client);
    }

    public InitialiseMap(MountParkDAO mountParkDAO) {
        this.mountParkDAO = mountParkDAO;
    }

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null || client.getPlayer().getMap() == null)
            return;
        
        if(client.containsAttachement(CUSTOM_ACTION_KEY)){
            if(!((CustomAction)client.detach(CUSTOM_ACTION_KEY)).doAction(client)){
                SocketManager.GAME_SEND_GDK_PACKET(client);
                return;
            }
        }
        
        //Maisons
        //House.LoadHouse(client.getPlayer(), client.getPlayer().getMap().getId());
        //Objets sur la carte
        client.send(new AddSprites(client.getPlayer().getMap().getAllSprites()));
        SocketManager.GAME_SEND_MAP_OBJECTS_GDS_PACKETS(client, client.getPlayer().getMap());
        SocketManager.GAME_SEND_MAP_FIGHT_COUNT(client, client.getPlayer().getMap());
        
        for(Fight fight : client.getPlayer().getMap().getFights()){
            if(fight.getState() != Constants.FIGHT_STATE_PLACE)
                continue;
            
            client.send(new FightCreated(fight));
            
            for(FightTeam team : fight.getTeams())
                client.send(TeamFighters.allTeam(team));
        }
        
        //items au sol
        client.send(AddCellObjects.fromMap(client.getPlayer().getMap()));
        // Interrupteur : Refresh des portes
        client.getPlayer().getMap().parseSwitchsToGDC(client.getPlayer());
        client.getPlayer().getMap().parseSwitchsToGDF(client.getPlayer());
        
        MountPark park = mountParkDAO.getMountParkByMap(client.getPlayer().getMap());
        if(park != null)
            client.send(new AddMountPark(park));
        
        SocketManager.GAME_SEND_GDK_PACKET(client);
    }

    @Override
    public String header() {
        return "GI";
    }
    
}
