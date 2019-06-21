/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.objects.player;

import java.util.HashMap;
import java.util.Map;
import org.peakemu.game.out.game.SpriteParser;
import org.peakemu.objects.Monster;
import org.peakemu.world.MapCell;
import org.peakemu.world.MonsterTemplate;
import org.peakemu.world.SpellLevel;
import org.peakemu.world.Stats;
import org.peakemu.world.enums.Effect;
import org.peakemu.world.enums.SpriteTypeEnum;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class Mutant implements PlayerData{
    final static public int PARAM_SHOW_PLAYER = 1;
    final static public int PARAM_USE_STUFF   = 2;
    
    final private Player player;
    final private MonsterTemplate monster;
    final private int parameters;
    final private int restrictions;
    
    private Monster grade = null;
    
    private SpellBook spellBook;

    public Mutant(Player player, MonsterTemplate monster, int parameters, int restrictions) {
        this.player = player;
        this.monster = monster;
        this.parameters = parameters;
        this.restrictions = restrictions;
        refreshSpellBook();
    }
    
    private void refreshSpellBook(){
        spellBook = new SpellBook(getGrade().getSpells(), new HashMap());
        spellBook.autoSetPositions();
    }

    @Override
    public SpriteTypeEnum getSpriteType() {
        return isShowPlayer() ? SpriteTypeEnum.MUTANT_PLAYER : SpriteTypeEnum.MUTANT;
    }
    
    public boolean getParam(int param){
        return (parameters & param) == param;
    }
    
    public boolean isShowPlayer(){
        return getParam(PARAM_SHOW_PLAYER);
    }

    public MonsterTemplate getMonster() {
        return monster;
    }
    
    public Map<Integer, SpellLevel> getSpells(){
        return getGrade().getSpells();
    }
    
    public Monster getGrade(){
        if(grade != null)
            return grade;
        
        grade = monster.getFirstGrade();
        
        for(Monster mg : monster.getGrades()){
            if(mg.getLevel() > player.getLevel())
                break;
            
            grade = mg;
        }
        
        return grade;
    }

    @Override
    public String getSpritePacket() {
        StringBuilder sb = new StringBuilder();
        
        sb.append(player.getCell().getID()).append(';')
          .append(player.getOrientation()).append(';') //direction
          .append(0).append(';') //bonus
          .append(player.getSpriteId()).append(';')
          .append(monster.getID())
        ;
        
        if(isShowPlayer()){
            sb.append('~').append(player.getName());
        }
        
        sb.append(';')
          .append(getSpriteType().toInt()).append(';')
          .append(monster.getGfxID()).append('^').append(player.getSize()).append(';')
          .append(player.getGender()).append(';')
          .append(getGrade().getGrade()).append(';') //grade monster
          .append(SpriteParser.stuffToString(player.getItems())).append(';')
          .append("").append(';') //emotes
          .append(';') //emote timer
          .append(Integer.toString(player.getRestrictions().toInt(), 36)).append(';') //restrictions
        ;
        
        return sb.toString();
    }

    @Override
    public int getSpriteId() {
        return player.getSpriteId();
    }

    @Override
    public MapCell getCell() {
        return player.getCell();
    }

    @Override
    public SpellBook getSpellBook() {
        return spellBook;
    }

    @Override
    public Stats getBaseStats() {
        return getGrade().getStats();
    }

    @Override
    public Stats getRaceStats() {
        Stats stats = new Stats();
        
        stats.addOneStat(Effect.ADD_PODS, 1000); //ADD pods by default
        
        return stats;
    }

    @Override
    public int getInitiative() {
        return player.getTotalStats().getEffect(Effect.ADD_INIT);
    }

    @Override
    public boolean canHaveMount() {
        return false;
    }

    @Override
    public boolean canHaveStuff() {
        return getParam(PARAM_USE_STUFF);
    }

    @Override
    public boolean canLearnSpells() {
        return false;
    }

    @Override
    public int getGfxID() {
        return monster.getGfxID();
    }

    public int getRestrictions() {
        return restrictions;
    }
}
