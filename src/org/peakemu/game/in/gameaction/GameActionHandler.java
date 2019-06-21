/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.gameaction;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class GameActionHandler {
    final private Collection<GameActionArg> pending = new ArrayList<>();
    private GameActionArg currentAction = null;
    
    final private GameActionRegistry gameActionRegistry;

    public GameActionHandler(GameActionRegistry gameActionRegistry) {
        this.gameActionRegistry = gameActionRegistry;
    }
    
    public boolean isBusy(){
        return currentAction != null;
    }
    
    public void handleAction(GameActionArg gameActionArg){
        if(isBusy()){
            pending.add(gameActionArg);
            return;
        }
        
        gameActionRegistry.startGameAction(gameActionArg);
    }

    public void setCurrentAction(GameActionArg currentAction) {
        if(this.currentAction != null)
            pending.add(currentAction);
        else
            this.currentAction = currentAction;
    }
    
    public void endCurrent(boolean success, String args){
        if(currentAction == null)
            return;
        
        Collection<GameActionArg> copyPending = new ArrayList<>(pending);
        pending.clear();
        
        gameActionRegistry.endGameAction(currentAction, success, args);
        currentAction = null;
        
        if(success){
            for (GameActionArg gaa : copyPending) {
                handleAction(gaa);
            }
        }
    }
}
