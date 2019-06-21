/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.objects.player;

import java.util.ArrayList;
import java.util.Collection;
import org.peakemu.game.out.exchange.PlayerShopUpdated;
import org.peakemu.game.out.game.SpriteParser;
import org.peakemu.objects.item.Item;
import org.peakemu.world.MapCell;
import org.peakemu.world.Sprite;
import org.peakemu.world.enums.SpriteTypeEnum;
import org.peakemu.world.exchange.PlayerStoreBuyExchange;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class Seller implements Sprite{
    final private Player player;
    
    final private Collection<PlayerStoreBuyExchange> exchanges = new ArrayList<>();

    public Seller(Player player) {
        this.player = player;
    }

    @Override
    public SpriteTypeEnum getSpriteType() {
        return SpriteTypeEnum.OFFLINE_CHARACTER;
    }

    @Override
    public String getSpritePacket() {
        StringBuilder str = new StringBuilder();
        str.append(player.getCell().getID()).append(";");
        str.append(player.getOrientation()).append(";");
        str.append("0").append(";");
        str.append(getSpriteId()).append(";");
        str.append(player.getName()).append(";");
        str.append(getSpriteType().toInt()).append(";");//Merchant identifier
        str.append(player.getGfxID()).append("^").append(player.getSize()).append(";");
        str.append((player.getColor1() == -1 ? "-1" : Integer.toHexString(player.getColor1()))).append(";");
        str.append((player.getColor2() == -1 ? "-1" : Integer.toHexString(player.getColor2()))).append(";");
        str.append((player.getColor3() == -1 ? "-1" : Integer.toHexString(player.getColor3()))).append(";");
        str.append(SpriteParser.stuffToString(player.getItems())).append(";");//acessories
        str.append((player.getGuild() != null ? player.getGuild().getName() : "")).append(";");//guildName
        str.append((player.getGuild() != null ? player.getGuild().getEmbem() : "")).append(";");//emblem
        str.append("0;");//offlineType

        return str.toString();
    }

    @Override
    public int getSpriteId() {
        return player.getId();
    }
    
    
    public PlayerStore getStore(){
        return player.getStore();
    }
    
    public PlayerStoreBuyExchange startExchange(Player buyer){
        PlayerStoreBuyExchange exchange = new PlayerStoreBuyExchange(this, buyer);
        
        synchronized(exchanges){
            exchanges.add(exchange);
        }
        
        return exchange;
    }
    
    public void endExchange(PlayerStoreBuyExchange exchange){
        synchronized(exchanges){
            exchanges.remove(exchange);
        }
    }
    
    public void endAllExchanges(){
        synchronized(exchanges){
            for(PlayerStoreBuyExchange exchange : new ArrayList<>(exchanges)){
                exchange.cancel();
            }
        }
    }
    
    public void sellItem(Item item, int qte){
        long price = getStore().getPrice(item);
        long total = price * qte;
        getStore().removeItem(item, qte);
        player.addKamas(total);
        
        if(getStore().isEmpty()){
            player.unsetSeller();
            return;
        }
        
        if(item.getQuantity() > 0){
            sendToClients(new PlayerShopUpdated(PlayerShopUpdated.ACTION_ADD, item, price));
        }else{
            sendToClients(new PlayerShopUpdated(PlayerShopUpdated.ACTION_REMOVE, item, price));
        }
    }
    
    private void sendToClients(Object packet){
        for(PlayerStoreBuyExchange exchange : exchanges)
            exchange.getBuyer().send(packet);
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public MapCell getCell() {
        return player.getCell();
    }
}
