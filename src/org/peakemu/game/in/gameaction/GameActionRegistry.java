/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.gameaction;

import java.util.HashMap;
import java.util.Map;
import org.peakemu.Peak;
import org.peakemu.common.Logger;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
final public class GameActionRegistry {
    final private Map<Integer, GameAction> gameActions = new HashMap<>();

    public GameActionRegistry(Peak peak) {
        addGameAction(new Move(peak.getWorld().getActionHandler(), peak.getWorld().getFightHandler()));
        addGameAction(new IOAction(peak.getWorld().getInteractiveObjectHandler()));
        addGameAction(new QuestSign());
        addGameAction(new CastSpell());
        addGameAction(new UseWeapon());
        addGameAction(new ManageHouse());
        addGameAction(new PrismMenu(peak.getWorld().getWaypointHandler()));
        addGameAction(new MarriedYes());
        addGameAction(new MarriedNo());
        addGameAction(new AskDuel());
        addGameAction(new AcceptDuel(peak.getWorld().getFightHandler()));
        addGameAction(new DeclineDuel());
        addGameAction(new JoinFight());
        addGameAction(new Assault(peak.getWorld().getFightHandler()));
        addGameAction(new AttackCollector(peak.getWorld().getFightHandler()));
        addGameAction(new AttackPrism(peak.getWorld().getFightHandler()));
        addGameAction(new MutantAttack(peak.getWorld().getFightHandler()));
    }
    
    public void addGameAction(GameAction ga){
        gameActions.put(ga.actionId(), ga);
    }
    
    public void startGameAction(GameActionArg arg){
        if(!gameActions.containsKey(arg.getActionId())){
            Peak.worldLog.addToLog(Logger.Level.ERROR, "GameAction %d not found", arg.getActionId());
            return;
        }
        
        GameAction ga = gameActions.get(arg.getActionId());
        Peak.worldLog.addToLog(Logger.Level.DEBUG, "Start GameAction : %s for %s", ga.getClass().getSimpleName(), arg.getClient().getPlayer().getName());
        ga.start(arg);
    }
    
    public void endGameAction(GameActionArg arg, boolean success, String args){
        GameAction ga = gameActions.get(arg.getActionId());
        Peak.worldLog.addToLog(Logger.Level.DEBUG, "End GameAction : %s for %s", ga.getClass().getSimpleName(), arg.getClient().getPlayer().getName());
        ga.end(arg, success, args);
    }
}
