/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.dialog;

import java.util.Collection;
import org.peakemu.world.NPCQuestion;
import org.peakemu.world.NPCResponse;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class DialogQuestion {
    private NPCQuestion question;
    private Collection args;

    public DialogQuestion(NPCQuestion question, Collection args) {
        this.question = question;
        this.args = args;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(64);
        
        sb.append("DQ").append(question.getId());
        
        boolean b = false;
        
        for(Object o : args){
            if(b){
                sb.append(',');
            }else{
                sb.append(';');
                b = true;
            }
            
            sb.append(o);
        }
        
        sb.append('|');
        
        b = false;
        
        for(NPCResponse response : question.getReponses()){
            if(b)
                sb.append(';');
            else
                b = true;
            
            sb.append(response.get_id());
        }
        
        return sb.toString();
    }
    
    
}
