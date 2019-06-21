/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.peakemu.world.handler;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.peakemu.Peak;
import org.peakemu.common.ConditionParser;
import org.peakemu.common.Logger;
import org.peakemu.database.dao.LiveActionDAO;
import org.peakemu.objects.item.Item;
import org.peakemu.objects.player.Player;
import org.peakemu.world.LiveAction;
import org.peakemu.world.MapCell;
import org.peakemu.world.action.Action;
import org.peakemu.world.action.ActionPerformer;
import org.peakemu.world.action.AddEnergy;
import org.peakemu.world.action.AddHonor;
import org.peakemu.world.action.AddJobXP;
import org.peakemu.world.action.AddKamas;
import org.peakemu.world.action.AddObject;
import org.peakemu.world.action.AddPrism;
import org.peakemu.world.action.AddSpellPoint;
import org.peakemu.world.action.AddStats;
import org.peakemu.world.action.AddXP;
import org.peakemu.world.action.Alignment;
import org.peakemu.world.action.Candy;
import org.peakemu.world.action.CreateGuilde;
import org.peakemu.world.action.Firework;
import org.peakemu.world.action.ForgotSpell;
import org.peakemu.world.action.LearnJob;
import org.peakemu.world.action.LeftJail;
import org.peakemu.world.action.Moon;
import org.peakemu.world.action.OpenBank;
import org.peakemu.world.action.Prisoner;
import org.peakemu.world.action.QuestObjective;
import org.peakemu.world.action.QuestStart;
import org.peakemu.world.action.QuestStep;
import org.peakemu.world.action.ResetCarac;
import org.peakemu.world.action.WarpToSavePos;
import org.peakemu.world.action.SpawnMob;
import org.peakemu.world.action.SpeechNPC;
import org.peakemu.world.action.Stalking;
import org.peakemu.world.action.StalkingReward;
import org.peakemu.world.action.StalkingTarget;
import org.peakemu.world.action.StartFight;
import org.peakemu.world.action.Teleportation;
import org.peakemu.world.action.TeleportationDungeon;
import org.peakemu.world.action.TeleportationHouseGuild;
import org.peakemu.world.action.TeleportationIncarnamToAstrub;
import org.peakemu.world.action.TeleportationMountPark;
import org.peakemu.world.action.UnlearnJob;
import org.peakemu.world.action.Heal;
import org.peakemu.world.action.LearnSpell;
import org.peakemu.world.action.Mutation;
import org.peakemu.world.action.RandomMutation;
import org.peakemu.world.action.WeddingCell;
import org.peakemu.world.action.WeddingEnd;
import org.peakemu.world.action.WeddingRequest;
import org.peakemu.world.config.WorldConfig;


/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
final public class ActionHandler {
    final private Peak peak;
    final private LiveActionDAO liveActionDAO;
    final private WorldConfig config;
    
    final private Map<Integer, ActionPerformer> actions = new HashMap<>();
    final private ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

    public ActionHandler(Peak peak, LiveActionDAO liveActionDAO, WorldConfig config) {
        this.peak = peak;
        this.liveActionDAO = liveActionDAO;
        this.config = config;
    }
    
    public void registerAction(ActionPerformer action){
        actions.put(action.actionId(), action);
    }
    
