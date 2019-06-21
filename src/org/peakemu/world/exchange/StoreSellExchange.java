/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.exchange;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public interface StoreSellExchange extends ExchangeBase{
    public void sellItem(int itemId, int qte, long price);
    public void removeSellItem(int itemId, int qte);
}
