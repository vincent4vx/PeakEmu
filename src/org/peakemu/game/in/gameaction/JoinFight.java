/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.gameaction;

import org.peakemu.Peak;
import org.peakemu.common.Constants;
import org.peakemu.game.out.game.GameActionResponse;
import org.peakemu.world.fight.Fight;
import org.peakemu.objects.player.Player;
import org.peakemu.world.fight.team.FightTeam;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class JoinFight implements GameAction{
    final static public int ACTION_ID = 903;
    
    final static public String CHALENGE_FULL = "c";
    final static public String TEAM_FULL = "t";
    final static public String TEAM_DIFFERENT_ALIGNMENT = "a";
    final static public String CANT_DO_BECAUSE_GUILD = "g";
    final static public String CANT_DO_TOO_LATE = "l";
    final static public String CANT_U_ARE_MUTANT = "m";
    final static public String CANT_BECAUSE_MAP = "p";
    final static public String CANT_BECAUSE_ON_RESPAWN = "r";
    final static public String CANT_YOU_R_OCCUPED = "o";
    final static public String CANT_YOU_OPPONENT_OCCUPED = "z";
    final static public String CANT_FIGHT = "h";
    final static public String CANT_FIGHT_NO_RIGHTS = "i";
    final static public String ERROR_21 = "s";
    final static public String SUBSCRIPTION_OUT = "n";
    final static public String A_NOT_SUBSCRIB = "b";
    final static public String TEAM_CLOSED = "f";
    final static public String NO_ZOMBIE_ALLOWED = "d";

    @Override
    public void start(GameActionArg arg) {
        Player player = arg.getClient().getPlayer();
        String[] infos = arg.getArg().split(";");
        Fight fight = player.getMap().getFight(Integer.parseInt(infos[0]));
        
        if(fight == null){
            return;
        }
        
        if (infos.length == 1) {
            fight = player.getMap().getFight(Integer.parseInt(infos[0]));
            fight.getSpectators().join(player);
        } else {
            try {
                String teamId = infos[1];
                
                if (player.isAway()) {
                    arg.getClient().send(GameActionResponse.joinFightError(teamId, CANT_YOU_R_OCCUPED));
                    return;
                }
                
                FightTeam team = null;
                    
                for(FightTeam ft : fight.getTeams()){
                    if(ft.getId().equals(teamId)){
                        team = ft;
                        break;
                    }
                }
                
                if(team == null){
                    arg.getClient().send(GameActionResponse.joinFightError(teamId, CANT_FIGHT));
                    return;
                }
                
                if(fight.getState() != Constants.FIGHT_STATE_PLACE){
                    arg.getClient().send(GameActionResponse.joinFightError(teamId, CANT_DO_TOO_LATE));
                    return;
                }
                
                fight.joinTeam(player, team);
            } catch (Exception e) {
                Peak.errorLog.addToLog(e);
            }
        }
    }

    @Override
    public void end(GameActionArg arg, boolean success, String args) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int actionId() {
        return ACTION_ID;
    }
    
}
