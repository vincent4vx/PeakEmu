/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.handler;

import java.util.ArrayList;
import java.util.Collection;
import org.peakemu.Peak;
import org.peakemu.common.Constants;
import org.peakemu.common.Logger;
import org.peakemu.database.dao.PrismDAO;
import org.peakemu.game.out.area.AreaAlterAlign;
import org.peakemu.game.out.InfoMessage;
import org.peakemu.game.out.area.SubAreaAlterAlign;
import org.peakemu.game.out.conquest.PrismAttackers;
import org.peakemu.game.out.conquest.PrismDefenders;
import org.peakemu.objects.Prism;
import org.peakemu.objects.player.Player;
import org.peakemu.world.Area;
import org.peakemu.world.ExpLevel;
import org.peakemu.world.GameMap;
import org.peakemu.world.MapCell;
import org.peakemu.world.SubArea;
import org.peakemu.world.fight.ConquestFight;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class AlignementHandler {
    final private AreaHandler areaHandler;
    final private PrismDAO prismDAO;
    final private SessionHandler sessionHandler;
    final private ExpHandler expHandler;

    public AlignementHandler(AreaHandler areaHandler, PrismDAO prismDAO, SessionHandler sessionHandler, ExpHandler expHandler) {
        this.areaHandler = areaHandler;
        this.prismDAO = prismDAO;
        this.sessionHandler = sessionHandler;
        this.expHandler = expHandler;
    }
    
    public void load(){
        System.out.print("Chargement des prismes  : ");
        
        int nb = 0;
        for(Prism prism : prismDAO.getAll()){
            prism.getSubArea().setPrism(prism);
            
            if(prism.isOnArea())
                prism.getArea().setPrism(prism);
            
            prism.setSpriteId(prism.getMap().getNextSpriteId());
            prism.getMap().addSprite(prism);
            ++nb;
        }
        
        System.out.println(nb + " prismes chargés");
    }
    
    public Collection<Prism> getPrismsByAlignement(int align){
        Collection<Prism> prisms = new ArrayList<>();
        
        for(Prism prism : prismDAO.getAll()){
            if(prism.getAlignement() == align)
                prisms.add(prism);
        }
        
        return prisms;
    }
    
    public Collection<Prism> getBontarienPrisms(){
        return getPrismsByAlignement(Constants.ALIGNEMENT_BONTARIEN);
    }
    
    public Collection<Prism> getBrakmarienPrisms(){
        return getPrismsByAlignement(Constants.ALIGNEMENT_BRAKMARIEN);
    }
    
    public void addPrism(int align, GameMap map, MapCell cell){
        addPrism(new Prism(-1, align, 1, map, cell, 0, false, prismDAO.getSpells()));
    }
    
    public void addPrism(Prism prism){
        if(prism.getArea().getalignement() == Constants.ALIGNEMENT_NONE){
            prism.setIsOnArea(true);
        }
        
        prism = prismDAO.insert(prism);
        
        prism.getSubArea().setPrism(prism);
        
        if(prism.isOnArea()){
            prism.getArea().setPrism(prism);
        }
        
        SubAreaAlterAlign packet = new SubAreaAlterAlign(prism.getSubArea(), true);
        
        for(Player player : sessionHandler.getOnlinePlayers()){
            player.send(packet.setShowMsg(player.getAlignement() != Constants.ALIGNEMENT_NEUTRE));
        }
        
        if(prism.isOnArea()){
            AreaAlterAlign packet2 = new AreaAlterAlign(prism.getArea());
            for(Player player : sessionHandler.getOnlinePlayers()){
                player.send(packet2);
            }
        }
        
        prism.setSpriteId(prism.getMap().getNextSpriteId());
        prism.getMap().addSprite(prism);
    }
    
    public void removePrism(Prism prism){
        prismDAO.delete(prism);
        
        prism.getSubArea().setPrism(null);
        prism.getMap().removeSprite(prism);
        
        SubAreaAlterAlign packet = new SubAreaAlterAlign(prism.getSubArea(), true);
        
        for(Player player : sessionHandler.getOnlinePlayers()){
            player.send(packet.setShowMsg(player.getAlignement() != Constants.ALIGNEMENT_NEUTRE));
        }
        
        if(prism.isOnArea()){
            prism.getArea().setPrism(null);
            AreaAlterAlign packet2 = new AreaAlterAlign(prism.getArea());
            for(Player player : sessionHandler.getOnlinePlayers()){
                player.send(packet2);
            }
        }
    }
    
    public boolean canAddPrism(Player player, GameMap map){
        if(player.getAlignement() != Constants.ALIGNEMENT_BONTARIEN && player.getAlignement() != Constants.ALIGNEMENT_BRAKMARIEN)
            return false;
        
        if(map.getSubArea().getAlignement() != Constants.ALIGNEMENT_NEUTRE){
            player.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(149));
            return false;
        }
        
        if(!map.getSubArea().isConquestable()){
            player.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(147));
            return false;
        }
        
        if(player.getLevel() < 10 || player.getGrade() < 3){
            player.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(155));
            return false;
        }
        
        if(!player.showWings()){
            player.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(148));
            return false;
        }
        
        return true;
    }
    
    public void saveAll(){
        Peak.worldLog.addToLog(Logger.Level.INFO, "Sauvegarde des prismes...");
        prismDAO.saveAll();
    }
    
    public void sendToAlignement(Object packet, int align){
        String sPacket = packet.toString();
        
        for(Player player : sessionHandler.getOnlinePlayers()){
            if(player.getAlignement() == align)
                player.send(sPacket);
        }
    }
    
    public int getWorldRate(int align){
        return getConquestedAreasCount(align) * 100 / areaHandler.getSubAreas().size();
    }
    
    public int getAreaRate(Area area, int align){
        int count = 0;
        
        for(SubArea subArea : area.getSubAreas()){
            if(subArea.getAlignement() == align)
                ++count;
        }
        
        return count * 100 / area.getSubAreas().size();
    }
    
    public int getConquestedAreasCount(int align){
        int count = 0;
        
        for(SubArea subArea : areaHandler.getSubAreas()){
            if(subArea.getAlignement() == align)
                ++count;
        }
        
        return count;
    }
    
    public int getConquestableAreasCount(){
        int count = 0;
        
        for(SubArea area : areaHandler.getSubAreas()){
            if(area.isConquestable())
                ++count;
        }
        
        return count;
    }
    
    public int getFreeAreasCount(){
        int count = 0;
        
        for(SubArea area : areaHandler.getSubAreas()){
            if(area.isConquestable() && area.getAlignement() == Constants.ALIGNEMENT_NEUTRE)
                ++count;
        }
        
        return count;
    }
    
    public void sendAddPrismDefender(Prism prism, Player defender){
        String packet = PrismDefenders.addDefender(prism, defender).toString();
        
        for(Player player : sessionHandler.getOnlinePlayers()){
            if(player.getAlignement() != prism.getAlignement()
                || !player.getMap().getSubArea().equals(prism.getSubArea()))
                continue;
            
            player.send(packet);
        }
    }
    
    public void sendAddPrismAttacker(Prism prism, Player attacker){
        String packet = PrismAttackers.addAttacker(prism, attacker).toString();
        
        for(Player player : sessionHandler.getOnlinePlayers()){
            if(player.getAlignement() != prism.getAlignement()
                || !player.getMap().getSubArea().equals(prism.getSubArea()))
                continue;
            
            player.send(packet);
        }
    }
    
    public void removeAllPrismFighters(ConquestFight fight){
        String packet1 = PrismAttackers.removeAllAttackers(fight).toString();
        String packet2 = PrismDefenders.removeAllDefenders(fight).toString();
        
        for(Player player : sessionHandler.getOnlinePlayers()){
            if(player.getAlignement() != fight.getPrism().getAlignement()
                || !player.getMap().getSubArea().equals(fight.getPrism().getSubArea()))
                continue;
            
            player.send(packet1);
            player.send(packet2);
        }
    }
    
    public void prismAddHonor(Prism prism, int honor){
        if(prism.getLevel() == expHandler.getMaxPvpLevel())
            return;
        
        ExpLevel expLevel = expHandler.getLevel(prism.getLevel());
        prism.addHonor(honor);
        
        while(expLevel.getNext() != null
            && expLevel.getNext().pvp != -1
            && prism.getHonor() >= expLevel.getNext().pvp){
            expLevel = expLevel.getNext();
        }
        
        if(prism.getLevel() != expLevel.level)
            prism.setLevel(expLevel.level);
    }
}
