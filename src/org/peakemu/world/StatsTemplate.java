/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.peakemu.common.util.Util;
import org.peakemu.world.enums.Effect;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class StatsTemplate {
    final private List<StatEntry> entries = new ArrayList<>();
    
    public static class StatEntry{
        final private Effect effect;
        final private int min;
        final private int max;
        final private int special;
        final private String text;

        public StatEntry(Effect effect, int min, int max, int special, String text) {
            this.effect = effect;
            this.min = min;
            this.max = max;
            this.special = special;
            this.text = text;
        }

        public Effect getEffect() {
            return effect;
        }

        public int getMin() {
            return min;
        }

        public int getMax() {
            return max > min ? max : min;
        }
        
        public int getArg1(){
            return  min;
        }
        
        public int getArg2(){
            return max;
        }

        public String getText() {
            return text;
        }

        public int getSpecial() {
            return special;
        }
        
        public boolean isWeaponEffect(){
            for(Effect e : Effect.WEAPON_EFFECTS){
                if(effect == e)
                    return true;
            }
            
            return false;
        }
        
        public boolean isSpecial(){
            return special > 0;
        }
        
        public boolean isText(){
            for(Effect e : Effect.TEXT_EFFECTS){
                if(effect == e)
                    return true;
            }
            
            return false;
        }
        
        public boolean isSimpleStat(){
            return !isSpecial() && !isText() && !isWeaponEffect();
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 67 * hash + Objects.hashCode(this.effect);
            hash = 67 * hash + this.min;
            hash = 67 * hash + this.max;
            hash = 67 * hash + this.special;
            hash = 67 * hash + Objects.hashCode(this.text);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final StatEntry other = (StatEntry) obj;
            if (this.effect != other.effect) {
                return false;
            }
            if (this.min != other.min) {
                return false;
            }
            if (this.max != other.max) {
                return false;
            }
            if (this.special != other.special) {
                return false;
            }
            if (!Objects.equals(this.text, other.text)) {
                return false;
            }
            return true;
        }
    }

    public StatsTemplate() {
    }

    public StatsTemplate(StatsTemplate template) {
        entries.addAll(template.entries);
    }
    
    public void addEntry(StatEntry entry){
        entries.add(entry);
    }
    
    public Collection<StatEntry> getEntries(){
        return entries;
    }
    
    public StatsTemplate randomizeStats(){
        StatsTemplate statsTemplate = new StatsTemplate();
        
        for(StatEntry entry : entries){
            StatEntry newEntry = entry;
            if(entry.isSimpleStat()){
                int value = Util.rand(entry.getMin(), entry.getMax());
                newEntry = new StatEntry(entry.effect, value, 0, 0, "");
            }
            
            statsTemplate.addEntry(newEntry);
        }
        
        return statsTemplate;
    }
    
    public StatsTemplate maximizeStats(){
        StatsTemplate statsTemplate = new StatsTemplate();
        
        for(StatEntry entry : entries){
            StatEntry newEntry = entry;
            if(entry.isSimpleStat()){
                int value = entry.getMin();
                
                if(entry.getMax() > entry.getMin()){
                    value = entry.getMax();
                }
                
                newEntry = new StatEntry(entry.effect, value, 0, 0, "");
            }
            
            statsTemplate.addEntry(newEntry);
        }
        
        return statsTemplate;
    }
    
    public StatsTemplate minimizeStats(){
        StatsTemplate statsTemplate = new StatsTemplate();
        
        for(StatEntry entry : entries){
            StatEntry newEntry = entry;
            if(entry.isSimpleStat()){
                int value = entry.getMin();
                newEntry = new StatEntry(entry.effect, value, 0, 0, "");
            }
            
            statsTemplate.addEntry(newEntry);
        }
        
        return statsTemplate;
    }
    
    public Stats generateRandomStats(){
        Stats stats = new Stats();
        
        for (StatEntry entry : entries) {
            if(entry.isSimpleStat())
                stats.addOneStat(entry.getEffect(), Util.rand(entry.getMin(), entry.getMax()));
        }
        
        return stats;
    }
    
    public Stats generateMaxStats(){
        Stats stats = new Stats();
        
        for (StatEntry entry : entries) {
            if(entry.isSimpleStat())
                stats.addOneStat(entry.getEffect(), entry.getMax());
        }
        
        return stats;
    }
    
    public Stats generateMinStats(){
        Stats stats = new Stats();
        
        for (StatEntry entry : entries) {
            if(entry.isSimpleStat())
                stats.addOneStat(entry.getEffect(), entry.getMin());
        }
        
        return stats;
    }
    
    public Map<Effect, String> getTexts(){
        Map<Effect, String> texts = new HashMap<>();
        
        for (StatEntry entry : entries) {
            if(entry.isText())
                texts.put(entry.getEffect(), entry.getText());
        }
        
        return texts;
    }
    
    public Collection<StatEntry> getSpecials(){
        Collection<StatEntry> specials = new ArrayList<>();
        
        for(StatEntry entry : entries){
            if(entry.isSpecial())
                specials.add(entry);
        }
        
        return specials;
    }
    
    public Stats getStats(){
        Stats stats = new Stats();
        addAllStats(stats);
        return stats;
    }
    
    public void addAllStats(Stats stats){
        for(StatEntry entry : entries){
            if(entry.isSimpleStat())
                stats.addOneStat(entry.effect, entry.getMin());
        }
    }
    
    public void setEntry(StatEntry entry){
        int index = indexOf(entry.getEffect());
        
        if(index == -1){
            entries.add(entry);
        }else{
            entries.set(index, entry);
        }
    }
    
    public void setSimpleStat(Effect effect, int value){
        StatEntry entry = new StatEntry(effect, value, 0, 0, "");
        setEntry(entry);
    }
    
    public void setText(Effect effect, String value){
        StatEntry entry = new StatEntry(effect, 0, 0, 0, value);
        setEntry(entry);
    }
    
    public void setSpecial(Effect effect, int value){
        StatEntry entry = new StatEntry(effect, 0, 0, value, "");
        setEntry(entry);
    }
    
    private int indexOf(Effect effect){
        for(int i = 0; i < entries.size(); ++i){
            if(entries.get(i).getEffect() == effect)
                return i;
        }
        return -1;
    }
    
    public StatEntry findFirstEntry(Effect effect){
        for (StatEntry entry : entries) {
            if(entry.getEffect() == effect)
                return entry;
        }
        
        return null;
    }
    
    public String getText(Effect effect){
        StatEntry entry = findFirstEntry(effect);
        
        if(entry == null)
            return null;
        
        return entry.getText();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.entries);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final StatsTemplate other = (StatsTemplate) obj;
        
        if(other.entries.size() != entries.size())
            return false;
        
        for(int i = 0; i < entries.size(); ++i){
            if(!entries.get(i).equals(other.entries.get(i)))
                return false;
        }
        
        return true;
    }
}
