/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world;

import org.peakemu.game.in.gameaction.GameActionArg;
import org.peakemu.objects.player.Player;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public interface IOActionPerformer {
    public boolean canDoAction(JobSkill skill, MapCell cell, Player player, GameActionArg arg);
    public void perform(JobSkill skill, MapCell cell, Player player, GameActionArg arg);
    public void finish(JobSkill skill, MapCell cell, Player player, GameActionArg arg);
}
