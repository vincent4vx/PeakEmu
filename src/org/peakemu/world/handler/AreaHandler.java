/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.handler;

import java.util.Collection;
import org.peakemu.database.dao.AreaDAO;
import org.peakemu.database.dao.SubAreaDAO;
import org.peakemu.world.Area;
import org.peakemu.world.SubArea;
import org.peakemu.world.SuperArea;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class AreaHandler {
    final private AreaDAO areaDAO;
    final private SubAreaDAO subAreaDAO;

    public AreaHandler(AreaDAO areaDAO, SubAreaDAO subAreaDAO) {
        this.areaDAO = areaDAO;
        this.subAreaDAO = subAreaDAO;
    }
    
    public SuperArea getSuperArea(int id){
        return areaDAO.getSuperArea(id);
    }
    
    public Area getArea(int id){
        return areaDAO.getAreaById(id);
    }
    
    public SubArea getSubArea(int id){
        return subAreaDAO.getSubAreaById(id);
    }
    
    public Collection<SubArea> getSubAreas(){
        return subAreaDAO.getAll();
    }
    
    public void load(){
        System.out.println("==> Chargement des zones <==");
        loadAreas();
        loadSubAreas();
    }
    
    private void loadAreas(){
        System.out.print("Chargement des zones : ");
        System.out.println(areaDAO.getAll().size() + " zones chargées");
    }
    
    private void loadSubAreas(){
        System.out.print("Chargement des sous-zones : ");
        System.out.println(subAreaDAO.getAll().size() + " sous-zones chargées");
    }
}
