/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.handler;

import org.peakemu.common.Constants;
import org.peakemu.common.SocketManager;
import org.peakemu.common.util.Interval;
import org.peakemu.database.dao.CraftDAO;
import org.peakemu.database.dao.JobDAO;
import org.peakemu.database.dao.JobSkillDAO;
import org.peakemu.game.out.job.JobAvailableSkills;
import org.peakemu.game.out.job.JobLevelUp;
import org.peakemu.game.out.job.JobXp;
import org.peakemu.game.out.object.InventoryWeight;
import org.peakemu.objects.item.Item;
import org.peakemu.objects.player.JobStats;
import org.peakemu.objects.player.Player;
import org.peakemu.world.Craft;
import org.peakemu.world.Job;
import org.peakemu.world.JobSkill;
import org.peakemu.world.config.JobConfig;
import org.peakemu.world.config.PlayerConfig;
import org.peakemu.world.config.RateConfig;
import org.peakemu.world.enums.InventoryPosition;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class JobHandler {
    final private JobConfig config;
    final private JobDAO jobDAO;
    final private JobSkillDAO jobSkillDAO;
    final private CraftDAO craftDAO;
    final private RateConfig rateConfig;
    final private ExpHandler expHandler;
    final private PlayerConfig playerConfig;

    public JobHandler(JobConfig config, JobDAO jobDAO, JobSkillDAO jobSkillDAO, CraftDAO craftDAO, RateConfig rateConfig, ExpHandler expHandler, PlayerConfig playerConfig) {
        this.config = config;
        this.jobDAO = jobDAO;
        this.jobSkillDAO = jobSkillDAO;
        this.craftDAO = craftDAO;
        this.rateConfig = rateConfig;
        this.expHandler = expHandler;
        this.playerConfig = playerConfig;
    }
    
    public void load(){
        System.out.print("Chargement des crafts : ");
        System.out.println(craftDAO.getAll().size() + " crafts chargés");
        
        System.out.print("Chargement des métiers : ");
        System.out.println(jobDAO.getAll().size() + " métiers chargés");
        
        System.out.print("Chargement des job skills : ");
        System.out.println(jobSkillDAO.getAll().size() + " skills chargés");
    }
    
    public int getHarvestDuraction(JobSkill skill, JobStats jobStats){
        return config.getDefaultHarvestTime() - jobStats.getLevel() * config.getWinHarvestTimeWinPerLevel();
    }
    
    public Interval getHarvestDropInterval(JobSkill skill, JobStats jobStats){
        int lvl = jobStats.getLevel() - skill.getLevel();
        int min = config.getHarvestDropBase() + (int)(lvl * config.getWinHarvestDropPerLevel());
        int max = min + config.getHarvestDropMaxBonus();
        return new Interval(min, max);
    }
    
    public long getHarvestWinXp(JobSkill skill){
        long xp = config.getBaseHarvestWinXp() + (long)(skill.getLevel() * config.getHarvestWinXpPerSkillLevel());
        return (long)(xp * rateConfig.getJobXp());
    }
    
    public void addXp(Player player, JobStats jobStats, long xp){
        if(jobStats.getLevel() >= expHandler.getMaxJobLevel()
            && !playerConfig.getAddJobXpOnMaxLevel())
            return;
        
        if(jobStats.addXp(xp)){
            player.send(new JobLevelUp(jobStats));
            player.send(new JobAvailableSkills().addSkill(getJobSkillsPacket(jobStats)));
            SocketManager.GAME_SEND_STATS_PACKET(player);
            player.send(new InventoryWeight(player));
        }
        
        player.send(new JobXp(jobStats));
    }
    
    public String getJobSkillsPacket(JobStats stats){
        StringBuilder sb = new StringBuilder();
        
        sb.append(stats.getJob().getId()).append(';');
        
        boolean b = false;
        
        for(JobSkill skill : stats.getAvailableSkills()){
            if(b)
                sb.append(',');
            else
                b = true;
            
            sb.append(skill.getId()).append('~');
            
            if(skill.isHarvest()){
                Interval i = getHarvestDropInterval(skill, stats);
                sb.append(i.getMin()).append('~').append(i.getMax()).append("~0~").append(getHarvestDuraction(skill, stats));
            }else{
                sb.append(getMaxCraftIngredients(stats)).append("~0~0~").append(Constants.getChanceForMaxCase(stats.getLevel()));
            }
        }
        
        return sb.toString();
    }
    
    public void learnJob(Player player, Job job){
        if(player.learnJob(job, expHandler.getLevel(config.getStartJobLevel()))){
            JobStats jobStats = player.getJobStats(job);
            
            JobAvailableSkills jas = new JobAvailableSkills();
            jas.addSkill(getJobSkillsPacket(jobStats));
            player.send(jas);
            player.send(new JobXp(jobStats));
            
            Item obj = player.getItems().getItemByPos(InventoryPosition.NO_EQUIPED);
            
            if (obj != null) {
                if (job.isValidTool(obj.getTemplate())) {
                    SocketManager.GAME_SEND_OT_PACKET(player.getAccount().getGameThread(), job.getId());
                }
            }
        }
    }

    public Job getJobById(int id) {
        return jobDAO.getJobById(id);
    }
    
    public int getMaxCraftIngredients(JobStats jobStats){
        if(jobStats.getLevel() < 10)
            return 2;
        
        if(jobStats.getLevel() > 99)
            return 9;
        
        return jobStats.getLevel() / 20 + 3;
    }
    
    public long getCraftWinXp(JobStats jobStats, Craft craft){
        return (long)(Constants.calculXpWinCraft(jobStats.getLevel(), craft.getCraftCases()) * rateConfig.getJobXp());
    }
}
