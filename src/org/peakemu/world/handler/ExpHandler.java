/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.handler;

import org.peakemu.database.dao.ExpDAO;
import org.peakemu.world.ExpLevel;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class ExpHandler {
    final private ExpDAO expDAO;
    
    private int maxPlayerLevel = 0;
    private int maxMountLevel = 0;
    private int maxPvpLevel = 0;
    private int maxJobLevel = 0;
    private int maxGuildLevel = 0;
    private int maxGlobalLevel = 0;

    public ExpHandler(ExpDAO expDAO) {
        this.expDAO = expDAO;
    }
    
    public void load(){
        System.out.print("Chargement des niveaux : ");
        
        for(ExpLevel level : expDAO.getAll()){
            if(level.player != -1)
                maxPlayerLevel = level.level;
            
            if(level.job != -1)
                maxJobLevel = level.level;
            
            if(level.mount != -1)
                maxMountLevel = level.level;
            
            if(level.pvp != -1)
                maxPvpLevel = level.level;
            
            if(level.guild != -1)
                maxGuildLevel = level.level;
            
            maxGlobalLevel = level.level;
        }
        
        System.out.println(maxPlayerLevel + " niveaux chargés");
    }

    public int getMaxPlayerLevel() {
        return maxPlayerLevel;
    }

    public int getMaxMountLevel() {
        return maxMountLevel;
    }

    public int getMaxPvpLevel() {
        return maxPvpLevel;
    }

    public int getMaxJobLevel() {
        return maxJobLevel;
    }

    public int getMaxGuildLevel() {
        return maxGuildLevel;
    }
    
    public int getMaxGlobalLevel(){
        return maxGlobalLevel;
    }

    public ExpLevel getLevel(int level) {
        if(level < 1)
            level = 1;
        
        if(level > maxGlobalLevel)
            level = maxGlobalLevel;
        
        return expDAO.getLevel(level);
    }
    
    public ExpLevel getRealPlayerLevel(ExpLevel curLevel, long xp){
        while(curLevel.getNext() != null && xp >= curLevel.getNext().player){
            ExpLevel nextLevel = curLevel.getNext();
            
            if(nextLevel.player == -1)
                break;
            
            curLevel = nextLevel;
        }
        
        return curLevel;
    }
    
    public int getRealPvpLevel(int honour){
        for(ExpLevel level : expDAO.getAll()){
            if(level.pvp == -1 || honour < level.pvp)
                return level.level - 1;
        }
        
        return getMaxPvpLevel();
    }
}
