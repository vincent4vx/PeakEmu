/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world;

import org.peakemu.objects.player.Mutant;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class Restrictions {
    final static public int DENY_ASSAULT = 1;
    final static public int DENY_CHALLENGE = 2;
    final static public int DENY_EXCHANGE = 4;
    final static public int ALLOW_ATTACK = 8;
    final static public int DENY_CHAT = 16;
    final static public int DENY_MERCHAND = 32;
    final static public int DENY_USE_OBJECT = 64;
    final static public int DENY_INTERACT_COLLECTOR = 128;
    final static public int DENY_USE_IO = 256;
    final static public int DENY_SPEAK_NPC = 512;
    final static public int ALLOW_DUNGEON_MUTANT = 4096;
    final static public int ALLOW_MOVE_ALL_DIRECTION = 8192;
    final static public int ALLOW_ATTACK_MONSTERS_MUTANT = 16384;
    final static public int DENY_INTERACT_PRISM = 32768;
    
    final static public int OTHER_DENY_ASSAULT = 1;
    final static public int OTHER_DENY_CHALLENGE = 2;
    final static public int OTHER_DENY_EXCHANGE = 4;
    final static public int OTHER_DENY_ATTACK = 8;
    final static public int OTHER_FORCE_WALK = 16;
    final static public int OTHER_IS_SLOW = 32;
    final static public int OTHER_DENY_CREATURE_MODE = 64;
    final static public int OTHER_IS_TOMB = 128;
    
    final static public int DEATH_RESTRICTIONS = DENY_ASSAULT | DENY_CHALLENGE | DENY_CHAT | DENY_EXCHANGE 
        | DENY_INTERACT_COLLECTOR | DENY_INTERACT_PRISM | DENY_MERCHAND | DENY_SPEAK_NPC | DENY_USE_OBJECT;
    
    private int restriction;
    private int distantRestrictions;

    public Restrictions(int restriction, int distantRestrictions) {
        this.restriction = restriction;
        this.distantRestrictions = distantRestrictions;
    }
    
    public void addRestriction(int restriction){
        this.restriction |= restriction;
    }
    
    public void removeRestriction(int restriction){
        this.restriction &= ~restriction;
    }

    public int toInt() {
        return restriction;
    }

    public void setRestriction(int restriction) {
        this.restriction = restriction;
    }
    
    public void setMutantRestrictions(Mutant mutant){
        removeRestriction(ALLOW_MOVE_ALL_DIRECTION);
        addRestriction(mutant.getRestrictions());
    }
    
    public void unsetMutantRestrictions(Mutant mutant){
        addRestriction(ALLOW_MOVE_ALL_DIRECTION);
        removeRestriction(mutant.getRestrictions());
    }
    
    public void setDeathRestrictions(){
        addRestriction(DEATH_RESTRICTIONS);
    }
    
    public void unsetDeathRestrictions(){
        removeRestriction(DEATH_RESTRICTIONS);
    }
    
    public boolean canAssault(){
        return (restriction & DENY_ASSAULT) != DENY_ASSAULT;
    }
    
    public boolean canChallenge(){
        return (restriction & DENY_CHALLENGE) != DENY_CHALLENGE;
    }
    
    public boolean canAttack(){
        return (restriction & ALLOW_ATTACK) == ALLOW_ATTACK;
    }
    
    public boolean canChat(){
        return (restriction & DENY_CHAT) != DENY_CHAT;
    }
    
    public boolean canBeMerchant(){
        return (restriction & DENY_MERCHAND) != DENY_MERCHAND;
    }
    
    public boolean canUseObject(){
        return (restriction & DENY_USE_OBJECT) != DENY_USE_OBJECT;
    }
    
    public boolean canInteractCollector(){
        return (restriction & DENY_INTERACT_COLLECTOR) != DENY_INTERACT_COLLECTOR;
    }
    
    public boolean canUseIO(){
        return (restriction & DENY_USE_IO) != DENY_USE_IO;
    }
    
    public boolean canSpeakNPC(){
        return (restriction & DENY_SPEAK_NPC) != DENY_SPEAK_NPC;
    }
    
    public boolean canAttackDungeonWhenMutant(){
        return (restriction & ALLOW_DUNGEON_MUTANT) == ALLOW_DUNGEON_MUTANT;
    }
    
    public boolean canMoveAllDirections(){
        return (restriction & ALLOW_MOVE_ALL_DIRECTION) == ALLOW_MOVE_ALL_DIRECTION;
    }
    
    public boolean canAttackMonsterWhenMutant(){
        return (restriction & ALLOW_ATTACK_MONSTERS_MUTANT) == ALLOW_ATTACK_MONSTERS_MUTANT;
    }
    
    public boolean canInteractWithPrism(){
        return (restriction & DENY_INTERACT_PRISM) != DENY_INTERACT_PRISM;
    }
    
    static public Restrictions createDefaultRestrictions(){
        return new Restrictions(ALLOW_MOVE_ALL_DIRECTION, 0);
    }
}
