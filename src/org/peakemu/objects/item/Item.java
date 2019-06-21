package org.peakemu.objects.item;

import org.peakemu.objects.player.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import org.peakemu.Peak;
import org.peakemu.common.Constants;
import org.peakemu.common.Formulas;
import org.peakemu.database.Database;
import org.peakemu.database.parser.StatsParser;
import org.peakemu.world.ItemTemplate;
import org.peakemu.world.Stats;
import org.peakemu.world.StatsTemplate;
import org.peakemu.world.enums.Effect;
import org.peakemu.world.enums.InventoryPosition;
import org.peakemu.world.enums.ItemType;

public class Item implements Cloneable{

    protected ItemTemplate template;
    protected int quantity = 1;
    protected InventoryPosition position = InventoryPosition.NO_EQUIPED;
    protected int guid;
    protected String owner;
    protected int obvijevan;
    protected int obvijevanLook;
    protected StatsTemplate statsTemplate;

    public Item(String owner, int Guid, ItemTemplate template, int qua, InventoryPosition pos, StatsTemplate statsTemplate) {
        this.owner = owner;
        this.guid = Guid;
        this.template = template;
        this.quantity = qua;
        this.position = pos;
        this.statsTemplate = statsTemplate;
    }

    public int getObvijevanPos() {
        return obvijevan;
    }

    public void setObvijevanPos(int pos) {
        obvijevan = pos;

    }

    public int getObvijevanLook() {
        return obvijevanLook;
    }

    public void setObvijevanLook(int look) {
        obvijevanLook = look;
    }

