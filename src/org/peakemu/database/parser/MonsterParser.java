/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.database.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.peakemu.common.util.StringUtil;
import org.peakemu.common.util.Util;
import org.peakemu.database.dao.MonsterDAO;
import org.peakemu.database.dao.SpellDAO;
import org.peakemu.objects.Monster;
import org.peakemu.world.FixedMonsterGroup;
import org.peakemu.world.MonsterTemplate;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class MonsterParser {
    static public List<Monster> parseMapMonsterList(MonsterDAO dao, String list){
        List<Monster> monsters = new ArrayList<>();
        
        for(String sMob : StringUtil.split(list, "|")){
            if(sMob.isEmpty())
                continue;
            
            int id, level;
            try{
                String[] data = StringUtil.split(sMob, ",");
                id = Integer.parseInt(data[0]);
                level = Integer.parseInt(data[1]);
            }catch(NumberFormatException e){
                continue;
            }
            
            MonsterTemplate template = dao.getMonsterById(id);
            
            if(template == null)
                continue;
            
            Monster monster = template.getGradeByLevel(level);
            
            if(monster == null)
                continue;
            
            monsters.add(monster);
        }
        
        return monsters;
    }
    
    static public Collection<FixedMonsterGroup.MonsterEntry> parseMonsterEntries(MonsterDAO dao, String data){
        Collection<FixedMonsterGroup.MonsterEntry> entries = new ArrayList<>();
        
        for(String strEntry : StringUtil.split(data, ";")){
            String[] entryData = StringUtil.split(strEntry, ",", 3);
            
            try{
                MonsterTemplate template = dao.getMonsterById(Integer.parseInt(entryData[0]));
                
                if(template == null)
                    continue;
                
                int minLevel = entryData.length >= 2 ? Integer.parseInt(entryData[1]) : 0;
                int maxLevel = entryData.length >= 3 ? Integer.parseInt(entryData[2]) : Integer.MAX_VALUE;
                entries.add(new FixedMonsterGroup.MonsterEntry(template, minLevel, maxLevel));
            }catch(NumberFormatException e){}
        }
        
        return entries;
    }
    
    static public String monsterEntriesToString(Collection<FixedMonsterGroup.MonsterEntry> entries){
        StringBuilder sb = new StringBuilder();
        
        for(FixedMonsterGroup.MonsterEntry entry : entries){
            sb.append(entry.getTemplate().getID()).append(',')
              .append(entry.getMinLevel()).append(',')
              .append(entry.getMaxLevel()).append(';');
        }
        
        return sb.toString();
    }
        
    static public void setMonsterGrades(SpellDAO spellDAO, MonsterTemplate template, String sGrades, String sSpells, String sStats, String sLifes, String sPoints, String sInits, String sExps){
        template.setGrades(parseGrades(
            spellDAO, 
            template, 
            StringUtil.split(sGrades, "|"), 
            StringUtil.split(sSpells, "|"), 
            StringUtil.split(sStats, "|"), 
            StringUtil.split(sLifes, "|"), 
            StringUtil.split(sPoints, "|"), 
            StringUtil.split(sInits, "|"), 
            StringUtil.split(sExps, "|")
        ));
    }
    
    static public List<Monster> parseGrades(SpellDAO spellDAO, MonsterTemplate template, String[] grades, String[] spells, String[] stats, String[] lifes, String[] points, String[] inits, String[] exps){
        List<Monster> monsters = new ArrayList<>();
        
        int maxLen = Util.minArrayLength(grades, spells, stats, lifes, points, inits, exps);
        
        for(int i = 0; i < maxLen; ++i){
            if(grades[i].isEmpty() || grades[i].equals("-1"))
                continue;
            
            String[] grade = StringUtil.split(grades[i], "@", 2);
            
            if(grade.length != 2
                || grade[0].isEmpty()
                || grade[1].isEmpty())
                continue;
            
            int level = Integer.parseInt(grade[0]);
            String res = grade[1];
            
            String spell = spells[i];
            
            if(spell.equals("-1"))
                spell = "";
            
            int life = Integer.parseInt(lifes[i]);
            int init = Integer.parseInt(inits[i]);
            
            String[] pts = StringUtil.split(points[i], ";", 2);
            
            int pa = Integer.parseInt(pts[0]);
            int pm = Integer.parseInt(pts[1]);
            
            String stat = stats[i];
            int exp = Integer.parseInt(exps[i]);
            
            monsters.add(new Monster(
                template, 
                i + 1,
                level,
                StatsParser.parseMonsterStats(res, stat, life, pa, pm, init),
                SpellParser.parseMonsterSpells(spellDAO, spell),
                exp
            ));
        }
            
        return monsters;
    }
}
