/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world;

import java.util.ArrayList;
import java.util.Collection;
import org.peakemu.objects.Prism;

public class Area {
    final private int id;
    final private SuperArea superArea;
    final private String name;
    final private Collection<SubArea> subAreas = new ArrayList<>();
    //prisme
    final private int alignement;
    
    private Prism prism;

    public Area(int id, String name, SuperArea superArea, int alignement) {
        this.id = id;
        this.name = name;
        this.superArea = superArea;
        this.alignement = alignement;
    }

    public int getalignement() {
        if(prism != null)
            return prism.getAlignement();
        
        return alignement;
    }

    public Prism getPrism() {
        return prism;
    }

    public void setPrism(Prism prism) {
        this.prism = prism;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public SuperArea getSuperArea() {
        return superArea;
    }

    public void addSubArea(SubArea sa) {
        subAreas.add(sa);
    }

    public Collection<SubArea> getSubAreas() {
        return subAreas;
    }

    public Collection<GameMap> getMaps() {
        Collection<GameMap> maps = new ArrayList<>();
        for (SubArea SA : subAreas) {
            maps.addAll(SA.getMaps());
        }
        return maps;
    }
}
