/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.exchange;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class SetSellerConfirmation {
    private long totalPrice;
    private double taxRate;
    private long toPay;

    public SetSellerConfirmation(long totalPrice, double taxRate, long toPay) {
        this.totalPrice = totalPrice;
        this.taxRate = taxRate;
        this.toPay = toPay;
    }

    @Override
    public String toString() {
        return "Eq" + totalPrice + "|" + taxRate + "|" + toPay;
    }
    
    
}
