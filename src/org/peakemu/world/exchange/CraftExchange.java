/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.exchange;

import org.peakemu.game.out.exchange.CraftLoopProgress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.peakemu.common.Constants;
import org.peakemu.common.util.Util;
import org.peakemu.game.out.InfoMessage;
import org.peakemu.game.out.exchange.CraftError;
import org.peakemu.game.out.exchange.CraftLoopEnd;
import org.peakemu.game.out.exchange.CraftSuccess;
import org.peakemu.game.out.exchange.ExchangeDistantMove;
import org.peakemu.game.out.exchange.ExchangeLeaved;
import org.peakemu.game.out.exchange.ExchangeMoved;
import org.peakemu.game.out.game.InteractiveObjectsState;
import org.peakemu.game.out.info.CraftedObject;
import org.peakemu.objects.item.Item;
import org.peakemu.objects.player.JobStats;
import org.peakemu.objects.player.Player;
import org.peakemu.world.Craft;
import org.peakemu.world.JobSkill;
import org.peakemu.world.MapCell;
import org.peakemu.world.enums.ExchangeType;
import org.peakemu.world.enums.IOState;
import org.peakemu.world.handler.ItemHandler;
import org.peakemu.world.handler.JobHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class CraftExchange implements ExchangeItem{
    final private ItemHandler itemHandler;
    final private JobHandler jobHandler;
    final private Player player;
    final private JobStats jobStats;
    final private JobSkill skill;
    final private MapCell cell;
    
    final private Map<Item, Integer> ingredients = new HashMap<>();
    final private Map<Item, Integer> last = new HashMap<>();
    
    final static public int CRAFT_LOOP_END_OK = 1;
    final static public int CRAFT_LOOP_END_INTERRUPT = 2;
    final static public int CRAFT_LOOP_END_FAIL = 3;
    final static public int CRAFT_LOOP_END_INVALID = 4;
    
    private boolean onCraftLoop = false;

    public CraftExchange(ItemHandler itemHandler, JobHandler jobHandler, Player player, JobStats jobStats, JobSkill skill, MapCell cell) {
        this.itemHandler = itemHandler;
        this.jobHandler = jobHandler;
        this.player = player;
        this.jobStats = jobStats;
        this.skill = skill;
        this.cell = cell;
    }

    @Override
    public void addItem(int itemId, int qte) {
        Item item = player.getItems().get(itemId);
        
        if(item == null)
            return;
        
        qte = ingredients.getOrDefault(item, 0) + qte;
        
        if(qte > item.getQuantity())
            qte = item.getQuantity();
        
        if(qte < 0)
            ingredients.remove(item);
        else
            ingredients.put(item, qte);
        
        player.send(new ExchangeMoved(item, qte));
    }

    @Override
    public void removeItem(int itemId, int qte) {
        Item item = player.getItems().get(itemId);
        
        if(item == null)
            return;
        
        qte = ingredients.getOrDefault(item, 0) - qte;
        
        if(qte > item.getQuantity())
            qte = item.getQuantity();
        
        if(qte < 0)
            ingredients.remove(item);
        else
            ingredients.put(item, qte);
        
        player.send(new ExchangeMoved(item, qte));
    }

    @Override
    public void cancel() {
        if(onCraftLoop)
            onCraftLoop = false;
        
        player.send(new ExchangeLeaved());
        cell.getObject().setState(IOState.FULL);
        cell.getMap().sendToMap(new InteractiveObjectsState(cell));
    }

    @Override
    public ExchangeType getType() {
        return ExchangeType.FACTORY_TABLE;
    }
    
    private Craft getCorrespondingCraft(){
        for(Craft craft : skill.getCrafts()){
            if(craft.corresponds(ingredients))
                return craft;
        }
        return null;
    }
    
    private boolean checkIngredients(){
        Iterator<Map.Entry<Item, Integer>> it = ingredients.entrySet().iterator();
        
        while(it.hasNext()){
            Map.Entry<Item, Integer> entry = it.next();
            
            if(entry.getValue() <= 0){
                it.remove();
                continue;
            }
            
            if(entry.getKey().getQuantity() < entry.getValue())
                return false;
        }
        
        return true;
    }
    
    public int doCraft(){
        if(!checkIngredients())
            return CRAFT_LOOP_END_FAIL;
        
        for(Map.Entry<Item, Integer> entry : ingredients.entrySet()){
            player.getItems().changeQuantity(entry.getKey(), entry.getKey().getQuantity() - entry.getValue());
        }
        
        try{
            Thread.sleep(500);
        }catch(InterruptedException e){}
        
        if(ingredients.size() > jobHandler.getMaxCraftIngredients(jobStats)){
            player.send(new CraftError(CraftError.NO_CRAFT_RESULT));
            return CRAFT_LOOP_END_INVALID;
        }
        
        Craft craft = getCorrespondingCraft();
        
        if(craft == null){
            player.send(new CraftError(CraftError.NO_CRAFT_RESULT));
            return CRAFT_LOOP_END_INVALID;
        }
        
        int chance = Constants.getChanceByNbrCaseByLvl(jobStats.getLevel(), craft.getCraftCases());
        boolean success = Util.randBool(chance);
        
        if(success){
            Item item = itemHandler.createNewItem(craft.getItem(), 1, false);
            item = player.getItems().addItem(item);
            
            player.send(new ExchangeDistantMove(item, 1));
            player.send(new CraftSuccess(craft.getItem()));
            long xp = jobHandler.getCraftWinXp(jobStats, craft);
            jobHandler.addXp(player, jobStats, xp);
        }else{
            player.send(new CraftError(CraftError.CRAFT_FAILED));
            player.send(new InfoMessage(InfoMessage.Type.INFO).addMessage(118));
        }
        
        player.getMap().sendToMap(new CraftedObject(player, craft.getItem(), success));
        
        last.clear();
        last.putAll(ingredients);
        ingredients.clear();
        return CRAFT_LOOP_END_OK;
    }
    
    public int redoCraft(){
        ingredients.putAll(last);
        return doCraft();
    }
    
    public void craftLoop(int count){
        onCraftLoop = true;
        int state = CRAFT_LOOP_END_OK;
        
        for(;count > 0; --count){
            if(!onCraftLoop){
                state = CRAFT_LOOP_END_INTERRUPT;
                break;
            }
                
            player.send(new CraftLoopProgress(count));
            state = redoCraft();
            
            if(state != CRAFT_LOOP_END_OK)
                break;
        }
        
        player.send(new CraftLoopEnd(state));
        
        onCraftLoop = false;
    }
    
    public void endCraftLoop(){
        onCraftLoop = false;
    }

    public boolean isOnCraftLoop() {
        return onCraftLoop;
    }
    
    public void putLastIngredients(){
        ingredients.clear();
        
        for(Map.Entry<Item, Integer> entry : last.entrySet()){
            ingredients.put(entry.getKey(), entry.getValue());
            player.send(new ExchangeMoved(entry.getKey(), entry.getValue()));
        }
    }
}
