/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world;

import java.util.ArrayList;
import java.util.Collection;

public class SuperArea {

    private int id;
    private Collection<Area> areas = new ArrayList<>();

    public SuperArea(int a_id) {
        id = a_id;
    }

    public void addArea(Area A) {
        areas.add(A);
    }

    public int get_id() {
        return id;
    }
}
