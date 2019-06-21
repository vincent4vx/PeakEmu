/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class IOTemplate {
    private int id;
    private int respawnTime;
    private int duration;
    private int unk;
    private boolean walkable;

    public IOTemplate(int id, int respawnTime, int duraction, int unknow, boolean walkable) {
        this.id = id;
        this.respawnTime = respawnTime;
        this.duration = duraction;
        this.unk = unknow;
        this.walkable = walkable;
    }

    public int getId() {
        return id;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public int getRespawnTime() {
        return respawnTime;
    }

    public int getDuration() {
        return duration;
    }

    public int getUnk() {
        return unk;
    }
}
