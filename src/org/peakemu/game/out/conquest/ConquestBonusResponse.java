/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.conquest;

import org.peakemu.common.util.StringUtil;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class ConquestBonusResponse {
    static public class Data{
        public double xp;
        public double drop;
        public double recolte;

        public Data(double xp, double drop, double recolte) {
            this.xp = xp;
            this.drop = drop;
            this.recolte = recolte;
        }

        @Override
        public String toString() {
            return xp + "," + drop + "," + recolte;
        }
    }
    
    final private Data[] datas = new Data[3];

    public ConquestBonusResponse setAlignBonus(Data data){
        datas[0] = data;
        return this;
    }
    
    public ConquestBonusResponse setRankMultiplicator(Data data){
        datas[1] = data;
        return this;
    }
    
    public ConquestBonusResponse setAlignMalus(Data data){
        datas[2] = data;
        return this;
    }

    @Override
    public String toString() {
        return "CB" + StringUtil.join(datas, ";");
    }
}
