/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.objects.player;

import org.peakemu.game.out.game.SpriteParser;
import org.peakemu.world.MapCell;
import org.peakemu.world.Stats;
import org.peakemu.world.enums.Effect;
import org.peakemu.world.enums.PlayerRace;
import org.peakemu.world.enums.SpriteTypeEnum;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class BaseCharacter implements PlayerData{
    final private Player player;
    final private PlayerRace race;
    final private SpellBook spellBook;
    final private Stats baseStats;
    private int gfxID;

    public BaseCharacter(Player player, PlayerRace race, SpellBook spellBook, Stats baseStats, int gfxID) {
        this.player = player;
        this.race = race;
        this.spellBook = spellBook;
        this.baseStats = baseStats;
        this.gfxID = gfxID;
    }

    public PlayerRace getRace() {
        return race;
    }

    @Override
    public int getGfxID() {
        return gfxID;
    }

    public void setGfxID(int gfxID) {
        this.gfxID = gfxID;
    }

    @Override
    public SpellBook getSpellBook() {
        return spellBook;
    }

    @Override
    public SpriteTypeEnum getSpriteType() {
        return SpriteTypeEnum.PLAYER;
    }

    @Override
    public String getSpritePacket() {
        StringBuilder str = new StringBuilder();
        str.append(getCell().getID()).append(";").append(player.getOrientation()).append(";");
        str.append(getSpriteType().toInt()).append(";");//FIXME:?
        str.append(getSpriteId()).append(";").append(player.getName()).append(";").append(race.ordinal());

       //30^100,1247;
        //FIXME pnj suiveur ? 
        str.append(player.get_title()).append(";");
        str.append(getGfxID()).append("^").append(player.getSize()) //gfxID^size //FIXME ,GFXID pnj suiveur
                .append(",").append("") //followers
                //.append(",").append("1247") // mob suvieur1
                //.append(",").append("1503") //mob suiveur2
                //.append(",").append("1451") //mob suiveur 3
                //.append(",").append("1186") // mob suiveur 4
                //.append(",").append("8013") // MS5
                //.append(",").append("8018") // MS6
                //.append(",").append("8017") // MS7 ... Infini quoi
                .append(";");
        str.append(player.getGender()).append(";");
        str.append(player.getAlignement()).append(",");
        str.append("0").append(",");//FIXME:?
        str.append((player.showWings() ? player.getGrade() : "0")).append(",");
        str.append(player.getLevel() + getSpriteId());
        if (player.showWings() && player.getDisgrace() > 0) {
            str.append(",");
            str.append(player.getDisgrace() > 0 ? 1 : 0).append(';');
        } else {
            str.append(";");
        }
        //str.append(_lvl).append(";");
        str.append((player.getColor1() == -1 ? "-1" : Integer.toHexString(player.getColor1()))).append(";");
        str.append((player.getColor2() == -1 ? "-1" : Integer.toHexString(player.getColor2()))).append(";");
        str.append((player.getColor3() == -1 ? "-1" : Integer.toHexString(player.getColor3()))).append(";");
        str.append(SpriteParser.stuffToString(player.getItems())).append(";");
        str.append((player.getLevel() > 99 ? (player.getLevel() > 199 ? (2) : (1)) : (0))).append(";"); //TODO: aura system
        str.append(";");//Emote
        str.append(";");//Emote timer
        if (player.getGuild() != null && player.getGuild().getMembers().size() > 9)//>9TODO:
        {
            str.append(player.getGuild().getName()).append(";").append(player.getGuild().getEmbem()).append(";");
        } else {
            str.append(";;");
        }
        str.append(Integer.toString(player.getRestrictions().toInt(), 36)).append(";");//Restriction
        str.append((player.isOnMount() ? player.getMount().get_color(player.parsecolortomount()) : "")).append(";");
        str.append(";");
        return str.toString();
    }

    @Override
    public int getSpriteId() {
        return player.getId();
    }

    @Override
    public MapCell getCell() {
        return player.getCell();
    }

    @Override
    public Stats getBaseStats() {
        return baseStats;
    }

    @Override
    public int getInitiative() {
        int fact = 4;
        int pvmax = player.getMaxLifePoints();
        int pv = player.getLifePoints();
        
        if (race == PlayerRace.SACRIEUR) {
            fact = 8;
        }
        
        Stats totalStats = player.getTotalStats();
        
        double coef = pvmax / fact;

        coef += totalStats.getEffect(Effect.ADD_INIT);
        coef += totalStats.getEffect(Effect.ADD_AGIL);
        coef += totalStats.getEffect(Effect.ADD_CHAN);
        coef += totalStats.getEffect(Effect.ADD_INTE);
        coef += totalStats.getEffect(Effect.ADD_FORC);

        int init = 1;
        if (pvmax != 0) {
            init = (int) (coef * ((double) pv / (double) pvmax));
        }
        if (init < 0) {
            init = 0;
        }
        return init;
    }

    @Override
    public Stats getRaceStats() {
        return race.getStartStats(player.getLevel());
    }

    @Override
    public boolean canHaveMount() {
        return player.getLevel() >= 60;
    }

    @Override
    public boolean canHaveStuff() {
        return true;
    }

    @Override
    public boolean canLearnSpells() {
        return true;
    }
}
