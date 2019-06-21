/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.peakemu.Ancestra;
import org.peakemu.Peak;
import org.peakemu.common.util.Util;
import org.peakemu.database.dao.MonsterDAO;
import org.peakemu.database.parser.MonsterParser;
import org.peakemu.game.GameServer;
import org.peakemu.objects.item.Item;
import org.peakemu.objects.Monster;
import org.peakemu.objects.player.Player;
import org.peakemu.world.MapCell;
import org.peakemu.objects.MonsterGroup;
import org.peakemu.world.FixedMonsterGroup;
import org.peakemu.world.World;
import org.peakemu.world.handler.fight.FightHandler;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class StartFight implements ActionPerformer {
    final private FightHandler fightHandler;
    final private MonsterDAO monsterDAO;

    public StartFight(FightHandler fightHandler, MonsterDAO monsterDAO) {
        this.fightHandler = fightHandler;
        this.monsterDAO = monsterDAO;
    }

    @Override
    public int actionId() {
        return 27;
    }

    @Override
    public boolean apply(Action action, Player caster, Player target, MapCell cell, Item item) {
        String ValidMobGroup = "";
        try {
            for (String MobAndLevel : action.getArgs().split("\\|")) {
                int monsterID = -1;
                int monsterLevel = -1;
                String[] MobOrLevel = MobAndLevel.split(",");
                monsterID = Integer.parseInt(MobOrLevel[0]);
                monsterLevel = Integer.parseInt(MobOrLevel[1]);

                if (World.getMonstre(monsterID) == null || World.getMonstre(monsterID).getGradeByLevel(monsterLevel) == null) {
                    if (Ancestra.CONFIG_DEBUG) {
                        GameServer.addToLog("Monstre invalide : monsterID:" + monsterID + " monsterLevel:" + monsterLevel);
                    }
                    continue;
                }
                ValidMobGroup += monsterID + "," + monsterLevel + "," + monsterLevel + ";";
            }
            if (ValidMobGroup.isEmpty()) {
                return false;
            }
            
            
            Collection<Monster> monsters = new ArrayList<>();

            for(FixedMonsterGroup.MonsterEntry entry : MonsterParser.parseMonsterEntries(monsterDAO, ValidMobGroup)){
                List<Monster> possible = new ArrayList<>();

                for(Monster monster : entry.getTemplate().getGrades()){
                    if(monster.getLevel() >= entry.getMinLevel() || monster.getLevel() <= entry.getMaxLevel())
                        possible.add(monster);
                }

                if(possible.isEmpty())
                    continue;

                monsters.add(Util.rand(possible));
            }

            MonsterGroup group = new MonsterGroup(monsters);
            group.setCell(caster.getCell());
            
            fightHandler.startPvMFight(caster.getMap(), caster, group);
            return true;
        } catch (Exception e) {
            Peak.errorLog.addToLog(e);
            return false;
        }
    }

}
