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
public enum Effect {
    ADD_PM2(78){

        @Override
        public Effect[] getEquivalentStats() {
            return new Effect[]{ADD_PM};
        }

        @Override
        public Effect[] getOppositeStats() {
            return new Effect[]{REM_PM, REM_PM2};
        }
        
    },
    REM_PA(101),
    ADD_VIE(110),
    ADD_PA(111){

        @Override
        public Effect[] getEquivalentStats() {
            return new Effect[]{ADD_PA2};
        }

        @Override
        public Effect[] getOppositeStats() {
            return new Effect[]{REM_PA, REM_PA2};
        }
        
    },
    ADD_DOMA(112){

        @Override
        public Effect[] getOppositeStats() {
            return new Effect[]{REM_DOMA};
        }
        
    },
    MULTIPLY_DOMMAGE(114),
    ADD_CC(115){

        @Override
        public Effect[] getOppositeStats() {
            return new Effect[]{REM_CC};
        }
        
    },
    REM_PO(116),
    ADD_PO(117){

        @Override
        public Effect[] getEquivalentStats() {
            return new Effect[]{CASTER_ADD_PO};
        }

        @Override
        public Effect[] getOppositeStats() {
            return new Effect[]{REM_PO, CASTER_REM_PO};
        }
        
    },
    ADD_FORC(118){

        @Override
        public Effect[] getOppositeStats() {
            return new Effect[]{REM_FORC};
        }
        
    },
    ADD_AGIL(119){

        @Override
        public Effect[] getOppositeStats() {
            return new Effect[]{REM_AGIL};
        }
        
    },
    ADD_PA2(120){

        @Override
        public Effect[] getEquivalentStats() {
            return new Effect[]{ADD_PA};
        }

        @Override
        public Effect[] getOppositeStats() {
            return new Effect[]{REM_PA, REM_PA2};
        }
        
    },
    ADD_EC(122),
    ADD_CHAN(123){

        @Override
        public Effect[] getOppositeStats() {
            return new Effect[]{REM_CHAN};
        }
        
    },
    ADD_SAGE(124){

        @Override
        public Effect[] getOppositeStats() {
            return new Effect[]{REM_SAGE};
        }
        
    },
    ADD_VITA(125){

        @Override
        public Effect[] getEquivalentStats() {
            return new Effect[]{BOOST_VITA};
        }

        
        @Override
        public Effect[] getOppositeStats() {
            return new Effect[]{REM_VITA};
        }
        
    },
    ADD_INTE(126){

        @Override
        public Effect[] getOppositeStats() {
            return new Effect[]{REM_INTE};
        }
        
    },
    REM_PM(127),
    ADD_PM(128){

        @Override
        public Effect[] getEquivalentStats() {
            return new Effect[]{ADD_PM2};
        }

        @Override
        public Effect[] getOppositeStats() {
            return new Effect[]{REM_PM, REM_PM2};
        }
        
    },
    ADD_TOURS(811),
    ADD_PERDOM(138),
    ADD_PDOM(142),
    REM_DOMA(145),
    REM_CHAN(152),
    REM_VITA(153),
    REM_AGIL(154),
    REM_INTE(155),
    REM_SAGE(156),
    REM_FORC(157),
    ADD_PODS(158){

        @Override
        public Effect[] getOppositeStats() {
            return new Effect[]{REM_PODS};
        }
        
    },
    REM_PODS(159),
    ADD_AFLEE(160){

        @Override
        public Effect[] getOppositeStats() {
            return new Effect[]{REM_AFLEE};
        }
        
    },
    ADD_MFLEE(161){

        @Override
        public Effect[] getOppositeStats() {
            return new Effect[]{REM_MFLEE};
        }
        
    },
    REM_AFLEE(162),
    REM_MFLEE(163),
    ADD_MAITRISE(165),
    REM_PA2(168),
    REM_PM2(169),
    REM_CC(171),
    ADD_INIT(174){

        @Override
        public Effect[] getOppositeStats() {
            return new Effect[]{REM_INIT};
        }
        
    },
    REM_INIT(175),
    ADD_PROS(176){

        @Override
        public Effect[] getOppositeStats() {
            return new Effect[]{REM_PROS};
        }
        
    },
    REM_PROS(177),
    ADD_SOIN(178){

        @Override
        public Effect[] getOppositeStats() {
            return new Effect[]{REM_SOIN};
        }
        
    },
    REM_SOIN(179),
    CREATURE(182),
    ADD_RP_TER(210){

        @Override
        public Effect[] getOppositeStats() {
            return new Effect[]{REM_RP_TER};
        }
        
    },
    ADD_RP_EAU(211){

        @Override
        public Effect[] getOppositeStats() {
            return new Effect[]{REM_RP_EAU};
        }
        
    },
    ADD_RP_AIR(212){

        @Override
        public Effect[] getOppositeStats() {
            return new Effect[]{REM_RP_AIR};
        }
        
    },
    ADD_RP_FEU(213){

        @Override
        public Effect[] getOppositeStats() {
            return new Effect[]{REM_RP_FEU};
        }
    },
    ADD_RP_NEU(214){

        @Override
        public Effect[] getOppositeStats() {
            return new Effect[]{REM_RP_NEU};
        }
        
    },
    REM_RP_TER(215),
    REM_RP_EAU(216),
    REM_RP_AIR(217),
    REM_RP_FEU(218),
    REM_RP_NEU(219),
    RETDOM(220),
    TRAPDOM(225),
    TRAPPER(226),
    ADD_R_FEU(240){

        @Override
        public Effect[] getOppositeStats() {
            return new Effect[]{REM_R_FEU};
        }
        
    },
    ADD_R_NEU(241){

        @Override
        public Effect[] getOppositeStats() {
            return new Effect[]{REM_R_NEU};
        }
        
    },
    ADD_R_TER(242){

        @Override
        public Effect[] getOppositeStats() {
            return new Effect[]{REM_R_TER};
        }
        
    },
    ADD_R_EAU(243){

        @Override
        public Effect[] getOppositeStats() {
            return new Effect[]{REM_R_EAU};
        }
        
    },
    ADD_R_AIR(244){

        @Override
        public Effect[] getOppositeStats() {
            return new Effect[]{REM_R_AIR};
        }
        
    },
    REM_R_FEU(245),
    REM_R_NEU(246),
    REM_R_TER(247),
    REM_R_EAU(248),
    REM_R_AIR(249),
    ADD_RP_PVP_TER(250){

        @Override
        public Effect[] getOppositeStats() {
            return new Effect[]{REM_RP_PVP_TER};
        }
        
    },
    ADD_RP_PVP_EAU(251){

        @Override
        public Effect[] getOppositeStats() {
            return new Effect[]{REM_RP_PVP_EAU};
        }
        
    },
    ADD_RP_PVP_AIR(252){

        @Override
        public Effect[] getOppositeStats() {
            return new Effect[]{REM_RP_PVP_AIR};
        }
        
    },
    ADD_RP_PVP_FEU(253){

        @Override
        public Effect[] getOppositeStats() {
            return new Effect[]{REM_RP_PVP_FEU};
        }
        
    },
    ADD_RP_PVP_NEU(254){

        @Override
        public Effect[] getOppositeStats() {
            return new Effect[]{REM_RP_PVP_NEU};
        }
        
    },
    REM_RP_PVP_TER(255),
    REM_RP_PVP_EAU(256),
    REM_RP_PVP_AIR(257),
    REM_RP_PVP_FEU(258),
    REM_RP_PVP_NEU(259),
    ADD_R_PVP_TER(260),
    ADD_R_PVP_EAU(261),
    ADD_R_PVP_AIR(262),
    ADD_R_PVP_FEU(263),
    ADD_R_PVP_NEU(264),
    // Effets de classe 
    ADD_PO_SPELL(281),
    MK_POM_SPELL(282), // PO modifiable 
    ADD_DMG_SPELL(283),
    ADD_SOIN_SPELL(284),
    REM_PA_SPELL(285),
    REM_CD_SPELL(286),
    ADD_CC_SPELL(287),
    REM_LINE_SPELL(288),
    REM_LOF_SPELL(289), // Ligne de vue
    ADD_LPT_SPELL(290), // nombre de lancer par tour 
    ADD_LPC_SPELL(291), // nombre ce lancer par cible

