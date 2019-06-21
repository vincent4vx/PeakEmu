/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.dialog;

import java.util.Map;
import org.peakemu.game.GameClient;
import org.peakemu.game.out.dialog.DialogCreated;
import org.peakemu.game.out.dialog.DialogCreationError;
import org.peakemu.network.InputPacket;
import org.peakemu.objects.Collector;
import org.peakemu.objects.NPC;
import org.peakemu.world.NPCQuestion;
import org.peakemu.world.Sprite;
import org.peakemu.world.World;
import org.peakemu.world.handler.NPCHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class CreateDialog implements InputPacket<GameClient>{
    final private NPCHandler npcHandler;

    public CreateDialog(NPCHandler npcHandler) {
        this.npcHandler = npcHandler;
    }

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null)
            return;
        
        Sprite sprite;
        
        try{
            sprite = client.getPlayer().getMap().getSprite(Integer.parseInt(args));
        }catch(NumberFormatException e){
            return;
        }
        
        if(sprite == null){
            client.send(new DialogCreationError());
            return;
        }
        
        if(sprite instanceof NPC){
            NPC npc = (NPC)sprite;
            npcDialog(npc, client);
        }else if(sprite instanceof Collector){
        }else{
            client.send(new DialogCreationError());
        }
    }
    
    private void npcDialog(NPC npc, GameClient client){
        // Objectif quêtes : Aller voir x
        //client.getPlayer().confirmObjective(1, npc.getTemplate().get_id() + "", null);

        //TODO: Quests
//        boolean pendingStep = false;
//        // Quest Master
//        String quests = npc.getTemplate().get_quests(); // Quetes du npc
//        if (quests != null) // Npc a des quetes
//        {
//            String[] splitQuests = quests.split(";");
//            for (String curQuest : splitQuests) // Boucle chaque quete du npc
//            {
//                Map<String, String> questPerso = client.getPlayer().get_quest(Integer.parseInt(curQuest));
//                if (questPerso != null) // Le perso à la quête
//                {
//                    String curStep = questPerso.get("curStep");
//                    if (!curStep.equals("-1")) // Si quete non terminee
//                    {
//                        Map<String, String> stepDetails = World.getStep(Integer.parseInt(curStep));
//                        if (!questPerso.get("objectivesToDo").isEmpty() && !questPerso.get("objectivesToDo").equals(" ")) // Etape en cours
//                        {
//                            pendingStep = true;
//                        }
//                        //qID = Integer.parseInt(stepDetails.get("question"));
//                        break;
//                    } else { // Quete terminee
//                        Map<String, String> questSteps = World.getQuest(Integer.parseInt(curQuest)); // Template quete
//                        //qID = Integer.parseInt(questSteps.get("endQuestion")); // Question de fin de quete
//                    }
//                } else {
//                    Map<String, String> questTmpl = World.getQuest(Integer.parseInt(curQuest)); // Template quete
//                    //qID = Integer.parseInt(questTmpl.get("startQuestion"));
//                    break;
//                }
//
//            }
//        }
//        
//        
//        // Pnj quete secondaire
//        int newAnswer = -1;
//        Map<Integer, Map<String, String>> objectives = World.getObjectiveByNpcTarget(npc.getTemplate().get_id());
//        if (objectives != null) // Il y a un objectif lié à ce pnj
//        {
//            for (Map.Entry<Integer, Map<String, String>> objective : objectives.entrySet()) {
//                if (client.getPlayer().hasObjective(objective.getKey())) // Si le perso doit faire cet objectif
//                {
//                    //qID = Integer.parseInt(objective.getValue().get("optQuestion"));
//                    // Le perso ne peut pas accomplir l'objectif
//                    if (!client.getPlayer().canDoObjective(Integer.parseInt(objective.getValue().get("type")), objective.getValue().get("args"))) {
//                        pendingStep = true;
//                    } else {
//                        pendingStep = false;
//                        newAnswer = Integer.parseInt(objective.getValue().get("optAnswer"));
//                        break;
//                    }
//                }
//            }
//        }
        
        //END Quests
        
        
        NPCQuestion question = npc.getTemplate().getInitQuestion();
        question = npcHandler.getRealQuestion(question, client.getPlayer());
        
        if(question == null){
            client.send(new DialogCreationError());
            return;
        }

        client.send(new DialogCreated(npc));
        npcHandler.newQuestion(question, client.getPlayer());
    }

    @Override
    public String header() {
        return "DC";
    }
    
}
