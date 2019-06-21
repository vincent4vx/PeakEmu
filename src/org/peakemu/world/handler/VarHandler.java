/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.handler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.peakemu.Peak;
import org.peakemu.common.Logger;
import org.peakemu.common.util.StringUtil;
import org.peakemu.objects.player.Player;
import org.peakemu.world.var.BankCostVar;
import org.peakemu.world.var.NameVar;
import org.peakemu.world.var.Var;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class VarHandler {
    final private Peak peak;
    
    final private Map<String, Var> vars = new HashMap<>();

    public VarHandler(Peak peak) {
        this.peak = peak;
    }
    
    public void registerVar(Var var){
        vars.put(var.name(), var);
    }
    
    public void load(){
        registerVar(new NameVar());
        registerVar(new BankCostVar());
    }
    
    public Object getValue(String var, Player player){
        if(!vars.containsKey(var)){
            Peak.worldLog.addToLog(Logger.Level.DEBUG, "Undefined var %s", var);
            return null;
        }
            
        return vars.get(var).getValue(player);
    }
    
    public Collection parseVarString(String varString, Player player){
        Collection ret = new ArrayList();
        
        for(String var : StringUtil.split(varString, ",")){
            if(var.startsWith("[") && var.endsWith("]")){
                ret.add(getValue(var.substring(1, var.length() - 1), player));
            }else{
                ret.add(var);
            }
        }
        
        return ret;
    }
}
