/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight.team;

import org.peakemu.world.fight.fighter.MonsterFighter;
import java.util.List;
import org.peakemu.objects.player.Player;
import org.peakemu.world.MapCell;
import org.peakemu.objects.MonsterGroup;
import org.peakemu.objects.Monster;
import org.peakemu.world.fight.fighter.Fighter;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class MonstersTeam extends AbstractTeam{
    final private MonsterGroup group;

    public MonstersTeam(MonsterGroup group, List<MapCell> startCells, int startCellsIndex) {
        super(startCells, startCellsIndex);
        this.group = group;
        initFighters();
    }
    
    private void initFighters(){
        int id = -1;
        for (Monster monster : group.getMobs()) {
            Fighter mob = new MonsterFighter(--id, monster);
            addFighter(mob);
        }
    }
    
    @Override
    public String getId() {
        return group.getSpriteId() + "";
    }

    @Override
    public int getCell() {
        return group.getCell().getID() + 1;
    }

    @Override
    public boolean canAddFighter(Player player) {
        return false;
    }

    @Override
    public int getAlignement() {
        return group.getAlignement();
    }

    @Override
    public int getType() {
        return 1; //?
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void sendToTeam(Object packet) {} //no player on monster team
}
