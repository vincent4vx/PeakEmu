/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight;

import org.peakemu.world.fight.fighter.Fighter;
import java.util.Collection;
import org.peakemu.common.Constants;
import org.peakemu.objects.Prism;
import org.peakemu.objects.player.Player;
import org.peakemu.world.ExpLevel;
import org.peakemu.world.GameMap;
import org.peakemu.world.World;
import org.peakemu.world.enums.FightType;
import org.peakemu.world.fight.fighter.PlayerFighter;
import org.peakemu.world.fight.fighter.PrismFighter;
import org.peakemu.world.handler.ExpHandler;
import org.peakemu.world.handler.fight.FightHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
abstract public class HonourFight extends Fight{
    final private ExpHandler expHandler;

    public HonourFight(ExpHandler expHandler, FightHandler fightHandler, int id, FightType type, GameMap map) {
        super(fightHandler, id, type, map);
        this.expHandler = expHandler;
    }

    @Override
    protected String parseRewards(FightRewards rewards) {
        StringBuilder sb = new StringBuilder(128);
        
        sb.append(rewards.getTeam().getId()).append(";")
            .append(rewards.getFighter().getSpriteId()).append(";")
            .append(rewards.getFighter().getPacketsName()).append(";")
            .append(rewards.getFighter().get_lvl()).append(";")
            .append((rewards.getFighter().isDead() ? "1" : "0")).append(";");
        
        
        int minHonour = 0;
        int curHonour = 0;
        int maxHonour = 0;
        int grade = 0;
        int disgrace = 0;
        
        if(rewards.getFighter() instanceof PlayerFighter){
            Player player = rewards.getFighter().getPlayer();
            
            if(player.getAlignement() != Constants.ALIGNEMENT_NEUTRE){
                grade = player.getGrade();
                disgrace = player.getDeshonor();
                curHonour = player.getHonor();
            }
        }else if(rewards.getFighter() instanceof PrismFighter){
            Prism prism = rewards.getFighter().getPrism();
            grade = prism.getLevel();
            curHonour = prism.getHonor();
        }
        
        if(grade > 0){
            ExpLevel expLevel = expHandler.getLevel(grade);
            minHonour = expLevel.pvp;
            maxHonour = expLevel.getNext().pvp == -1 ? minHonour : expLevel.getNext().pvp;
        }
        
        sb.append(minHonour).append(";");
        sb.append(curHonour).append(";");
        sb.append(maxHonour).append(";");
        sb.append(rewards.getWinHonour()).append(";");
        sb.append(grade).append(";");
        sb.append(disgrace).append(";");
        sb.append(rewards.getWinDisgrace());
        sb.append(";;0;0;0;0;0"); //TODO: rest of rewards
        
        return sb.toString();
    }

    @Override
    public boolean canCancel() {
        return false;
    }

    @Override
    protected void applyDefaultEndFightActionsToLoosers(Collection<Fighter> loosers) {
        fightHandler.removeEnergy(this, loosers);
        fightHandler.jailLoosers(this, loosers);
    }

    @Override
    protected void applyDefaultEndFightActionsToWinners(Collection<Fighter> winners) {}

    @Override
    protected void applyDefaultEndFightActionsToAll(Collection<Fighter> fighters) {
        fightHandler.setLifePoints(fighters);
        fightHandler.inventoryDammages(fighters);
    }

    @Override
    protected int getGESheetType() {
        return 1;
    }
}
