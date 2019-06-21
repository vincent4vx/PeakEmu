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
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.peakemu.Peak;
import org.peakemu.common.Logger;
import org.peakemu.common.util.StringUtil;
import org.peakemu.database.Database;
import org.peakemu.world.ClassData;
import org.peakemu.world.Spell;
import org.peakemu.world.enums.PlayerStat;
import org.peakemu.world.enums.PlayerRace;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class ClassDataDAO {
    final private Database database;
    final private SpellDAO spellDAO;
    
    final private Map<PlayerRace, ClassData> classDatas = new HashMap<>();

    public ClassDataDAO(Database database, SpellDAO spellDAO) {
        this.database = database;
        this.spellDAO = spellDAO;
    }
    
    public ClassData createByRS(ResultSet RS) throws SQLException{
        PlayerRace race = PlayerRace.values()[RS.getInt("class_id")];
        String name = RS.getString("class_name");
        
        List<Spell> spells = new ArrayList<>();
        
        for(String sId : StringUtil.split(RS.getString("class_spells"), "|")){
            if(sId.isEmpty())
                continue;
            
            int spellId = Integer.parseInt(sId);
            Spell spell = spellDAO.getSpellById(spellId);
            
            if(spell == null){
                Peak.worldLog.addToLog(Logger.Level.ERROR, "Class Spell %d not found for class %s", spellId, race);
                continue;
            }
            
            spells.add(spell);
        }
        
        Map<PlayerStat, List<ClassData.BoostInterval>> boostStats = new EnumMap<>(PlayerStat.class);
        
        for(PlayerStat bs : PlayerStat.values()){
            List<ClassData.BoostInterval> intervals = new ArrayList<>();
            
            for(String sInt : StringUtil.split(RS.getString("b" + bs.getId()), "|")){
                String[] aInt = StringUtil.split(sInt, ";", 3);
                
                int minStats = Integer.parseInt(aInt[0]);
                int cost = Integer.parseInt(aInt[1]);
                int points = aInt.length >= 3 ? Integer.parseInt(aInt[2]) : 1;
                
                intervals.add(new ClassData.BoostInterval(minStats, cost, points));
            }
            
            boostStats.put(bs, intervals);
        }
        
        ClassData cd = new ClassData(race, name, spells, boostStats);
        classDatas.put(race, cd);
        return cd;
    }
    
    public Collection<ClassData> getAll(){
        if(!classDatas.isEmpty())
            return classDatas.values();
        
        try(ResultSet RS = database.query("SELECT * FROM class_data")){
            while(RS.next())
                createByRS(RS);
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
        
        return classDatas.values();
    }
    
    public ClassData getClassDataByRace(PlayerRace race){
        if(classDatas.containsKey(race))
            return classDatas.get(race);
        
        try(PreparedStatement stmt = database.prepare("SELECT * FROM class_data WHERE class_id = ?")){
            stmt.setInt(1, race.ordinal());
            
            try(ResultSet RS = stmt.executeQuery()){
                if(!RS.next())
                    return null;
                
                return createByRS(RS);
            }
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
            return null;
        }
    }
}