    //ITEMS de classe
    BOOST_SPELL_RANGE(281),
    BOOST_SPELL_RANGEABLE(282),
    BOOST_SPELL_DMG(283),
    BOOST_SPELL_HEAL(284),
    BOOST_SPELL_AP_COST(285),
    BOOST_SPELL_CAST_INTVL(286),
    BOOST_SPELL_CC(287),
    BOOST_SPELL_CASTOUTLINE(288),
    BOOST_SPELL_NOLINEOFSIGHT(289),
    BOOST_SPELL_MAXPERTURN(290),
    BOOST_SPELL_MAXPERTARGET(291),
    BOOST_SPELL_SET_INTVL(292),
    BOOST_SPELL_DOMA(293),
    
    //fight effects
    TELEPORT(4),
    REPULSE(5),
    ATTRACT(6),
    DIVORCE(7),
    EXCHANGE_PLACE(8),
    ESQUIVE(9),
    USE_EMOT(10),
    CARRY(50),
    THROW(51),
    VOL_PM(77),
    CHANCE_ECA(79),
    HEAL1(81),
    VOL_VIE(82),
    VOL_PA(84),
    DOMA_PERCENT_EAU(85){

        @Override
        public Element getRelatedElement() {
            return Element.WATER;
        }
        
    },
    DOMA_PERCENT_TER(86){

        @Override
        public Element getRelatedElement() {
            return Element.EARTH;
        }
        
    },
    DOMA_PERCENT_AIR(87){

        @Override
        public Element getRelatedElement() {
            return Element.AIR;
        }
        
    },
    DOMA_PERCENT_FEU(88){

        @Override
        public Element getRelatedElement() {
            return Element.FIRE;
        }
        
    },
    DOMA_PERCENT_NEU(89){

        @Override
        public Element getRelatedElement() {
            return Element.NEUTRAL;
        }
        
    },
    DON_PERCENT_VIE(90),
    VOL_VIE_EAU(91){

        @Override
        public Element getRelatedElement() {
            return Element.WATER;
        }
        
    },
    VOL_VIE_TER(92){

        @Override
        public Element getRelatedElement() {
            return Element.EARTH;
        }
        
    },
    VOL_VIE_AIR(93){

        @Override
        public Element getRelatedElement() {
            return Element.AIR;
        }
        
    },
    VOL_VIE_FEU(94){

        @Override
        public Element getRelatedElement() {
            return Element.FIRE;
        }
        
    },
    VOL_VIE_NEU(95){

        @Override
        public Element getRelatedElement() {
            return Element.NEUTRAL;
        }
        
    },
    DOMA_EAU(96){

        @Override
        public Element getRelatedElement() {
            return Element.WATER;
        }
        
    },
    DOMA_TER(97){

        @Override
        public Element getRelatedElement() {
            return Element.EARTH;
        }
        
    },
    DOMA_AIR(98){

        @Override
        public Element getRelatedElement() {
            return Element.AIR;
        }
        
    },
    DOMA_FEU(99){

        @Override
        public Element getRelatedElement() {
            return Element.FIRE;
        }
        
    },
    DOMA_NEU(100){

        @Override
        public Element getRelatedElement() {
            return Element.NEUTRAL;
        }
        
    },
    IMMUNITY(105),
    RETURNS_SPELL(106),
    RETURNS_DOMA(107),
    HEAL(108),
    CASTER_DOMA(109),
    FIX_HEAL(110),
    ADD_DOM2(121),
    VOL_KAMAS(130),
    DOMA_PER_PA(131),
    DEBUFF(132),
    CASTER_REM_PO(135),
    CASTER_ADD_PO(136){

        @Override
        public Effect[] getEquivalentStats() {
            return new Effect[]{ADD_PO};
        }

        @Override
        public Effect[] getOppositeStats() {
            return new Effect[]{CASTER_REM_PO, REM_PO};
        }
        
    },
    ADD_ENERGY(139),
    SKIP_TURN(140),
    KILL(141),
    FIXED_HEAL(143),
    DOMA_NO_BOOST(144),
    CHANGE_SPEAK(146),
    CHANGE_SKIN(149),
    INVISIBILITY(150),
    REM_RES_MAG(172),
    REM_RES_PHY(173),
    CLONE(180),
    INVOC(181),
    RES_MAG(183){

        @Override
        public Effect[] getOppositeStats() {
            return new Effect[]{REM_RES_MAG};
        }
        
    },
    RES_PHY(184){

        @Override
        public Effect[] getOppositeStats() {
            return new Effect[]{REM_RES_PHY};
        }
        
    },
    STATIC_INVOC(185),
    REM_PDOM(186),
    UNKNOW1(193),
    WIN_KAMAS(194),
    TRANSFORM(197),
    ADD_FIGHT_OBJECT(201),
    PERCEPTION(202),
    RESUSCITATE(206),
    FIX_RES_DOMA(265),
    VOL_CHAN(266),
    VOL_VITA(267),
    VOL_AGIL(268),
    VOL_INTE(269),
    VOL_SAGE(270),
    VOL_FORC(271),
    DAMMAGE_PERCENT_MISSING_LIFE_WATER(275),
    DAMMAGE_PERCENT_MISSING_LIFE_EARTH(276),
    DAMMAGE_PERCENT_MISSING_LIFE_AIR(277),
    DAMMAGE_PERCENT_MISSING_LIFE_FIRE(278),
    DAMMAGE_PERCENT_MISSING_LIFE_NEUTRAL(279),
    VOL_PO(320),
    CHANGE_COLOR(333),
    TRAP(400),
    GLYPHE(401),
    GLYPHE_BLOP(402),
    REPLACE_BY_INVOC(405),
    
