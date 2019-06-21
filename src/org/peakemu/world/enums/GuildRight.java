/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.enums;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public enum GuildRight {
    BOSS(1),
    BOOST(2),			//G�rer les boost
	RIGHT(4),			//G�rer les droits
	INVITE(8),			//Inviter de nouveaux membres
	BAN(16),				//Bannir
	ALLXP(32),			//G�rer les r�partitions d'xp
	RANK(64),			//G�rer les rangs
	POSPERCO(128),		//Poser un percepteur
	HISXP(256),			//G�rer sa r�partition d'xp
	COLLPERCO(512),		//Collecter les percepteurs
	USEENCLOS(4096),		//Utiliser les enclos
	AMENCLOS(8192),		//Am�nager les enclos
	OTHDINDE(16384),		//G�rer les montures des autres membres
;
    
    final private int right;

    private GuildRight(int right) {
        this.right = right;
    }
    
    public int toInt(){
        return right;
    }
    
    static public int getAllRights(){
        int rights = 0;
        
        
        for (GuildRight gr : values()) {
            if(gr == BOSS) //don't add BOSS right
                continue;
            
            rights |= gr.toInt();
        }
        
        return rights;
    }
}
