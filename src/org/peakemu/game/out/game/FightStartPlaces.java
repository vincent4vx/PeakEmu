/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.game;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class FightStartPlaces {
    private String places;
    private int team;

    public FightStartPlaces(String places, int team) {
        this.places = places;
        this.team = team;
    }

    @Override
    public String toString() {
        return "GP" + places + "|" + team;
    }
    
    
}
