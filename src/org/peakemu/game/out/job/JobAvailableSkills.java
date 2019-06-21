/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.job;

import java.util.ArrayList;
import java.util.Collection;
import org.peakemu.common.util.StringUtil;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class JobAvailableSkills {
    private Collection<String> skills = new ArrayList<>();
    
    public JobAvailableSkills addSkill(String str){
        skills.add(str);
        return this;
    }

    @Override
    public String toString() {
        return "JS|" + StringUtil.join(skills, "|");
    }
    
}
