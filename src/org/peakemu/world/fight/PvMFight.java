/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight;

import org.peakemu.world.fight.fighter.Fighter;
import org.peakemu.world.fight.team.MonstersTeam;
import org.peakemu.world.fight.fighter.CollectorFighter;
import org.peakemu.world.fight.team.ChallengeTeam;
import org.peakemu.world.fight.team.FightTeam;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.peakemu.Ancestra;
import org.peakemu.Peak;
import org.peakemu.common.Formulas;
import org.peakemu.common.Logger;
import org.peakemu.common.SocketManager;
import org.peakemu.common.util.Pair;
import org.peakemu.common.util.Util;
import org.peakemu.objects.Collector;
import org.peakemu.objects.player.Player;
import org.peakemu.world.GameMap;
import org.peakemu.world.ItemTemplate;
import org.peakemu.objects.MonsterGroup;
import org.peakemu.world.World;
import org.peakemu.world.World.Drop;
import org.peakemu.world.enums.Effect;
import org.peakemu.world.enums.EndFightTeam;
import org.peakemu.world.enums.FightType;
import org.peakemu.world.handler.fight.FightHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class PvMFight extends DropFight {

    public PvMFight(FightHandler fightHandler, int id, GameMap map, Player init1, MonsterGroup group) {
        super(fightHandler, id, FightType.PVM, map);

        init1.stopAllRegen();
        SocketManager.GAME_SEND_ILF_PACKET(init1, 0);
        init1.getMap().removePlayer(init1);
        
        if(map.containsSprite(group))
            map.removeSprite(group);
        
        group.startFight();

        FightTeam team0 = new ChallengeTeam(init1, parsePlaces(0), 0);
        FightTeam team1 = new MonstersTeam(group, parsePlaces(1), 1);

        addTeam(team0);
        addTeam(team1);

        initFight();
    }

    @Override
    protected FightDrops getDrops(Collection<Fighter> looseFighters, Collection<Fighter> winFighters) {
        //compute prospection
        int groupPP = 0;
        for (Fighter F : winFighters) {
            if (!F.isInvocation() || (F.getMonster() != null)) {
                groupPP += F.getTotalStats().getEffect(Effect.ADD_PROS);
                groupPP += (int) (F.getTotalStats().getEffect(Effect.ADD_CHAN) / 10);
            }
        }

        if (groupPP < 0) {
            groupPP = 0;
        }

        int minkamas = 0;
        int maxkamas = 0;
        long totalXP = 0;
        List<Drop> possibleDrops = new ArrayList<>();

        //get all drops
        for (Fighter F : looseFighters) {
            if (F.isInvocation() || F.getMonster() == null) {
                continue;
            }
            minkamas += F.getMonster().getTemplate().getMinKamas();
            maxkamas += F.getMonster().getTemplate().getMaxKamas();
            totalXP += F.getMonster().getBaseXp();
            for (World.Drop D : F.getMonster().getDrops()) {
                if (D.getMinProsp() <= groupPP) {
                    //On augmente le taux en fonction de la PP
                    float taux = ((groupPP * D.get_taux() * Ancestra.CONFIG_DROP) / 100);
                    possibleDrops.add(new World.Drop(D.get_itemID(), 0, taux, D.get_max()));
                }
            }
        }
        
        Collections.shuffle(possibleDrops); //randomize drops

        return new FightDrops(minkamas, maxkamas, totalXP, possibleDrops);
    }

    /**
     * Add monster capture
     * @param winners
     * @param loosers
     * @param drops
     * @return 
     */
    @Override
    protected Collection<FightRewards> getWinnersRewards(Collection<Fighter> winners, Collection<Fighter> loosers, FightDrops drops) {
        Collection<FightRewards> rewards = super.getWinnersRewards(winners, loosers, drops);
        
        if(World.isArenaMap(get_map().getId()))
            return rewards;
        
        //Capture d'âmes
        boolean mobCapturable = true;
        for (Fighter F : loosers) {
            try {
                mobCapturable &= F.getMonster().getTemplate().isCapturable();
            } catch (Exception e) {
                mobCapturable = false;
                break;
            }
        }
        isCapturable |= mobCapturable;
        
        if(!isCapturable)
            return rewards;

//        if (isCapturable && !World.isArenaMap(get_map().getId())) {
//            boolean isFirst = true;
//            int maxLvl = 0;
//            String pierreStats = "";
//
//            for (Fighter F : loosers) //CrÃ©ation de la pierre et verifie si le groupe peut Ãªtre capturÃ©
//            {
//                if (!isFirst) {
//                    pierreStats += "|";
//                }
//
//                pierreStats += F.getMonster().getTemplate().getID() + "," + F.getLevel();//Converti l'ID du monstre en Hex et l'ajoute au stats de la futur pierre d'Ã¢me
//
//                isFirst = false;
//
//                if (F.getLevel() > maxLvl) //Trouve le monstre au plus haut lvl du groupe (pour la puissance de la pierre)
//                {
//                    maxLvl = F.getLevel();
//                }
//            }
//
//            pierrePleine = new PierreAme("", -1, 1, World.getObjTemplate(7010), Constants.ITEM_POS_NO_EQUIPED, pierreStats);	//CrÃ©e la pierre d'Ã¢me
//
//            for (Fighter F : winners) //RÃ©cupÃ¨re les captureur
//            {
//                if (!F.isInvocation() && F.isState(Constants.ETAT_CAPT_AME)) {
//                    _captureur.add(F);
//                }
//            }
//            
//            if (_captureur.size() > 0 && !World.isArenaMap(get_map().getId())) //S'il y a des captureurs
//            {
//                for (int i = 0; i < _captureur.size(); i++) {
//                    try {
//                        Fighter f = _captureur.get(Formulas.getRandomValue(0, _captureur.size() - 1));	//RÃ©cupÃ¨re un captureur au hasard dans la liste
//                        if (!(f.getPlayer().getItemByPos(Constants.ITEM_POS_ARME).getTemplate().getType() == ItemType.PIERRE_AME)) {
//                            _captureur.remove(f);
//                            continue;
//                        }
//                        World.Couple<Integer, Integer> pierreJoueur = Formulas.decompPierreAme(f.getPlayer().getItemByPos(Constants.ITEM_POS_ARME));//RÃ©cupÃ¨re les stats de la pierre Ã©quippÃ©
//
//                        if (pierreJoueur.second < maxLvl) //Si la pierre est trop faible
//                        {
//                            _captureur.remove(f);
//                            continue;
//                        }
//
//                        int captChance = Formulas.totalCaptChance(pierreJoueur.first, f.getPlayer());
//
//                        if (Formulas.getRandomValue(1, 100) <= captChance) //Si le joueur obtiens la capture
//                        {
//                            //Retire la pierre vide au personnage et lui envoie ce changement
//                            int pierreVide = f.getPlayer().getItemByPos(Constants.ITEM_POS_ARME).getGuid();
//                            f.getPlayer().deleteItem(pierreVide);
//                            SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(f.getPlayer(), pierreVide);
//
//                            captWinner = f.getSpriteId();
//                            break;
//                        }
//                    } catch (NullPointerException e) {
//                        continue;
//                    }
//                }
//            }
//        }
        
        for(FightRewards r : rewards){ //Add the spirit stone on rewards
            if (r.getFighter().getSpriteId() == captWinner && pierrePleine != null){
                r.getFighter().getPlayer().getItems().addItem(pierrePleine);
                r.getDrops().add(new Pair<>(pierrePleine.getTemplate().getID(), 1));
            }
        }
        
        return rewards;
    }
    
    protected Collection<FightRewards> getCollectorRewards(Collection<Fighter> winners, Collection<Fighter> loosers, FightDrops drops){
        Collector p = oldMap.getCollector();
        
        if(p == null)
            return Collections.EMPTY_LIST;
        
        long winxp = (int) Math.floor(Formulas.getXpWinPerco(p, winners, loosers, drops.getTotalXP()) / 5);
        long winkamas = (int) Math.floor(Formulas.getKamasWinPerco(drops.getMinKamas(), drops.getMinKamas()) / 5);

        Map<Integer, Integer> itemWon = new HashMap<>();

        for (Drop D : drops.getDrops()) {
            int t = (int) (D.get_taux() * 100);//Permet de gerer des taux>0.01
            int jet = Util.rand(0, 100 * 100);
            if (jet < t) {
                ItemTemplate OT = World.getObjTemplate(D.get_itemID());
                if (OT == null) {
                    continue;
                }
                //on ajoute a la liste
                itemWon.put(OT.getID(), (itemWon.get(OT.getID()) == null ? 0 : itemWon.get(OT.getID())) + 1);
            }
        }

        Collection<Pair<Integer, Integer>> dropRewards = new ArrayList<>();

        for (Map.Entry<Integer, Integer> entry : itemWon.entrySet()) {
            dropRewards.add(new Pair<>(entry.getKey(), entry.getValue()));
        }

        return Collections.singleton(new FightRewards(
            EndFightTeam.COLLECTOR, 
            new CollectorFighter(p), 
            0, 
            winxp, 
            0, 
            winkamas, 
            0, 
            0, 
            dropRewards
        ));
    }

    @Override
    protected Collection<FightRewards> getAllRewards(Collection<Fighter> winners, Collection<Fighter> loosers, FightDrops drops) {
        Collection<FightRewards> rewards = super.getAllRewards(winners, loosers, drops);
        rewards.addAll(getCollectorRewards(winners, loosers, drops));
        return rewards;
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
        Peak.worldLog.addToLog(Logger.Level.DEBUG, "Applying default end fight actions");
        fightHandler.teleportLoosers(loosers);
        fightHandler.removeEnergy(this, loosers);
    }

    @Override
    protected void applyDefaultEndFightActionsToWinners(Collection<Fighter> winners) {}

    @Override
    protected void applyDefaultEndFightActionsToAll(Collection<Fighter> fighters) {
        fightHandler.setLifePoints(fighters);
        fightHandler.inventoryDammages(fighters);
    }
}
