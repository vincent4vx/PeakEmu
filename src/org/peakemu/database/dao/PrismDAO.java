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
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.peakemu.Peak;
import org.peakemu.database.Database;
import org.peakemu.database.parser.SpellParser;
import org.peakemu.objects.Prism;
import org.peakemu.world.GameMap;
import org.peakemu.world.MapCell;
import org.peakemu.world.SpellLevel;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class PrismDAO {
    final private Database database;
    final private MapDAO mapDAO;
    final private SpellDAO spellDAO;
    
    final private Map<Integer, Prism> prisms = new ConcurrentHashMap<>();
    private Map<Integer, SpellLevel> spells = null;

    public PrismDAO(Database database, MapDAO mapDAO, SpellDAO spellDAO) {
        this.database = database;
        this.mapDAO = mapDAO;
        this.spellDAO = spellDAO;
    }
    
    private Prism createByRS(ResultSet RS) throws SQLException{
        GameMap map = mapDAO.getMapById(RS.getShort("map"));
        MapCell cell = map.getCell(RS.getInt("cell"));
        
        Prism prism = new Prism(
            RS.getInt("id"),
            RS.getInt("alignement"),
            RS.getInt("level"),
            mapDAO.getMapById(RS.getShort("map")),
            cell,
            RS.getInt("honor"),
            RS.getBoolean("isOnArea"),
            getSpells()
        );
        
        prisms.put(prism.getPrismUniqueId(), prism);
        
        return prism;
    }
    
    public Map<Integer, SpellLevel> getSpells(){
        if(spells != null)
            return spells;
        
        spells = Collections.unmodifiableMap(SpellParser.parseMonsterSpells(spellDAO, Prism.SPELLS));
        return spells;
    }
    
    public Collection<Prism> getAll(){
        if(!prisms.isEmpty())
            return prisms.values();
        
        try(ResultSet RS = database.query("SELECT * FROM prismes")){
            while(RS.next())
                createByRS(RS);
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
        
        return prisms.values();
    }
    
    public Prism insert(Prism prism){
        String baseQuery = "INSERT INTO prismes(`alignement`,`level`,`map`,`cell`, `honor`, `isOnArea`) VALUES(?,?,?,?,?,?)";
        
        try(PreparedStatement stmt = database.prepareInsert(baseQuery)){
            int i = 0;
            stmt.setInt(++i, prism.getAlignement());
            stmt.setInt(++i, prism.getLevel());
            stmt.setInt(++i, prism.getMap().getId());
            stmt.setInt(++i, prism.getCell().getID());
            stmt.setInt(++i, prism.getHonor());
            stmt.setBoolean(++i, prism.isOnArea());
            stmt.executeUpdate();
            
            try(ResultSet RS = stmt.getGeneratedKeys()){
                if(RS.next()){
                    prism = prism.copy(RS.getInt(1));
                    prisms.put(prism.getPrismUniqueId(), prism);
                    return prism;
                }
            }
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
        
        return null;
    }
    
    public void save(Prism prism){
        String baseQuery = "REPLACE INTO prismes(`id`,`alignement`,`level`,`map`,`cell`, `honor`, `isOnArea`) VALUES(?,?,?,?,?,?,?)";
        
        try(PreparedStatement stmt = database.prepare(baseQuery)){
            int i = 0;
            stmt.setInt(++i, prism.getPrismUniqueId());
            stmt.setInt(++i, prism.getAlignement());
            stmt.setInt(++i, prism.getLevel());
            stmt.setInt(++i, prism.getMap().getId());
            stmt.setInt(++i, prism.getCell().getID());
            stmt.setInt(++i, prism.getHonor());
            stmt.setBoolean(++i, prism.isOnArea());
            stmt.executeUpdate();
            
            prisms.put(prism.getPrismUniqueId(), prism);
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
    }
        
    public void saveAll(){
        String baseQuery = "REPLACE INTO prismes(`id`,`alignement`,`level`,`map`,`cell`, `honor`, `isOnArea`) VALUES(?,?,?,?,?,?,?);";
        
        try(PreparedStatement stmt = database.prepare(baseQuery)){
            for(Prism prism : new ArrayList<>(getAll())){
                int i = 0;
                stmt.setInt(++i, prism.getPrismUniqueId());
                stmt.setInt(++i, prism.getAlignement());
                stmt.setInt(++i, prism.getLevel());
                stmt.setInt(++i, prism.getMap().getId());
                stmt.setInt(++i, prism.getCell().getID());
                stmt.setInt(++i, prism.getHonor());
                stmt.setBoolean(++i, prism.isOnArea());
                stmt.executeUpdate();

                prisms.put(prism.getPrismUniqueId(), prism);
            }
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
    }
    
    public void delete(Prism prism){
        prisms.remove(prism.getPrismUniqueId());
        
        try(PreparedStatement stmt = database.prepare("DELETE FROM prismes WHERE id = ?")){
            stmt.setInt(1, prism.getPrismUniqueId());
            stmt.executeUpdate();
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
    }
}
