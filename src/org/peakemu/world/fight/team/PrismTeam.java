/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight.team;

import java.util.List;
import org.peakemu.game.in.gameaction.JoinFight;
import org.peakemu.game.out.game.GameActionResponse;
import org.peakemu.objects.Prism;
import org.peakemu.objects.player.Player;
import org.peakemu.world.MapCell;
import org.peakemu.world.fight.fighter.PrismFighter;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class PrismTeam extends AbstractTeam{
    final private Prism prism;

    public PrismTeam(Prism prism, List<MapCell> startCells, int startCellsIndex) {
        super(startCells, startCellsIndex);
        this.prism = prism;
        addFighter(new PrismFighter(prism));
    }

    @Override
    public String getId() {
        return prism.getSpriteId() + "";
    }

    @Override
    public int getCell() {
        return prism.getCell().getID();
    }

    @Override
    public boolean canAddFighter(Player player) {
        if(player.getAlignement() != prism.getAlignement()){
            player.send(GameActionResponse.joinFightError(getId(), JoinFight.TEAM_DIFFERENT_ALIGNMENT));
            return false;
        }
        
        return true;
    }

    @Override
    public int getAlignement() {
        return prism.getAlignement();
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public boolean isReady() {
        return false;
    }
    
}
