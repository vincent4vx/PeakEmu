/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight.team;

import java.util.List;
import java.util.Set;
import org.peakemu.objects.player.Player;
import org.peakemu.world.MapCell;
import org.peakemu.world.fight.fighter.Fighter;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public interface FightTeam {
    public String getId();
    public int getNumber();
    public void setNumber(int number);
    public int getCell();
    public Set<Fighter> getFighters();
    public boolean canAddFighter(Player player);
    public int getAlignement();
    public int getType();
    public boolean isReady();
    public List<MapCell> getStartCells();
    public int getStartCellsIndex();
    public void addFighter(Fighter fighter);
    public boolean isAllDead();
    public void removeFighter(Fighter fighter);
    public boolean containsFighter(Fighter fighter);
    public void sendToTeam(Object packet);
    public int getSize();
}
