/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.peakemu.Peak;
import org.peakemu.common.Logger;
import org.peakemu.common.util.Util;
import org.peakemu.database.dao.FixedMonsterGroupDAO;
import org.peakemu.game.in.gameaction.Move;
import org.peakemu.game.out.game.GameActionResponse;
import org.peakemu.maputil.Compressor;
import org.peakemu.maputil.MapUtil;
import org.peakemu.maputil.PathException;
import org.peakemu.maputil.Pathfinding;
import org.peakemu.world.FixedMonsterGroup;
import org.peakemu.world.GameMap;
import org.peakemu.world.InteractiveObject;
import org.peakemu.world.config.LifeConfig;
import org.peakemu.world.MapCell;
import org.peakemu.world.MoveableSprite;
import org.peakemu.world.Sprite;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class LifeHandler {
    final private ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
    final private LifeConfig config;
    final private MapHandler mapHandler;
    final private MonsterHandler monsterHandler;
    final private FixedMonsterGroupDAO fixedMonsterGroupDAO;

    public LifeHandler(LifeConfig config, MapHandler mapHandler, MonsterHandler monsterHandler, FixedMonsterGroupDAO fixedMonsterGroupDAO) {
        this.config = config;
        this.mapHandler = mapHandler;
        this.monsterHandler = monsterHandler;
        this.fixedMonsterGroupDAO = fixedMonsterGroupDAO;
    }
    
    public void initSpwanMonsters(){
        System.out.print("spawn des monstres : ");
        int nb = 0;
        
        try{
            for(GameMap map : mapHandler.getMaps()){
                if(map.getCells().isEmpty())
                    continue;

                for(int i = 0; i < map.getMaxGroupNumb(); ++i){
                    monsterHandler.spawnMonsterGroupOnMap(map);
                }
                nb += map.getMaxGroupNumb();
            }
        }catch(Exception e){
            Peak.errorLog.addToLog(e);
        }
        
        System.out.println(nb + " monstres spawn");
        
        service.scheduleAtFixedRate(() -> {
            Peak.worldLog.addToLog(Logger.Level.DEBUG, "Respawn monsters");
            try{
                for(GameMap map : mapHandler.getMaps()){
                    if(map.getCells().isEmpty())
                        continue;
                    
                    monsterHandler.spawnMonsterGroupOnMap(map);
                }
            }catch(Exception e){
                Peak.errorLog.addToLog(e);
            }
        }, config.getSpawnMonstersDelay(), config.getSpawnMonstersDelay(), TimeUnit.SECONDS);
        
        service.scheduleAtFixedRate(() -> {
            Peak.worldLog.addToLog(Logger.Level.DEBUG, "Moving sprites");
            
            try{
                for(GameMap map : mapHandler.getMaps()){
                    moveRandomSprites(map);
                }
            }catch(Exception e){
                Peak.errorLog.addToLog(e);
            }
        }, config.getMoveSpritesDelay(), config.getMoveSpritesDelay(), TimeUnit.SECONDS);
    }
    
    public void initRefreshIO(){
        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                for(GameMap map : mapHandler.getMaps()){
                    for(MapCell cell : map.getCells()){
                        if(!cell.isInteractive())
                            continue;
                        
                        InteractiveObject io = cell.getObject();
                        
                        if(io.getStateChangeTime() + io.getRespawnDelay() < System.currentTimeMillis()){
                            io.respawn();
                        }
                    }
                }
            }
        }, config.getRefreshInteractiveObjectsDelay(), config.getRefreshInteractiveObjectsDelay(), TimeUnit.SECONDS);
    }
    
    public void moveRandomSprites(GameMap map){
        List<MoveableSprite> sprites = new ArrayList<>();
        
        for(Sprite sprite : map.getNonPlayerSprites()){
            if(sprite instanceof MoveableSprite){
                MoveableSprite moveableSprite = (MoveableSprite) sprite;
                
                if(moveableSprite.canMove())
                    sprites.add(moveableSprite);
            }
        }
        
        if(sprites.isEmpty())
            return;
        
        MoveableSprite sprite = Util.rand(sprites);
        MapCell freeCell = MapUtil.getRandomFreeCell(map, MapUtil.getCellsAround(sprite.getCell(), config.getMoveSpritesDistance()));
        
        if(freeCell == null)
            return;
        
        String strPath;
        
        try{
            List<MapCell> path = Pathfinding.findPath(map, sprite.getCell(), freeCell, false);
            strPath = Compressor.compressPath(path);
        }catch(PathException e){
            return;
        }
        
        if(strPath == null)
            return;
        
        sprite.setCell(freeCell);
        map.sendToMap(new GameActionResponse(Move.ACTION_ID, sprite.getSpriteId(), strPath));
    }
    
    public void initFixedGroups(){
        System.out.print("Chargement des groupes fixes : ");
        int nb = 0;
        
        for(FixedMonsterGroup fmg : fixedMonsterGroupDAO.getAll()){
            fmg.respawn();
            ++nb;
        }
        System.out.println(nb + " groupes chargés");
        
        service.scheduleAtFixedRate(() -> {
            try{
                for(FixedMonsterGroup fmg : fixedMonsterGroupDAO.getAll()){
                    fmg.respawn();
                }
            }catch(Exception e){
                Peak.errorLog.addToLog(e);
            }
        }, config.getRespawnFixedMonstersDelay(), config.getRespawnFixedMonstersDelay(), TimeUnit.SECONDS);
    }
    
    public void stop(){
        service.shutdownNow();
    }
}
