/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.guild;

import org.peakemu.common.Constants;
import org.peakemu.objects.Collector;
import org.peakemu.objects.Guild;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class InfosTaxCollectorsMovement {
    private Guild guild;

    public InfosTaxCollectorsMovement(Guild guild) {
        this.guild = guild;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        
        sb.append("gITM");
        
        if(guild.getCollectors().isEmpty()){
            sb.append("null");
            return sb.toString();
        }
        
        sb.append('+');
        
        boolean b = false;
        for(Collector collector : guild.getCollectors()){
            if(b)
                sb.append('|');
            else
                b = true;
            
            sb.append(Integer.toString(collector.getGuid(), 36)).append(';')
              .append(collector.get_N1()).append(',').append(collector.get_N2()).append(';')
              .append(Integer.toString(collector.getMap().getId(), 36)).append(',').append(collector.getMap().getX()).append(',').append(collector.getMap().getY()).append(';')
              .append(collector.get_inFight()).append(';')
            ;
            
            if (collector.getFight() != null && collector.getFight().getState() == Constants.FIGHT_STATE_PLACE) {
                sb.append(collector.getFight().getRemaningTime()).append(";");//TimerActuel
                sb.append(45000).append(";");//TimerInit
                sb.append(collector.getFight().getCollectorTeam().getStartCells().size() - 1).append("7;");//Nombre de place maximum FIXME : En fonction de la map
                sb.append("?,?,");//?
            } else {
                sb.append("0;");
                sb.append(45000).append(";");
                sb.append("7;");
                sb.append("?,?,");
            }
            sb.append("1,2,3,4,5");

            //	?,?,callername,startdate(Base 10),lastHarvesterName,lastHarvestDate(Base 10),nextHarvestDate(Base 10)
        }
        
        return sb.toString();
    }
    
    
}
