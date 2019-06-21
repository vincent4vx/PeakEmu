/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight.team;

import java.util.List;
import org.peakemu.common.Constants;
import org.peakemu.objects.player.Player;
import org.peakemu.world.MapCell;
import org.peakemu.world.fight.fighter.PlayerFighter;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class MutantTeam extends AbstractTeam{
    final private Player init;

    public MutantTeam(Player init, List<MapCell> startCells, int startCellsIndex) {
        super(startCells, startCellsIndex);
        this.init = init;
        PlayerFighter fighter = new PlayerFighter(init);
        init.setFighter(fighter);
        addFighter(fighter);
    }

    @Override
    public String getId() {
        return init.getSpriteId() + "";
    }

    @Override
    public int getCell() {
        return init.getCell().getID();
    }

    @Override
    public boolean canAddFighter(Player player) {
        return player.isMutant();
    }

    @Override
    public int getAlignement() {
        return Constants.ALIGNEMENT_NONE;
    }

    @Override
    public int getType() {
        return 1; //TODO: real mutant team
    }
    
}
