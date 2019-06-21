/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.enums;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public enum FighterState {
    NEUTRE(0),
    SAOUL(1),
    CHERCHEUR_D_AMES(2),
    PORTEUR(3),
    PEUREUX(4),
    DESORIENTE(5),
    ENRACINE(6),
    PESANTEUR(7),
    PORTE(8),
    MOTIVATION_SYLVESTRE(9),
    APPRIVOISEMENT(10),
    CHEVAUCHANT(11),
    PAS_SAGE(12),
    VRAIMENT_PAS_SAGE(13),
    ENNEIGE(14),
    EVEILLE(15),
    FRAGILISE(16),
    SEPARE(17),
    GELE(18),
    FISSURE(19),
    ENDORMI(26),
    LEOPARDO(27),
    LIBRE(28),
    GLYPHE_IMPAIRE(29),
    GLYPHE_PAIRE(30),
    ENCRE_PRIMAIRE(31),
    ENCRE_SECONDAIRE(32),
    ENCRE_TERTIAIRE(33),
    ENCRE_QUATERNAIRE(34),
    ENVIE_DE_TUER(35),
    ENVIE_DE_PARALYSER(36),
    ENVIE_DE_MAUDIRE(37),
    ENVIE_D_EMPOISONNER(38),
    FLOU(39),
    CORROMPU(40),
    SILENCIEUX(41),
    AFFAIBLI(42),
    WAIT__OVNI(43),
    WAIT__PAS_CONTENTE(44),
    WAIT__CONTENTE(46),
    WAIT__MAUVAISE_HUMEUR(47),
    CONFUS(48),
    GOULIFIE(49),
    ALTRUISTE(50),
    WIP_CHATIMENT_AGILE(51),
    WIP_CHATIMENT_OSE(52),
    WIP_CHATIMENT_SPIRITUEL(53),
    WIP_CHATIMENT_VITALESQUE(54),
    RETRAITE(55),
    WIP_INVULNERABLE(56),
    COMPTE_A_REBOURS___2(57),
    COMPTE_A_REBOURS___1(58),
    DEVOUE(60),
    BAGARREUR(61)
    ;
    
    final static private Map<Integer, FighterState> stateById = new HashMap<>();
    
    static {
        for(FighterState state : values()){
            stateById.put(state.id, state);
        }
    }
    
    final private int id;

    private FighterState(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
    
    static public FighterState fromId(int id){
        return stateById.get(id);
    }
}
