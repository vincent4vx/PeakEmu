/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world;

import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import org.peakemu.common.Formulas;
import org.peakemu.game.GameServer;
import org.peakemu.objects.Prism;

public class SubArea {

    class SpawnTimerTask extends TimerTask {

        int timerDelay;

        public SpawnTimerTask(int delay) {
            timerDelay = delay;
        }

        @Override
        public void run() {
            // You can do anything you want with param 
        }
    }

    final private int id;
    final private Area area;
    final private int alignement;
    final private String name;
    final private ArrayList<GameMap> maps = new ArrayList<GameMap>();
    //prisme
    final private boolean canCapture;
    private Prism prism;
    
    /*
     * Timers de respawn par subarea
     * Integer = Délai du timer
     * Couple.first(String) = string des groupes
     * Couple.second(Timer) = Le timer 
     */
    public Map<Integer, World.Couple<String, Timer>> _spawnTimers = new TreeMap<Integer, World.Couple<String, Timer>>();

    public SubArea(int id, Area area, int alignement, String name, boolean capturable) {
        this.id = id;
        this.name = name;
        this.area = area;
        this.alignement = alignement;
        canCapture = capturable;
    }

    public boolean isConquestable() {
        return canCapture;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public Area getArea() {
        return area;
    }

    public int getAlignement() {
        if(prism != null)
            return prism.getAlignement();
        
        return alignement;
    }

    public ArrayList<GameMap> getMaps() {
        return maps;
    }

    public void addMap(GameMap carte) {
        maps.add(carte);
    }

    public Prism getPrism() {
        return prism;
    }

    public void setPrism(Prism prism) {
        this.prism = prism;
    }
}
