/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.mount;

import org.peakemu.database.parser.StatsParser;
import org.peakemu.objects.Mount;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class MountParser {
    static private String xp(Mount mount){
        long cur = mount.getExp();
        long min = mount.getExpLevel().mount;
        long max = mount.getExpLevel().getNext().mount == -1 ? min : mount.getExpLevel().getNext().mount;
        
        return cur + "," + min + "," + max;
    }
    
    static public String parseMount(Mount mount){
        StringBuilder str = new StringBuilder();
        str.append(mount.getId()).append(":");
        str.append(mount.getTemplate().getId()).append(":");
        str.append(mount.get_ancetres()).append(":");
        str.append(",,").append(mount.get_ability()).append(":");//FIXME capacités
        str.append(mount.getName()).append(":");
        str.append(mount.getGender()).append(":");
        str.append(xp(mount)).append(":");
        str.append(mount.getLevel()).append(":");
        str.append("1").append(":");//FIXME
        str.append(mount.getTotalPod()).append(":");
        str.append("0").append(":");//FIXME podActuel?
        str.append(mount.get_endurance()).append(",10000:");
        str.append(mount.get_maturite()).append(",").append(mount.getMaxMatu()).append(":");
        str.append(mount.get_energie()).append(",").append(mount.getMaxEnergie()).append(":");
        str.append(mount.get_serenite()).append(",-10000,10000:");
        str.append(mount.get_amour()).append(",10000:");
        str.append("-1").append(":");//FIXME
        str.append("0").append(":");//FIXME
        str.append(StatsParser.serializeItemStats(mount.getStats())).append(":");
        str.append(mount.get_fatigue()).append(",240:");
        str.append(mount.get_reprod()).append(",20:");
        return str.toString();
    }
}
