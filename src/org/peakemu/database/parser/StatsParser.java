/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.database.parser;

import java.util.Map;
import org.peakemu.Peak;
import org.peakemu.common.Logger;
import org.peakemu.common.util.StringUtil;
import org.peakemu.world.Stats;
import org.peakemu.world.StatsTemplate;
import org.peakemu.world.enums.Effect;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
final public class StatsParser {
    static public StatsTemplate parseStatsTemplate(String strStats){
        StatsTemplate template = new StatsTemplate();
        
        for(String strStat : StringUtil.split(strStats, ",")){
            String[] statData = StringUtil.split(strStat, "#");
            
            if(strStat.isEmpty() || statData.length < 5)
                continue;
            
            try{
                int effectId = Integer.parseInt(statData[0], 16);
                int min = Integer.parseInt(statData[1], 16);
                int max = Integer.parseInt(statData[2], 16);
                int special = Integer.parseInt(statData[3], 16);
                String text = statData[4];
                Effect effect = Effect.valueOf(effectId);
                
                if(effect == null){
                    Peak.worldLog.addToLog(Logger.Level.DEBUG, "Undefined effect %d", effectId);
                    continue;
                }
            
                template.addEntry(new StatsTemplate.StatEntry(effect, min, max, special, text));
            }catch(Exception e){
                Peak.errorLog.addToLog(e);
            }
        }
        
        return template;
    }
    
    static public String statsTemplateToString(StatsTemplate statsTemplate){
        StringBuilder sb = new StringBuilder(512);
        
        for(StatsTemplate.StatEntry entry : statsTemplate.getEntries()){
            sb.append(Integer.toHexString(entry.getEffect().getId())).append('#')
              .append(Integer.toHexString(entry.getArg1())).append('#')
              .append(Integer.toHexString(entry.getArg2())).append('#')
              .append(Integer.toHexString(entry.getSpecial())).append('#')
              .append(entry.getText()).append(',');
        }
        
        return sb.toString();
    }
    
    final static public Effect[] MONSTER_RES_ORDER = new Effect[]{Effect.ADD_RP_NEU, Effect.ADD_RP_TER, Effect.ADD_RP_FEU, Effect.ADD_RP_EAU, Effect.ADD_RP_AIR, Effect.ADD_AFLEE, Effect.ADD_MFLEE};
    final static public Effect[] MONSTER_STATS_ORDER = new Effect[]{Effect.ADD_FORC, Effect.ADD_SAGE, Effect.ADD_INTE, Effect.ADD_CHAN, Effect.ADD_AGIL};
    
    static public Stats parseMonsterStats(String sRes, String sStats, int life, int pa, int pm, int init){
        Stats stats = new Stats();
        
        stats.addOneStat(Effect.ADD_VITA, life);
        stats.addOneStat(Effect.ADD_PA, pa);
        stats.addOneStat(Effect.ADD_PM, pm);
        stats.addOneStat(Effect.ADD_INIT, init);
        stats.addOneStat(Effect.CREATURE, 1);
        
        String[] aRes = StringUtil.split(sRes, ";");
        
        for(int i = 0; i < aRes.length && i < MONSTER_RES_ORDER.length; ++i){
            if(aRes[i].isEmpty())
                continue;
            
            stats.addOneStat(MONSTER_RES_ORDER[i], Integer.parseInt(aRes[i]));
        }
        
        String[] aStats = StringUtil.split(sStats, ",");
        
        for(int i = 0; i < aStats.length && i < MONSTER_STATS_ORDER.length; ++i){
            if(aStats[i].isEmpty())
                continue;
            
            stats.addOneStat(MONSTER_STATS_ORDER[i], Integer.parseInt(aStats[i]));
        }
        
        return stats;
    }
    
    static public Stats parseCollectorStats(String sStats){
        Stats stats = new Stats();
        
        for(String sEntry : StringUtil.split(sStats, "|")){
            if(sEntry.isEmpty())
                continue;
            
            String[] aData = StringUtil.split(sEntry, ";", 2);
            
            Effect effect = Effect.valueOf(Integer.parseInt(aData[0]));
            int value = Integer.parseInt(aData[1]);
            
            stats.addOneStat(effect, value);
        }
        
        return stats;
    }
    
    static public String serializeCollectorStats(Stats stats){
        StringBuilder sb = new StringBuilder();
        
        boolean b = false;
        for(Map.Entry<Effect, Integer> entry : stats.getMap().entrySet()){
            if(b)
                sb.append('|');
            else
                b = true;
            
            sb.append(entry.getKey().getId()).append(';').append(entry.getValue());
        }
        
        return sb.toString();
    }
    
    static public String serializeItemStats(Stats stats) {
        StringBuilder str = new StringBuilder();
        
        for (Map.Entry<Effect, Integer> entry : stats.getMap().entrySet()) {
            if (str.length() > 0) {
                str.append(",");
            }
            str.append(Integer.toHexString(entry.getKey().getId())).append("#").append(Integer.toHexString(entry.getValue())).append("#0#0");
        }
        
        return str.toString();
    }
}
