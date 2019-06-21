/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.account;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class CharacterCreation {
    private boolean success;
    private char error = '\0';
    
    final static public char SUBSCRIPTION_OUT = 's';
    final static public char CREATE_CHARACTER_FULL = 'f';
    final static public char NAME_ALEREADY_EXISTS = 'e';
    final static public char CREATE_CHARACTER_BAD_NAME = 'n';

    public CharacterCreation(boolean success) {
        this.success = success;
    }

    public CharacterCreation(boolean success, char error) {
        this.success = success;
        this.error = error;
    }

    @Override
    public String toString() {
        return "AA" + (success ? "K" : "E" + error);
    }
}
