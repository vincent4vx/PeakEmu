/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.config;

import fr.quatrevieux.crisis.config.AbstractConfigPackage;
import fr.quatrevieux.crisis.config.DefaultValue;
import fr.quatrevieux.crisis.config.item.IntConfigItem;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class LifeConfig extends AbstractConfigPackage{
    @DefaultValue("15")
    private IntConfigItem spawnMonstersDelay;
    
    @DefaultValue("60")
    private IntConfigItem refreshInteractiveObjectsDelay;
    
    @DefaultValue("60")
    private IntConfigItem moveSpritesDelay;
    
    @DefaultValue("5")
    private IntConfigItem moveSpritesDistance;
    
    @DefaultValue("60")
    private IntConfigItem respawnFixedMonstersDelay;

    public int getSpawnMonstersDelay() {
        return spawnMonstersDelay.getValue();
    }

    public int getRefreshInteractiveObjectsDelay() {
        return refreshInteractiveObjectsDelay.getValue();
    }

    public int getMoveSpritesDelay() {
        return moveSpritesDelay.getValue();
    }

    public int getMoveSpritesDistance() {
        return moveSpritesDistance.getValue();
    }

    public int getRespawnFixedMonstersDelay() {
        return respawnFixedMonstersDelay.getValue();
    }
}
