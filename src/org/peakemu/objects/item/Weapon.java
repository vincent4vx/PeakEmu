/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.objects.item;

import java.util.ArrayList;
import java.util.Collection;
import org.peakemu.world.ItemTemplate;
import org.peakemu.world.SpellEffect;
import org.peakemu.world.StatsTemplate;
import org.peakemu.world.enums.InventoryPosition;
import org.peakemu.world.fight.effect.FightEffect;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class Weapon extends Item{

    public Weapon(String owner, int Guid, ItemTemplate template, int qua, InventoryPosition pos, StatsTemplate statsTemplate) {
        super(owner, Guid, template, qua, pos, statsTemplate);
    }
    
    private String getAreaString(){
        switch(template.getType()){
            case MARTEAU:
                return "Cb";
            case BATON:
                return "Tb";
            default:
                return "Ca";
        }
    }
    
    public Collection<SpellEffect> getSpellEffects(){
        Collection<SpellEffect> effects = new ArrayList<>();
        
        for(StatsTemplate.StatEntry entry : statsTemplate.getEntries()){
            if(entry.isWeaponEffect()){
                SpellEffect effect = new SpellEffect(
                    entry.getEffect(), 
                    null, 
                    entry.getMin(), 
                    entry.getMax(), 
                    entry.getSpecial(), 
                    0, 
                    0, 
                    getAreaString(), 
                    FightEffect.TARGET_NOT_SELF
                );
                
                effects.add(effect);
                
            }
        }
        
        return effects;
    }
    
    public Collection<SpellEffect> getCriticalEffects(){
        Collection<SpellEffect> effects = new ArrayList<>();
        
        for(StatsTemplate.StatEntry entry : statsTemplate.getEntries()){
            if(entry.isWeaponEffect()){
                SpellEffect effect = new SpellEffect(
                    entry.getEffect(), 
                    null, 
                    entry.getMin() + template.getBonusCC(), 
                    entry.getMax() + template.getBonusCC(), 
                    entry.getSpecial(), 
                    0, 
                    0, 
                    getAreaString(), 
                    FightEffect.TARGET_NOT_SELF
                );
                
                effects.add(effect);
                
            }
        }
        
        return effects;
    }
}
