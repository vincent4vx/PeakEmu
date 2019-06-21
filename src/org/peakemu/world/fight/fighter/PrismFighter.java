/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight.fighter;

import org.peakemu.common.Constants;
import org.peakemu.objects.Prism;
import org.peakemu.world.Stats;
import org.peakemu.world.enums.SpriteTypeEnum;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class PrismFighter extends Fighter{
    final private Prism prism;

    public PrismFighter(Prism prism) {
        super(-1, prism.getLevel() * 10000, prism.getLevel() * 10000, prism.getAlignement() == Constants.ALIGNEMENT_BONTARIEN ? 8101 : 8100);
        this.prism = prism;
        prism.refreshStats();
    }

    @Override
    public Stats getBaseStats() {
        return prism.getStats();
    }

    @Override
    public Prism getPrism() {
        return prism;
    }

    @Override
    public int getInitiative() {
        return 0;
    }

    @Override
    public int get_lvl() {
        return prism.getLevel();
    }

    @Override
    public String getPacketsName() {
        return (prism.getAlignement() == Constants.ALIGNEMENT_BONTARIEN ? 1111 : 1112) + "";
    }

    @Override
    public SpriteTypeEnum getSpriteType() {
        return SpriteTypeEnum.MONSTER;
    }

    @Override
    public String getSpritePacket() {
        StringBuilder str = prepareSpritePacket();
        str.append(getSpriteType().toInt()).append(";");
        str.append((prism.getAlignement() == 1 ? 8101 : 8100) + "^100;");
        str.append(prism.getLevel() + ";");
        str.append("-1;-1;-1;");
        str.append("0,0,0,0;");
        str.append(getPDVMAX() + ";");
        str.append(0 + ";");
        str.append(0 + ";");
        str.append(getTeam().getNumber());        
        return str.toString();
    }

    @Override
    public long getXpGive() {
        return 0;
    }

    @Override
    public int getDefaultGfx() {
        return prism.getAlignement() == Constants.ALIGNEMENT_BONTARIEN ? 8101 : 8100;
    }

    @Override
    public int getPdvMaxOutFight() {
        return prism.getLevel() * 10000;
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