    //parchments + boosts
    LEARN_JOB(603),
    LEARN_SPELL(604),
    BOOST_XP(605),
    BOOST_SAGE(606),
    BOOST_FORC(607),
    BOOST_CHAN(608),
    BOOST_AGIL(609),
    BOOST_VITA(610),
    BOOST_INTE(611),
    BOOST_CAPITAL(612),
    BOOST_SPELL_PTS(613),
    BOOST_JOB_XP(614),
    UNLEARN_JOB(615),
    UNLEARN_SPELL(616),
    
    CONSULT(620),
    INVOC_GRADE(621),
    INVOC_CAPTURED(623),
    NO_EFFECT(666),
    COMB_SPONTANEE(671),
    PUNITION(672),
    CHANGE_ATTACK_ELEM(700),
    ADD_DURABILITY_PTS(702),
    CAPTURE_MOUNT(706),
    ADDITIONAL_COST(710),
    RENAME_GUILD(725),
    BONUS_CAPTURE(750),
    BONUS_XP_MOUNT(751),
    SACRIFICE(765),
    CONFU_HOURS1(770),
    CONFU_HOURS2(771),
    CONFU_HOURS3(772),
    CONFU_HOURS4(773),
    CONFU_HOURS5(774),
    CONFU_HOURS6(775),
    ADD_PERCENT_SUDDEN_DAMMAGES(776),
    LAISSE_SPIRIT(780),
    MINI_JET(781),
    MAXI_JET(782),
    POUSSE_CASE(783),
    RAULEBAQUE(784),
    HEAL_ON_ATTACK(786),
    MOT_LOTOF(787),
    CHATIMENT(788),
    HUNTING_WEAPON(795),
    ADD_LIFE(800),
    ADD_XP(805),
    CHANGE_SIZE(810),
    RES_ARME_EHTEREE(812),
    
