/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight.fighter;

import org.peakemu.game.out.game.SpriteParser;
import org.peakemu.objects.player.Player;
import org.peakemu.world.Stats;
import org.peakemu.world.enums.Effect;
import org.peakemu.world.enums.SpriteTypeEnum;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class DoubleFighter extends Fighter{
    final private Player _double;

    public DoubleFighter(Player _double) {
        super(_double.getSpriteId(), _double.getMaxLifePoints(), _double.getLifePoints(), _double.getGfxID());
        this._double = _double;
    }

    @Override
    public Stats getBaseStats() {
        return _double.getTotalStats();
    }

    @Override
    public int getInitiative() {
        return _double.getInitiative();
    }

    @Override
    public int get_lvl() {
        return _double.getLevel();
    }

    @Override
    public String getPacketsName() {
        return _double.getName();
    }

    @Override
    public SpriteTypeEnum getSpriteType() {
        return SpriteTypeEnum.PLAYER;
    }

    @Override
    public String getSpritePacket() {
        StringBuilder str = prepareSpritePacket();
        str.append(_double.getRace()).append(";");
        str.append(_double.getGfxID()).append("^").append(_double.getSize()).append(";");
        str.append(_double.getGender()).append(";");
        str.append(_double.getLevel()).append(";");
        str.append(_double.getAlignement()).append(",");
        str.append("0,");//FIXME:?
        str.append((_double.showWings() ? _double.getAlignLevel() : "0")).append(",");
        str.append(_double.showWings() ? "1" : "0");
        str.append(_double.getSpriteId()).append(";");
        str.append((_double.getColor1() == -1 ? "-1" : Integer.toHexString(_double.getColor1()))).append(";");
        str.append((_double.getColor2() == -1 ? "-1" : Integer.toHexString(_double.getColor2()))).append(";");
        str.append((_double.getColor3() == -1 ? "-1" : Integer.toHexString(_double.getColor3()))).append(";");
        str.append(SpriteParser.stuffToString(_double.getItems())).append(";");
        str.append(getPDV()).append(";");
        str.append(getTotalStats().getEffect(Effect.ADD_PA)).append(";");
        str.append(getTotalStats().getEffect(Effect.ADD_PM)).append(";");
        str.append(getTotalStats().getEffect(Effect.ADD_RP_NEU)).append(";");
        str.append(getTotalStats().getEffect(Effect.ADD_RP_TER)).append(";");
        str.append(getTotalStats().getEffect(Effect.ADD_RP_FEU)).append(";");
        str.append(getTotalStats().getEffect(Effect.ADD_RP_EAU)).append(";");
        str.append(getTotalStats().getEffect(Effect.ADD_RP_AIR)).append(";");
        str.append(getTotalStats().getEffect(Effect.ADD_AFLEE)).append(";");
        str.append(getTotalStats().getEffect(Effect.ADD_MFLEE)).append(";");
        str.append(getTeam().getNumber()).append(";");
        if (_double.isOnMount() && _double.getMount() != null) {
            str.append(_double.getMount().get_color(_double.parsecolortomount())); //didne caméléone
        }
        str.append(";");   
        return str.toString();
    }

    @Override
    public boolean isDouble() {
        return true;
    }

    @Override
    public long getXpGive() {
        return 0;
    }

    @Override
    public int getDefaultGfx() {
        return _double.getGfxID();
    }

    @Override
    public int getPdvMaxOutFight() {
        return _double.getMaxLifePoints();
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
