/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight.team;

import java.util.List;
import org.peakemu.common.Constants;
import org.peakemu.game.in.gameaction.JoinFight;
import org.peakemu.game.out.game.FightOption;
import org.peakemu.game.out.game.GameActionResponse;
import org.peakemu.game.out.InfoMessage;
import org.peakemu.objects.player.Player;
import org.peakemu.world.MapCell;
import org.peakemu.world.fight.fighter.Fighter;
import org.peakemu.world.fight.fighter.PlayerFighter;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class ChallengeTeam extends AbstractTeam{
    final private PlayerFighter init;
    
    private boolean locked = false;
    private boolean onlyGroup = false;
    private boolean help = false;

    public ChallengeTeam(Player player, List<MapCell> startCells, int startCellsIndex) {
        super(startCells, startCellsIndex);
        
        init = new PlayerFighter(player);
        
        addFighter(init);
        player.setFighter(init);
    }

    @Override
    public String getId() {
        return init.getSpriteId()+ "";
    }

    @Override
    public int getCell() {
        return init.getPlayer().getCell().getID();
    }

    @Override
    public boolean canAddFighter(Player player) {
        if(locked){
            player.send(GameActionResponse.joinFightError(getId(), JoinFight.TEAM_CLOSED));
            return false;
        }
        
        if(getSize() >= getStartCells().size()){
            player.send(GameActionResponse.joinFightError(getId(), JoinFight.TEAM_FULL));
            return false;
        }
        
        if(onlyGroup && !getInit().getPlayer().getGroup().getPlayers().contains(player)){
            player.send(GameActionResponse.joinFightError(getId(), JoinFight.TEAM_CLOSED));
            return false;
        }
        
        if(player.isMutant()){
            if(player.getMutant().isShowPlayer())
                return true;
            
            if(player.isMutant() && !player.getRestrictions().canAttackMonsterWhenMutant()){
                player.send(GameActionResponse.joinFightError(getId(), JoinFight.CANT_U_ARE_MUTANT));
                return false;
            }
        }
        
        return true;
    }

    @Override
    public int getAlignement() {
        return Constants.ALIGNEMENT_NONE; //no align on challenge
    }

    @Override
    public int getType() {
        return 0;
    }

    public Fighter getInit() {
        return init;
    }
    
    public void toggleHelp(){
        help = !help;
        
        init.getPlayer().getMap().sendToMap(new FightOption(this, help, FightOption.HELP));
        
        if(help)
            sendToTeam(new InfoMessage(InfoMessage.Type.INFO).addMessage(103));
        else
            sendToTeam(new InfoMessage(InfoMessage.Type.INFO).addMessage(104));
    }
    
    public void toggleLock(){
        locked = !locked;
        
        init.getPlayer().getMap().sendToMap(new FightOption(this, locked, FightOption.LOCK));
        
        if(locked)
            sendToTeam(new InfoMessage(InfoMessage.Type.INFO).addMessage(95));
        else
            sendToTeam(new InfoMessage(InfoMessage.Type.INFO).addMessage(96));
    }
    
    public void toggleOnlyGroup(){
        onlyGroup = !onlyGroup;
        
        init.getPlayer().getMap().sendToMap(new FightOption(this, onlyGroup, FightOption.ONLY_GROUP));
        
        if(onlyGroup)
            sendToTeam(new InfoMessage(InfoMessage.Type.INFO).addMessage(93));
        else
            sendToTeam(new InfoMessage(InfoMessage.Type.INFO).addMessage(94));
    }
}
