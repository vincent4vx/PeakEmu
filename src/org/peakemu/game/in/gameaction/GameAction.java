/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.gameaction;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public interface GameAction {
    //fight
    final static public int ALTER_PM_ON_BUFF     = 127;
    final static public int ALTER_PM_ON_ACTION   = 129;
    final static public int ALTER_PA_ON_BUFF     = 101;
    final static public int ALTER_PA_ON_ACTION   = 102;
    final static public int FIGHTER_DIE          = 103;
    final static public int TACKLE               = 104;
    final static public int LAUNCH_SPELL         = 300;
    final static public int CRITICAL_HIT         = 301;
    final static public int CRITICAL_MISS        = 302;
    final static public int USE_WEAPON           = 303;
    final static public int WEAPON_CRITICAL_MISS = 305;
    final static public int ALTER_LIFE_POINTS    = 100;
    final static public int REDUCED_DAMMAGES     = 105;
    final static public int TRIGGER_TRAP         = 306;
    final static public int TRIGGER_GLYPHE       = 307;
    final static public int DODGE_REMOVE_AP      = 308;
    final static public int DODGE_REMOVE_MP      = 309;
    final static public int ALTER_STATE          = 950;
    
    //job & environment
    final static public int JOB_ACTION = 501;
    
    //misc
    final static public int FAKE_PACKET = 999;
    
    public void start(GameActionArg arg);
    public void end(GameActionArg arg, boolean success, String args);
    public int actionId();
}
