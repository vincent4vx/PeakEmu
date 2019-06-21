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
public enum ItemType {
    NONE(0),
    AMULETTE(1),
    ARC(2),
    BAGUETTE(3),
    BATON(4),
    DAGUES(5),
    EPEE(6),
    MARTEAU(7),
    PELLE(8),
    ANNEAU(9),
    CEINTURE(10),
    BOTTES(11),
    POTION(12),
    PARCHO_EXP(13),
    DONS(14),
    RESSOURCE(15),
    COIFFE(16),
    CAPE(17),
    FAMILIER(18),
    HACHE(19),
    OUTIL(20),
    PIOCHE(21),
    FAUX(22),
    DOFUS(23),
    QUETES(24),
    DOCUMENT(25),
    FM_POTION(26),
    TRANSFORM(27),
    BOOST_FOOD(28),
    BENEDICTION(29),
    MALEDICTION(30),
    RP_BUFF(31),
    PERSO_SUIVEUR(32),
    PAIN(33),
    CEREALE(34),
    FLEUR(35),
    PLANTE(36),
    BIERE(37),
    BOIS(38),
    MINERAIS(39),
    ALLIAGE(40),
    POISSON(41),
    BONBON(42),
    POTION_OUBLIE(43),
    POTION_METIER(44),
    POTION_SORT(45),
    FRUIT(46),
    OS(47),
    POUDRE(48),
    COMESTI_POISSON(49),
    PIERRE_PRECIEUSE(50),
    PIERRE_BRUTE(51),
    FARINE(52),
    PLUME(53),
    POIL(54),
    ETOFFE(55),
    CUIR(56),
    LAINE(57),
    GRAINE(58),
    PEAU(59),
    HUILE(60),
    PELUCHE(61),
    POISSON_VIDE(62),
    VIANDE(63),
    VIANDE_CONSERVEE(64),
    QUEUE(65),
    METARIA(66),
    UNKNOWN67(67),
    LEGUME(68),
    VIANDE_COMESTIBLE(69),
    TEINTURE(70),
    EQUIP_ALCHIMIE(71),
    OEUF_FAMILIER(72),
    MAITRISE(73),
    FEE_ARTIFICE(74),
    PARCHEMIN_SORT(75),
    PARCHEMIN_CARAC(76),
    CERTIFICAT_CHANIL(77),
    RUNE_FORGEMAGIE(78),
    BOISSON(79),
    OBJET_MISSION(80),
    SAC_DOS(81),
    BOUCLIER(82),
    PIERRE_AME(83),
    CLEFS(84),
    PIERRE_AME_PLEINE(85),
    POPO_OUBLI_PERCEP(86),
    PARCHO_RECHERCHE(87),
    PIERRE_MAGIQUE(88),
    CADEAUX(89),
    FANTOME_FAMILIER(90),
    DRAGODINDE(91),
    BOUFTOU(92),
    OBJET_ELEVAGE(93),
    OBJET_UTILISABLE(94),
    PLANCHE(95),
    ECORCE(96),
    CERTIF_MONTURE(97),
    RACINE(98),
    FILET_CAPTURE(99),
    SAC_RESSOURCE(100),
    UNKNOWN101(101),
    ARBALETE(102),
    PATTE(103),
    AILE(104),
    OEUF(105),
    OREILLE(106),
    CARAPACE(107),
    BOURGEON(108),
    OEIL(109),
    GELEE(110),
    COQUILLE(111),
    PRISME(112),
    OBJET_VIVANT(113),
    ARME_MAGIQUE(114),
    FRAGM_AME_SHUSHU(115),
    POTION_FAMILIER(116)
    ;

    final private int id;
    
    final static private Map<Integer, ItemType> types = new HashMap<Integer, ItemType>();
    
    final static public ItemType[] WEAPONS = new ItemType[]{
        OUTIL, EPEE, ARBALETE, ARC, ARME_MAGIQUE, HACHE, PIOCHE, 
        FAUX, BAGUETTE, BATON, DAGUES, MARTEAU, PELLE
    };
    
    final static public ItemType[] BUFFS = new ItemType[]{
        MALEDICTION, BENEDICTION, TRANSFORM, PERSO_SUIVEUR,
        BONBON
    };
    
    final static public ItemType[] USABLE = new ItemType[]{
        POTION, PAIN, PARCHEMIN_CARAC, PRISME, DONS, FEE_ARTIFICE,
        BIERE, BOOST_FOOD, COMESTI_POISSON, BONBON
    };
    
    static {
        for(ItemType t : values()){
            types.put(t.id, t);
        }
    }

    private ItemType(int id) {
        this.id = id;
        assert id == ordinal();
    }

    public int getId() {
        return id;
    }
    
    static public ItemType valueOf(int id){
        return types.get(id);
    }
    
    static public boolean isWeapon(ItemType type){
        for(ItemType t : WEAPONS){
            if(t == type)
                return true;
        }
        
        return false;
    }
    
    static public boolean isAMantle(ItemType type){
        return type == CAPE || type == SAC_DOS;
    }
    
    static public boolean isBuff(ItemType type){
        for(ItemType t : BUFFS){
            if(t == type)
                return true;
        }
        
        return false;
    }
    
    static public boolean isUsable(ItemType type){
        for(ItemType t : USABLE){
            if(t == type)
                return true;
        }
        
        return false;
    }
}
