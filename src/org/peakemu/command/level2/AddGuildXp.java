/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command.level2;

import org.peakemu.command.Command;
import org.peakemu.command.CommandPerformer;
import org.peakemu.database.dao.GuildDAO;
import org.peakemu.objects.Guild;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class AddGuildXp implements Command{
    final private GuildDAO guildDAO;

    public AddGuildXp(GuildDAO guildDAO) {
        this.guildDAO = guildDAO;
    }

    @Override
    public String name() {
        return "ADDGUILDXP";
    }

    @Override
    public String shortDescription() {
        return "Ajoute de l'xp à une guilde";
    }

    @Override
    public String help() {
        return "ADDGUILDXP [xp] {guildname}";
    }

    @Override
    public int minLevel() {
        return 2;
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        Guild guild = performer.getPlayer().getGuild();
        
        long xp;
        
        try{
            xp = Long.parseLong(args[0]);
        }catch(Exception e){
            performer.displayError("Commande invalide");
            return;
        }
        
        if(args.length >= 2){
            guild = null;
            for(Guild g : guildDAO.getAll()){
                if(g.getName().equalsIgnoreCase(args[1])){
                    guild = g;
                    break;
                }
            }
        }
        
        if(guild == null){
            performer.displayError("Guilde introuvable");
            return;
        }
        
        guild.addXp(xp);
        guildDAO.save(guild);
        
        performer.displayMessage(xp + " de points d'xp ont été ajoutés à la guilde " + guild.getName());
    }
    
}
