/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.database.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.peakemu.Peak;
import org.peakemu.common.Logger;
import org.peakemu.database.Database;
import org.peakemu.objects.NPC;
import org.peakemu.world.GameMap;
import org.peakemu.world.MapCell;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class NpcDAO {
    final private Database database;
    final private MapDAO mapDAO;
    final private NPCTemplateDAO npcTemplateDAO;

    public NpcDAO(Database database, MapDAO mapDAO, NPCTemplateDAO npcTemplateDAO) {
        this.database = database;
        this.mapDAO = mapDAO;
        this.npcTemplateDAO = npcTemplateDAO;
    }
    
    public int load(){
        int nb = 0;
        try(ResultSet RS = database.query("SELECT * FROM npcs")){
            while(RS.next()){
                ++nb;
                GameMap map = mapDAO.getMapById(RS.getShort("mapid"));
                MapCell cell = map.getCell(RS.getInt("cellid"));
                
                if(cell == null){
                    Peak.worldLog.addToLog(Logger.Level.ERROR, "Invalid NPC position : map = %d, cell = %d", RS.getShort("mapid"), RS.getInt("cellid"));
                    continue;
                }
                
                NPC npc = new NPC(npcTemplateDAO.getTemplateById(RS.getInt("npcid")), cell, RS.getByte("orientation"));
                npc.setSpriteId(map.getNextSpriteId());
                map.addSprite(npc);
            }
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
        return nb;
    }
    
    public boolean insert(NPC npc){
        try(PreparedStatement stmt = database.prepare("INSERT INTO npcs(mapid, cellid, npcid, orientation) VALUES(?,?,?,?)")){
            stmt.setShort(1, npc.getCell().getMap().getId());
            stmt.setInt(2, npc.getCell().getID());
            stmt.setInt(3, npc.getTemplate().get_id());
            stmt.setInt(4, npc.get_orientation());
            stmt.execute();
            return true;
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
            return false;
        }
    }
    
    public boolean delete(NPC npc){
        try(PreparedStatement stmt = database.prepare("DELETE FROM npcs WHERE mapid = ? AND cellid = ?")){
            stmt.setShort(1, npc.getCell().getMap().getId());
            stmt.setInt(2, npc.getCell().getID());
            stmt.execute();
            return true;
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
            return false;
        }
    }
}
