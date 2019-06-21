package org.peakemu.world;

import java.util.ArrayList;
import java.util.Collection;

public class NPCTemplate {

    private int _id;
    private int _bonusValue;
    private int _gfxID;
    private int _scaleX;
    private int _scaleY;
    private int _sex;
    private int _color1;
    private int _color2;
    private int _color3;
    private String _acces;
    private int _extraClip;
    private int _customArtWork;
    private NPCQuestion initQuestion;
    private Collection<ItemTemplate> store;
    private String _quests;

    public NPCTemplate(int _id, int value, int _gfxid, int _scalex, int _scaley,
        int _sex, int _color1, int _color2, int _color3, String _acces,
        int clip, int artWork, NPCQuestion question, Collection<ItemTemplate> store, String quests) {
        this._id = _id;
        _bonusValue = value;
        _gfxID = _gfxid;
        _scaleX = _scalex;
        _scaleY = _scaley;
        this._sex = _sex;
        this._color1 = _color1;
        this._color2 = _color2;
        this._color3 = _color3;
        this._acces = _acces;
        _extraClip = clip;
        _customArtWork = artWork;
        initQuestion = question;
        _quests = quests;
        this.store = store;
    }

    public int get_id() {
        return _id;
    }

    public int get_bonusValue() {
        return _bonusValue;
    }

    public int get_gfxID() {
        return _gfxID;
    }

    public int get_scaleX() {
        return _scaleX;
    }

    public int get_scaleY() {
        return _scaleY;
    }

    public int get_sex() {
        return _sex;
    }

    public int get_color1() {
        return _color1;
    }

    public int get_color2() {
        return _color2;
    }

    public int get_color3() {
        return _color3;
    }

    public String get_acces() {
        return _acces;
    }

    public int get_extraClip() {
        return _extraClip;
    }

    public int get_customArtWork() {
        return _customArtWork;
    }

    public NPCQuestion getInitQuestion() {
        return initQuestion;
    }

    public String get_quests() {
        return _quests;
    }

    public Collection<ItemTemplate> getStore() {
        return store;
    }
    
    public ItemTemplate getSellItem(int id){
        for(ItemTemplate it : store){
            if(it.getID() == id)
                return it;
        }
        
        return null;
    }

    public boolean addItemVendor(ItemTemplate T) {
        if (store.contains(T)) {
            return false;
        }
        store.add(T);
        return true;
    }

    public boolean delItemVendor(int tID) {
        ArrayList<ItemTemplate> newVentes = new ArrayList<ItemTemplate>();
        boolean remove = false;
        for (ItemTemplate T : store) {
            if (T.getID() == tID) {
                remove = true;
                continue;
            }
            newVentes.add(T);
        }
        store = newVentes;
        return remove;
    }

    public boolean haveItem(int templateID) {
        for (ItemTemplate curTemp : store) {
            if (curTemp.getID() == templateID) {
                return true;
            }
        }

        return false;
    }
}
