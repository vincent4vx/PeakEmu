/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight.fighter;

import org.peakemu.objects.Guild;
import org.peakemu.objects.Collector;
import org.peakemu.world.Stats;
import org.peakemu.world.World;
import org.peakemu.world.enums.SpriteTypeEnum;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class CollectorFighter extends Fighter{
    final private Collector collector;

    public CollectorFighter(Collector collector) {
        super(-1, collector.getGuild().getLevel() * 100, collector.getGuild().getLevel() * 100, 6000);
        this.collector = collector;
    }

    @Override
    public Stats getBaseStats() {
        return collector.getGuild().getStatsFight();
    }

    @Override
    public Collector getCollector() {
        return collector;
    }

    @Override
    public int getInitiative() {
        return collector.getGuild().getLevel();
    }

    @Override
    public int get_lvl() {
        return collector.getGuild().getLevel();
    }

    @Override
    public String getPacketsName() {
        return (collector.get_N1() + "," + collector.get_N2());
    }

    @Override
    public int getPA() {
        return super.getPA() + 6;
    }

    @Override
    public int getPM() {
        return super.getPM() + 3;
    }

    @Override
    public SpriteTypeEnum getSpriteType() {
        return SpriteTypeEnum.COLLECTOR;
    }

    @Override
    public String getSpritePacket() {
        StringBuilder str = prepareSpritePacket();
        str.append(getSpriteType().toInt()).append(";");//Perco
        str.append("6000^130;");//GFXID^Size
        Guild G = collector.getGuild();
        str.append(G.getLevel()).append(";");
        str.append("1;");//FIXME
        str.append("2;4;");//FIXME
        str.append((int) Math.floor(G.getLevel() / 2)).append(";")
           .append((int) Math.floor(G.getLevel() / 2)).append(";")
           .append((int) Math.floor(G.getLevel() / 2)).append(";")
           .append((int) Math.floor(G.getLevel() / 2)).append(";")
           .append((int) Math.floor(G.getLevel() / 2)).append(";")
           .append((int) Math.floor(G.getLevel() / 2)).append(";")
           .append((int) Math.floor(G.getLevel() / 2)).append(";");//Résistances
        str.append(getTeam().getNumber());        
        return str.toString();
    }

    @Override
    public long getXpGive() {
        return 0;
    }

    @Override
    public int getDefaultGfx() {
        return 6000;
    }

    @Override
    public int getPdvMaxOutFight() {
        return collector.getGuild().getLevel() * 100;
    }

    @Override
    public String xpString(String str) {
        return "0" + str + "0" + str + "0";
    }

    @Override
    public boolean isReady() {
        return false;
    }
    
    
}
