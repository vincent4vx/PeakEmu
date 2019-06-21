/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.handler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.peakemu.common.Formulas;
import org.peakemu.common.util.Util;
import org.peakemu.database.dao.MonsterDAO;
import org.peakemu.objects.Monster;
import org.peakemu.objects.MonsterGroup;
import org.peakemu.world.GameMap;
import org.peakemu.world.MonsterTemplate;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class MonsterHandler {
    final private MonsterDAO monsterDAO;

    public MonsterHandler(MonsterDAO monsterDAO) {
        this.monsterDAO = monsterDAO;
    }
    
    public void load(){
        System.out.print("Chargement des templates de monstres : ");
        System.out.println(monsterDAO.getAll().size() + " monstres chargés");
    }

    public MonsterTemplate getMonsterById(int id) {
        return monsterDAO.getMonsterById(id);
    }
    
    public MonsterGroup createMonsterGroup(List<Monster> available, int maxSize){
        if(available.isEmpty())
            return null;
        
        int rand = 0;
        int nbr = 0;

        switch (maxSize) {
            case 0:
                return null;
            case 1:
                nbr = 1;
                break;
            case 2:
                nbr = Formulas.getRandomValue(1, 2);	//1:50%	2:50%
                break;
            case 3:
                nbr = Formulas.getRandomValue(1, 3);	//1:33.3334%	2:33.3334%	3:33.3334%
                break;
            case 4:
                rand = Formulas.getRandomValue(0, 99);
                if (rand < 22) //1:22%
                {
                    nbr = 1;
                } else if (rand < 48) //2:26%
                {
                    nbr = 2;
                } else if (rand < 74) //3:26%
                {
                    nbr = 3;
                } else //4:26%
                {
                    nbr = 4;
                }
                break;
            case 5:
                rand = Formulas.getRandomValue(0, 99);
                if (rand < 15) //1:15%
                {
                    nbr = 1;
                } else if (rand < 35) //2:20%
                {
                    nbr = 2;
                } else if (rand < 60) //3:25%
                {
                    nbr = 3;
                } else if (rand < 85) //4:25%
                {
                    nbr = 4;
                } else //5:15%
                {
                    nbr = 5;
                }
                break;
            case 6:
                rand = Formulas.getRandomValue(0, 99);
                if (rand < 10) //1:10%
                {
                    nbr = 1;
                } else if (rand < 25) //2:15%
                {
                    nbr = 2;
                } else if (rand < 45) //3:20%
                {
                    nbr = 3;
                } else if (rand < 65) //4:20%
                {
                    nbr = 4;
                } else if (rand < 85) //5:20%
                {
                    nbr = 5;
                } else //6:15%
                {
                    nbr = 6;
                }
                break;
            case 7:
                rand = Formulas.getRandomValue(0, 99);
                if (rand < 9) //1:9%
                {
                    nbr = 1;
                } else if (rand < 20) //2:11%
                {
                    nbr = 2;
                } else if (rand < 35) //3:15%
                {
                    nbr = 3;
                } else if (rand < 55) //4:20%
                {
                    nbr = 4;
                } else if (rand < 75) //5:20%
                {
                    nbr = 5;
                } else if (rand < 91) //6:16%
                {
                    nbr = 6;
                } else //7:9%
                {
                    nbr = 7;
                }
                break;
            default:
                rand = Formulas.getRandomValue(0, 99);
                if (rand < 9) //1:9%
                {
                    nbr = 1;
                } else if (rand < 20) //2:11%
                {
                    nbr = 2;
                } else if (rand < 33) //3:13%
                {
                    nbr = 3;
                } else if (rand < 50) //4:17%
                {
                    nbr = 4;
                } else if (rand < 67) //5:17%
                {
                    nbr = 5;
                } else if (rand < 80) //6:13%
                {
                    nbr = 6;
                } else if (rand < 91) //7:11%
                {
                    nbr = 7;
                } else //8:9%
                {
                    nbr = 8;
                }
                break;
        }
        
        Collection<Monster> chooseMonsters = new ArrayList<>(nbr);
        
        Monster first = available.get(Util.rand(0, available.size() - 1));
        chooseMonsters.add(first);
        
        while(chooseMonsters.size() < nbr){
            Monster tmp = available.get(Util.rand(0, available.size() - 1));
            
            if(tmp.getTemplate().getAlign() != first.getTemplate().getAlign())
                continue;
            
            chooseMonsters.add(tmp);
        }
        
        return new MonsterGroup(chooseMonsters);
    }
    
    public void spawnMonsterGroupOnMap(GameMap map){
        if(map.getMonsterGroupsCount() >= map.getGroupMaxSize())
            return;
        
        MonsterGroup monsterGroup = createMonsterGroup(map.getAvailableMonsters(), map.getGroupMaxSize());

        if(monsterGroup == null)
            return;

        map.spawnGroup(monsterGroup);
    }

    public Collection<MonsterTemplate> getAllMonsters() {
        return monsterDAO.getAll();
    }
}
