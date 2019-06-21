/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.realm.out;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class LoginError {
    final static public char ALREADY_LOGGED_GAME_SERVER = 'c';
    final static public char ALREADY_LOGGED = 'a';
    final static public char BANNED = 'b';
    final static public char U_DISCONNECT_ACCOUNT = 'd';
    final static public char KICKED = 'k';
    final static public char LOGIN_ERROR = 'f';
    
    private char errorType;

    public LoginError(char errorType) {
        this.errorType = errorType;
    }

    public char getErrorType() {
        return errorType;
    }

    public void setErrorType(char errorType) {
        this.errorType = errorType;
    }
    
    public String toString(){
        return "AlE" + errorType;
    }
}
