/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.dialog;

import java.util.Map;
import org.peakemu.common.util.StringUtil;
import org.peakemu.game.GameClient;
import org.peakemu.game.out.dialog.DialogLeaved;
import org.peakemu.network.InputPacket;
import org.peakemu.world.NPCQuestion;
import org.peakemu.world.NPCResponse;
import org.peakemu.world.World;
import org.peakemu.world.handler.ActionHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class DialogResponse implements InputPacket<GameClient>{
    final private ActionHandler actionHandler;

    public DialogResponse(ActionHandler actionHandler) {
        this.actionHandler = actionHandler;
    }

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null || client.getPlayer().getCurQuestion() == null)
            return;
        
        String[] data = StringUtil.split(args, "|");
        
        int qId, rId;
        
        try{
            qId = Integer.parseInt(data[0]);
            rId = Integer.parseInt(data[1]);
        }catch(Exception e){
            client.getPlayer().setCurQuestion(null);
            client.send(new DialogLeaved());
            return;
        }
        
        // Quest
//        Map<Integer, Map<String, String>> objectives = World.getObjectiveByOptAnswer(rId);
//        if (objectives != null) // C'est une réponse de quête avec au moins 1 objectif
//        {
//            for (Map.Entry<Integer, Map<String, String>> objective : objectives.entrySet()) {
//                if (client.getPlayer().hasObjective(objective.getKey())) // Si le perso à cet objectif
//                {
//                    if (client.getPlayer().canDoObjective(Integer.parseInt(objective.getValue().get("type")), objective.getValue().get("args"))) {
//                        client.getPlayer().confirmObjective(Integer.parseInt(objective.getValue().get("type")), objective.getValue().get("args"), null);
//                    }
//                }
//            }
//        }
        // Fin Quetes
        
        NPCQuestion question = client.getPlayer().getCurQuestion();
        NPCResponse response = question.getResponse(rId);
        
        if(response == null){
            client.getPlayer().setCurQuestion(null);
            client.send(new DialogLeaved());
            return;
        }
        
        if(response.applyActions(actionHandler, client.getPlayer())){
            client.getPlayer().setCurQuestion(null);
            client.send(new DialogLeaved());
        }
    }

    @Override
    public String header() {
        return "DR";
    }
    
}
