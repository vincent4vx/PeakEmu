/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.action;

import java.util.Objects;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class Action {

    private final int id;
    private final String args;
    private final String cond;

    public Action(int id, String args, String cond) {
        this.id = id;
        this.args = args;
        this.cond = cond;
    }

    public int getId() {
        return id;
    }

    public String getArgs() {
        return args;
    }

    public String getCond() {
        return cond;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + this.id;
        hash = 67 * hash + Objects.hashCode(this.args);
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
        final Action other = (Action) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.args, other.args)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Action{" + "id=" + id + ", args=" + args + ", cond=" + cond + '}';
    }
}
