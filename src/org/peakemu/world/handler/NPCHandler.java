/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.handler;

import java.util.Collection;
import org.peakemu.common.ConditionParser;
import org.peakemu.database.dao.NPCQuestionDAO;
import org.peakemu.database.dao.NPCResponseDAO;
import org.peakemu.database.dao.NPCTemplateDAO;
import org.peakemu.database.dao.NpcDAO;
import org.peakemu.game.out.dialog.DialogLeaved;
import org.peakemu.game.out.dialog.DialogQuestion;
import org.peakemu.objects.player.Player;
import org.peakemu.world.NPCQuestion;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class NPCHandler {
    final private NPCQuestionDAO questionDAO;
    final private NPCResponseDAO responseDAO;
    final private NPCTemplateDAO templateDAO;
    final private NpcDAO npcDAO;
    final private VarHandler varHandler;

    public NPCHandler(NPCQuestionDAO questionDAO, NPCResponseDAO responseDAO, NPCTemplateDAO templateDAO, NpcDAO npcDAO, VarHandler varHandler) {
        this.questionDAO = questionDAO;
        this.responseDAO = responseDAO;
        this.templateDAO = templateDAO;
        this.npcDAO = npcDAO;
        this.varHandler = varHandler;
    }
    
    public void load(){
        System.out.print("Chargement des réponses des pnj : ");
        System.out.println(responseDAO.getAll().size() + " réponses chargées");
        
        System.out.print("Chargement des questions des pnj : ");
        System.out.println(questionDAO.getAll().size() + " questions chargées");
        
        System.out.print("Chargement des npc templates : ");
        System.out.println(templateDAO.getAll().size() + " templates chargés");
        
        System.out.print("Chargement des npc : ");
        System.out.println(npcDAO.load() + " npcs chargés");
    }
    
    public NPCQuestion getRealQuestion(NPCQuestion question, Player player){
        while(question != null){
            if(question.getCond().isEmpty() || ConditionParser.validConditions(player, question.getCond())){
                break;
            }
            
            question = question.getAlternativeQuestion();
        }
        
        return question;
    }
    
    public NPCQuestion getRealQuestion(int qId, Player player){
        NPCQuestion question = questionDAO.getQuestionById(qId);
        return getRealQuestion(question, player);
    }
    
    public void newQuestion(NPCQuestion question, Player player){
        if(question == null){
            player.setCurQuestion(null);
            player.send(new DialogLeaved());
            return;
        }
        
        player.setCurQuestion(question);
        Collection args = varHandler.parseVarString(question.getArgs(), player);
        player.send(new DialogQuestion(question, args));
    }
}
