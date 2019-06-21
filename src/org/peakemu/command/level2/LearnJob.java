/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.peakemu.command.level2;

import org.peakemu.command.Command;
import org.peakemu.command.CommandPerformer;
import org.peakemu.objects.player.Player;
import org.peakemu.world.Job;
import org.peakemu.world.handler.JobHandler;
import org.peakemu.world.handler.SessionHandler;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class LearnJob implements Command{
    final private JobHandler jobHandler;
    final private SessionHandler sessionHandler;

    public LearnJob(JobHandler jobHandler, SessionHandler sessionHandler) {
        this.jobHandler = jobHandler;
        this.sessionHandler = sessionHandler;
    }

    @Override
    public String name() {
        return "LEARNJOB";
    }

    @Override
    public String shortDescription() {
        return "Apprend un métier";
    }

    @Override
    public String help() {
        return "LEARNJOB [jobid] {qui}";
    }

    @Override
    public int minLevel() {
        return 2;
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        if(args.length < 1){
            performer.displayError("Commande invalide");
            return;
        }
        
        Job job;
        
        try{
            int jobId = Integer.parseInt(args[0]);
            job = jobHandler.getJobById(jobId);
        }catch(NumberFormatException e){
            performer.displayError("Métier invalide");
            return;
        }
        
        if(job == null){
            performer.displayError("Métier invalide");
            return;
        }
        
        Player target = performer.getPlayer();
        
        if(args.length >= 2){
            target = sessionHandler.searchPlayer(args[1]);
        }
        
        if(target == null){
            performer.displayError("Joueur introuvable");
            return;
        }
        
        jobHandler.learnJob(target, job);
    }
    
    

}
