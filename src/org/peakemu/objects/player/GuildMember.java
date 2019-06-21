/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.objects.player;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.peakemu.objects.Guild;
import org.peakemu.world.enums.GuildRight;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class GuildMember {
    final private Player player;
    final private Guild _guild;
    private int _rank = 0;
    private byte _pXpGive = 0;
    private long _xpGave = 0;
    private int rights = 0;

    public GuildMember(Player player, Guild guild, int rank, long xp, byte xpPer, int rights) {
        this.player = player;
        _guild = guild;
        _rank = rank;
        _xpGave = xp;
        _pXpGive = xpPer;
        this.rights = rights;
    }

    public int getRank() {
        return _rank;
    }

    public Guild getGuild() {
        return _guild;
    }

    public int getRights() {
        return rights;
    }

    public void setRights(int rights) {
        this.rights = rights;
    }

    public long getXpGave() {
        return _xpGave;
    }

    public int getPXpGive() {
        return _pXpGive;
    }

    public int getHoursFromLastCo() {
        String[] strDate = player.getAccount().getLastConnectionDate().split("~");

        LocalDate lastCo = new LocalDate(Integer.parseInt(strDate[0]), Integer.parseInt(strDate[1]), Integer.parseInt(strDate[2]));
        LocalDate now = new LocalDate();

        return Days.daysBetween(lastCo, now).getDays() * 24;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean hasRight(GuildRight right) {
        return (rights & GuildRight.BOSS.toInt()) == GuildRight.BOSS.toInt() 
            || (rights & right.toInt()) == right.toInt();
    }

    public void setRank(int i) {
        _rank = i;
    }

    public void addXp(long xp) {
        this._xpGave += xp;
        this._guild.addXp(xp);
    }

    public void setpXpGive(byte _pXpGive) {
        this._pXpGive = _pXpGive;
    }
}
