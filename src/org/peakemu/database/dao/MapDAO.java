/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.database.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.peakemu.Peak;
import org.peakemu.common.util.StringUtil;
import org.peakemu.database.Database;
import org.peakemu.database.parser.MapParser;
import org.peakemu.database.parser.MonsterParser;
import org.peakemu.world.GameMap;
import org.peakemu.world.Point;
import org.peakemu.world.SubArea;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class MapDAO {
    final private Database database;
    final private SubAreaDAO subAreaDAO;
    final private IOTemplateDAO ioTemplateDAO;
    final private TriggerDAO triggerDAO;
    final private MonsterDAO monsterDAO;
    
    final private Map<Short, GameMap> maps = new ConcurrentHashMap<>();

    public MapDAO(Database database, SubAreaDAO subAreaDAO, IOTemplateDAO ioTemplateDAO, TriggerDAO triggerDAO, MonsterDAO monsterDAO) {
        this.database = database;
        this.subAreaDAO = subAreaDAO;
        this.ioTemplateDAO = ioTemplateDAO;
        this.triggerDAO = triggerDAO;
        this.monsterDAO = monsterDAO;
    }
    
    private GameMap createByRS(ResultSet RS) throws SQLException{
        String[] pos = StringUtil.split(RS.getString("mappos"), ",", 3);
        Point position = new Point(Integer.parseInt(pos[0]), Integer.parseInt(pos[1]));
        SubArea subArea = subAreaDAO.getSubAreaById(Integer.parseInt(pos[2]));
        
        GameMap map = new GameMap(
            RS.getShort("id"),
            RS.getString("date"),
            RS.getByte("width"),
            RS.getByte("heigth"),
            RS.getString("key"),
            RS.getString("places"),
            MonsterParser.parseMapMonsterList(monsterDAO, RS.getString("monsters")),
            position,
            subArea,
            RS.getByte("numgroup"),
            RS.getByte("groupmaxsize"),
            triggerDAO.getTriggersByMap(RS.getShort("id"))
        );
        
        MapParser.parseCells(map, RS.getString("mapData"), RS.getString("mapData"), ioTemplateDAO);
        
        maps.put(map.getId(), map);
        
        if(subArea != null)
            subArea.addMap(map);
        
        return map;
    }

    public Collection<GameMap> getAll() {
        if(!maps.isEmpty())
            return maps.values();
        
        ResultSet RS = null;
        try {
            RS = database.query("SELECT * from maps");
            
            while (RS.next()) {
                createByRS(RS);
            }
        } catch (SQLException e) {
            Peak.errorLog.addToLog(e);
            System.exit(1);
        }finally{
            try{
                RS.close();
            }catch(Exception e){}
        }
        
        return maps.values();
    }
    
    public GameMap getMapById(short id){
        if(maps.containsKey(id))
            return maps.get(id);
        
        try(PreparedStatement stmt = database.prepare("SELECT * FROM maps WHERE id = ?")){
            stmt.setInt(1, id);
            try(ResultSet RS = stmt.executeQuery()){
                if(RS.next())
                    return createByRS(RS);
            }
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
        
        return null;
    }
}
