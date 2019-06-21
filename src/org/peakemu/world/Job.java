package org.peakemu.world;

import org.peakemu.objects.player.JobStats;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.Timer;

import org.peakemu.Ancestra;


import org.peakemu.common.Constants;
import org.peakemu.common.Formulas;
import org.peakemu.common.SocketManager;
import org.peakemu.game.in.gameaction.GameActionArg;
import org.peakemu.game.out.job.JobXp;
import org.peakemu.objects.item.Item;
import org.peakemu.objects.player.Player;
import org.peakemu.world.enums.Effect;
import org.peakemu.world.enums.IOState;
import org.peakemu.world.enums.InventoryPosition;

public class Job {
    final private int id;
    final private Collection<ItemTemplate> tools;
    final private Collection<JobSkill> skills = new ArrayList<>();
    final private Job baseJob;

    public Job(int id, Collection<ItemTemplate> tools, Job baseJob) {
        this.id = id;
        this.tools = tools;
        this.baseJob = baseJob;
    }

    public int getId() {
        return id;
    }
    
    public boolean isBaseJob(){
        return id == 1;
    }

    public Job getBaseJob() {
        return baseJob;
    }
    
    public void addSkill(JobSkill skill){
        skills.add(skill);
    }
    
    public boolean isMageJob(){
        return baseJob != null;
    }

    public boolean isValidTool(ItemTemplate item) {
        return tools.contains(item);
    }

    public static byte ViewActualStatsItem(Item obj, String stats) {
//        if (!obj.parseStatsString().isEmpty()) {
//            for (Entry<Effect, Integer> entry : obj.getStats().getMap().entrySet()) {
//                if (Integer.toHexString(((Integer) entry.getKey().getId()).intValue()).compareTo(stats) > 0) {
//                    if ((Integer.toHexString(((Integer) entry.getKey().getId()).intValue()).compareTo("98") == 0) && (stats.compareTo("7b") == 0)) {
//                        return 2;
//                    }
//                    if ((Integer.toHexString(((Integer) entry.getKey().getId()).intValue()).compareTo("9a") == 0) && (stats.compareTo("77") == 0)) {
//                        return 2;
//                    }
//                    if ((Integer.toHexString(((Integer) entry.getKey().getId()).intValue()).compareTo("9b") == 0) && (stats.compareTo("7e") == 0)) {
//                        return 2;
//                    }
//                    if ((Integer.toHexString(((Integer) entry.getKey().getId()).intValue()).compareTo("9d") == 0) && (stats.compareTo("76") == 0)) {
//                        return 2;
//                    }
//                    if ((Integer.toHexString(((Integer) entry.getKey().getId()).intValue()).compareTo("74") == 0) && (stats.compareTo("75") == 0)) {
//                        return 2;
//                    }
//                    if ((Integer.toHexString(((Integer) entry.getKey().getId()).intValue()).compareTo("99") == 0) && (stats.compareTo("7d") == 0)) {
//                        return 2;
//                    }
//
//                } else if (Integer.toHexString(((Integer) entry.getKey().getId()).intValue()).compareTo(stats) == 0) {
//                    return 1;
//                }
//            }
//            return 0;
//        }

        return 0;
    }

    public static byte ViewBaseStatsItem(Item obj, String ItemStats) {
        String[] splitted = obj.getTemplate().getStrTemplate().split(",");
        for (String s : splitted) {
            String[] stats = s.split("#");
            if (stats[0].compareTo(ItemStats) > 0) {
                if ((stats[0].compareTo("98") == 0) && (ItemStats.compareTo("7b") == 0)) {
                    return 2;
                }
                if ((stats[0].compareTo("9a") == 0) && (ItemStats.compareTo("77") == 0)) {
                    return 2;
                }
                if ((stats[0].compareTo("9b") == 0) && (ItemStats.compareTo("7e") == 0)) {
                    return 2;
                }
                if ((stats[0].compareTo("9d") == 0) && (ItemStats.compareTo("76") == 0)) {
                    return 2;
                }
                if ((stats[0].compareTo("74") == 0) && (ItemStats.compareTo("75") == 0)) {
                    return 2;
                }
                if ((stats[0].compareTo("99") == 0) && (ItemStats.compareTo("7d") == 0)) {
                    return 2;
                }

            } else if (stats[0].compareTo(ItemStats) == 0) {
                return 1;
            }
        }
        return 0;
    }

    public static int getBaseMaxJet(int templateID, String statsModif) {
        ItemTemplate t = World.getObjTemplate(templateID);
        String[] splitted = t.getStrTemplate().split(",");
        for (String s : splitted) {
            String[] stats = s.split("#");
            if (stats[0].compareTo(statsModif) > 0) {
                continue;
            }
            if (stats[0].compareTo(statsModif) != 0) {
                continue;
            }
            int max = Integer.parseInt(stats[2], 16);
            if (max == 0) {
                max = Integer.parseInt(stats[1], 16);
            }
            return max;
        }

        return 0;
    }

    public static int getActualJet(Item obj, String statsModif) {
        for (Entry<Effect, Integer> entry : obj.getStats().getMap().entrySet()) {
            if (Integer.toHexString(((Integer) entry.getKey().getId()).intValue()).compareTo(statsModif) > 0) {
                continue;
            }
            if (Integer.toHexString(((Integer) entry.getKey().getId()).intValue()).compareTo(statsModif) != 0) {
                continue;
            }
            int JetActual = ((Integer) entry.getValue()).intValue();
            return JetActual;
        }

        return 0;
    }

    public Collection<JobSkill> getSkills() {
        return skills;
    }
}
