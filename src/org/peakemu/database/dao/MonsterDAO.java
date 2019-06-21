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
import java.util.HashMap;
import java.util.Map;
import org.peakemu.Peak;
import org.peakemu.common.Logger;
import org.peakemu.database.Database;
import org.peakemu.database.parser.MonsterParser;
import org.peakemu.world.MonsterTemplate;
import org.peakemu.world.World;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class MonsterDAO {
    final private Database database;
    final private SpellDAO spellDAO;
    
    final private Map<Integer, MonsterTemplate> templates = new HashMap<>();

    public MonsterDAO(Database database, SpellDAO spellDAO) {
        this.database = database;
        this.spellDAO = spellDAO;
    }
    
    private MonsterTemplate createByRS(ResultSet RS) throws SQLException{
        int id = RS.getInt("id");
        int gfxID = RS.getInt("gfxID");
        int align = RS.getInt("align");
        String colors = RS.getString("colors");
        String grades = RS.getString("grades");
        String spells = RS.getString("spells");
        String stats = RS.getString("stats");
        String pdvs = RS.getString("pdvs");
        String pts = RS.getString("points");
        String inits = RS.getString("inits");
        int mK = RS.getInt("minKamas");
        int MK = RS.getInt("maxKamas");
        int IAType = RS.getInt("AI_Type");
        String xp = RS.getString("exps");
        boolean capturable = RS.getBoolean("capturable");

        MonsterTemplate tpl = new MonsterTemplate(
            id,
            gfxID,
            align,
            colors,
            mK,
            MK,
            xp,
            IAType,
            capturable
        );
        
        MonsterParser.setMonsterGrades(spellDAO, tpl, grades, spells, stats, pdvs, pts, inits, xp);
        
        templates.put(tpl.getID(), tpl);
        return tpl;
    }
    
    public Collection<MonsterTemplate> getAll(){
        if(!templates.isEmpty())
            return templates.values();
        
        
        try(ResultSet RS = database.query("SELECT * FROM monsters")){
            while (RS.next()) {
                try{
                    createByRS(RS);
                }catch(Exception e){
                    Peak.errorLog.addToLog(Logger.Level.ERROR, "Invalid monster template %d (%s)", RS.getInt("id"), RS.getString("name"));
                    Peak.errorLog.addToLog(e);
                }
            }
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
        
        return templates.values();
    }
    
    public MonsterTemplate getMonsterById(int id){
        if(templates.containsKey(id))
            return templates.get(id);
        
        try(PreparedStatement stmt = database.prepare("SELECT * FROM monsters WHERE id = ?")){
            stmt.setInt(1, id);
            
            try(ResultSet RS = stmt.executeQuery()){
                if(RS.next())
                    return createByRS(RS);
                else
                    return null;
            }
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
            return null;
        }
    }
}
