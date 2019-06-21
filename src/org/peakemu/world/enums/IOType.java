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
public enum IOType {
    FRENE(new int[]{7500}, new int[]{6}),
    SCIE(new int[]{7003}, new int[]{101}),
    CHENE(new int[]{7503}, new int[]{10}),
    TABLE_DE_CONFECTION(new int[]{7011}, new int[]{13, 14}),
    ATELIER(new int[]{7008, 7009, 7010}, new int[]{11, 12}),
    ETABLI(new int[]{7013}, new int[]{17, 149, 148, 15, 16, 147}),
    CHAUDRON(new int[]{}, new int[]{}),
    ZAAP(new int[]{4287, 7000, 7026, 7029}, new int[]{44, 114}),
    FER(new int[]{7520}, new int[]{24}),
    FOUR(new int[]{7001}, new int[]{109, 27}),
    ARGENT(new int[]{7526}, new int[]{29}),
    OR(new int[]{7527}, new int[]{30}),
    PIERRE_DE_BAUXITE(new int[]{7528}, new int[]{31}),
    MOULE(new int[]{7002}, new int[]{32}),
    IF(new int[]{7505}, new int[]{33}),
    EBENE(new int[]{7507}, new int[]{34}),
    ORME(new int[]{7509}, new int[]{35}),
    ERABLE(new int[]{7504}, new int[]{37}),
    CHARME(new int[]{7508}, new int[]{38}),
    CHATAIGNIER(new int[]{7501}, new int[]{39}),
    NOYER(new int[]{7502}, new int[]{40}),
    MERISIER(new int[]{7506}, new int[]{41}),
    PIERRE_DE_KOBALTE(new int[]{7525}, new int[]{28}),
    BLE(new int[]{7511}, new int[]{45}),
    HOUBLON(new int[]{7512}, new int[]{46}),
    MOULIN(new int[]{}, new int[]{}),
    MEULE(new int[]{7005}, new int[]{48}),
    LIN(new int[]{7513}, new int[]{68, 50}),
    ORGE(new int[]{7515}, new int[]{53}),
    SEIGLE(new int[]{7516}, new int[]{52}),
    AVOINE(new int[]{7517}, new int[]{57}),
    CHANVRE(new int[]{7514}, new int[]{54, 69}),
    MALT(new int[]{7518}, new int[]{58}),
    TAS_DE_PATATES(new int[]{7510}, new int[]{42}),
    TABLE_A_PATATES(new int[]{7006}, new int[]{22}),
    CONCASSEUR(new int[]{7007}, new int[]{47, 122}),
    ETAIN(new int[]{7521}, new int[]{55}),
    PIERRE_CUIVREE(new int[]{7522}, new int[]{25}),
    MANGANESE(new int[]{7524}, new int[]{56}),
    BRONZE(new int[]{7523}, new int[]{26}),
    SOURCE_DE_JOUVENCE(new int[]{7004}, new int[]{62}),
    ENCLUME(new int[]{7012}, new int[]{143, 19, 145, 144, 142, 146, 67, 21, 65, 66, 20, 18}),
    MACHINE_A_COUDRE(new int[]{7014, 7016}, new int[]{63}),
    MARMITE(new int[]{7017}, new int[]{111}),
    EDELWEISS(new int[]{7536}, new int[]{74}),
    ALEMBIC(new int[]{}, new int[]{96}),
    FROMENT(new int[]{}, new int[]{}),
    EPEAUTRE(new int[]{}, new int[]{}),
    SORGHO(new int[]{}, new int[]{}),
    MENTHE_SAUVAGE(new int[]{7534}, new int[]{72}),
    TREFLE_A_5_FEUILLES(new int[]{7533}, new int[]{71}),
    ORCHIDEE_FREYESQUE(new int[]{7535}, new int[]{73}),
    MORTIER_PILON(new int[]{}, new int[]{}),
    PORTE(new int[]{4516, 6700, 6701, 6702, 6703, 6704, 6705, 6706, 6707, 6708, 6709, 6710, 6711, 6713, 6714, 6715, 6716, 6717, 6718, 6719, 6720, 6721, 6722, 6723, 6724, 6725, 6726, 6729, 6730, 6731, 6732, 6733, 6734, 6735, 6736, 6737, 6738, 6739, 6740, 6741, 6742, 6743, 6744, 6745, 6746, 6747, 6748, 6749, 6750, 6751, 6752, 6753, 6754, 6755, 6756, 6757, 6758, 6759, 6760, 6761, 6762, 6764, 6765, 6768, 6769, 6770, 6773, 6774, 6775, 6776}, new int[]{98, 100, 97, 108, 81, 84}),
    PETITS_POISSONS_MER(new int[]{7530}, new int[]{128}),
    SOMOON_AGRESSIF(new int[]{}, new int[]{}),
    PWOULPE(new int[]{}, new int[]{}),
    POISSONS_RIVIERE(new int[]{7532}, new int[]{125}),
    PETITS_POISSONS_RIVIERE(new int[]{7529}, new int[]{124}),
    GROS_POISSONS_RIVIERE(new int[]{7537}, new int[]{126}),
    POISSONS_MER(new int[]{7531}, new int[]{129}),
    GROS_POISSONS_MER(new int[]{7538}, new int[]{130}),
    POISSONS_GEANTS_RIVIERE(new int[]{7539}, new int[]{127}),
    TRUITE_VASEUSE(new int[]{}, new int[]{}),
    POISSONS_GEANTS_MER(new int[]{7540}, new int[]{131}),
    COTON(new int[]{}, new int[]{}),
    FILEUSE(new int[]{}, new int[]{95}),
    PUITS(new int[]{7519}, new int[]{102}),
    COFFRE(new int[]{7350, 7351, 7353}, new int[]{104, 105, 106}),
    MACHINE_A_COUDRE2(new int[]{7015}, new int[]{123, 64}),
    ETABLI_EN_BOIS(new int[]{7018}, new int[]{110}),
    ALAMBIC(new int[]{7019}, new int[]{23}),
    ENCLUME_MAGIQUE(new int[]{7020}, new int[]{1, 117, 118, 115, 113, 120, 119, 116}),
    CONCASSEUR_MUNSTER(new int[]{7021}, new int[]{181, 121}),
    PLAN_DE_TRAVAIL1(new int[]{7022}, new int[]{135}),
    PLAN_DE_TRAVAIL2(new int[]{7025}, new int[]{132}),
    PLAN_DE_TRAVAIL3(new int[]{7024}, new int[]{133}),
    PLAN_DE_TRAVAIL4(new int[]{7023}, new int[]{134}),
    BOMBU(new int[]{7541}, new int[]{139}),
    OMBRE_ETRANGE(new int[]{7543}, new int[]{140}),
    PICHON(new int[]{7544}, new int[]{136}),
    OLIVIOLET(new int[]{7542}, new int[]{141}),
    MACHINE_DE_FORCE(new int[]{7546, 7547}, new int[]{150}),
    ETABLI_PYROTECHNIQUE(new int[]{7028}, new int[]{151}),
    KOINKOIN(new int[]{7549}, new int[]{152}),
    POUBELLE(new int[]{7352}, new int[]{153}),
    ZAAPI(new int[]{7030, 7031}, new int[]{157}),
    ENCLUME_A_BOUCLIERS(new int[]{7027}, new int[]{156}),
    BAMBOU(new int[]{7553}, new int[]{154}),
    BAMBOU_SOMBRE(new int[]{7554}, new int[]{155}),
    BAMBOU_SACRE(new int[]{7552}, new int[]{158}),
    RIZ(new int[]{7550}, new int[]{159}),
    PANDOUILLE(new int[]{7551}, new int[]{160}),
    DOLOMITE(new int[]{7555}, new int[]{161}),
    SILICATE(new int[]{7556}, new int[]{162}),
    MACHINE_A_COUDRE_MAGIQUE(new int[]{7036}, new int[]{165, 167, 166}),
    ATELIER_MAGIQUE(new int[]{7038}, new int[]{169, 168}),
    TABLE_MAGIQUE(new int[]{7037}, new int[]{163, 164}),
    LISTE_DES_ARTISANS(new int[]{7035}, new int[]{170}),
    ENCLOS(new int[]{6763, 6766, 6767, 6772}, new int[]{177, 175, 176, 178}),
    KALIPTUS(new int[]{7557}, new int[]{174}),
    ATELIER_DE_BRICOLAGE(new int[]{7039}, new int[]{182, 171}),
    LEVIER(new int[]{7041, 7042, 7043, 7044, 7045}, new int[]{179}),
    STATUE_DE_CLASSE(new int[]{1845, 1853, 1854, 1855, 1856, 1857, 1858, 1859, 1860, 1861, 1862, 2319}, new int[]{183}),
    ;
    
    final private int[] objectIds;
    final private int[] validActions;

    final static private Map<Integer, IOType> typesById = new HashMap<>();

    static {
        for (IOType type : values()) {
            for (int id : type.objectIds) {
                typesById.put(id, type);
            }
        }
    }

    private IOType(int[] objectIds, int[] validActions) {
        this.objectIds = objectIds;
        this.validActions = validActions;
    }

    public int[] getObjectIds() {
        return objectIds;
    }

    public int[] getValidActions() {
        return validActions;
    }
    
    public boolean canDoAction(int action){
        for(int act : validActions){
            if(act == action)
                return true;
        }
        
        return false;
    }

    static public IOType valueOf(int id) {
        return typesById.get(id);
    }
}
