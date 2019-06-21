/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.account;

import org.peakemu.common.SocketManager;
import org.peakemu.database.dao.PlayerDAO;
import org.peakemu.game.GameClient;
import org.peakemu.game.out.InfoMessage;
import org.peakemu.game.out.account.CharacterSelected;
import org.peakemu.game.out.account.CharacterSelectionError;
import org.peakemu.game.out.area.SubAreasList;
import org.peakemu.game.out.basic.ServerMessage;
import org.peakemu.game.out.job.JobAvailableSkills;
import org.peakemu.game.out.job.JobXp;
import org.peakemu.network.InputPacket;
import org.peakemu.objects.Account;
import org.peakemu.objects.item.Item;
import org.peakemu.objects.player.JobStats;
import org.peakemu.objects.player.Player;
import org.peakemu.world.config.WorldConfig;
import org.peakemu.world.enums.InventoryPosition;
import org.peakemu.world.handler.AreaHandler;
import org.peakemu.world.handler.JobHandler;
import org.peakemu.world.listener.OutputInventoryListener;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class SelectCharacter implements InputPacket<GameClient>{
    final private PlayerDAO playerDAO;
    final private WorldConfig config;
    final private JobHandler jobHandler;
    final private AreaHandler areaHandler;

    public SelectCharacter(PlayerDAO playerDAO, WorldConfig config, JobHandler jobHandler, AreaHandler areaHandler) {
        this.playerDAO = playerDAO;
        this.config = config;
        this.jobHandler = jobHandler;
        this.areaHandler = areaHandler;
    }

    @Override
    public void parse(String args, GameClient client) {
        if(client.getAccount() == null || client.getPlayer() != null){
            client.send(new CharacterSelectionError());
            return;
        }
        
        int id;
        try{
            id = Integer.parseInt(args);
        }catch(NumberFormatException e){
            client.send(new CharacterSelectionError());
            return;
        }
        
        Player p = playerDAO.getPlayerById(id);
        
        if(p == null){
            client.send(new CharacterSelectionError());
            return;
        }
        
        client.setPlayer(p);
        
        client.send(new CharacterSelected(p));
        
        p.OnJoinGame();
        client.send(new SubAreasList(areaHandler.getSubAreas()));
        
        OutputInventoryListener outputInventoryListener = new OutputInventoryListener(client);
        client.addRegistry(outputInventoryListener);
        client.getPlayer().getItems().addListener(outputInventoryListener);
        
        if(!p.getJobs().isEmpty()){
            JobAvailableSkills jas = new JobAvailableSkills();
            
            for(JobStats jobStats : p.getJobs()){
                jas.addSkill(jobHandler.getJobSkillsPacket(jobStats));
            }
            
            client.send(jas);
            client.send(new JobXp(p.getJobs()));
            SocketManager.GAME_SEND_JO_PACKET(p, p.getJobs());
            Item obj = p.getItems().getItemByPos(InventoryPosition.ARME);
            
            if (obj != null) {
                for (JobStats sm : p.getJobs()) {
                    if (sm.getJob().isValidTool(obj.getTemplate())) {
                        SocketManager.GAME_SEND_OT_PACKET(client, sm.getJob().getId());
                        break;
                    }
                }
            }
        }
        
        String friendConnectionMessage = InfoMessage.friendConnection(client.getPlayer()).toString();
        
        //notify friend connection
        for(Account friend : client.getAccount().getFriendList().getFriends()){
            if(friend.getCurPlayer() == null 
                || !friend.getCurPlayer().showFriendConnection()
                || !client.getAccount().getFriendList().isRealFriend(friend))
                continue;
            
            friend.send(friendConnectionMessage);
        }
        
        if(p.isSeller())
            p.unsetSeller();
        
        if(!config.getWelcomeMessage().isEmpty()){
            client.send(ServerMessage.unsafeColoredMessage(ServerMessage.INFO_CHAT_COLOR, config.getWelcomeMessage()));
        }
    }

    @Override
    public String header() {
        return "AS";
    }
    
}
