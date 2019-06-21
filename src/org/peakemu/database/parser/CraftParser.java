/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.database.parser;

import java.util.ArrayList;
import java.util.Collection;
import org.peakemu.common.util.Pair;
import org.peakemu.common.util.StringUtil;
import org.peakemu.database.dao.ItemDAO;
import org.peakemu.world.ItemTemplate;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class CraftParser {
    static public Collection<Pair<ItemTemplate, Integer>> getRecipe(ItemDAO itemDAO, String str){
        Collection<Pair<ItemTemplate, Integer>> ingredients = new ArrayList<>();
        
        for(String s : StringUtil.split(str, ";")){
            if(s.isEmpty())
                continue;
            
            String[] data = StringUtil.split(s, "*", 2);
            
            ingredients.add(new Pair<>(
                itemDAO.getById(Integer.parseInt(data[0])),
                Integer.parseInt(data[1])
            ));
        }
        
        return ingredients;
    }
}
