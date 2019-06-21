/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.exchange;

import org.peakemu.world.JobSkill;
import org.peakemu.world.enums.ExchangeType;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class CraftTableOpened {
    private int maxIngredients;
    private JobSkill skill;

    public CraftTableOpened(int maxIngredients, JobSkill skill) {
        this.maxIngredients = maxIngredients;
        this.skill = skill;
    }

    @Override
    public String toString() {
        return "ECK" + ExchangeType.FACTORY_TABLE.getId() + "|" + maxIngredients + ";" + skill.getId();
    }
    
    
}
