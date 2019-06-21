/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.handler;

import java.util.ArrayList;
import java.util.Collection;
import org.peakemu.Peak;
import org.peakemu.common.Logger;
import org.peakemu.database.dao.CollectorDAO;
import org.peakemu.database.dao.GuildDAO;
import org.peakemu.database.dao.GuildMemberDAO;
import org.peakemu.database.dao.MountParkDAO;
import org.peakemu.database.dao.SpellDAO;
import org.peakemu.database.parser.SpellParser;
import org.peakemu.database.parser.StatsParser;
import org.peakemu.game.out.guild.CreateGuildLeaved;
import org.peakemu.game.out.guild.GuildCreated;
import org.peakemu.game.out.guild.GuildCreationError;
import org.peakemu.game.out.guild.GuildInfoMembers;
import org.peakemu.game.out.guild.GuildStats;
import org.peakemu.game.out.InfoMessage;
import org.peakemu.game.out.guild.CollectorInfoMessage;
import org.peakemu.game.out.guild.InfosTaxCollectorsMovement;
import org.peakemu.objects.Collector;
import org.peakemu.objects.Guild;
import static org.peakemu.objects.Guild.DEFAULT_SPELLS;
import static org.peakemu.objects.Guild.DEFAULT_STATS;
import org.peakemu.objects.MountPark;
import org.peakemu.objects.player.GuildMember;
import org.peakemu.objects.player.Player;
import org.peakemu.world.ExpLevel;
import org.peakemu.world.config.GuildConfig;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class GuildHandler {
    final private GuildDAO guildDAO;
    final private GuildMemberDAO guildMemberDAO;
    final private CollectorDAO collectorDAO;
    final private MountParkDAO mountParkDAO;
    final private SpellDAO spellDAO;
    final private ExpHandler expHandler;
    final private GuildConfig guildConfig;

    public GuildHandler(GuildDAO guildDAO, GuildMemberDAO guildMemberDAO, CollectorDAO collectorDAO, MountParkDAO mountParkDAO, SpellDAO spellDAO, ExpHandler expHandler, GuildConfig guildConfig) {
        this.guildDAO = guildDAO;
        this.guildMemberDAO = guildMemberDAO;
        this.collectorDAO = collectorDAO;
        this.mountParkDAO = mountParkDAO;
        this.spellDAO = spellDAO;
        this.expHandler = expHandler;
        this.guildConfig = guildConfig;
    }
    
    public void load(){
        System.out.print("Chargement des guildes : ");
        System.out.println(guildDAO.getAll().size() + " guildes chargées");
        
        System.out.print("Chargement des membres de guildes : ");
        guildMemberDAO.load();
        System.out.println("Ok");
        
        System.out.print("Chargement des percepteurs : ");
        System.out.println(collectorDAO.getAll().size() + " percepteurs chargés");
    }
    
    public void saveAll(){
        Peak.worldLog.addToLog(Logger.Level.INFO, "Sauvegarde des guildes...");
        
        for(Guild guild : guildDAO.getAll()){
            guildDAO.save(guild);
            for(GuildMember gm : guild.getMembers()){
                guildMemberDAO.save(gm);
            }
        }
        
        Peak.worldLog.addToLog(Logger.Level.INFO, "Sauvegarde des percepteurs...");
        
        for(Collector collector : collectorDAO.getAll()){
            collectorDAO.save(collector);
        }
    }

    public Guild getGuildById(int guildId) {
        return guildDAO.getGuildById(guildId);
    }
    
    public Guild createGuild(Player player, String bgId, String bgColor, String embId, String embColor, String name){
        if(player.getGuild() != null){
            player.send(new GuildCreationError(GuildCreationError.GUILD_CREATE_ALLREADY_IN_GUILD));
            return null;
        }
        
        String emblem = bgId + "," + bgColor + "," + embId + "," + embColor;
        
        if(guildDAO.checkEmblemExists(emblem)){
            player.send(new GuildCreationError(GuildCreationError.GUILD_CREATE_ALLREADY_USE_EMBLEM));
            return null;
        }
        
        if(guildDAO.checkNameExists(name)){
            player.send(new GuildCreationError(GuildCreationError.GUILD_CREATE_ALLREADY_USE_NAME));
            return null;
        }
        
        //ugly check name
        boolean isValid = true;
        int tiretCount = 0;
        for (char curLetter : name.toLowerCase().toCharArray()) {
            if (!((curLetter >= 'a' && curLetter <= 'z')
                || curLetter == '-')) {
                isValid = false;
                break;
            }
            if (curLetter == '-') {
                if (tiretCount >= 2) {
                    isValid = false;
                    break;
                } else {
                    tiretCount++;
                }
            }
        }
        
        if(!isValid){
            player.send(new GuildCreationError(GuildCreationError.GUILD_CREATE_ALLREADY_USE_NAME));
            return null;
        }
        
        Guild guild = guildDAO.insert(new Guild(-1, name, emblem, 1, 0, 0, 0, 
            SpellParser.parseCollectorSpells(spellDAO, DEFAULT_SPELLS), 
            StatsParser.parseCollectorStats(DEFAULT_STATS)
        ));
        
        if(guild == null)
            return null;
        
        player.send(new GuildCreated());
        
        addPlayerToGuild(player, guild, true);
        player.send(new CreateGuildLeaved());
        return guild;
    }
    
    private void addPlayerToGuild(Player player, Guild guild, boolean isOwner){
        if(player.getGuild() != null)
            return;
        
        GuildMember member = new GuildMember(player, guild, isOwner ? 1 : 0, 0, (byte)0, isOwner ? 1 : 0);
        
        if(!guildMemberDAO.save(member))
            return;
        
        guild.addMember(member);
        player.setGuildMember(member);
    }
    
    public void joinGuild(Player player, Guild guild){
        addPlayerToGuild(player, guild, false);
    }
    
    public void kickGuild(GuildMember member){
        guildMemberDAO.delete(member);
        
        if(member.getGuild().getSize() == 0){ //empty guild => delete guild
            guildDAO.delete(member.getGuild());
            return;
        }
        
        if(member.getRank() == 1){ //boss quit guild
            //found highest rights member
            GuildMember gm = null;
            
            for(GuildMember gm2 : member.getGuild().getMembers()){
                if(gm == null || gm.getRights() < gm2.getRights())
                    gm = gm2;
            }
            
            //set as boss
            gm.setRank(1);
            gm.setRights(1);
            
            if(gm.getPlayer().isOnline()){
                gm.getPlayer().send(new GuildStats(gm));
            }
            
            gm.getGuild().sendToMembers(new InfoMessage(InfoMessage.Type.ERROR).addMessage(199, gm.getGuild().getName(), gm.getPlayer().getName()));
            guildMemberDAO.save(gm);
        }
        
        member.getGuild().sendToMembers(new GuildInfoMembers(member.getGuild()));
    }
    
    public void recoltCollector(Player player, Collector collector){
        addXp(player.getGuildMember(), collector.getXp());
        guildMemberDAO.save(player.getGuildMember());
        guildDAO.save(player.getGuild());
        collectorDAO.remove(collector);
        collector.getGuild().sendToMembers(new CollectorInfoMessage(collector, CollectorInfoMessage.RECOLT, player));
        collector.getGuild().sendToMembers(new InfosTaxCollectorsMovement(collector.getGuild()));
    }
    
    public Collection<MountPark> getMountParks(Guild guild){
        Collection<MountPark> parks = new ArrayList<>();
        
        for(MountPark park : mountParkDAO.getAll()){
            if(park.getGuild() != null && park.getGuild().equals(guild))
                parks.add(park);
        }
        
        return parks;
    }
    
    public void addXp(GuildMember member, long xp){
        member.addXp(xp);
        Guild guild = member.getGuild();
        
        if(guild.getLevel() >= expHandler.getMaxGuildLevel())
            return;
        
        ExpLevel expLevel = expHandler.getLevel(guild.getLevel());
        
        while(expLevel.getNext() != null
            && expLevel.guild != -1
            && guild.getXp() > expLevel.guild){
            expLevel = expLevel.getNext();
            
            guild.setCapital(guild.getCapital() + guildConfig.getCapitalPointsPerLevel());
        }
        
        guild.setLevel(expLevel.level);
    }
}
