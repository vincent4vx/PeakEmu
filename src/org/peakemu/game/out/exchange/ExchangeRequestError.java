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
public class ExchangeRequestError {
    final static public char ALREADY_EXCHANGE = 'O';
    final static public char NOT_NEAR_CRAFT_TABLE = 'T';
    final static public char ERROR_85 = 'J';
    final static public char ERROR_70 = 'o';
    final static public char ERROR_62 = 'S';
    final static public char CANT_EXCHANGE = 'I';

    public ExchangeRequestError(char error) {
        this.error = error;
    }
    
    private char error;

    public char getError() {
        return error;
    }

    public void setError(char error) {
        this.error = error;
    }
    
    

    @Override
    public String toString() {
        return "ERE" + error;
    }
    
}
