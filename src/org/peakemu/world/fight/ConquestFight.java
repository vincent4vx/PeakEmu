/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight;

import org.peakemu.world.fight.fighter.Fighter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.peakemu.common.SocketManager;
import org.peakemu.game.out.conquest.PrismAttacked;
import org.peakemu.game.out.conquest.PrismDead;
import org.peakemu.game.out.conquest.PrismSurvived;
import org.peakemu.objects.Prism;
import org.peakemu.objects.player.Player;
import org.peakemu.world.GameMap;
import org.peakemu.world.enums.EndFightTeam;
import org.peakemu.world.enums.FightType;
import org.peakemu.world.fight.fighter.PlayerFighter;
import org.peakemu.world.fight.fighter.PrismFighter;
import org.peakemu.world.fight.team.AgressionTeam;
import org.peakemu.world.fight.team.FightTeam;
import org.peakemu.world.fight.team.PrismTeam;
import org.peakemu.world.handler.AlignementHandler;
import org.peakemu.world.handler.ExpHandler;
import org.peakemu.world.handler.fight.FightHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class ConquestFight extends HonourFight{
    final private Prism prism;
    final private AlignementHandler alignementHandler;
    
    final private PrismTeam prismTeam;
    final private AgressionTeam attackersTeam;

    public ConquestFight(ExpHandler expHandler, AlignementHandler alignementHandler, FightHandler fightHandler, int id, GameMap map, Player player, Prism prism) {
        super(expHandler, fightHandler, id, FightType.CONQUETE, map);
        
        this.alignementHandler = alignementHandler;
        this.prism = prism;
        prism.setFight(this);
        map.removeSprite(prism);
        
        player.stopAllRegen();
        SocketManager.GAME_SEND_ILF_PACKET(player, 0);
        map.removePlayer(player);
        
        prismTeam = new PrismTeam(prism, parsePlaces(0), 0);
        attackersTeam = new AgressionTeam(player, parsePlaces(1), 1);
        
        addTeam(prismTeam);
        addTeam(attackersTeam);
        
        initFight();
        alignementHandler.sendToAlignement(new PrismAttacked(prism), prism.getAlignement());
    }
    
    public FightTeam getDefendersTeam(){
        return prismTeam;
    }

    public AgressionTeam getAttackersTeam() {
        return attackersTeam;
    }

    public Prism getPrism() {
        return prism;
    }

    @Override
    public boolean joinTeam(Player player, FightTeam team) {
        if(!super.joinTeam(player, team))
            return false;
        
        if(team == prismTeam)
            alignementHandler.sendAddPrismDefender(prism, player);
        else
            alignementHandler.sendAddPrismAttacker(prism, player);
        
        return true;
    }
    
    private int computeWinHonour(Collection<Fighter> winners, Collection<Fighter> loosers){
        int totalWinnersGrade = 0;
        int totalWinnersLevel = 0;
        int totalLoosersGrade = 0;
        int totalLoosersLevel = 0;
        
        for (Fighter looser : loosers) {
            if(looser.getPlayer() != null){
                totalLoosersGrade += looser.getPlayer().getGrade();
                totalLoosersLevel += looser.getPlayer().getLevel();
            }else if(looser.getPrism() != null){
                totalLoosersGrade += 100 + looser.getPrism().getLevel() * 15;
                totalLoosersLevel += 200;
            }
        }
        
        for (Fighter winner : winners) {
            if(winner.getPlayer() != null){
                totalWinnersGrade += winner.getPlayer().getGrade();
                totalWinnersLevel += winner.getPlayer().getLevel();
            }else if(winner.getPrism() != null){
                totalWinnersGrade += 100 + winner.getPrism().getLevel() * 15;
                totalWinnersLevel += 200;
            }
        }
        
        double rate = (double)(totalLoosersGrade * totalLoosersLevel) / (double)(totalWinnersGrade * totalWinnersLevel);
        rate /= winners.size();
        
        return (int) (100 * rate * fightHandler.getRates().getHonour());
    }

    @Override
    protected Collection<FightRewards> getAllRewards(Collection<Fighter> winners, Collection<Fighter> loosers) {
        Collection<FightRewards> rewards = new ArrayList<>(winners.size() + loosers.size());
        
        final int baseWinHonour = computeWinHonour(winners, loosers);
        
        for(Fighter winner : winners){
            if((winner instanceof PlayerFighter)){
                Player player = winner.getPlayer();
                
                if(player.getDisgrace() > 0){
                    rewards.add(new FightRewards(EndFightTeam.WINNER, winner, 0, 0, 0, 0, 0, -1, Collections.EMPTY_LIST));
                }else{
                    rewards.add(new FightRewards(EndFightTeam.WINNER, winner, 0, 0, 0, 0, baseWinHonour, 0, Collections.EMPTY_LIST));
                }
            }else if(winner instanceof PrismFighter){
                int winHonour = (int) (1.5 * baseWinHonour);
                prism.addHonor(winHonour);
                rewards.add(new FightRewards(EndFightTeam.WINNER, winner, 0, 0, 0, 0, winHonour, 0, Collections.EMPTY_LIST));
            }
        }
        
        for(Fighter looser : loosers){
            if(looser instanceof PlayerFighter){
                Player player = looser.getPlayer();
                
                int looseHonour = 2 * baseWinHonour;
                
                if(looseHonour > player.getHonor())
                    looseHonour = player.getHonor();
                
                rewards.add(new FightRewards(EndFightTeam.LOOSER, looser, 0, 0, 0, 0, -looseHonour, 0, Collections.EMPTY_LIST));
            }else{ //do not change for prism in loosers : it will be deleted after
                rewards.add(new FightRewards(EndFightTeam.LOOSER, looser, 0, 0, 0, 0, 0, 0, Collections.EMPTY_LIST));
            }
        }
        
        return rewards;
    }

    @Override
    public int getPlacementTime() {
        return fightHandler.getConfig().getConquestPlacementTime();
    }

    @Override
    protected void applyDefaultEndFightActionsToLoosers(Collection<Fighter> loosers) {
        super.applyDefaultEndFightActionsToLoosers(loosers);
        for(Fighter F : loosers){
            if (F.getPrism() == prism) { //prism loose
                alignementHandler.sendToAlignement(new PrismDead(prism), prism.getAlignement());
                alignementHandler.removePrism(prism);
            }
        }
    }

    @Override
    protected void applyDefaultEndFightActionsToWinners(Collection<Fighter> winners) {
        super.applyDefaultEndFightActionsToWinners(winners);
        for(Fighter F : winners){
            if (F.getPrism() == prism) { //prism win
                alignementHandler.sendToAlignement(new PrismSurvived(prism), prism.getAlignement());
                prism.setFight(null);
                prism.getMap().addSprite(prism);
            }
        }
    }

    @Override
    protected void applyDefaultEndFightActionsToAll(Collection<Fighter> fighters) {
        super.applyDefaultEndFightActionsToAll(fighters);
        alignementHandler.removeAllPrismFighters(this);
    }
}
