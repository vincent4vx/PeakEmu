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
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicReference;
import org.peakemu.common.Formulas;
import org.peakemu.common.util.Pair;
import org.peakemu.world.GameMap;
import org.peakemu.world.ItemTemplate;
import org.peakemu.world.World;
import org.peakemu.world.enums.Effect;
import org.peakemu.world.enums.EndFightTeam;
import org.peakemu.world.enums.FightType;
import org.peakemu.world.handler.fight.FightHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
abstract public class DropFight extends Fight{

    public DropFight(FightHandler fightHandler, int id, FightType type, GameMap map) {
        super(fightHandler, id, type, map);
    }
    
    abstract protected FightDrops getDrops(Collection<Fighter> looseFighters, Collection<Fighter> winFighters);
    
    protected Collection<FightRewards> getWinnersRewards(Collection<Fighter> winners, Collection<Fighter> loosers, FightDrops drops){
        Collection<FightRewards> rewards = new ArrayList<>(winners.size());
        
        //create a new scope
        {
            //On Réordonne la liste en fonction de la PP
            //cannot order directly winFighters, because it's not a List
            ArrayList<Fighter> tmp = new ArrayList<>(winners);
            Collections.sort(tmp, new Comparator<Fighter>() {
                private int pp(Fighter F){
                    return F.getTotalStats().getEffect(Effect.ADD_PROS) + ((int) (F.getTotalStats().getEffect(Effect.ADD_CHAN) / 10));
                }

                @Override
                public int compare(Fighter t, Fighter t1) {
                    return pp(t1) - pp(t);
                }
            });

            winners = tmp;
        }
        
        List<World.Drop> dropList = new ArrayList<>(drops.getDrops());
        Collections.shuffle(dropList);
        
        for(Fighter fighter : winners){            
            //retire les invoc, sauf coffre enu
            if(fighter.isInvocation() && fighter.getMonster() != null && fighter.getMonster().getTemplate().getID() != 258)
                continue;
            
            /**
             * Objecti quête "Vaincre xNbre MobId en 1 seul combat" *
             */
            if (fighter.getPlayer() != null) // C'est bien un joueur 
            {
                Map<Integer, Integer> mobs = new HashMap<Integer, Integer>();
                for (Fighter mob : loosers) // On liste les mobs battus & leur nombre
                {
                    if (mob.getMonster() != null) // C'est bien un mob
                    {
                        if (mobs.get(mob.getMonster().getTemplate().getID()) != null) // Mob déjà repertorié
                        {
                            mobs.put(mob.getMonster().getTemplate().getID(), // Id
                                mobs.get(mob.getMonster().getTemplate().getID()) + 1); // Quantite
                        } else {
                            mobs.put(mob.getMonster().getTemplate().getID(), 1);
                        }
                    }
                }
                String mobsWon = "";
                for (Map.Entry<Integer, Integer> entry : mobs.entrySet()) // On transforme en String
                {
                    mobsWon += entry.getKey() + "," + entry.getValue() + ";";
                }
//                fighter.getPlayer().confirmObjective(6, mobsWon, null);
            }
            /**
             * Fin Objectif Quête "Vaincre x Mob" *
             */

            long winxp = Formulas.getXpWinPvm2(fighter, winners, loosers, drops.getTotalXP());
            AtomicReference<Long> XP = new AtomicReference<Long>();
            XP.set(winxp);

            long guildxp = Formulas.getGuildXpWin(fighter, XP);
            long mountxp = 0;

            if (fighter.getPlayer() != null && fighter.getPlayer().isOnMount()) {
                mountxp = Formulas.getMountXpWin(fighter, XP);
            }
            
            int winKamas = Formulas.getKamasWin(fighter, winners, drops.getMinKamas(), drops.getMaxKamas());
            
            Map<Integer, Integer> itemWon = new TreeMap<Integer, Integer>();
            int k = 0;
            int maxItemCount = (int) Math.floor(dropList.size() / winners.size());
            
            Iterator<World.Drop> it = dropList.iterator();
            
            while(it.hasNext() && k <= maxItemCount){
                World.Drop D = it.next();
                float t = D.get_taux();
                //int jet = Formulas.getRandomValue(0, 100*100); // Anciennement 100*100
                float jet = Formulas.getFloatRandom();
                if (jet < t) {
                    ItemTemplate OT = World.getObjTemplate(D.get_itemID());
                    if (OT == null) {
                        continue;
                    }
                    //on ajoute a la liste
                    itemWon.put(OT.getID(), (itemWon.get(OT.getID()) == null ? 0 : itemWon.get(OT.getID())) + 1);

                    D.setMax(D.get_max() - 1);
                    if (D.get_max() == 0) {
                        it.remove();
                    }
                }
                k++;
            }
            
            Collection<Pair<Integer, Integer>> dropsRewards = new ArrayList<>();

            for (Map.Entry<Integer, Integer> entry : itemWon.entrySet()) {
                dropsRewards.add(new Pair<>(entry.getKey(), entry.getValue()));
            }tType());

            
            //fin drop system
            winxp = XP.get();
            
            rewards.add(new FightRewards(
                EndFightTeam.WINNER, 
                fighter, 
                winxp, 
                guildxp, 
                mountxp, 
                winKamas, 
                0, 
                0, 
                dropsRewards
            ));
        }
        
