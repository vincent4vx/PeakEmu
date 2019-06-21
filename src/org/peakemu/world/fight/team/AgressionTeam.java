/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight.team;

import java.util.List;
import org.peakemu.game.in.gameaction.JoinFight;
import org.peakemu.game.out.game.GameActionResponse;
import org.peakemu.objects.player.Player;
import org.peakemu.world.MapCell;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class AgressionTeam extends ChallengeTeam{

    public AgressionTeam(Player player, List<MapCell> startCells, int startCellsIndex) {
        super(player, startCells, startCellsIndex);
    }

    @Override
    public int getAlignement() {
        return getInit().getPlayer().getAlignement();
    }

    @Override
    public boolean canAddFighter(Player player) {
        if(player.getAlignement() != getAlignement()){
            player.send(GameActionResponse.joinFightError(getId(), JoinFight.TEAM_DIFFERENT_ALIGNMENT));
            return false;
        }
        
        return super.canAddFighter(player);
    }
}
