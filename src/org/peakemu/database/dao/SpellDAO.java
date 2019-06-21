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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.peakemu.Peak;
import org.peakemu.common.Logger;
import org.peakemu.common.util.StringUtil;
import org.peakemu.database.Database;
import org.peakemu.database.parser.SpellParser;
import org.peakemu.world.Spell;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class SpellDAO {
    final private Database database;
    
    final private Map<Integer, Spell> spells = new HashMap<>();

    public SpellDAO(Database database) {
        this.database = database;
    }
    
    private Spell createByRS(ResultSet RS) throws SQLException{
        int id = RS.getInt("id");
        String name = RS.getString("nom");
        int sprite = RS.getInt("sprite");
        String spriteInfos = RS.getString("spriteInfos");
        
        String sEffectTargets = RS.getString("effectTarget").trim();
        int[] effectTarget;
        
        if(!sEffectTargets.isEmpty()){
            String[] strET = StringUtil.split(sEffectTargets, ";");
            List<Integer> list = new ArrayList<>(strET.length);

            for(String s : strET){
                if(s.isEmpty())
                    continue;
                
                try{
                    list.add(Integer.parseInt(s));
                }catch(NumberFormatException e){}
            }
            
            effectTarget = new int[list.size()];
            
            for(int i = 0; i < list.size(); ++i){
                effectTarget[i] = list.get(i);
            }
        }else{
            effectTarget = new int[0];
        }
        
        Spell spell = new Spell(id, name, sprite, spriteInfos, effectTarget);
        
        for(int level = Spell.MIN_SPELL_LEVEL; level <= Spell.MAX_SPELL_LEVEL; ++level){
            String levelStr = RS.getString("lvl" + level).trim();
            
            if(levelStr.isEmpty() || levelStr.equals("-1"))
                break;
            
            try{
                spell.addSpellLevel(SpellParser.parseSpellLevel(spell, level, levelStr));
            }catch(Exception e){
                Peak.errorLog.addToLog(Logger.Level.ERROR, "Invalid spell level %d for spell %d (%s)", level, id, spell.getName());
                Peak.errorLog.addToLog(e);
                break;
            }
        }
        
        spells.put(id, spell);
        
        return spell;
    }
    
    public Collection<Spell> getAll(){
        if(!spells.isEmpty())
            return spells.values();
        
        try(ResultSet RS = database.query("SELECT * FROM sorts")){
            while(RS.next())
                createByRS(RS);
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
        
        return spells.values();
    }
    
    public Spell getSpellById(int id){
        if(spells.containsKey(id))
            return spells.get(id);
        
        try(PreparedStatement stmt = database.prepare("SELECT * FROM sorts WHERE id = ?")){
            stmt.setInt(1, id);
            
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
