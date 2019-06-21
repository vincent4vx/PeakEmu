/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world;

import java.util.Collection;
import org.peakemu.common.Constants;
import org.peakemu.database.parser.StatsParser;
import org.peakemu.game.GameServer;
import org.peakemu.objects.item.Item;
import org.peakemu.world.action.Action;
import org.peakemu.world.enums.Effect;
import org.peakemu.world.enums.InventoryPosition;
import org.peakemu.world.enums.ItemType;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class ItemTemplate {
    private int ID;
    private StatsTemplate statsTemplate;
    private String name;
    private ItemType type;
    private int level;
    private int pod;
    private int prix;
    private ItemSet itemSet;
    private String conditions;
    private int PACost, POmin, POmax, TauxCC, TauxEC, BonusCC;
    private boolean isTwoHanded;
    final private Collection<Action> onUseActions;
    private long sold;
    private int avgPrice;

    public ItemTemplate(int id, StatsTemplate statsTemplate, String name, ItemType type, int level, int pod, int prix, ItemSet itemSet, String conditions, String armesInfos, int sold, int avgPrice, Collection<Action> onUseActions) {
        this.ID = id;
        this.statsTemplate = statsTemplate;
        this.name = name;
        this.type = type;
        this.level = level;
        this.pod = pod;
        this.prix = prix;
        this.itemSet = itemSet;
        this.conditions = conditions;
        this.PACost = -1;
        this.POmin = 1;
        this.POmax = 1;
        this.TauxCC = 100;
        this.TauxEC = 2;
        this.BonusCC = 0;
        this.sold = sold;
        this.avgPrice = avgPrice;
        this.onUseActions = onUseActions;

        try {
            String[] infos = armesInfos.split(";");
            PACost = Integer.parseInt(infos[0]);
            POmin = Integer.parseInt(infos[1]);
            POmax = Integer.parseInt(infos[2]);
            TauxCC = Integer.parseInt(infos[3]);
            TauxEC = Integer.parseInt(infos[4]);
            BonusCC = Integer.parseInt(infos[5]);
            isTwoHanded = infos[6].equals("1");
        } catch (Exception e) {
        };

    }

    public int get_obviType() {
        try {
            for(StatsTemplate.StatEntry entry : statsTemplate.getEntries()){
                if(entry.getEffect() == Effect.OBVI_TYPE){
                    return entry.getSpecial();
                }
            }
        } catch (Exception e) {
            GameServer.addToLog(e.getMessage());
            return Constants.ITEM_TYPE_OBJET_VIVANT;
        }
        return Constants.ITEM_TYPE_OBJET_VIVANT; //Si erreur on retourne le type de base
    }

    public boolean isTwoHanded() {
        return isTwoHanded;
    }

    public int getBonusCC() {
        return BonusCC;
    }

    public int getPOmin() {
        return POmin;
    }

    public int getPOmax() {
        return POmax;
    }

    public int getTauxCC() {
        return TauxCC;
    }

    public int getTauxEC() {
        return TauxEC;
    }

    public int getPACost() {
        return PACost;
    }

    public int getID() {
        return ID;
    }

    public StatsTemplate getStatsTemplate() {
        return statsTemplate;
    }
    
    @Deprecated
    public String getStrTemplate(){
        return StatsParser.statsTemplateToString(statsTemplate);
    }

    public String getName() {
        return name;
    }

    public ItemType getType() {
        return type;
    }

    public int getLevel() {
        return level;
    }

    public int getPod() {
        return pod;
    }

    public int getPrix() {
        return prix;
    }

    public ItemSet getItemSet() {
        return itemSet;
    }

    public String getConditions() {
        return conditions;
    }

    public String parseItemTemplateStats() {
        return (this.ID + ";" + statsTemplate);
    }

    public int getAvgPrice() {
        return avgPrice;
    }

    public long getSold() {
        return this.sold;
    }

    public synchronized void newSold(int amount, int price) {
        long oldSold = sold;
        sold += amount;
        avgPrice = (int) ((avgPrice * oldSold + price) / sold);
    }
    
//    public Item createNewItem(int qte, boolean useMax){
//        ItemType itemType = getType();
//        return itemType.createItem(
//            "", 
//            -1, 
//            this, 
//            qte, 
//            InventoryPosition.NO_EQUIPED, 
//            useMax ? getStatsTemplate().maximizeStats() : getStatsTemplate().randomizeStats()
//        );
//    }
    
    public Collection<Action> getUseActions(){
        return onUseActions;
    }
}