        return rewards;
    }
    
    protected Collection<FightRewards> getLoosersRewards(Collection<Fighter> winners, Collection<Fighter> loosers, FightDrops drops){
        Collection<FightRewards> rewards = new ArrayList<>(loosers.size());
        
        for(Fighter fighter : loosers){
            if(fighter.isInvocation() || fighter.isDouble())
                continue;
            
            rewards.add(FightRewards.emptyRewards(EndFightTeam.LOOSER, fighter));
        }
        
        return rewards;
    }

    @Override
    protected Collection<FightRewards> getAllRewards(Collection<Fighter> winners, Collection<Fighter> loosers) {
        return getAllRewards(winners, loosers, getDrops(loosers, winners));
    }
    
    protected Collection<FightRewards> getAllRewards(Collection<Fighter> winners, Collection<Fighter> loosers, FightDrops drops){
        Collection<FightRewards> rewards = new ArrayList<>();
        
        rewards.addAll(getWinnersRewards(winners, loosers, drops));
        rewards.addAll(getLoosersRewards(winners, loosers, drops));
        
        return rewards;
    }

    @Override
    protected String parseRewards(FightRewards rewards) {
        StringBuilder sb = new StringBuilder(128);
        
        sb.append(rewards.getTeam().getId()).append(';')
          .append(rewards.getFighter().getSpriteId()).append(';')
          .append(rewards.getFighter().getPacketsName()).append(';')
          .append(rewards.getFighter().get_lvl()).append(';')
          .append(rewards.getFighter().isDead() ? "1" : "0").append(';')
          .append(rewards.getFighter().xpString(";")).append(';');
        
        if(rewards.getWinXp() > 0)
          sb.append(rewards.getWinXp());
        sb.append(';');
        
        if(rewards.getGuildXp() > 0)
          sb.append(rewards.getGuildXp());
        sb.append(';');
        
        if(rewards.getMountXp() > 0)
          sb.append(rewards.getMountXp());
        sb.append(';');
          
        boolean b = false;
        
        for(Pair<Integer, Integer> pair : rewards.getDrops()){
            if(b)
                sb.append(',');
            else
                b = true;
            
            sb.append(pair.getFirst()).append('~').append(pair.getSecond());
        }
        
        sb.append(';');
        
        if(rewards.getWinKamas() > 0)
            sb.append(rewards.getWinKamas());
        
        return sb.toString();
    }

    @Override
    protected int getGESheetType() {
        return 0;
    }
}
