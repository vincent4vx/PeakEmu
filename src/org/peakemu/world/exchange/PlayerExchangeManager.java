/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.exchange;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.peakemu.common.SocketManager;
import org.peakemu.game.GameClient;
import org.peakemu.game.out.exchange.ExchangeDistantMove;
import org.peakemu.game.out.exchange.ExchangeLeaved;
import org.peakemu.game.out.exchange.ExchangeMoveError;
import org.peakemu.game.out.exchange.ExchangeMoved;
import org.peakemu.game.out.exchange.ExchangeValidated;
import org.peakemu.objects.item.Item;
import org.peakemu.objects.player.Player;
import org.peakemu.world.enums.ExchangeType;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class PlayerExchangeManager{
    final private Collection<PlayerExchange> exchanges = new ArrayList<>(2);
    private boolean validated = false;
    
    private void sendValidateExchange(){
        for(PlayerExchange e1 : exchanges){
            GameClient out = e1.player.getAccount().getGameThread();
            
            for(PlayerExchange e2 : exchanges){
                out.send(new ExchangeValidated(e2.player.getSpriteId(), e2.validated));
            }
        }
    }
    
    private void unvalidateAll(){
        for(PlayerExchange e : exchanges)
            e.validated = false;
        
        validated = false;
        sendValidateExchange();
    }
    
    private void checkValidate(){
        //check all validated
        for(PlayerExchange pe : exchanges){
            if(!pe.validated)
                return;
        }
        
        //perform exchange
        for(PlayerExchange sender : exchanges){
            for(PlayerExchange target : exchanges){
                if(sender == target)
                    continue;
                
                //items exchange
                for(Map.Entry<Item, Integer> entry : sender.pendingItems.entrySet()){
                    if(entry.getValue() <= 0)
                        continue;
                    
                    sender.player.getItems().changeQuantity(entry.getKey(), entry.getKey().getQuantity() - entry.getValue());
                    Item newItem = entry.getKey().cloneItem(entry.getValue());
                    target.player.getItems().addItem(newItem);
                }
                
                //kamas exchange
                sender.player.setKamas(sender.player.getKamas() - sender.kamas);
                target.player.setKamas(target.player.getKamas() + sender.kamas);
            }
        }
        
        validated = true;
        
        //send packets
        for(PlayerExchange pe : exchanges){
            Player player = pe.player;
            player.setCurExchange(null);
            player.setRequest(null);
            SocketManager.GAME_SEND_Ow_PACKET(player);
            SocketManager.GAME_SEND_STATS_PACKET(player);
            player.getAccount().getGameThread().send(new ExchangeLeaved(true));
        }
    }
    
    public void cancelAll(){
        for (PlayerExchange exchange : exchanges) {
            exchange.player.setCurExchange(null);
            exchange.validated = false;
            exchange.pendingItems.clear();
            exchange.player.send(new ExchangeLeaved());
        }
        
        validated = false;
    }
    
    public class PlayerExchange implements ValidatableExchange{
        final private Player player;
        
        private long kamas;
        final private Map<Item, Integer> pendingItems = new HashMap<>();
        private boolean validated = false;

        public PlayerExchange(Player player) {
            this.player = player;
            exchanges.add(this);
        }

        @Override
        public void addKamas(long qte) {
            if(qte > player.getKamas())
                qte = player.getKamas();
            
            kamas = qte;
            unvalidateAll();
            
            for(PlayerExchange pe : exchanges){
                GameClient out = pe.player.getAccount().getGameThread();
                
                if(pe.player == player)
                    out.send(new ExchangeMoved(kamas));
                else
                    out.send(new ExchangeDistantMove(kamas));
            }
        }

        @Override
        public void removeKamas(long qte) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void addItem(int itemId, int qte) {
            Item item = player.getItems().get(itemId);
            if(item == null){
                player.getAccount().getGameThread().send(new ExchangeMoveError());
                return;
            }
            
            unvalidateAll();
            
            int totalQte = pendingItems.getOrDefault(item, 0) + qte;
            
            if(totalQte > item.getQuantity())
                totalQte = item.getQuantity();
            
            pendingItems.put(item, totalQte);
            sendItemMoved(item, totalQte);
        }

        @Override
        public void removeItem(int itemId, int qte) {
            Item item = player.getItems().get(itemId);
            if(item == null){
                player.getAccount().getGameThread().send(new ExchangeMoveError());
                return;
            }
            
            unvalidateAll();
            
            int totalQte = pendingItems.getOrDefault(item, 0) - qte;
            
            if(totalQte < 0){
                totalQte = 0;
            }
            
            pendingItems.put(item, totalQte);
            sendItemMoved(item, totalQte);
        }
        
        private void sendItemMoved(Item item, int qte){
            for(PlayerExchange pe : exchanges){
                GameClient out = pe.player.getAccount().getGameThread();
                
                if(pe.player == player)
                    out.send(new ExchangeMoved(item, qte));
                else
                    out.send(new ExchangeDistantMove(item, qte));
            }
        }

        @Override
        public void toogleValidate() {
            validated = !validated;
            sendValidateExchange();
            checkValidate();
        }

        @Override
        public void cancel() {
            cancelAll();
        }

        @Override
        public ExchangeType getType() {
            return ExchangeType.PLAYER_EXCHANGE;
        }

        @Override
        public boolean isValidated() {
            return validated;
        }
        
        public Collection<Player> getPlayers(){
            Collection<Player> players = new ArrayList<>(2);
            
            for(PlayerExchange exchange : exchanges)
                players.add(exchange.player);
            
            return players;
        }
    }
    
    public ValidatableExchange createExchange(Player player){
        return new PlayerExchange(player);
    }
}