    //Boost mount
    INCREASE_AGGRES(931),
    INCRESE_MATURE(936),
    INCREASE_ABILITY(940),
    PARK_OBJECT(948),
    
    //Obvi
    ADD_STATE(950),
    REM_STATE(951),
    OBVI_SKIN(972),
    OBVI_TYPE(973),
    
    ALIGN(960),
    GRADE(961),
    LEVEL(962),
    NAME(964),
    NULL1(971), //?
    NULL2(974), //?
    MODIFIED_BY(985),
    OWNER(987),
    MAKER(988),
    SEARCH(989),
    TEXT(990),
    MOUNT(995),
    MOUNT_OWNER(996),
    MOUNT_NAME(997),
    ;

    final private int id;
    
    final static private Map<Integer, Effect> effects = new HashMap<>();
    
    final static public Effect[] WEAPON_EFFECTS = new Effect[]{
        VOL_VIE_EAU, VOL_VIE_TER, VOL_VIE_AIR, VOL_VIE_FEU, VOL_VIE_NEU,
        DOMA_EAU, DOMA_TER, DOMA_AIR, DOMA_FEU, DOMA_NEU,
        REM_PA, VOL_KAMAS, HEAL, ADD_VIE
    };
    
    final static public Effect[] TEXT_EFFECTS = new Effect[]{
        MOUNT_NAME, TEXT, SEARCH, OWNER, MAKER, MODIFIED_BY, NAME, RENAME_GUILD, ALIGN,
        LEVEL, GRADE, MOUNT_OWNER
    };
    
    static {
        for(Effect effect : values()){
            effects.put(effect.id, effect);
        }
    }

    private Effect(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
    
    public Effect[] getOppositeStats(){
        return new Effect[]{};
    }
    
    public Effect[] getEquivalentStats(){
        return new Effect[]{};
    }
    
    public Element getRelatedElement(){
        return Element.NONE;
    }

    static public Effect valueOf(int id){
        return effects.get(id);
    }
}
