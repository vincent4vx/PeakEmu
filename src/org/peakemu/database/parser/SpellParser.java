/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.database.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import org.peakemu.Peak;
import org.peakemu.common.Logger;
import org.peakemu.common.util.StringUtil;
import org.peakemu.database.dao.SpellDAO;
import org.peakemu.objects.player.SpellBook;
import org.peakemu.world.Spell;
import org.peakemu.world.SpellEffect;
import org.peakemu.world.SpellLevel;
import org.peakemu.world.enums.Effect;
import org.peakemu.world.enums.FighterState;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class SpellParser {
    final static public SpellLevel parseSpellLevel(Spell spell, int level, String levelData){
        String[] datas = StringUtil.split(levelData, "|", 20);
      
        String effectsStr = datas[0];
        String criticalEffectsStr = datas[1];
        int apCost = Integer.parseInt(datas[2].trim());
        int minRange = Integer.parseInt(datas[3].trim());
        int maxRange = Integer.parseInt(datas[4].trim());
        int criticalRate = Integer.parseInt(datas[5].trim());
        int failRate = Integer.parseInt(datas[6].trim());
        boolean lineOnly = Boolean.parseBoolean(datas[7].trim());
        boolean lineOfSight = Boolean.parseBoolean(datas[8].trim());
        boolean freeCell = Boolean.parseBoolean(datas[9].trim());
        boolean boostRange = Boolean.parseBoolean(datas[10].trim());
        int classId = Integer.parseInt(datas[11].trim());
        int launchCountByTurn = Integer.parseInt(datas[12].trim());
        int launchCountByTarget = Integer.parseInt(datas[13].trim());
        int launchDelay = Integer.parseInt(datas[14].trim());
        String effectAreaString = datas[15].trim();
        Set<FighterState> requiredStates = parseStates(datas[16].trim());
        Set<FighterState> forbiddenStates = parseStates(datas[17].trim());
        int minPlayerLevel = Integer.parseInt(datas[18].trim());
        boolean failEndTurn = Boolean.parseBoolean(datas[19].trim());
        
        SpellLevel spellLevel = new SpellLevel(spell, level, apCost, minRange, maxRange, criticalRate, failRate, lineOnly, lineOfSight, freeCell, boostRange, classId, launchCountByTurn, launchCountByTarget, launchDelay, effectAreaString, requiredStates, forbiddenStates, minPlayerLevel, failEndTurn);
        
        Collection<SpellEffect> effects = parseSpellEffects(effectsStr, spellLevel);
        spellLevel.setSpellEffects(effects == null ? Collections.EMPTY_LIST : effects); //no effects ? effects are required
        spellLevel.setCriticalSpellEffects(parseSpellEffects(criticalEffectsStr, spellLevel)); //critical can be null
        
        return spellLevel;
    }
    
    static private Set<FighterState> parseStates(String statesStr){
        Set<FighterState> states = new HashSet<>();
        
        for(String str : StringUtil.split(statesStr, ";")){
            str = str.trim();
            
            if(str.isEmpty() || str.equals("-1"))
                continue;
            
            int stateId = Integer.parseInt(str);
            FighterState state = FighterState.fromId(stateId);
            
            if(state == null){
                Peak.worldLog.addToLog(Logger.Level.ERROR, "Undefined state %d", stateId);
                continue;
            }
                
            states.add(state);
        }
        
        return states;
    }
    
    final static private Collection<SpellEffect> parseSpellEffects(String effectsStr, SpellLevel level){
        effectsStr = effectsStr.trim();
        
        if(effectsStr.isEmpty() || effectsStr.equals("-1"))
            return null;
        
        Collection<SpellEffect> effects = new ArrayList<>();
        
        int index = 0;
        for(String str : StringUtil.split(effectsStr, ";")){
            if(str.isEmpty())
                continue;
            
            String area = level.getEffectAreaString().substring(2*index, 2*index + 2);
            
            SpellEffect effect = parseSpellEffect(str, area, level, level.getSpell().getEffectTarget(index));
            
            if(effect != null)
                effects.add(effect);
            
            ++index;
        }
        
        return effects;
    }
    
    final static public SpellEffect parseSpellEffect(String str, String area, SpellLevel spell, int target){
        String[] data = StringUtil.split(str, ",");
        
        Effect effect = Effect.valueOf(Integer.parseInt(data[0]));
        
        if(effect == null){
            Peak.worldLog.addToLog(Logger.Level.ERROR, "Undefined effect %s", data[0]);
            return null;
        }
        
        int min = data[1].isEmpty() ? -1 : Integer.parseInt(data[1]);
        int max = data[2].isEmpty() ? -1 : Integer.parseInt(data[2]);
        int arg3 = data[3].isEmpty() ? -1 : Integer.parseInt(data[3]);
        int turn = data[4].isEmpty() ? -1 : Integer.parseInt(data[4]);
        int chance = data[5].isEmpty() ? -1 : Integer.parseInt(data[5]);
            
        return new SpellEffect(
            effect, 
            spell,
            min,
            max,
            arg3,
            turn,
            chance,
            area,
            target
        );
    }
    
    public static Map<Integer, SpellLevel> parseCollectorSpells(SpellDAO spellDAO, String sSpells) {
        return parseMonsterSpells(spellDAO, sSpells, "|", ";");
    }
    
    public static Map<Integer, SpellLevel> parseMonsterSpells(SpellDAO spellDAO, String sSpells) {
        return parseMonsterSpells(spellDAO, sSpells, ";", "@");
    }

    public static Map<Integer, SpellLevel> parseMonsterSpells(SpellDAO spellDAO, String sSpells, String spellsSeprator, String levelSeparator) {
        Map<Integer, SpellLevel> spells = new HashMap<>();
        for (String sSpell : StringUtil.split(sSpells, spellsSeprator)) {
            if(sSpell.isEmpty())
                continue;
            
            String[] aSpell = StringUtil.split(sSpell, levelSeparator, 2);
            
            if(aSpell.length < 2)
                continue;
            
            int spellId = Integer.parseInt(aSpell[0].trim());
            int level = Integer.parseInt(aSpell[1].trim());
            Spell spell = spellDAO.getSpellById(spellId);
            
            if (spell == null) {
                Peak.worldLog.addToLog(Logger.Level.ERROR, "Undefined spell %d", spellId);
                continue;
            }
            
            if (level > spell.getMaxLevel()) {
                level = spell.getMaxLevel();
            }
            
            spells.put(spellId, spell.getLevel(level));
        }
        return spells;
    }
    
    static public String serializeMonsterSpells(Map<Integer, SpellLevel> spells){
        return serializeMonsterSpells(spells, ";", "@");
    }
    
    static public String serializeCollectorSpells(Map<Integer, SpellLevel> spells){
        return serializeMonsterSpells(spells, "|", ";");
    }
    
    static public String serializeMonsterSpells(Map<Integer, SpellLevel> spells, String spellsSeparator, String levelSeparator){
        StringBuilder sb = new StringBuilder(spells.size() * 6);
        
        boolean b = false;
        
        for(Map.Entry<Integer, SpellLevel> entry : spells.entrySet()){
            if(b)
                sb.append(spellsSeparator);
            else
                b = true;
            
            sb.append(entry.getKey()).append(levelSeparator).append(entry.getValue().getLevel());
        }
        
        return sb.toString();
    }
    
    static public SpellBook parsePlayerSpellBook(SpellDAO spellDAO, String sSpells){
        Map<Integer, SpellLevel> spells = new LinkedHashMap<>();
        Map<Integer, Character> positions = new HashMap<>();
        
        for(String sSpell : StringUtil.split(sSpells, ",")){
            String[] aSpell = StringUtil.split(sSpell, ";", 3);
            
            int spellId = Integer.parseInt(aSpell[0]);
            int spellLevel = Integer.parseInt(aSpell[1]);
            char pos = aSpell[2].charAt(0);
            
            Spell spell = spellDAO.getSpellById(spellId);
            
            if(spell == null)
                continue;
            
            if(spellLevel > spell.getMaxLevel())
                spellLevel = spell.getMaxLevel();
            
            SpellLevel level = spell.getLevel(spellLevel);
            
            spells.put(spellId, level);
            positions.put(spellId, pos);
        }
        
        return new SpellBook(spells, positions);
    }
    
    static public String serializePlayerSpellBook(SpellBook spellBook){
        StringBuilder sb = new StringBuilder();
        
        boolean b = false;
        
        for(Map.Entry<Integer, SpellLevel> entry : spellBook.getSpells().entrySet()){
            if(b)
                sb.append(',');
            else
                b = true;
            
            sb.append(entry.getKey()).append(';')
                .append(entry.getValue().getLevel()).append(';')
                .append(spellBook.getSpellPosition(entry.getKey()));
        }
        
        return sb.toString();
    }
}
