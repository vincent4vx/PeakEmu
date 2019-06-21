/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.enums;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public enum IOState {
    FULL,
	EMPTYING,
	EMPTY,
	EMPTY2,
	FULLING,
    ;
    
    public int getValue(){
        return ordinal() + 1;
    }
}
