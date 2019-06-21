/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command.level3;

import java.util.Collection;
import org.peakemu.command.Command;
import org.peakemu.command.CommandPerformer;
import org.peakemu.database.Database;
import org.peakemu.database.dao.FixedMonsterGroupDAO;
import org.peakemu.database.dao.MonsterDAO;
import org.peakemu.database.parser.MonsterParser;
import org.peakemu.objects.player.Player;
import org.peakemu.world.FixedMonsterGroup;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class SpawnFix implements Command {
    final private MonsterDAO monsterDAO;
    final private FixedMonsterGroupDAO fixedMonsterGroupDAO;

    public SpawnFix(MonsterDAO monsterDAO, FixedMonsterGroupDAO fixedMonsterGroupDAO) {
        this.monsterDAO = monsterDAO;
        this.fixedMonsterGroupDAO = fixedMonsterGroupDAO;
    }

    @Override
    public String name() {
        return "SPAWNFIX";
    }

    @Override
    public String shortDescription() {
        return "Ajoute un groupe de monstre fixe sur la map";
    }

    @Override
    public String help() {
        return "SPAWNFIX [groupe] {respawn time}";
    }

    @Override
    public int minLevel() {
        return 3;
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        String groupData = args[0];
        int respawn = 0;
        
        if(args.length >= 2){
            try{
                respawn = Integer.parseInt(args[1]);
            }catch(NumberFormatException e){
                performer.displayError("Temps de respawn invalide : doit être un nombre de secondes valide");
                return;
            }
        }
            
        if(respawn < 0)
            performer.displayError("Temps de respawn invalide : doit être un nombre de secondes valide");

        Collection<FixedMonsterGroup.MonsterEntry> entries = MonsterParser.parseMonsterEntries(monsterDAO, groupData);
        
        if(entries.isEmpty()){
            performer.displayError("Données du groupe invalides");
            return;
        }
        
        FixedMonsterGroup fmg = new FixedMonsterGroup(performer.getPlayer().getMap(), performer.getPlayer().getCell(), respawn * 1000, entries);
        
        for(FixedMonsterGroup group : fixedMonsterGroupDAO.getAll()){
            if(group.getCell().equals(fmg.getCell())){
                performer.displayError("Un groupe existe déjà !");
                return;
            }
        }
        
        if(!fixedMonsterGroupDAO.insert(fmg)){
            performer.displayError("Erreur lors de l'insertion en BD");
            return;
        }
        
        fmg.respawn();
        performer.displayMessage("Le groupe a bien été ajouté !");
    }

}
