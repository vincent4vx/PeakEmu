/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.handler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.peakemu.Peak;
import org.peakemu.common.Logger;
import org.peakemu.common.util.Matcher;
import org.peakemu.common.util.StringUtil;
import org.peakemu.database.dao.AccountDAO;
import org.peakemu.database.dao.PlayerDAO;
import org.peakemu.game.GameClient;
import org.peakemu.network.DofusClient;
import org.peakemu.network.out.Message;
import org.peakemu.objects.Account;
import org.peakemu.objects.player.Player;
import org.peakemu.realm.RealmClient;
import org.peakemu.world.config.WorldConfig;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class SessionHandler {
    final private Collection<DofusClient> clients = Collections.synchronizedSet(new HashSet<DofusClient>());
    final private Map<String, Account> pendingAccounts = new ConcurrentHashMap<>();
    final private WorldConfig config;
    final private ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
    
    final private PlayerDAO playerDAO;
    final private AccountDAO accountDAO;
    
    private int maxClientCount = 0;
    
    final static public Matcher<DofusClient> GAME_CLIENT_MATCHER = new Matcher<DofusClient>() {
        @Override
        public boolean match(DofusClient obj) {
            return (obj instanceof GameClient);
        }
    };
    
    final static public Matcher<DofusClient> REALM_CLIENT_MATCHER = new Matcher<DofusClient>() {
        @Override
        public boolean match(DofusClient obj) {
            return (obj instanceof RealmClient);
        }
    };
    
    final static public Matcher<DofusClient> ONLINE_CLIENT_MATCHER = new Matcher<DofusClient>() {
        @Override
        public boolean match(DofusClient obj) {
            if(!(obj instanceof GameClient))
                return false;
            
            GameClient gc = (GameClient)obj;
            
            return gc.getPlayer() != null;
        }
    };

    public SessionHandler(WorldConfig config, PlayerDAO playerDAO, AccountDAO accountDAO) {
        this.config = config;
        this.playerDAO = playerDAO;
        this.accountDAO = accountDAO;
        startInactivityKick();
    }
    
    private void startInactivityKick(){
        service.scheduleAtFixedRate(() -> {
            for(DofusClient client : new ArrayList<>(clients)){
                if(client == null)
                    continue;
                
                if(client.isClosed()){
                    removeSession(client);
                    continue;
                }
                
                if(client.getLastPacketTime() + (config.getInactivityDelay() * 60 * 1000) < System.currentTimeMillis()){
                    client.send(new Message(false, 1, new String[]{}));
                    client.close();
                }
            }
        }, 1, 1, TimeUnit.MINUTES);
    }
    
    public boolean isLogged(String accountName){
        return getSessionByAccountName(accountName) != null;
    }
    
    public DofusClient getSessionByAccountName(String accountName){
        for (DofusClient client : clients) {
            if(!client.isClosed() && client.getAccount() != null && client.getAccount().getName().equalsIgnoreCase(accountName))
                return client;
        }
        
        return null;
    }
    
    public void addSession(DofusClient client){
        clients.add(client);
        
        if(clients.size() > maxClientCount)
            maxClientCount = clients.size();
    }
    
    public void removeSession(DofusClient client){
        clients.remove(client);
    }
    
    public void closeSession(DofusClient client){
        clients.remove(client);
        client.close();
    }
    
    public String addPendingAccount(Account account){
        String key = account.get_GUID() + "_" + StringUtil.generateRandString(32);
        pendingAccounts.put(key, account);
        return key;
    }
    
    public Account removePendingAccount(String key){
        return pendingAccounts.remove(key);
    }
    
    public Collection<RealmClient> getRealmClients(){
        Collection<RealmClient> realmClients = new ArrayList<>(100);
        
        for (DofusClient client : clients) {
            if(client instanceof RealmClient)
                realmClients.add((RealmClient) client);
        }
        
        return realmClients;
    }
    
    public int getClientCount(){
        return clients.size();
    }
    
    public boolean isFull(){
        return getClientCount() >= config.getMaxClients();
    }
    
    public Collection<GameClient> getGameClients(){
        Collection<GameClient> gameClients = new ArrayList<>(100);
        
        for (DofusClient client : clients) {
            if(client instanceof GameClient)
                gameClients.add((GameClient) client);
        }
        
        return gameClients;
    }
    
    public void saveAll(){
        Peak.worldLog.addToLog(Logger.Level.INFO, "Sauvegarde des personnages...");
        for(GameClient client : getGameClients()){
            if(client.getPlayer() != null)
                playerDAO.save(client.getPlayer(), true);
            
            if(client.getAccount() != null)
                accountDAO.save(client.getAccount());
        }
    }
    
    public Player searchPlayer(String name){
        for(GameClient client : getGameClients()){
            if(client.getPlayer() != null && client.getPlayer().getName().equalsIgnoreCase(name))
                return client.getPlayer();
        }
        
        return null;
    }
    
    public Collection<Player> getOnlinePlayers(){
        Collection<Player> players = new ArrayList<>();
        
        for(GameClient client : getGameClients()){
            if(client.getPlayer() != null)
                players.add(client.getPlayer());
        }
        
        return players;
    }
    
    public int getIpCount(String ip){
        int count = 0;
        
        for(DofusClient client : clients){
            if(client.getIP().equals(ip))
                ++count;
        }
        
        return count;
    }

    public int getMaxIpPerClient() {
        return config.getMaxIpPerClient();
    }
    
    public void sendToAll(Object packet, Matcher<DofusClient> matcher){
        String str = packet.toString();
        
        for(DofusClient client : clients){
            if(matcher.match(client))
                client.send(str);
        }
    }
    
    public void sendToAll(Object packet){
        sendToAll(packet, Matcher.ALLOW_ALL);
    }
    
    public void sendToGame(Object packet){
        sendToAll(packet, GAME_CLIENT_MATCHER);
    }
    
    public void sendToRealm(Object packet){
        sendToAll(packet, REALM_CLIENT_MATCHER);
    }
    
    public void sendToOnline(Object packet){
        sendToAll(packet, ONLINE_CLIENT_MATCHER);
    }

    public int getMaxClientCount() {
        return maxClientCount;
    }
    
    public void kickAll(){
        Collection<DofusClient> copy = new ArrayList<>(clients);
        clients.clear();
        
        for(DofusClient client : copy)
            client.close();
    }
}
