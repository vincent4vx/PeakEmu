/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.common.util;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class Util {
    final static private Random RANDOM = new Random();
    
    static public int rand(int min, int max){
        if(max <= min)
            return min;
        
        return RANDOM.nextInt(max + 1 - min) + min;
    }
    
    static public String getStringTime(){
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND);
    }
    
    static public String getStringDate(){
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "-" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "-" + Calendar.getInstance().get(Calendar.YEAR);
    }
    
    static public int maxArrayLength(Object[]... arrays){
        int maxLen = 0;
        
        for(Object[] array : arrays){
            if(array.length > maxLen)
                maxLen = array.length;
        }
        
        return maxLen;
    }
    
    static public int minArrayLength(Object[]... arrays){
        int minLen = Integer.MAX_VALUE;
        
        for(Object[] array : arrays){
            if(array.length < minLen)
                minLen = array.length;
        }
        
        return minLen;
    }
    
    static public int randInt(int max){
        return RANDOM.nextInt(max);
    }
    
    static public<T> T rand(List<T> list){
        return list.get(RANDOM.nextInt(list.size()));
    }
    
    static public boolean randBool(int percent){
        return RANDOM.nextInt(100) < percent;
    }
    
    static public<T> T rand(T[] array){
        return array[RANDOM.nextInt(array.length)];
    }
    
    static public int max(int... values){
        if(values.length == 0)
            throw new RuntimeException("Util.max : need 1 or more values");
        
        int max = values[0];
        
        for(int value : values){
            if(value > max)
                max = value;
        }
        
        return max;
    }
}