    public void load(){
        System.out.print("Chargement des actions : ");
        registerAction(new Prisoner());
        registerAction(new AddEnergy());
        registerAction(new AddHonor());
        registerAction(new AddJobXP(peak.getWorld().getJobHandler()));
        registerAction(new AddObject(peak.getWorld().getItemHandler()));
        registerAction(new AddPrism(peak.getWorld().getAlignementHandler()));
        registerAction(new AddSpellPoint());
        registerAction(new AddStats());
        registerAction(new AddXP(peak.getWorld().getPlayerHandler()));
        registerAction(new Alignment());
        registerAction(new OpenBank());
        registerAction(new Candy());
        registerAction(new CreateGuilde());
        registerAction(new Firework());
        registerAction(new ForgotSpell());
        registerAction(new AddKamas());
        registerAction(new LearnJob(peak.getWorld().getJobHandler(), peak.getWorld().getConfig().getJob()));
        registerAction(new LearnSpell(peak.getWorld().getSpellHandler()));
        registerAction(new LeftJail(peak.getWorld().getPlayerHandler()));
        registerAction(new Moon(peak.getWorld().getPlayerHandler()));
        registerAction(new QuestObjective());
        registerAction(new QuestStart());
        registerAction(new QuestStep());
        registerAction(new ResetCarac());
        registerAction(new WarpToSavePos(peak.getWorld().getPlayerHandler()));
        registerAction(new SpawnMob());
        registerAction(new SpeechNPC(peak.getWorld().getNpcHandler()));
        registerAction(new Stalking(peak.getWorld().getItemHandler()));
        registerAction(new StalkingTarget(peak.getWorld().getSessionHandler()));
        registerAction(new StalkingReward(peak.getWorld().getPlayerHandler()));
        registerAction(new StartFight(peak.getWorld().getFightHandler(), peak.getDao().getMonsterDAO()));
        registerAction(new Teleportation(peak.getWorld().getPlayerHandler()));
        registerAction(new TeleportationDungeon(peak.getWorld().getPlayerHandler()));
        registerAction(new TeleportationHouseGuild());
        registerAction(new TeleportationIncarnamToAstrub(peak.getWorld().getPlayerHandler()));
        registerAction(new TeleportationMountPark());
        registerAction(new UnlearnJob());
        registerAction(new Heal());
        registerAction(new WeddingCell());
        registerAction(new WeddingEnd());
        registerAction(new WeddingRequest());
        registerAction(new RandomMutation(peak.getWorld().getItemHandler(), peak.getWorld().getMonsterHandler()));
        registerAction(new Mutation(peak.getWorld().getItemHandler()));
        System.out.println(actions.size() + " actions chargées");
    }
    
    public void initLiveActions(){
        service.scheduleAtFixedRate(() -> {
            Peak.worldLog.addToLog(Logger.Level.INFO, "Application des live actions");
            
            Collection<LiveAction> liveActions = liveActionDAO.load();
            
            for(LiveAction liveAction : liveActions){
                try{
                    apply(liveAction.getAction(), liveAction.getTarget(), null, null, null);
                }catch(Exception e){
                    Peak.errorLog.addToLog(e);
                }
            }
            
            liveActionDAO.delete(liveActions);
        }, 0, config.getReloadLiveActionsDelay(), TimeUnit.SECONDS);
    }

    public boolean apply(Action action, Player caster, Player target, MapCell cell, Item item){
        if(caster == null) //should always have a caster !
            return false;
        
        if(!actions.containsKey(action.getId())){
            Peak.worldLog.addToLog(Logger.Level.ERROR, "ActionID not found, %s cancelled", action);
            return false;
        }
        
        if(!ConditionParser.validConditions(caster, action.getCond())){
            Peak.worldLog.addToLog(Logger.Level.DEBUG, "Can't do %s : invalid condition", action);
            return false;
        }
        
        ActionPerformer performer = actions.get(action.getId());
        
        Peak.worldLog.addToLog(Logger.Level.DEBUG, "Applying action type %s : %s", performer.getClass().getSimpleName(), action);
        
        try{
            return performer.apply(action, caster, target, cell, item);
        }catch(Exception e){
            Peak.errorLog.addToLog(e);
            return false;
        }
    }
    
    public boolean applyAll(Collection<Action> actions, Player caster, Player target, MapCell cell, Item item){
        for (Action action : actions) {
            if(!apply(action, caster, target, cell, item))
                return false;
        }
        
        return true;
    }
    
    public ActionPerformer getActionPerformer(int id){
        return actions.get(id);
    }
    
    public Collection<ActionPerformer> getActions(){
        return actions.values();
    }
}
