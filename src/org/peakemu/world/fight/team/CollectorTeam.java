/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight.team;

import java.util.List;
import org.peakemu.common.Constants;
import org.peakemu.objects.Collector;
import org.peakemu.objects.player.Player;
import org.peakemu.world.MapCell;
import org.peakemu.world.fight.fighter.CollectorFighter;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class CollectorTeam extends AbstractTeam{
    final private Collector collector;

    public CollectorTeam(Collector collector, List<MapCell> startCells, int startCellsIndex) {
        super(startCells, startCellsIndex);
        this.collector = collector;
        addFighter(new CollectorFighter(collector));
    }

    @Override
    public String getId() {
        return collector.getSpriteId() + "";
    }

    @Override
    public int getCell() {
        return collector.getCell().getID();
    }

    @Override
    public boolean canAddFighter(Player player) {
        return player.getGuild() == collector.getGuild();
    }

    @Override
    public int getAlignement() {
        return Constants.ALIGNEMENT_NONE;
    }

    @Override
    public int getType() {
        return 3;
    }

    public Collector getCollector() {
        return collector;
    }

    @Override
    public boolean isReady() {
        return false; //cannot start fight before end of time
    }

}
