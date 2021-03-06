/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.friend;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class AddEnemyError {
    final static public char CANT_ADD_FRIEND_NOT_FOUND = 'f';
    final static public char CANT_ADD_YOU = 'y';
    final static public char ALREADY_YOUR_ENEMY = 'a';
    final static public char FRIENDS_LIST_FULL = 'm';
    
    private char error;

    public AddEnemyError(char error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "iAE" + error;
    }
    
}
