/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import org.peakemu.world.enums.FighterState;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class SpellLevel {
    final private Spell spell;
    final private int level;
    private Collection<SpellEffect> spellEffects = null;
    private Collection<SpellEffect> criticalSpellEffects = null;
    final private int apCost;
    final private int minRange;
    final private int maxRange;
    final private int criticalRate;
    final private int failRate;
    final private boolean lineOnly;
    final private boolean lineOfSight;
    final private boolean freeCell;
    final private boolean boostRange;
    final private int classId;
    final private int launchCountByTurn;
    final private int launchCountByTarget;
    final private int launchDelay;
    final private String effectAreaString;
    final private Set<FighterState> requiredStates;
    final private Set<FighterState> forbiddenStates;
    final private int minPlayerLevel;
    final private boolean failEndTurn;

    public SpellLevel(Spell spell, int level, int apCost, int minRange, int maxRange, int criticalRate, int failRate, boolean lineOnly, boolean lineOfSight, boolean freeCell, boolean boostRange, int classId, int launchCountByTurn, int launchCountByTarget, int launchDelay, String effectAreaString, Set<FighterState> requiredStates, Set<FighterState> forbiddenStates, int minPlayerLevel, boolean failEndTurn) {
        this.spell = spell;
        this.level = level;
        this.apCost = apCost;
        this.minRange = minRange;
        this.maxRange = maxRange;
        this.criticalRate = criticalRate;
        this.failRate = failRate;
        this.lineOnly = lineOnly;
        this.lineOfSight = lineOfSight;
        this.freeCell = freeCell;
        this.boostRange = boostRange;
        this.classId = classId;
        this.launchCountByTurn = launchCountByTurn;
        this.launchCountByTarget = launchCountByTarget;
        this.launchDelay = launchDelay;
        this.effectAreaString = effectAreaString;
        this.requiredStates = requiredStates;
        this.forbiddenStates = forbiddenStates;
        this.minPlayerLevel = minPlayerLevel;
        this.failEndTurn = failEndTurn;
    }

    public Collection<SpellEffect> getSpellEffects() {
        return spellEffects;
    }

    public Collection<SpellEffect> getCriticalSpellEffects() {
        return criticalSpellEffects;
    }

    public void setSpellEffects(Collection<SpellEffect> spellEffects) {
        if(this.spellEffects != null)
            throw new IllegalStateException("SpellLevel.spellEffects already set");
        
        this.spellEffects = Collections.unmodifiableCollection(spellEffects);
    }

    public void setCriticalSpellEffects(Collection<SpellEffect> criticalSpellEffects) {
        if(this.criticalSpellEffects != null)
            throw new IllegalStateException("SpellLevel.criticalSpellEffects already set");
        
        if(criticalSpellEffects == null)
            return;
        
        this.criticalSpellEffects = Collections.unmodifiableCollection(criticalSpellEffects);
    }

    public int getApCost() {
        return apCost;
    }
    
    public int getPACost(){
        return apCost;
    }

    public int getMinRange() {
        return minRange;
    }
    
    public int getMinPO(){
        return minRange;
    }

    public int getMaxRange() {
        return maxRange;
    }
    
    public int getMaxPO(){
        return maxRange;
    }

    public int getCriticalRate() {
        return criticalSpellEffects == null ? 0 : criticalRate;
    }
    
    public int getTauxCC(){
        return getCriticalRate();
    }

    public int getFailRate() {
        return failRate;
    }
    
    public int getTauxEC(){
        return failRate;
    }

    public boolean isLineOnly() {
        return lineOnly;
    }
    
    public boolean isLineLaunch(){
        return lineOnly;
    }

    public boolean isLineOfSight() {
        return lineOfSight;
    }

    public boolean isFreeCell() {
        return freeCell;
    }

    public boolean canBoostRange() {
        return boostRange;
    }

    public int getLaunchCountByTurn() {
        return launchCountByTurn;
    }
    
    public int getMaxLaunchbyTurn(){
        return launchCountByTurn;
    }

    public int getLaunchCountByTarget() {
        return launchCountByTarget;
    }
    
    public int getMaxLaunchbyByTarget(){
        return launchCountByTarget;
    }

    public int getLaunchDelay() {
        return launchDelay;
    }
    
    public int getCoolDown(){
        return launchDelay;
    }

    public String getEffectAreaString() {
        return effectAreaString;
    }
    
    public String getPorteeType(){
        return effectAreaString;
    }

    public Set<FighterState> getRequiredStates() {
        return requiredStates;
    }

    public Set<FighterState> getForbiddenStates() {
        return forbiddenStates;
    }

    public int getMinPlayerLevel() {
        return minPlayerLevel;
    }

    public boolean isFailEndTurn() {
        return failEndTurn;
    }
    
    public boolean isEcEndTurn(){
        return failEndTurn;
    }

    public Spell getSpell() {
        return spell;
    }

    public int getLevel() {
        return level;
    }

    public int getSpellID() {
        return spell.getSpellID();
    }

    public int getSpriteID() {
        return spell.getSpriteID();
    }

    public String getSpriteInfos() {
        return spell.getSpriteInfos();
    }
    
    public boolean isMaxLevel(){
        return level == spell.getMaxLevel();
    }
    
    public SpellLevel getNextLevel(){
        if(isMaxLevel())
            return this;
        
        return spell.getLevel(level + 1);
    }
}
