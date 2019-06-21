/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.handler;

import org.peakemu.common.util.Util;
import org.peakemu.database.dao.MountDAO;
import org.peakemu.database.dao.MountParkDAO;
import org.peakemu.database.dao.MountTemplateDAO;
import org.peakemu.objects.Mount;
import org.peakemu.objects.MountPark;
import org.peakemu.world.ExpLevel;
import org.peakemu.world.GameMap;
import org.peakemu.world.ItemTemplate;
import org.peakemu.world.MapCell;
import org.peakemu.world.MountTemplate;
import org.peakemu.world.config.MountConfig;
import org.peakemu.world.enums.IOType;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class MountHandler {
    final private MountParkDAO mountParkDAO;
    final private MountDAO mountDAO;
    final private MapHandler mapHandler;
    final private MountConfig config;
    final private MountTemplateDAO mountTemplateDAO;
    final private ExpHandler expHandler;

    public MountHandler(MountParkDAO mountParkDAO, MountDAO mountDAO, MapHandler mapHandler, MountConfig config, MountTemplateDAO mountTemplateDAO, ExpHandler expHandler) {
        this.mountParkDAO = mountParkDAO;
        this.mountDAO = mountDAO;
        this.mapHandler = mapHandler;
        this.config = config;
        this.mountTemplateDAO = mountTemplateDAO;
        this.expHandler = expHandler;
    }
    
    public void loadMounts(){
        System.out.print("Chargement des templates de montures : ");
        System.out.println(mountTemplateDAO.getAll().size() + " templates chargés");
        
        System.out.print("Chargement des dragodindes : ");
        System.out.println(mountDAO.getAll().size() + " dragodindes chargées");
    }
    
    public void loadParks(){
        System.out.print("Chargement des enclos : ");
        System.out.println(mountParkDAO.getAll().size() + " enclos chargés");
        
        if(config.getLoadMountParksWithMapData()){
            System.out.print("Chargement des enclos via map data : ");
            
            int count = 0;
            
            for(GameMap map : mapHandler.getMaps()){
                for(MapCell cell : map.getCells()){
                    if(cell.isInteractive() && cell.getObject().getType() == IOType.ENCLOS){
                        ++count;
                        
                        MountPark park = mountParkDAO.getMountParkByMap(map);
                        
                        if(park == null){
                            createDefaultMountPark(cell);
                        }else{
                            park.setCell(cell);
                            mountParkDAO.save(park);
                        }
                    }
                }
            }
            
            System.out.println(count + " enclos chargés");
        }
    }
    
    public MountPark createDefaultMountPark(MapCell cell){
        MountPark park = new MountPark(null, cell.getMap(), cell, config.getDefaultMountParkSize(), config.getDefaultMountParkPrice(), false);
        mountParkDAO.save(park);
        return park;
    }
    
    public MountTemplate getMountTemplateByParchment(ItemTemplate parchment){
        for(MountTemplate template : mountTemplateDAO.getAll()){
            if(parchment.equals(template.getParchment()))
                return template;
        }
        
        return null;
    }
    
    public Mount createMount(MountTemplate template){
        Mount mount = new Mount(
            -1, 
            template, 
            Util.rand(0, 1), 
            0, 0, 
            expHandler.getLevel(config.getStartMountLevel()), 0, 
            config.getDefaultName(), 0, 1000, 0, 1000, 0, null, ",,,,,,,,,,,,,", ""
        );
        
        return mountDAO.insert(mount);
    }
    
    public void saveAll(){
        System.out.println("Sauvegarde des enclos...");
        for(MountPark mp : mountParkDAO.getAll()){
            mountParkDAO.save(mp);
        }
        
        System.out.println("Sauvegarde des dragodindes...");
        for(Mount mount : mountDAO.getAll()){
            mountDAO.save(mount);
        }
    }

    public Mount getMountById(int id) {
        return mountDAO.getMountById(id);
    }
    
    public void addXp(Mount mount, long xp){
        if(mount.getLevel() >= expHandler.getMaxMountLevel()
            && !config.getAddXpOnMaxLevel())
            return;
        
        mount.addXp(xp);
        
        ExpLevel level = mount.getExpLevel();
        
        while(level.getNext() != null
            && level.getNext().mount != -1
            && mount.getExp() > level.mount){
            level = level.getNext();
        }
        
        mount.setExpLevel(level);
    }
    
}
