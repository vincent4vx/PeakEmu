/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.objects.player;

import java.util.ArrayList;
import org.peakemu.common.SocketManager;
import org.peakemu.game.out.group.GroupChief;
import org.peakemu.game.out.group.GroupCreated;
import org.peakemu.game.out.group.GroupInvitationAccepted;
import org.peakemu.game.out.group.GroupInvitationRefused;
import org.peakemu.game.out.group.GroupList;
import org.peakemu.game.out.group.GroupPlayerAdded;
import org.peakemu.world.Request;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class Group {
    final private ArrayList<Player> players = new ArrayList<Player>();
    final private Player chief;

    public Group(Player chief) {
        this.chief = chief;
        players.add(chief);
    }

    public boolean isChief(int guid) {
        return chief.getSpriteId() == guid;
    }

    public void addPlayer(Player p) {
        players.add(p);
    }

    public int getPersosNumber() {
        return players.size();
    }

    public int getGroupLevel() {
        int lvls = 0;
        for (Player p : players) {
            lvls += p.getLevel();
        }
        return lvls;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Player getChief() {
        return chief;
    }
    
    public void sendToGroup(Object packet){
        String str = packet.toString();
        
        for(Player player : players)
            player.send(str);
    }

    public void leave(Player p) {
        if (!players.contains(p)) {
            return;
        }
        p.setGroup(null);
        players.remove(p);
        if (players.size() == 1) {
            players.get(0).setGroup(null);
            if (players.get(0).getAccount() == null || players.get(0).getAccount().getGameThread() == null) {
                return;
            }
            SocketManager.GAME_SEND_PV_PACKET(players.get(0).getAccount().getGameThread(), "");
        } else {
            SocketManager.GAME_SEND_PM_DEL_PACKET_TO_GROUP(this, p.getSpriteId());
        }
    }
    
    public int getSize(){
        return players.size();
    }
    
    static public class InvitationRequest implements Request{
        final private Player performer;
        final private Player target;

        public InvitationRequest(Player performer, Player target) {
            this.performer = performer;
            this.target = target;
        }

        @Override
        public Player getPerformer() {
            return performer;
        }

        @Override
        public Player getTarget() {
            return target;
        }

        @Override
        public void accept() {
            performer.send(new GroupInvitationAccepted());
            target.send(new GroupInvitationAccepted());
            
            if(performer.getGroup() == null){
                performer.setGroup(new Group(performer));
                performer.send(new GroupCreated(performer.getGroup()));
                performer.send(new GroupChief(performer.getGroup()));
                performer.send(new GroupPlayerAdded(performer));
            }
            
            target.setGroup(performer.getGroup());
            target.send(new GroupCreated(performer.getGroup()));
            target.send(new GroupChief(performer.getGroup()));
            performer.getGroup().sendToGroup(new GroupPlayerAdded(target));
            performer.getGroup().addPlayer(target);
            target.send(new GroupList(target.getGroup()));
            
            target.setRequest(null);
            performer.setRequest(null);
        }

        @Override
        public void cancel() {
            target.send(new GroupInvitationRefused());
            performer.send(new GroupInvitationRefused());
            target.setRequest(null);
            performer.setRequest(null);
        }

        @Override
        public void refuse() {
            cancel();
        }
        
    }
}
