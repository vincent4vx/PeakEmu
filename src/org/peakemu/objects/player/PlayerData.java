/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.objects.player;

import org.peakemu.world.Sprite;
import org.peakemu.world.Stats;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public interface PlayerData extends Sprite{
    public SpellBook getSpellBook();
    public Stats getBaseStats();
    public Stats getRaceStats();
    public int getInitiative();
    public boolean canHaveMount();
    public boolean canHaveStuff();
    public boolean canLearnSpells();
    public int getGfxID();
}