    public Stats getStats() {
        return statsTemplate.getStats();
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public InventoryPosition getPosition() {
        return position;
    }

    public void setPosition(InventoryPosition position) {
        this.position = position;
    }

    public ItemType getType() {
        return template.getType();
    }

    public ItemTemplate getTemplate() {
        return template;
    }

    public int getGuid() {
        return guid;
    }
    
    public String getPacket(){
        return Integer.toHexString(getGuid()) + "~" + Integer.toHexString(getTemplate().getID()) + "~" + Integer.toHexString(getQuantity())
            + "~" + (getPosition() == InventoryPosition.NO_EQUIPED ? "" : Integer.toHexString(getPosition().getId())) + "~" + StatsParser.statsTemplateToString(getStatsTemplate());
    }

    public String parseCandyStatsString() // parse stats bonbons
    {
        StringBuilder stats = new StringBuilder();
//        int turns = this.stats.getEffect(Effect.ADD_TOURS); // Tours restants
//
//        boolean isFirst = true;
//        for (Entry<Effect, Integer> entry : this.stats.getMap().entrySet()) {
//            if (!isFirst) {
//                stats.append(",");
//            }
//            int statID = entry.getKey().getId();
//
//            if ((statID == 970) || (statID == 971) || (statID == 972) || (statID == 973) || (statID == 974) || (statID == 811)) {
//                int jet = ((Integer) entry.getValue()).intValue();
//
//                if ((statID == 974) || (statID == 972) || (statID == 970) || (statID == 811)) {
//                    stats.append(Integer.toHexString(statID)).append("#0#0#").append(Integer.toHexString(jet));
//                } else {
//                    stats.append(Integer.toHexString(statID)).append("#0#0#").append(jet);
//                }
//                if (statID == 973) {
//                    setObvijevanPos(jet);
//                }
//                if (statID == 972) {
//                    setObvijevanLook(jet);
//                }
//            } else {
//                String jet = "0d0+" + entry.getValue();
//                stats.append(Integer.toHexString(statID)).append("#");
//                stats.append(Integer.toHexString(((Integer) entry.getValue()).intValue())).append("#0#0#").append(jet);
//            }
//            isFirst = false;
//        }

//        for (_SpellEffect SE : effects) {
//            if (!isFirst) {
//                stats.append(",");
//            }
//
//            String[] infos = SE.getArgs().split(";");
//            try { // Correction tours bonbons
//                if (SE.getEffect() != Effect.ADD_TOURS) {
//                    stats.append(Integer.toHexString(SE.getEffect().getId())).append("#").append(infos[0]).append("#").append(infos[1]).append("#0#").append(infos[5]);
//                } else {
//                    stats.append(Integer.toHexString(SE.getEffect().getId())).append("#").append(infos[0]).append("#").append(infos[1]).append("#").append(turns).append("#").append(infos[5]);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                continue;
//            };
//
//            isFirst = false;
//        }

        return stats.toString();
    }

//    public String parseStatsString() {
//        if (getTemplate().getType() == ItemType.BONBON) // Si c'est un bonbon
//        {
//            return parseCandyStatsString();
//        }
//
//        StringBuilder stats = new StringBuilder();
//        boolean isFirst = true;
//        for (_SpellEffect SE : effects) {
//            if (!isFirst) {
//                stats.append(",");
//            }
//
//            String[] infos = SE.getArgs().split(";");
//            try {
//                stats.append(Integer.toHexString(SE.getEffect().getId())).append("#").append(infos[0]).append("#").append(infos[1]).append("#0#").append(infos[5]);
//            } catch (Exception e) {
//                e.printStackTrace();
//                continue;
//            };
//
//            isFirst = false;
//        }
//
//        for (Entry<Effect, Integer> entry : this.stats.getMap().entrySet()) {
//            if (!isFirst) {
//                stats.append(",");
//            }
//            int statID = entry.getKey().getId();
//
//            if ((statID == 970) || (statID == 971) || (statID == 972) || (statID == 973) || (statID == 974)) {
//                int jet = entry.getValue();
//                if ((statID == 974) || (statID == 972) || (statID == 970)) {
//                    stats.append(Integer.toHexString(statID)).append("#0#0#").append(Integer.toHexString(jet));
//                } else {
//                    stats.append(Integer.toHexString(statID)).append("#0#0#").append(jet);
//                }
//                if (statID == 973) {
//                    setObvijevanPos(jet);
//                }
//                if (statID == 972) {
//                    setObvijevanLook(jet);
//                }
//            } else {
//                String jet = "0d0+" + entry.getValue();
//                stats.append(Integer.toHexString(statID)).append("#");
//                stats.append(Integer.toHexString(entry.getValue())).append("#0#0#").append(jet);
//            }
//            isFirst = false;
//        }
//
//        for (Entry<Effect, String> entry : txtStats.entrySet()) {
//            if (!isFirst) {
//                stats.append(",");
//            }
//
//            if (entry.getKey() == Effect.INVOC_CAPTURED) {
//                stats.append(Integer.toHexString(entry.getKey().getId())).append("#0#0#").append(entry.getValue());
//            } else {
//                stats.append(Integer.toHexString(entry.getKey().getId())).append("#0#0#0#").append(entry.getValue());
//            }
//            isFirst = false;
//        }
//        return stats.toString();
//    }

    public String obvijevanOCO_Packet(int pos) {
        StringBuilder str = new StringBuilder();
        String strPos = String.valueOf(pos);
        if (pos == -1) {
            strPos = "";
        }
        str.append("OCO" + Integer.toHexString(getGuid()) + "~" + Integer.toHexString(getTemplate().getID()) + "~" + Integer.toHexString(getQuantity()) + "~" + strPos + "~" + StatsParser.statsTemplateToString(statsTemplate));
        return str.toString();
    }

    public void obvijevanNourir(Item obj) {
        if (obj == null) {
            return;
        }
//        for (Map.Entry<Effect, Integer> entry : stats.getMap().entrySet()) {
//            if (entry.getKey().getId() != 974) // on ne boost que la stat de l'expérience de l'obvi
//            {
//                continue;
//            }
//            if (entry.getValue() > 500) // si le boost a une valeur supérieure à 500 (irréaliste)
//            {
//                return;
//            }
//            entry.setValue(Integer.valueOf(entry.getValue().intValue() + obj.getTemplate().getLevel() / 10)); // valeur d'origine + ObjLvl / 32
//        }
    }

    public void obvijevanChangeStat(Effect effect, int val) {
//        for (Map.Entry<Effect, Integer> entry : stats.getMap().entrySet()) {
//            if (entry.getKey() != effect) {
//                continue;
//            }
//            entry.setValue(val);
//        }
    }

    public void removeAllObvijevanStats() {
        setObvijevanPos(0);
//        Stats StatsSansObvi = new Stats();
//        for (Map.Entry<Effect, Integer> entry : stats.getMap().entrySet()) {
//            int statID = entry.getKey().getId();
//            if ((statID == 970) || (statID == 971) || (statID == 972) || (statID == 973) || (statID == 974)) {
//                continue;
//            }
//            StatsSansObvi.addOneStat(entry.getKey(), entry.getValue());
//        }
//        stats = StatsSansObvi;
    }

    public void removeAll_ExepteObvijevanStats() {
        setObvijevanPos(0);
        Stats StatsSansObvi = new Stats();
//        for (Map.Entry<Effect, Integer> entry : stats.getMap().entrySet()) {
//            int statID = entry.getKey().getId();
//            if ((statID != 971) && (statID != 972) && (statID != 973) && (statID != 974)) {
//                continue;
//            }
//            StatsSansObvi.addOneStat(entry.getKey(), entry.getValue());
//        }
//        stats = StatsSansObvi;
    }

    /**
     * ********FM SYSTEM* * * * * * * * *
     */
    public String parseFMStatsString(String statsstr, Item obj, int add, boolean negatif) {
        StringBuilder stats = new StringBuilder();
        boolean isFirst = true;
//        for (_SpellEffect SE : obj.effects) {
//            if (!isFirst) {
//                stats.append(",");
//            }
//
//            String[] infos = SE.getArgs().split(";");
//            try {
//                stats.append(Integer.toHexString(SE.getEffect().getId())).append("#").append(infos[0]).append("#").append(infos[1]).append("#0#").append(infos[5]);
//            } catch (Exception e) {
//                e.printStackTrace();
//                continue;
//            };
//
//            isFirst = false;
//        }

//        for (Entry<Effect, Integer> entry : obj.stats.getMap().entrySet()) {
//            if (!isFirst) {
//                stats.append(",");
//            }
//            if (Integer.toHexString(entry.getKey().getId()).compareTo(statsstr) == 0) {
//                int newstats = 0;
//                if (negatif) {
//                    newstats = entry.getValue() - add;
//                    if (newstats < 1) {
//                        continue;
//                    }
//                } else {
//                    newstats = entry.getValue() + add;
//                }
//                String jet = "0d0+" + newstats;
//                stats.append(Integer.toHexString(entry.getKey().getId())).append("#").append(Integer.toHexString(entry.getValue())).append(add).append("#0#0#").append(jet);
//            } else {
//                String jet = "0d0+" + entry.getValue();
//                stats.append(Integer.toHexString(entry.getKey().getId())).append("#").append(Integer.toHexString(entry.getValue())).append("#0#0#").append(jet);
//            }
//            isFirst = false;
//        }

//        for (Entry<Effect, String> entry : obj.txtStats.entrySet()) {
//            if (!isFirst) {
//                stats.append(",");
//            }
//            stats.append(Integer.toHexString(entry.getKey().getId())).append("#0#0#0#").append(entry.getValue());
//            isFirst = false;
//        }

        return stats.toString();
    }

    public String parseFMEchecStatsString(Item obj, double poid) {
        StringBuilder stats = new StringBuilder();
        boolean isFirst = true;
//        for (_SpellEffect SE : obj.effects) {
//            if (!isFirst) {
//                stats.append(",");
//            }
//
//            String[] infos = SE.getArgs().split(";");
//            try {
//                stats.append(Integer.toHexString(SE.getEffect().getId())).append("#").append(infos[0]).append("#").append(infos[1]).append("#0#").append(infos[5]);
//            } catch (Exception e) {
//                e.printStackTrace();
//                continue;
//            };
//
//            isFirst = false;
//        }
//
//        for (Entry<Effect, Integer> entry : obj.stats.getMap().entrySet()) {
//            //En cas d'echec les stats négatives Chance,Agi,Intel,Force,Portee,Vita augmentes
//            int newstats = 0;
//
//            if (entry.getKey().getId() == 152
//                || entry.getKey().getId() == 154
//                || entry.getKey().getId() == 155
//                || entry.getKey().getId() == 157
//                || entry.getKey().getId() == 116
//                || entry.getKey().getId() == 153) {
//                float a = (float) ((entry.getValue() * poid) / 100);
//                if (a < 1) {
//                    a = 1;
//                }
//                float chute = (float) (entry.getValue() + a);
//                newstats = (int) Math.floor(chute);
//					//On limite la chute du négatif a sont maximum
//                //metier
//                if (newstats > Job.getBaseMaxJet(obj.getTemplate().getID(), Integer.toHexString(entry.getKey().getId()))) {
//                    newstats = Job.getBaseMaxJet(obj.getTemplate().getID(), Integer.toHexString(entry.getKey().getId()));
//                }
//            } else {
//                if (entry.getKey().getId() == 127 || entry.getKey().getId() == 101) {
//                    continue;//PM, pas de négatif ainsi que PA
//                }
//                float chute = (float) (entry.getValue() - ((entry.getValue() * poid) / 100));
//                newstats = (int) Math.floor(chute);
//            }
//            if (newstats < 1) {
//                continue;
//            }
//            String jet = "0d0+" + newstats;
//            if (!isFirst) {
//                stats.append(",");
//            }
//            stats.append(Integer.toHexString(entry.getKey().getId())).append("#").append(Integer.toHexString(newstats)).append("#0#0#").append(jet);
//            isFirst = false;
//        }
//
//        for (Entry<Effect, String> entry : obj.txtStats.entrySet()) {
//            if (!isFirst) {
//                stats.append(",");
//            }
//            stats.append(Integer.toHexString(entry.getKey().getId())).append("#0#0#0#").append(entry.getValue());
//            isFirst = false;
//        }
        return stats.toString();
    }

    public static int getPoidOfActualItem(String statsTemplate)//Donne le poid de l'item actuel
    {
        int poid = 0;
        int somme = 0;
        String[] splitted = statsTemplate.split(",");
        for (String s : splitted) {
            String[] stats = s.split("#");
            int statID = Integer.parseInt(stats[0], 16);
            boolean follow = true;

            for (int a : Constants.ARMES_EFFECT_IDS)//Si c'est un Effet Actif
            {
                if (a == statID) {
                    follow = false;
                }
            }
            if (!follow) {
                continue;//Si c'était un effet Actif d'arme
            }
            String jet = "";
            int value = 1;
            try {
                jet = stats[4];
                value = Formulas.getRandomJet(jet);
                try {
                    //on prend le jet max
                    int min = Integer.parseInt(stats[1], 16);
                    int max = Integer.parseInt(stats[2], 16);
                    value = min;
                    if (max != 0) {
                        value = max;
                    }
                } catch (Exception e) {
                    value = Formulas.getRandomJet(jet);
                };
            } catch (Exception e) {
            };

            int multi = 1;
            if (statID == 118 || statID == 126 || statID == 125 || statID == 119 || statID == 123 || statID == 158 || statID == 174)//Force,Intel,Vita,Agi,Chance,Pod,Initiative
            {
                multi = 1;
            } else if (statID == 138 || statID == 666 || statID == 226 || statID == 220)//Domages %,Domage renvoyé,Piège %
            {
                multi = 2;
            } else if (statID == 124 || statID == 176)//Sagesse,Prospec
            {
                multi = 3;
            } else if (statID == 240 || statID == 241 || statID == 242 || statID == 243 || statID == 244)//Ré Feu, Air, Eau, Terre, Neutre
            {
                multi = 4;
            } else if (statID == 210 || statID == 211 || statID == 212 || statID == 213 || statID == 214)//Ré % Feu, Air, Eau, Terre, Neutre
            {
                multi = 5;
            } else if (statID == 225)//Piège
            {
                multi = 15;
            } else if (statID == 178 || statID == 112)//Soins,Dommage
            {
                multi = 20;
            } else if (statID == 115 || statID == 182)//Cri,Invoc
            {
                multi = 30;
            } else if (statID == 117)//PO
            {
                multi = 50;
            } else if (statID == 128)//PM
            {
                multi = 90;
            } else if (statID == 111)//PA
            {
                multi = 100;
            }
            poid = value * multi; //poid de la carac
            somme += poid;
        }
        return somme;
    }

    public static int getPoidOfBaseItem(int i)//Donne le poid de l'item actuel
    {
        int poid = 0;
        int somme = 0;
        String NaturalStatsItem = "";
        ResultSet RS;
        try {
            RS = Database.executeQuery("SELECT statsTemplate from `item_template` WHERE `id`='" + i + "';");
            RS.next();
            NaturalStatsItem = RS.getString("statsTemplate");
        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
            e.printStackTrace();
        }

        if (NaturalStatsItem == null || NaturalStatsItem.isEmpty()) {
            return 0;
        }
        String[] splitted = NaturalStatsItem.split(",");
        for (String s : splitted) {
            String[] stats = s.split("#");
            int statID = Integer.parseInt(stats[0], 16);
            boolean follow = true;

            for (int a : Constants.ARMES_EFFECT_IDS)//Si c'est un Effet Actif
            {
                if (a == statID) {
                    follow = false;
                }
            }
            if (!follow) {
                continue;//Si c'était un effet Actif d'arme
            }
            String jet = "";
            int value = 1;
            try {
                jet = stats[4];
                value = Formulas.getRandomJet(jet);
                try {
                    //on prend le jet max
                    int min = Integer.parseInt(stats[1], 16);
                    int max = Integer.parseInt(stats[2], 16);
                    value = min;
                    if (max != 0) {
                        value = max;
                    }
                } catch (Exception e) {
                    value = Formulas.getRandomJet(jet);
                };
            } catch (Exception e) {
            };

            int multi = 1;
            if (statID == 118 || statID == 126 || statID == 125 || statID == 119 || statID == 123 || statID == 158 || statID == 174)//Force,Intel,Vita,Agi,Chance,Pod,Initiative
            {
                multi = 1;
            } else if (statID == 138 || statID == 666 || statID == 226 || statID == 220)//Domages %,Domage renvoyé,Piège %
            {
                multi = 2;
            } else if (statID == 124 || statID == 176)//Sagesse,Prospec
            {
                multi = 3;
            } else if (statID == 240 || statID == 241 || statID == 242 || statID == 243 || statID == 244)//Ré Feu, Air, Eau, Terre, Neutre
            {
                multi = 4;
            } else if (statID == 210 || statID == 211 || statID == 212 || statID == 213 || statID == 214)//Ré % Feu, Air, Eau, Terre, Neutre
            {
                multi = 5;
            } else if (statID == 225)//Piège
            {
                multi = 15;
            } else if (statID == 178 || statID == 112)//Soins,Dommage
            {
                multi = 20;
            } else if (statID == 115 || statID == 182)//Cri,Invoc
            {
                multi = 30;
            } else if (statID == 117)//PO
            {
                multi = 50;
            } else if (statID == 128)//PM
            {
                multi = 90;
            } else if (statID == 111)//PA
            {
                multi = 100;
            }
            poid = value * multi; //poid de la carac
            somme += poid;
        }
        return somme;
    }
    /* *********FM SYSTEM********* */

    @Deprecated
    public static Item getCloneObjet(Item obj, int qua) {
        return obj.cloneItem(qua);
    }
    
    public Item cloneItem(int newQte){
        Item clone = this.clone();
        clone.setQuantity(newQte);
        return clone;
    }

    @SuppressWarnings("unchecked")
    public void obvijevanNourir(Item obj, Player perso) {
        if (obj == null) {
            return;
        }
//        for (@SuppressWarnings("rawtypes") Map.Entry entry : this.stats.getMap().entrySet()) {
//            if (((Integer) entry.getKey()).intValue() != 974) {
//                continue;
//            }
//            if (((Integer) entry.getValue()).intValue() > 500) {
//                return;
//            }
//            entry.setValue(Integer.valueOf(((Integer) entry.getValue()).intValue() + obj.getTemplate().getLevel()));
//
//            if (obj.getTemplate().getID() == getTemplate().getID()) {
//                entry.setValue(Integer.valueOf(((Integer) entry.getValue()).intValue() + obj.getTemplate().getLevel()));
//            }
//        }
    }

    //Baskwo:Concasseur
    public static String getRunes(Item Obj) {
        String Runes = "";
//        String StatsTemplate = Obj.parseStatsString();
//        boolean IsFirst = true;
//        String[] Splitted = StatsTemplate.split(",");
//        for (String s : Splitted) {
//            String[] Stats = s.split("#");
//            int StatID = Integer.parseInt(Stats[0], 16);
//            int Nombre = 0;
//            try {
//                Nombre = Integer.parseInt(Stats[4].replaceAll("0d0\\+", ""));
//            } catch (Exception e) {
//                continue;
//            }
//            if (Nombre == 0) {
//                continue;
//            }
//            boolean Follow = true;
//
//            for (int a : Constants.ARMES_EFFECT_IDS)//Si c'est un Effet Actif
//            {
//                if (a == StatID) {
//                    Follow = false;
//                }
//            }
//            if (!Follow) {
//                continue;//Si c'était un effet Actif d'arme
//            }
//            if (!IsFirst) {
//                Runes += ";";
//            }
//            Runes += Constants.StatsToRunes(StatID) + "," + Nombre;
//            IsFirst = false;
//        }
        return Runes;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
    
    public boolean canStack(Item other){
        return other.getStatsTemplate().equals(statsTemplate)
            && other.getTemplate().equals(template)
            && other.getTemplate().getType() != ItemType.PIERRE_AME_PLEINE
            && other.getGuid() != guid
            && getPosition() == InventoryPosition.NO_EQUIPED;
    }

    public StatsTemplate getStatsTemplate() {
        return statsTemplate;
    }

    public void setGuid(int guid) {
        this.guid = guid;
    }

    @Override
    public String toString() {
        return "Item{" + "template=" + template.getName() + ", quantity=" + quantity + ", position=" + position + ", guid=" + guid + ", owner=" + owner + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + this.guid;
        hash = 79 * hash + Objects.hashCode(this.owner);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Item other = (Item) obj;
        if (this.guid != other.guid) {
            return false;
        }
        if (!Objects.equals(this.owner, other.owner)) {
            return false;
        }
        return true;
    }

    @Override
    protected Item clone() {
        try{
            Item item = (Item)super.clone();

            item.owner = "";
            item.position = InventoryPosition.NO_EQUIPED;
            item.statsTemplate = new StatsTemplate(statsTemplate);

            return item;
        }catch(CloneNotSupportedException e){
            Peak.errorLog.addToLog(e);
            return null;
        }
    }
    
    
}
