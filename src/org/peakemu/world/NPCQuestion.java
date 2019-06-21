/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world;

import java.util.Collection;
import org.peakemu.common.ConditionParser;
import org.peakemu.objects.player.Player;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class NPCQuestion {

    final private int id;
    final private Collection<NPCResponse> reponses;
    final private String args;
    final private String cond;
    final private NPCQuestion alternativeQuestion;

    public NPCQuestion(int id, Collection<NPCResponse> reponses, String args, String cond, NPCQuestion alternativeQuestion) {
        this.id = id;
        this.reponses = reponses;
        this.args = args;
        this.cond = cond;
        this.alternativeQuestion = alternativeQuestion;
    }

    public int getId() {
        return id;
    }

    public NPCQuestion getAlternativeQuestion() {
        return alternativeQuestion;
    }

    public String getCond() {
        return cond;
    }

    public Collection<NPCResponse> getReponses() {
        return reponses;
    }
    
    public NPCResponse getResponse(int id){
        for(NPCResponse r : reponses){
            if(r.get_id() == id)
                return r;
        }
        
        return null;
    }

    public String getArgs() {
        return args;
    }
}
