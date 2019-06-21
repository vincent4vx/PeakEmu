/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.exchange;

import org.peakemu.world.ItemStorage;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public interface StorageExchange extends Exchange{
    public ItemStorage getDistantInventory();
    public long getDistantKamas();
    public void setDistantKamas(long kamas);
    public ItemStorage getOwnInventory();
    public long getOwnKamas();
    public void setOwnKamas(long kamas);
}
