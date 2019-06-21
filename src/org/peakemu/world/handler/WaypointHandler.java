/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.handler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import org.peakemu.common.Constants;
import org.peakemu.common.util.Pair;
import org.peakemu.database.dao.WaypointDAO;
import org.peakemu.game.out.waypoint.CantUseZaap;
import org.peakemu.game.out.waypoint.CantUseZaapi;
import org.peakemu.game.out.InfoMessage;
import org.peakemu.game.out.waypoint.PrismLeaved;
import org.peakemu.game.out.waypoint.ZaapLeaved;
import org.peakemu.game.out.waypoint.ZaapiLeaved;
import org.peakemu.objects.Prism;
import org.peakemu.objects.player.Player;
import org.peakemu.world.GameMap;
import org.peakemu.world.MapCell;
import org.peakemu.world.enums.IOType;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class WaypointHandler {
    static public class Waypoint{
        final private GameMap map;
        final private MapCell cell;

        public Waypoint(GameMap map, MapCell cell) {
            this.map = map;
            this.cell = cell;
        }

        public GameMap getMap() {
            return map;
        }

        public MapCell getCell() {
            return cell;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 79 * hash + Objects.hashCode(this.map);
            hash = 79 * hash + Objects.hashCode(this.cell);
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
            final Waypoint other = (Waypoint) obj;
            if (!Objects.equals(this.map, other.map)) {
                return false;
            }
            if (!Objects.equals(this.cell, other.cell)) {
                return false;
            }
            return true;
        }
    }
    
    final private WaypointDAO waypointDAO;
    final private PlayerHandler playerHandler;
    final private MapHandler mapHandler;
    final private AlignementHandler alignementHandler;

    public WaypointHandler(WaypointDAO waypointDAO, PlayerHandler playerHandler, MapHandler mapHandler, AlignementHandler alignementHandler) {
        this.waypointDAO = waypointDAO;
        this.playerHandler = playerHandler;
        this.mapHandler = mapHandler;
        this.alignementHandler = alignementHandler;
    }
    
    public void load(){
        System.out.print("Chargement des zaapis : ");
        int size = waypointDAO.getAllZaapis().size();
        
        if(size == 0){
            System.out.print("(via IOs) ");
            for(GameMap map : mapHandler.getMaps()){
                for(MapCell cell : map.getCells()){
                    if(cell.isInteractive() && cell.getObject().getType() == IOType.ZAAPI){
                        waypointDAO.createZaapi(new Waypoint(map, cell));
                        ++size;
                    }
                }
            }
        }
        
        System.out.println(size + " zaapis chargés");
    }
    
    public int getZaapCost(Player player, Waypoint current, Waypoint target){
        if(current.equals(target))
            return 0;
        
        return (int)(10 * Math.sqrt((current.getMap().getX() - target.getMap().getX()) * (current.getMap().getX() - target.getMap().getX()) + (current.getMap().getY() - target.getMap().getY()) * (current.getMap().getY() - target.getMap().getY())));
    }
    
    public Collection<Pair<Waypoint, Integer>> getZaapList(Player player, Waypoint current){
        Collection<Pair<Waypoint, Integer>> list = new ArrayList<>(player.getZaaps().size());
        
        for(Waypoint zaap : player.getZaaps()){
            if(current.getMap().getSubArea().getArea().getSuperArea() != zaap.getMap().getSubArea().getArea().getSuperArea()) //only same super area
                continue;
            
            list.add(new Pair<>(zaap, getZaapCost(player, current, zaap)));
        }
        
        return list;
    }
    
    public void useZaap(Player player, Waypoint current, short map){
        Waypoint target = null;
        
        for(Waypoint wp : player.getZaaps()){
            if(wp.getMap().getId() == map){
                target = wp;
                break;
            }
        }
        
        if(target == null){
            player.send(new CantUseZaap());
            return;
        }
        
        int cost = getZaapCost(player, current, target);
        
        if(cost > player.getKamas()){
            player.send(new CantUseZaap());
            return;
        }
        
        if(current.getMap().getSubArea().getArea().getSuperArea() != target.getMap().getSubArea().getArea().getSuperArea()){
            player.send(new CantUseZaap());
            return;
        }
        
        player.removeKamas(cost);
        player.setCurrentWaypoint(null);
        player.send(new ZaapLeaved());
        
        playerHandler.teleport(player, target.getMap(), target.getCell());
    }
    
    public Collection<Pair<Waypoint, Integer>> getZaapiList(Player player, Waypoint current){
        int align = current.getMap().getSubArea().getArea().getalignement();
        
        if(!canUseZaapi(player, current))
            return Collections.EMPTY_LIST;
        
        Collection<Pair<Waypoint, Integer>> list = new ArrayList<>();
        
        int cost = align == player.getAlignement() ? 10 : 20;
        
        for(Waypoint zaapi : waypointDAO.getAllZaapis()){
            if(zaapi.getMap().getSubArea().getArea() != current.getMap().getSubArea().getArea())
                continue;
            
            if(!zaapi.equals(current))
                list.add(new Pair<>(zaapi, cost));
            else
                list.add(new Pair<>(current, 0));
        }
        
        return list;
    }
    
    public boolean canUseZaapi(Player player, Waypoint zaapi){
        int align = zaapi.getMap().getSubArea().getArea().getalignement();
        
        if(player.getAlignement() == Constants.ALIGNEMENT_NEUTRE || player.getAlignement() == Constants.ALIGNEMENT_MERCENAIRE) //neutral and serian can always use zaapis
            return true;
        
        return (align != Constants.ALIGNEMENT_BONTARIEN && align != Constants.ALIGNEMENT_BRAKMARIEN) || player.getAlignement() == align;
    }
    
    public void useZaapi(Player player, Waypoint current, short map){
        Pair<Waypoint, Integer> target = null;
        
        for(Pair<Waypoint, Integer> pair : getZaapiList(player, current)){
            if(pair.getFirst().getMap().getId() == map){
                target = pair;
                break;
            }
        }
        
        if(target == null){
            player.send(new CantUseZaapi());
            return;
        }
        
        int cost = target.getSecond();
        
        if(cost > player.getKamas()){
            player.send(new CantUseZaapi());
            return;
        }
        
        player.removeKamas(cost);
        player.setCurrentWaypoint(null);
        player.send(new ZaapiLeaved());
        
        playerHandler.teleport(player, target.getFirst().getMap(), target.getFirst().getCell());
    }
    
    public int getPrismCost(Prism current, Prism target){ //same as zaaps ?
        if(current.equals(target))
            return 0;
        
        return (int)(10 * Math.sqrt((current.getMap().getX() - target.getMap().getX()) * (current.getMap().getX() - target.getMap().getX()) + (current.getMap().getY() - target.getMap().getY()) * (current.getMap().getY() - target.getMap().getY())));
    }
    
    public Collection<Pair<Prism, Integer>> getPrismList(Player player, Prism current){
        Collection<Pair<Prism, Integer>> prisms = new ArrayList<>();
        
        for(Prism prism : alignementHandler.getPrismsByAlignement(player.getAlignement())){
            prisms.add(new Pair<>(prism, getPrismCost(current, prism)));
        }
        
        return prisms;
    }
    
    public void usePrism(Player player, Prism current, Prism target){
        if(player.getAlignement() != current.getAlignement() || current.getAlignement() != target.getAlignement())
            return;
        
        int cost = getPrismCost(current, target);
        
        if(cost > player.getKamas()){
            player.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(82));
            return;
        }
        
        if(player.getDisgrace() >= 3){
            player.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(83));
            return;
        }
        
        if(!player.showWings()){
            player.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(144));
            return;
        }
        
        player.removeKamas(cost);
        player.send(new PrismLeaved());
        
        playerHandler.teleport(player, target.getMap(), target.getCell());
    }
}
