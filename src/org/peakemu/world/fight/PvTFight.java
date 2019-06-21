/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight;

import org.peakemu.world.fight.fighter.Fighter;
import org.peakemu.world.fight.team.FightTeam;
import org.peakemu.world.fight.team.ChallengeTeam;
import org.peakemu.world.fight.team.CollectorTeam;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.peakemu.Peak;
import org.peakemu.common.Logger;
import org.peakemu.common.SocketManager;
import org.peakemu.game.out.guild.CollectorAttacked;
import org.peakemu.game.out.guild.CollectorAttackers;
import org.peakemu.game.out.guild.CollectorDefenders;
import org.peakemu.game.out.guild.InfosTaxCollectorsMovement;
import org.peakemu.objects.Collector;
import org.peakemu.objects.Guild;
import org.peakemu.objects.player.Player;
import org.peakemu.world.GameMap;
import org.peakemu.world.World.Drop;
import org.peakemu.world.enums.FightType;
import org.peakemu.world.handler.fight.FightHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class PvTFight extends DropFight {
    final private Guild guild;
    final private Collector collector;
    final private CollectorTeam collectorTeam;
    
    final private Collection<Player> defenders = new HashSet<>();
    final private ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();

    public PvTFight(FightHandler fightHandler, int id, GameMap map, Player perso, Collector collector) {
        super(fightHandler, id, FightType.PVT, map);
        this.collector = collector;
        this.guild = collector.getGuild();
        
        collector.set_inFight((byte) 1);
        collector.setFight(this);
        map.removeSprite(collector);
        
        //on desactive le timer de regen coté client
        perso.stopAllRegen();
        SocketManager.GAME_SEND_ILF_PACKET(perso, 0);
        map.removePlayer(perso);
        
        collectorTeam = new CollectorTeam(collector, parsePlaces(0), 0);
        
        addTeam(collectorTeam);
        addTeam(new ChallengeTeam(perso, parsePlaces(1), 1));
        
        guild.sendToMembers(new InfosTaxCollectorsMovement(guild));
        guild.sendToMembers(new CollectorAttackers(collector));
        guild.sendToMembers(new CollectorDefenders(collector));
        guild.sendToMembers(new CollectorAttacked(collector, CollectorAttacked.TAX_ATTACKED));
        
        initFight();
        defendersJoinFight();
    }
    
    private void defendersJoinFight(){
        ses.schedule(new Runnable() {
            @Override
            public void run() {
                Peak.worldLog.addToLog(Logger.Level.DEBUG, "Teleport all defender into PvTFight");
                for(Player player : defenders){
                    player.setMap(collector.getMap());
                    player.setCell(collector.getCell());
                    joinTeam(player, collectorTeam);
                }
            }
        }, getPlacementTime() - 5000, TimeUnit.MILLISECONDS);
    }

    @Override
    void startFight() {
        collector.set_inFight((byte)2);
        guild.sendToMembers(new InfosTaxCollectorsMovement(guild));
        guild.sendToMembers(new CollectorAttackers(collector));
        guild.sendToMembers(new CollectorDefenders(collector));
        super.startFight(); //To change body of generated methods, choose Tools | Templates.
    }
    
    public Collection<Fighter> getAttackers(){
        Collection<Fighter> attackers = new ArrayList<>();
        
        for(FightTeam team : getTeams()){
            if(team instanceof CollectorTeam)
                continue;
            
            attackers.addAll(team.getFighters());
        }
        
        return attackers;
    }
    
    public Collection<Player> getDefenders(){
        return defenders;
    }
    
    public void addDefender(Player defender){
        if(defenders.size() >= collectorTeam.getStartCells().size() - 1)
            return;
        
        Peak.worldLog.addToLog(Logger.Level.DEBUG, "Adding defender : %s", defender.getName());
        if(defenders.add(defender)){
            guild.sendToMembers(new InfosTaxCollectorsMovement(guild));
            guild.sendToMembers(new CollectorAttackers(collector));
            guild.sendToMembers(new CollectorDefenders(collector));
            defender.setAway(true);
        }
    }
    
    public void removeDefender(Player defender){
        if(defenders.remove(defender)){
            guild.sendToMembers(new InfosTaxCollectorsMovement(guild));
            guild.sendToMembers(new CollectorAttackers(collector));
            guild.sendToMembers(new CollectorDefenders(collector));
            defender.setAway(false);
        }
    }

    public CollectorTeam getCollectorTeam() {
        return collectorTeam;
    }

    @Override
    protected FightDrops getDrops(Collection<Fighter> looseFighters, Collection<Fighter> winFighters) {
        for(Fighter fighter : looseFighters){
            if(!fighter.isPerco()){
                continue;
            }
            
            int minkamas = (int) Math.ceil(fighter.getCollector().getKamas() / winFighters.size());//Les kamas sont divisé en fonction du nombre de joueurs
            int maxkamas = minkamas;
            Collection<Drop> possibleDrops = new ArrayList(fighter.getCollector().getDrops());
            return new FightDrops(minkamas, maxkamas, 0, possibleDrops);
        }
        
        //no collector on the team
        return FightDrops.EMPTY_DROPS;
    }

    @Override
    public boolean canCancel() {
        return false;
    }

    @Override
    public int getPlacementTime() {
        return fightHandler.getConfig().getDefaultPlacementTime();
    }

    @Override
    protected void applyDefaultEndFightActionsToLoosers(Collection<Fighter> loosers) {
        fightHandler.removeEnergy(this, loosers);
        fightHandler.teleportLoosers(loosers);
        fightHandler.collectorLoose(loosers);
    }

    @Override
    protected void applyDefaultEndFightActionsToWinners(Collection<Fighter> winners) {
        for (Fighter F : winners) {
            if (F.isPerco()) { //collector win
                guild.sendToMembers(new InfosTaxCollectorsMovement(guild));
                guild.sendToMembers(new CollectorAttacked(collector, CollectorAttacked.TAX_ATTACKED_SUVIVED));
                
                F.getCollector().set_inFight((byte) 0);
                F.getCollector().setFight(null);
                F.getCollector().clearDefenseFight();
                getOldMap().addSprite(collector);
            }
        }
    }

    @Override
    protected void applyDefaultEndFightActionsToAll(Collection<Fighter> fighters) {
        fightHandler.setLifePoints(fighters);
        fightHandler.inventoryDammages(fighters);
    }
}
