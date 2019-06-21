/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.database.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import org.peakemu.Peak;
import org.peakemu.common.Logger;
import org.peakemu.database.Database;
import org.peakemu.database.parser.MonsterParser;
import org.peakemu.world.FixedMonsterGroup;
import org.peakemu.world.GameMap;
import org.peakemu.world.MapCell;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class FixedMonsterGroupDAO {
    final private Database database;
    final private MapDAO mapDAO;
    final private MonsterDAO monsterDAO;
    
    final private Collection<FixedMonsterGroup> fixedMonsterGroups = new ArrayList<>();

    public FixedMonsterGroupDAO(Database database, MapDAO mapDAO, MonsterDAO monsterDAO) {
        this.database = database;
        this.mapDAO = mapDAO;
        this.monsterDAO = monsterDAO;
    }
    
    public Collection<FixedMonsterGroup> getAll(){
        if(!fixedMonsterGroups.isEmpty())
            return fixedMonsterGroups;
        
        try(ResultSet RS = database.query("SELECT * FROM mobgroups_fix")){
            while(RS.next()){
                GameMap map = mapDAO.getMapById(RS.getShort("mapid"));
                MapCell cell = map.getCell(RS.getInt("cellid"));

                if(cell == null){
                    Peak.worldLog.addToLog(Logger.Level.ERROR, "mobgroups_fix : Invalid cell %d on map %d", RS.getInt("cellid"), map.getId());
                    continue;
                }

                int respawn = RS.getInt("Timer");
                Collection<FixedMonsterGroup.MonsterEntry> entries = MonsterParser.parseMonsterEntries(monsterDAO, RS.getString("groupData"));
                
                if(entries.isEmpty()){
                    Peak.errorLog.addToLog(Logger.Level.ERROR, "mobgroup_fix : Invalid group data (empty) on map %d and cell %d", map.getId(), cell.getID());
                    continue;
                }
                
                fixedMonsterGroups.add(new FixedMonsterGroup(map, cell, respawn, entries));
            }
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
        
        return fixedMonsterGroups;
    }
    
    public boolean insert(FixedMonsterGroup fmg){
        try(PreparedStatement stmt = database.prepare("INSERT INTO mobgroup_fix(mapid, cellid, groupData, Timer) VALUES(?,?,?,?)")){
            stmt.setShort(1, fmg.getMap().getId());
            stmt.setInt(2, fmg.getCell().getID());
            stmt.setString(3, MonsterParser.monsterEntriesToString(fmg.getMonsters()));
            stmt.setInt(4, fmg.getRespawnTime());
            stmt.execute();
            
            fixedMonsterGroups.add(fmg);
            return true;
        }catch(SQLException e){
            return false;
        }
    }
}
