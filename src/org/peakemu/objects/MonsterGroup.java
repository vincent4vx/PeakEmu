/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.objects;

import java.util.Collection;
import org.peakemu.common.Constants;
import org.peakemu.common.Formulas;
import org.peakemu.world.FixedMonsterGroup;
import org.peakemu.world.MapCell;
import org.peakemu.world.MoveableSprite;
import org.peakemu.world.enums.SpriteTypeEnum;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class MonsterGroup implements MoveableSprite {
    private int id = 0;
    
    private MapCell cell;
    
    private int orientation = 2;
    final private int align;
    final private int aggroDistance;
    final private FixedMonsterGroup fixedMonsterGroup;
    final private Collection<Monster> monsters;
    private String condition = "";
    final public long _creationDate;
    final private int assaultDistance;

    public MonsterGroup(Collection<Monster> monsters, FixedMonsterGroup fixedMonsterGroup) {
        this.monsters = monsters;
        this.orientation = Formulas.getRandomValue(0, 3) * 2 + 1;
        this.fixedMonsterGroup = fixedMonsterGroup;
        
        int maxLevel = 0;
        int align = Constants.ALIGNEMENT_NONE;
        
        for(Monster monster : monsters){
            if(monster.getLevel() > maxLevel)
                maxLevel = monster.getLevel();
            
            if(monster.getTemplate().getAlign() != Constants.ALIGNEMENT_NONE)
                align = monster.getTemplate().getAlign();
        }
        
        this.align = align;
        
        aggroDistance = align != Constants.ALIGNEMENT_NONE ? 15 : Constants.getAggroByLevel(maxLevel);
        _creationDate = System.currentTimeMillis();
        
        int assaultDistance = 0;
        for (Monster MG : monsters) {
            int newAssaultDistance = MG.getTemplate().getAssaultDistance();
            if (assaultDistance < newAssaultDistance) {
                assaultDistance = newAssaultDistance; //On met attribue la plus grande distance d'agression possible.
            }
        }
        
        this.assaultDistance = assaultDistance;
    }
    
    public MonsterGroup(Collection<Monster> monsters){
        this(monsters, null);
    }

    public boolean isAgressive() {
        if (align > 0) {
            int knightBonta = 296;
            int knightBrak = 372;
            for (Monster MG : monsters) {
                int templateId = MG.getTemplate().getID();
                if (templateId == knightBrak || templateId == knightBonta) {
                    if (align == Constants.ALIGNEMENT_BONTARIEN) {
                        if (templateId == knightBrak) {
                            return true;
                        } else {
                            return false;
                        }
                    } else if (align == Constants.ALIGNEMENT_BRAKMARIEN) {
                        if (templateId == knightBonta) {
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        //FIXME: Les mercenaires se font-il agresser par les miliciens des cit�s d'alignement ?
                    }
                }
            }
        }
        return assaultDistance > -1;
    }

    @Override
    public int getSpriteId() {
        return id;
    }
    
    public void setSpriteId(int id){
        this.id = id;
    }

    @Override
    public MapCell getCell() {
        if(fixedMonsterGroup != null)
            return fixedMonsterGroup.getCell();
        
        return cell;
    }

    public int getOrientation() {
        return orientation;
    }

    public int getAggroDistance() {
        return aggroDistance;
    }

    public boolean isFix() {
        return fixedMonsterGroup != null;
    }

    public void setOrientation(int o) {
        orientation = o;
    }

    @Override
    public void setCell(MapCell cell){
        this.cell = cell;
    }

    public int getAlignement() {
        return align;
    }

    public int getSize() {
        return monsters.size();
    }

    @Override
    public SpriteTypeEnum getSpriteType() {
        return SpriteTypeEnum.MONSTER_GROUP;
    }

    @Override
    public String getSpritePacket() {
        StringBuilder mobIDs = new StringBuilder();
        StringBuilder mobGFX = new StringBuilder();
        StringBuilder mobLevels = new StringBuilder();
        StringBuilder colors = new StringBuilder();
        StringBuilder toreturn = new StringBuilder();

        boolean isFirst = true;
        if (monsters.isEmpty()) {
            return "";
        }

        for (Monster monster : monsters) {
            if (!isFirst) {
                mobIDs.append(",");
                mobGFX.append(",");
                mobLevels.append(",");
            }
            mobIDs.append(monster.getTemplate().getID());
            mobGFX.append(monster.getTemplate().getGfxID()).append("^100");
            mobLevels.append(monster.getLevel());
            colors.append(monster.getTemplate().getColors()).append(";0,0,0,0;");

            isFirst = false;
        }
        toreturn.append(getCell().getID()).append(";").append(orientation).append(";");
        toreturn.append(";").append(getSpriteId()).append(";").append(mobIDs).append(';').append(getSpriteType().toInt()).append(';').append(mobGFX).append(";").append(mobLevels).append(";").append(colors);
        return toreturn.toString();
    }

    public Collection<Monster> getMobs() {
        return monsters;
    }

    public void setCondition(String cond) {
        this.condition = cond;
    }

    public String getCondition() {
        return this.condition;
    }

    @Override
    public boolean canMove() {
        return !isFix();
    }
    
    public void startFight(){
        if(fixedMonsterGroup != null)
            fixedMonsterGroup.setStartFightTime();
    }
}
