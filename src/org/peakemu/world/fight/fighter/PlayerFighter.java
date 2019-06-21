/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight.fighter;

import org.peakemu.common.SocketManager;
import org.peakemu.game.out.game.SpriteParser;
import org.peakemu.objects.player.Player;
import org.peakemu.world.Stats;
import org.peakemu.world.enums.Effect;
import org.peakemu.world.enums.SpriteTypeEnum;
import org.peakemu.world.fight.Fight;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class PlayerFighter extends Fighter{
    final private Player player;
    private boolean ready = false;

    public PlayerFighter(Player player) {
        super(player.getSpriteId(), player.getMaxLifePoints(), player.getLifePoints(), player.getGfxID());
        this.player = player;
    }

    @Override
    public void setFight(Fight fight) {
        super.setFight(fight);
        player.setFight(fight);
    }

    @Override
    public Stats getBaseStats() {
        return player.getTotalStats();
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public int getInitiative() {
        return player.getInitiative();
    }

    @Override
    public int get_lvl() {
        return player.getLevel();
    }

    @Override
    public String getPacketsName() {
        if(player.isMutant()){
            if(getSpriteType() == SpriteTypeEnum.MUTANT)
                return getPlayer().getMutant().getMonster().getID() + "";

            return getPlayer().getMutant().getMonster().getID() + "~" + getPlayer().getName();
        }
        
        return player.getName();
    }

    @Override
    public SpriteTypeEnum getSpriteType() {
        return player.getSpriteType();
    }

    @Override
    public String getSpritePacket() {
        StringBuilder str = prepareSpritePacket();
        
        if(player.isMutant()){
            str.append(getSpriteType().toInt()).append(';')
              .append(getGfxID()).append('^').append(getPlayer().getSize()).append(';')
              .append(player.getGender()).append(';')
              .append(player.getMutant().getGrade().getGrade()).append(';') //grade monster
              .append(SpriteParser.stuffToString(player.getItems())).append(';')
              .append(getPDVMAX()).append(';')
              .append(getPA()).append(';')
              .append(getPM()).append(';')
              .append(";;;;;;;")
              .append(getTeam().getNumber())
            ;        

            return str.toString();
        }

        str.append(player.getRace().ordinal()).append(";");
        str.append(getGfxID()).append("^").append(player.getSize()).append(";");
        str.append(player.getGender()).append(";");
        str.append(player.getLevel()).append(";");
        
        //ALIGNEMENT -------------------
        str.append(player.getAlignement()).append(",");
        str.append("0").append(",");
        str.append((player.showWings() ? player.getGrade() : "0")).append(",");
        str.append(player.getLevel() + player.getSpriteId());
        if (player.showWings() && player.getDisgrace() > 0) {
            str.append(",");
            str.append(player.getDisgrace() > 0 ? 1 : 0).append(';');
        } else {
            str.append(";");
        }
        // ALIGNEMENT --------------------
        
        str.append((player.getColor1() == -1 ? "-1" : Integer.toHexString(player.getColor1()))).append(";");
        str.append((player.getColor2() == -1 ? "-1" : Integer.toHexString(player.getColor2()))).append(";");
        str.append((player.getColor3() == -1 ? "-1" : Integer.toHexString(player.getColor3()))).append(";");
        str.append(SpriteParser.stuffToString(player.getItems())).append(";");
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
        if (player.isOnMount() && player.getMount() != null) {
            str.append(player.getMount().get_color(player.parsecolortomount())); //didne camé
        }
        str.append(";");
        return str.toString();
    }

    @Override
    public long getXpGive() {
        return 0;
    }

    @Override
    public int getDefaultGfx() {
        return player.getGfxID();
    }

    @Override
    public int getPdvMaxOutFight() {
        return player.getMaxLifePoints();
    }

    @Override
    public void addBuff(Effect effect, int val, int duration, int turns, boolean debuff, int spellID, String args, Fighter caster) {
        super.addBuff(effect, val, duration, turns, debuff, spellID, args, caster);
        SocketManager.GAME_SEND_STATS_PACKET(player);
    }

    @Override
    public String xpString(String str) {
        long min = player.getExpLevel().player;
        long cur = player.getCurExp();
        long max = player.getExpLevel().getNext() != null ? player.getExpLevel().getNext().player : -1;
        
        return min + str + cur + str + max;
    }

    @Override
    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }
}
