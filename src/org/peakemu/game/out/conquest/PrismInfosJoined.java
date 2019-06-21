/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.conquest;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class PrismInfosJoined {
    final static public int JOINABLE = 0;
    final static public int NOFIGHT  = -1;
    final static public int INFIGHT  = -2;
    final static public int NONE     = -3;
    
    private int state;
    private int remainingTime;
    private int placementTime;
    private int places;

    public PrismInfosJoined(int state) {
        this.state = state;
    }

    public PrismInfosJoined(int remainingTime, int placementTime, int places) {
        state = JOINABLE;
        this.remainingTime = remainingTime;
        this.placementTime = placementTime;
        this.places = places;
    }

    @Override
    public String toString() {
        if(state != JOINABLE)
            return "CIJ" + state;
        
        return "CIJ" + state + ";" + remainingTime + ";" + placementTime + ";" + places;
    }
    
    
}
