/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.objects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.peakemu.objects.player.Player;
import org.peakemu.world.GameMap;
import org.peakemu.world.MapCell;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class MountPark {
    private int size;
    private ArrayList<MapCell> _cases = new ArrayList<>();
    private Guild guild;
    final private GameMap map;
    private MapCell cell;
    private long price;
    final private boolean isPublic;
    final private Map<Mount, Player> mounts = new ConcurrentHashMap<>();

    public MountPark(Guild guild, GameMap map, MapCell cell, int size, long price, boolean isPublic) {
        this.guild = guild;
        this.size = size;
        this.map = map;
        this.cell = cell;
        this.price = price;
        this.isPublic = isPublic;
    }

    public Guild getGuild() {
        return guild;
    }

    public void setGuild(Guild guild) {
        this.guild = guild;
    }

    public int getSize() {
        return size;
    }

    public GameMap getMap() {
        return map;
    }

    public MapCell getCell() {
        return cell;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public int getObjectNumb() {
        int n = 0;
        for (MapCell C : _cases) {
            if (C.getObject() != null) {
                n++;
            }
        }
        return n;
    }

    public int mountCount() {
        return mounts.size();
    }
    
    public boolean isPublic(){
        return isPublic;
    }
    
    public boolean isFull(){
        return mounts.size() >= size;
    }
    
    public void addMount(Mount mount, Player owner){
        mounts.put(mount, owner);
    }
    
    public Mount getMountById(int id){
        for(Mount mount : mounts.keySet()){
            if(mount.getId() == id)
                return mount;
        }
        
        return null;
    }
    
    public Collection<Mount> getMounts(Player owner){
        Collection<Mount> m = new ArrayList<>();
        
        for(Map.Entry<Mount, Player> entry : mounts.entrySet()){
            if(!isPublic || entry.getValue().equals(owner))
                m.add(entry.getKey());
        }
        
        return m;
    }
    
    public boolean containsMount(Mount mount){
        return mounts.containsKey(mount);
    }
    
    public Map<Mount, Player> getMountsWithOwner(){
        return mounts;
    }
    
    public Player getOwner(Mount mount){
        return mounts.get(mount);
    }
    
    public void removeMount(Mount mount){
        mounts.remove(mount);
    }

    public void setCell(MapCell cell) {
        this.cell = cell;
    }
}
