/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight.fighter;

import org.peakemu.objects.Monster;
import org.peakemu.world.Stats;
import org.peakemu.world.enums.SpriteTypeEnum;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class MonsterFighter extends Fighter{
    final private Monster monster;

    public MonsterFighter(int id, Monster monster) {
        super(id, monster.getPDVMAX(), monster.getPDVMAX(), monster.getTemplate().getGfxID());
        this.monster = monster;
    }

    @Override
    public Stats getBaseStats() {
        return monster.getStats();
    }

    @Override
    public int getInitiative() {
        return monster.getInit();
    }

    @Override
    public int get_lvl() {
        return monster.getLevel();
    }

    @Override
    public String getPacketsName() {
        return monster.getTemplate().getID() + "";
    }

    @Override
    public Monster getMonster() {
        return monster;
    }

    @Override
    public SpriteTypeEnum getSpriteType() {
        return SpriteTypeEnum.MONSTER;
    }

    @Override
    public String getSpritePacket() {
        StringBuilder str = prepareSpritePacket();
        str.append(getSpriteType().toInt()).append(";");
        str.append(monster.getTemplate().getGfxID()).append("^100;");
        str.append(monster.getGrade()).append(";");
        str.append(monster.getTemplate().getColors().replace(",", ";")).append(";");
        str.append("0,0,0,0;");
        str.append(this.getPDVMAX()).append(";");
        str.append(monster.getPA()).append(";");
        str.append(monster.getPM()).append(";");
        str.append(getTeam().getNumber());
        return str.toString();
    }

    @Override
    public long getXpGive() {
        return monster.getBaseXp();
    }

    @Override
    public int getDefaultGfx() {
        return monster.getTemplate().getGfxID();
    }

    @Override
    public int getPdvMaxOutFight() {
        return monster.getPDVMAX();
    }

    @Override
    public String xpString(String str) {
        return "0" + str + "0" + str + "0";
    }

    @Override
    public boolean isReady() {
        return true;
    }
    
    
}
