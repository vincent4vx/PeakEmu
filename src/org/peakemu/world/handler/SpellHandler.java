/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.handler;

import org.peakemu.database.dao.SpellDAO;
import org.peakemu.world.Spell;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class SpellHandler {
    final private SpellDAO spellDAO;

    public SpellHandler(SpellDAO spellDAO) {
        this.spellDAO = spellDAO;
    }
    
    public void load(){
        System.out.print("Chargement des sorts : ");
        System.out.println(spellDAO.getAll().size() + " sorts chargés");
    }

    public Spell getSpellById(int id) {
        return spellDAO.getSpellById(id);
    }
    
}
