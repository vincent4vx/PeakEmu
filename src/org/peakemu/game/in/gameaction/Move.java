/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.gameaction;

import java.util.List;
import org.peakemu.Peak;
import org.peakemu.common.ConditionParser;
import org.peakemu.common.Constants;
import org.peakemu.common.Logger;
import org.peakemu.common.SocketManager;
import org.peakemu.common.util.StringUtil;
import org.peakemu.game.GameServer;
import org.peakemu.game.out.InfoMessage;
import org.peakemu.game.out.game.GameActionResponse;
import org.peakemu.maputil.Compressor;
import org.peakemu.maputil.Coordinate;
import org.peakemu.maputil.Direction;
import org.peakemu.maputil.PathException;
import org.peakemu.maputil.PathValidator;
import org.peakemu.objects.item.Item;
import org.peakemu.objects.player.Player;
import org.peakemu.world.GameMap;
import org.peakemu.world.MapCell;
import org.peakemu.objects.MonsterGroup;
import org.peakemu.world.action.Action;
import org.peakemu.world.fight.fighter.Fighter;
import org.peakemu.world.handler.ActionHandler;
import org.peakemu.world.handler.fight.FightHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class Move implements GameAction {
    final public static int ACTION_ID = 1;
    
    final private ActionHandler actionHandler;
    final private FightHandler fightHandler;

    public Move(ActionHandler actionHandler, FightHandler fightHandler) {
        this.actionHandler = actionHandler;
        this.fightHandler = fightHandler;
    }

    @Override
    public void start(GameActionArg arg) {
        if (arg.getClient().getPlayer().getFight() == null) {
            onRolePlayMove(arg);
        } else {
            onFightMove(arg);
        }
    }

    @Override
    public void end(GameActionArg arg, boolean success, String args) {
        Player player = arg.getClient().getPlayer();
        String[] infos = StringUtil.split(args, "|");
        
        if (success) {
            //Hors Combat
            if (player.getFight() == null) {
                List<MapCell> path = (List<MapCell>) arg.getAttachement("REAL_PATH");
                MapCell nextCell = path.get(path.size() - 1);

                player.setCell(nextCell);
                player.setOrientation(Compressor.getDirectionBetweenTwoCells(
                    path.get(path.size() - 2), 
                    nextCell, 
                    player.getRestrictions().canMoveAllDirections()
                ).ordinal());
                
                if (!(player.isDead() > 0)) {
                    player.setAway(false);
                }
                
                onPlayerArrivedOnCell(player, nextCell);
            } else//En combat
            {
                player.getFight().onGK(player);
                return;
            }

        } else {
            int newCellID;
            try {
                newCellID = Integer.parseInt(infos[1]);
            } catch (Exception e) {
                return;
            }
            
            MapCell endCell = player.getMap().getCell(newCellID);
            
            if(endCell == null)
                return;
            
            List<MapCell> path = (List<MapCell>) arg.getAttachement("REAL_PATH");
            
            if(!path.contains(endCell))
                return;
            
            Direction direction = Compressor.getDirectionBetweenTwoCells(endCell, path.get(path.size() - 1), player.getRestrictions().canMoveAllDirections());
            
            player.setCell(endCell);
            
            if(direction != null)
                player.setOrientation(direction.ordinal());
            
            SocketManager.GAME_SEND_BN(arg.getClient());
        }
    }

    @Override
    public int actionId() {
        return ACTION_ID;
    }
    
    private void onRolePlayMove(GameActionArg arg){  
        if (arg.getClient().getPlayer().getPodUsed() > arg.getClient().getPlayer().getMaxPod() || arg.getClient().getPlayer().isDead() == 1) {
            if (arg.getClient().getPlayer().isDead() != 1) {
                arg.getClient().send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(12));
            }
            
            arg.getClient().send(GameActionResponse.noGameAction());
            SocketManager.GAME_SEND_EXCHANGE_REQUEST_ERROR(arg.getClient(), 'o');
            return;
        }

        try{
            List<MapCell> path = Compressor.uncompressPath(
                arg.getArg(), 
                arg.getClient().getPlayer().getMap(), 
                arg.getClient().getPlayer().getCell(), 
                arg.getClient().getPlayer().getRestrictions().canMoveAllDirections()
            );
            
            path = PathValidator.validateRolePlayMovePath(path);
            
            if(path.size() <= 1){
                arg.getClient().send(GameActionResponse.noGameAction());
                return;
            }
            
            arg.attach("REAL_PATH", path);
            
            arg.getClient().getPlayer().getMap().sendToMap(GameActionResponse.move(arg.getClient().getPlayer(), path));
        }catch(PathException e){
            Peak.worldLog.addToLog(Logger.Level.DEBUG, e.toString());
            arg.getClient().send(GameActionResponse.noGameAction());
            return;
        }

        arg.getClient().getGameActionHandler().setCurrentAction(arg);

        if (!arg.getClient().getPlayer().inRegeneration(Constants.STAND_TIMER_REGEN)) {
            arg.getClient().getPlayer().setSitted(false);
        }

        arg.getClient().getPlayer().setAway(true);
    }
    
    private void onFightMove(GameActionArg arg){
        Fighter fighter = arg.getClient().getPlayer().getFighter();
        
        try{
            List<MapCell> path = Compressor.uncompressPath(arg.getArg(), fighter.getFight().get_map(), fighter.getCell(), false);
            if(!fighter.getFight().fighterDeplace(fighter, path)){
                arg.getClient().send(GameActionResponse.noGameAction());
            }
        }catch(Exception e){
            Peak.errorLog.addToLog(e);
            arg.getClient().send(GameActionResponse.noGameAction());
        }
    }

    private void onPlayerArrivedOnCell(Player player, MapCell cell){
        if(cell == null)
            return;
        
        GameMap map = player.getMap();
        
        Item obj = map.removeDropItem(cell);
        
        if (obj != null) {
            player.getItems().addItem(obj);
        }
        
        for(Action action : map.getTriggers(cell.getID())){
            actionHandler.apply(action, player, null, cell, null);
        }

        //no place for fight
        if (map.get_maxTeam0() < 1 || map.get_maxTeam1() < 1) {
            return;
        }
        
        //Si le joueur a changer de map ou ne peut etre aggro
        if (player.getMap() != map || !player.canAggro()) {
            return;
        }
        
        if(player.isMutant() && !player.getRestrictions().canAttackMonsterWhenMutant())
            return;
        
        Player target = map.getFirstPlayerOnCell(cell.getID());
        
        if(target != null
            && !player.equals(target)
            && target.isMutant()
            && !target.getMutant().isShowPlayer()
            && target.canAggro()){
            fightHandler.startMutantFight(map, target, player);
            return;
        }

        for (MonsterGroup group : map.getMonsterGroups()) {
            if (group.getCell().equals(cell) || Coordinate.getDistanceBetweenCells(cell, group.getCell()) <= group.getAggroDistance()){
                if ((group.getAlignement() == Constants.ALIGNEMENT_NONE || ((player.getAlignement() == Constants.ALIGNEMENT_BONTARIEN || player.getAlignement() == Constants.ALIGNEMENT_BRAKMARIEN) && (player.getAlignement() != group.getAlignement()))) && ConditionParser.validConditions(player, group.getCondition())) {
                    GameServer.addToLog(player.getName() + " lance un combat contre le groupe " + group.getSpriteId() + " sur la map " + map.getId());
                    fightHandler.startPvMFight(map, player, group);
                    return;
                }
            }
        }
    }
}
