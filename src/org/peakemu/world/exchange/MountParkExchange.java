/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.exchange;

import org.peakemu.game.out.exchange.ExchangeLeaved;
import org.peakemu.objects.MountPark;
import org.peakemu.objects.player.Player;
import org.peakemu.world.enums.ExchangeType;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class MountParkExchange implements ExchangeBase{
    final private Player player;
    final private MountPark mountPark;

    public MountParkExchange(Player player, MountPark mountPark) {
        this.player = player;
        this.mountPark = mountPark;
    }

    @Override
    public void cancel() {
        player.send(new ExchangeLeaved());
        player.setCurExchange(null);
    }

    @Override
    public ExchangeType getType() {
        return ExchangeType.MOUNT_PARK;
    }

    public Player getPlayer() {
        return player;
    }

    public MountPark getMountPark() {
        return mountPark;
    }
    
}
