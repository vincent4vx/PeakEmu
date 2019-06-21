/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.peakemu.world.enums.Effect;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class ItemSet {
    private int id;
    private Set<ItemTemplate> itemTemplates = new HashSet<>();
    private List<Stats> bonuses = new ArrayList<>();

    public ItemSet(int id, String bonuses) {
        this.id = id;

        //on ajoute un bonus vide pour 1 item
        this.bonuses.add(new Stats());
        //parse bonuses String
        for (String str : bonuses.split(";")) {
            Stats S = new Stats();
            //séparation des bonus pour un même nombre d'item
            for (String str2 : str.split(",")) {
                try {
                    String[] infos = str2.split(":");
                    int stat = Integer.parseInt(infos[0]);
                    int value = Integer.parseInt(infos[1]);
                    //on ajoute a la stat
                    S.addOneStat(Effect.valueOf(stat), value);
                } catch (Exception e) {
                };
            }
            //on ajoute la stat a la liste des bonus
            this.bonuses.add(S);
        }
    }

    public int getId() {
        return id;
    }

    public Stats getBonusStatByItemNumb(int numb) {
        if (numb > bonuses.size() || numb < 1) {
            return new Stats();
        }
        return bonuses.get(numb - 1);
    }

    public Set<ItemTemplate> getItemTemplates() {
        return itemTemplates;
    }
    
    public void addItemTemplate(ItemTemplate item){
        itemTemplates.add(item);
    }

    public boolean contains(ItemTemplate it) {
        return itemTemplates.contains(it);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + this.id;
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
        final ItemSet other = (ItemSet) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
}
