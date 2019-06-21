package org.peakemu.objects;


import java.util.ArrayList;
import java.util.List;
import org.peakemu.world.ExpLevel;

import org.peakemu.world.ItemStorage;
import org.peakemu.world.MountTemplate;
import org.peakemu.world.Stats;

public class Mount {
    final private int id;
    final private MountTemplate template;
    final private int gender;
    private int _amour;
    private int _endurance;
    private ExpLevel expLevel;
    private long exp;
    private String name;
    private int _fatigue;
    private int _energie;
    private int _reprod;
    private int _maturite;
    private int _serenite;
    private Stats stats = new Stats();
    private String _ancetres = ",,,,,,,,,,,,,";
    final private ItemStorage items;
	//Baskwo:Cameleone

    //TODO: Capacit�s
    private List<Integer> capacite = new ArrayList<Integer>(2);
    String _ability = ",";

    public Mount(int id, MountTemplate template, int sexe, int amour, int endurance,
        ExpLevel expLevel, long exp, String nom, int fatigue,
        int energie, int reprod, int maturite, int serenite, ItemStorage items, String anc,
        //Baskwo:Cameleone
        String ability) {
        this.id = id;
        this.template = template;
        this.gender = sexe;
        this._amour = amour;
        this._endurance = endurance;
        this.expLevel = expLevel;
        this.exp = exp;
        this.name = nom;
        _fatigue = fatigue;
        _energie = energie;
        _reprod = reprod;
        _maturite = maturite;
        _serenite = serenite;
        _ancetres = anc;
        this.stats = template.getStatsByLevel(expLevel.level);
                //Baskwo:Cameleone
		/*Baskwo:Cameleone
         Mathias52 : Modification*/
        _ability = ability;
        for (String s : ability.split(",", 2)) {// 2 : Maximum 2 capa ;)
            if (s != null && !s.isEmpty()) {
                int a = Integer.parseInt(s);
                try {
                    capacite.add(a);
                } catch (Exception e) {
                }
            }
        }

        this.items = items;
    }

    public int getId() {
        return id;
    }

    public MountTemplate getTemplate() {
        return template;
    }

    public int getGender() {
        return gender;
    }

    public int get_amour() {
        return _amour;
    }

    public String get_ancetres() {
        return _ancetres;
    }

    public int get_endurance() {
        return _endurance;
    }

    public int getLevel() {
        return expLevel.level;
    }

    public ExpLevel getExpLevel() {
        return expLevel;
    }

    public void setExpLevel(ExpLevel expLevel) {
        if(expLevel.equals(this.expLevel))
            return;
        
        this.expLevel = expLevel;
        stats = template.getStatsByLevel(expLevel.level);
    }

    public long getExp() {
        return exp;
    }

    public String getName() {
        return name;
    }

    public int get_fatigue() {
        return _fatigue;
    }

    public int get_energie() {
        return _energie;
    }

    public int get_reprod() {
        return _reprod;
    }

    public int get_maturite() {
        return _maturite;
    }

    public int get_serenite() {
        return _serenite;
    }

    public Stats getStats() {
        return stats;
    }

    public int getMaxEnergie() {
        return 1000 + getLevel() * 90;
    }

    public int getMaxMatu() {
        int matu = 1000;
        return matu;
    }

    public int getTotalPod() {
        int pod = 1000;

        return pod;
    }

    public int getMaxPod() {
        return getTotalPod();
    }

    public boolean isMountable() {
        if (_energie < 10
            || _maturite < getMaxMatu()
            || _fatigue == 240) {
            return false;
        }
        return true;
    }

    public void setName(String packet) {
        name = packet;
    }

    public void addXp(long amount) {
        exp += amount;
    }

    //Baskwo:Cameleone

    public String get_color(String a) {
        String b = "";
        if (capacite.contains(9)) {
            b = b + "," + a;
        }
        return template.getId() + b;
    }

    public boolean isCameleone() {
        return capacite.contains(9);
    }

    public String get_ability() {
        return _ability;
    }

    public boolean addCapacity(String capa) {
        int c = 0;
        for (String s : capa.split(",", 2)) {
            if (capacite.size() >= 2) {
                return false;
            }
            try {
                c = Integer.parseInt(s);
            } catch (Exception e) {
            }
            if (c != 0) {
                capacite.add(c);
            }
            if (capacite.size() == 1) {
                this._ability = capacite.get(0) + ",";
            } else {
                this._ability = capacite.get(0) + "," + capacite.get(1);
            }
        }
        return true;
    }

    public void setEnergie(int energie) {
        _energie = energie;
        if (_energie > getMaxEnergie()) {
            _energie = getMaxEnergie();
        }
    }

    public ItemStorage getItems() {
        return items;
    }
}
