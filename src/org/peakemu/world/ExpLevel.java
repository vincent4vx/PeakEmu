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
public class ExpLevel implements Comparable<ExpLevel>{
    final public int level;
    final public long player;
    final public long job;
    final public long mount;
    final public int pvp;
    final public long guild;
    
    private ExpLevel next = null;

    public ExpLevel(int level, long player, long job, long mount, int pvp) {
        this.level = level;
        this.player = player;
        this.job = job;
        this.mount = mount;
        this.pvp = pvp;
        this.guild = 10 * player;
    }

    public ExpLevel getNext() {
        return next;
    }

    public void setNext(ExpLevel next) {
        this.next = next;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + this.level;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ExpLevel other = (ExpLevel) obj;
        if (this.level != other.level) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(ExpLevel t) {
        return level - t.level;
    }
}
