/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.fight.fighter;

import org.peakemu.world.fight.team.FightTeam;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.peakemu.Ancestra;
import org.peakemu.common.Formulas;
import org.peakemu.common.SocketManager;
import org.peakemu.game.GameServer;
import org.peakemu.game.out.game.AddFightBuff;
import org.peakemu.game.out.game.GameActionResponse;
import org.peakemu.objects.Collector;
import org.peakemu.objects.Prism;
import org.peakemu.objects.player.Player;
import org.peakemu.world.MapCell;
import org.peakemu.objects.Monster;
import org.peakemu.world.SpellLevel;
import org.peakemu.world.Sprite;
import org.peakemu.world.Stats;
import org.peakemu.world.enums.Effect;
import org.peakemu.world.fight.Fight;
import org.peakemu.world.fight.LaunchedSort;
import static org.peakemu.world.enums.Effect.ADD_MAITRISE;
import static org.peakemu.world.enums.Effect.CHANCE_ECA;
import static org.peakemu.world.enums.Effect.CHATIMENT;
import static org.peakemu.world.enums.Effect.DOMA_AIR;
import static org.peakemu.world.enums.Effect.DOMA_NEU;
import static org.peakemu.world.enums.Effect.HEAL;
import static org.peakemu.world.enums.Effect.RETURNS_DOMA;
import static org.peakemu.world.enums.Effect.RETURNS_SPELL;
import org.peakemu.world.enums.FighterState;
import org.peakemu.world.fight.effect.Buff;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
abstract public class Fighter implements Sprite{
    final private int id;
    private boolean _canPlay = false;
    private Fight _fight;
    private FightTeam _team;
    private MapCell cell;
    final private Map<Integer, Integer> _chatiValue = new TreeMap<Integer, Integer>();
    private int _orientation;
    private Fighter _invocator;
    public int _nbInvoc = 0;
    private int _PDVMAX;
    private int _PDV;
    private boolean _isDead;
    private int _gfxID;
    private Fighter _isHolding;
    private Fighter _holdedBy;
    private ArrayList<LaunchedSort> _launchedSort = new ArrayList<LaunchedSort>();
    private Fighter _oldCible = null;
    public boolean _hasLevelUp = false; // Regen auto en fin de combat
    private String _defenseurs = "";
    final private Map<Object, Object> attachments = new HashMap<>();
    
    final private Set<FighterState> states = new HashSet<>();
    
    private boolean hidden = false;
    
    final private Collection<Buff> buffs = new ArrayList<>();
    
    private int curPA;
    private int curPM;

    public Fighter(int id, int _PDVMAX, int _PDV, int _gfxID) {
        this.id = id;
        this._PDVMAX = _PDVMAX;
        this._PDV = _PDV;
        this._gfxID = _gfxID;
    }

    public void setDefenseurs(String str) {
        _defenseurs = str;
    }

    public String getDefenseurs() {
        return _defenseurs;
    }

    public Fighter get_oldCible() {
        return _oldCible;
    }

    public void set_oldCible(Fighter cible) {
        _oldCible = cible;
    }
    
    public void setFight(Fight fight){
        this._fight = fight;
    }

    public ArrayList<LaunchedSort> getLaunchedSorts() {
        return _launchedSort;
    }

    public void ActualiseLaunchedSort() {
        ArrayList<LaunchedSort> copie = new ArrayList<LaunchedSort>();
        copie.addAll(_launchedSort);
        int i = 0;
        for (LaunchedSort S : copie) {
            S.ActuCooldown();
            if (S.getCooldown() <= 0) {
                _launchedSort.remove(i);
                i--;
            }
            i++;
        }
    }

    public void addLaunchedSort(Fighter target, SpellLevel sort) {
        LaunchedSort launched = new LaunchedSort(target, sort);
        _launchedSort.add(launched);
    }

    @Override
    public int getSpriteId() {
        return id;
    }

    public Fighter get_isHolding() {
        return _isHolding;
    }

    public void set_isHolding(Fighter isHolding) {
        _isHolding = isHolding;
    }

    public Fighter get_holdedBy() {
        return _holdedBy;
    }

    public void set_holdedBy(Fighter holdedBy) {
        _holdedBy = holdedBy;
    }

    public int getGfxID() {
        return _gfxID;
    }

    public void setGfxID(int gfxID) {
        _gfxID = gfxID;
    }

    public void set_fightCell(MapCell cell) {
        this.cell = cell;
    }

    @Override
    public MapCell getCell() {
        return cell;
    }

    public void setTeam(FightTeam team) {
        this._team = team;
    }

    public boolean isDead() {
        return _isDead || getPDV() <= 0;
    }

    public void setDead(boolean isDead) {
        _isDead = isDead;
    }

    public Player getPlayer() {
        return null;
    }

    public Collector getCollector() {
        return null;
    }

    public Prism getPrism() {
        return null;
    }

    public boolean testIfCC(int tauxCC) {
        if (tauxCC < 2) {
            return false;
        }
        int agi = getTotalStats().getEffect(Effect.ADD_AGIL);
        if (agi < 0) {
            agi = 0;
        }
        tauxCC -= getTotalStats().getEffect(Effect.ADD_CC);
        tauxCC = (int) ((tauxCC * 2.9901) / Math.log(agi + 12));//Influence de l'agi
        if (tauxCC < 2) {
            tauxCC = 2;
        }
        int jet = Formulas.getRandomValue(1, tauxCC);
        return (jet == tauxCC);
    }
    
    abstract public Stats getBaseStats();

    public Stats getTotalStats() {
        Stats stats = new Stats(getBaseStats());
        stats.addAll(getFightBuffStats());
        return stats;
    }

    private Stats getFightBuffStats() {
        Stats stats = new Stats();
        
        for(Buff buff : buffs){
            if(buff.getValue() != 0)
                stats.addOneStat(buff.getSpellEffect().getEffect(), buff.getValue());
        }
        
        return stats;
    }
    
    protected StringBuilder prepareSpritePacket(){
        StringBuilder str = new StringBuilder();
        str.append(cell.getID()).append(";");
        _orientation = 1;
        str.append(_orientation).append(";");
        str.append("0;");
        str.append(getSpriteId()).append(";");
        str.append(getPacketsName()).append(";");
        return str;
    }

    public int getPDV() {
        int pdv = _PDV + getBuffValue(Effect.ADD_VITA);
        return pdv;
    }

    public void removePDV(int pdv, Fighter caster) {
        if(pdv > _PDV)
            pdv = _PDV;

        if(pdv < 0)
            pdv = 0;
        
        _PDV -= pdv;
        
        _fight.sendToFight(GameActionResponse.removeLifePoints(caster, this, pdv));
        
        if(_PDV <= 0){
            _fight.onFighterDie(this, caster);
        }
    }
    
    public void removePDV(int pdv){
        removePDV(pdv, this);
    }
    
    public Collection<Buff> getBuffsByEffect(Effect effect){
        Collection<Buff> ret = new ArrayList<>();
        
        for(Buff buff : buffs){
            if(buff.getSpellEffect().getEffect() == effect)
                ret.add(buff);
        }
        
        return ret;
    }

    public boolean hasBuff(Effect effect) {
        return false;
    }

    public int getBuffValue(Effect effect) {
        int value = 0;
        return value;
    }

    public int getMaitriseDmg(int id) {
        int value = 0;
        return value;
    }

    public boolean getSpellValueBool(int id) {
        return false;
    }

    public void refreshfightBuff() {
//        //Copie pour contrer les modifications Concurentes
//        ArrayList<SpellEffect> b = new ArrayList<SpellEffect>();
//        for (_SpellEffect entry : _fightBuffs) {
//            if (entry.decrementDuration() != 0)//Si pas fin du buff
//            {
//                b.add(entry);
//            } else {
//                if (Ancestra.CONFIG_DEBUG) {
//                    GameServer.addToLog("Suppression du buff " + entry.getEffect() + " sur le joueur Fighter ID= " + getSpriteId());
//                }
//                switch (entry.getEffect()) {
//                    case HEAL:
//                        if (entry.getSpell() == 441) {
//                            //Baisse des pdvs max
//                            _PDVMAX = (_PDVMAX - entry.getValue());
//
//                            //Baisse des pdvs actuel
//                            int pdv = 0;
//                            if (_PDV - entry.getValue() <= 0) {
//                                pdv = 0;
//                                _fight.onFighterDie(this, this);
//                                _fight.verifIfTeamAllDead();
//                            } else {
//                                pdv = (_PDV - entry.getValue());
//                            }
//                            _PDV = pdv;
//                        }
//                        break;
//
//                    case INVISIBILITY://Invisibilité
//                        _invisible = false; // Pour que mobs n'attaquent pas si invisible
//                        SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(_fight, 7, 150, entry.getCaster().getSpriteId() + "", getSpriteId() + ",0");
//                        break;
//
//                    case ADD_STATE:
//                        String args = entry.getArgs();
//                        int id = -1;
//                        try {
//                            id = Integer.parseInt(args.split(";")[2]);
//                        } catch (Exception e) {
//                        }
//                        if (id == -1) {
//                            return;
//                        }
//                        setState(id, 0);
//                        SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(_fight, 7, 950, entry.getCaster().getSpriteId() + "", entry.getCaster().getSpriteId() + "," + id + ",0");
//                        break;
//                }
//            }
//        }
//        _fightBuffs.clear();
//        _fightBuffs.addAll(b);
    }

    public void addBuff(Effect effect, int val, int duration, int turns, boolean debuff, int spellID, String args, Fighter caster) {
        try {

            if (getMonster().getTemplate().getID() == 282 || getMonster().getTemplate().getID() == 556) {
                return;
            }
        } catch (Exception e) {
        }
        if (spellID == 99 || //Trêve
            spellID == 5 || // Immu
            spellID == 20 || // Prévention
            spellID == 127 || //Momif
            spellID == 89 || // Dévouement
            spellID == 126 || //Mot stimu
            spellID == 115 || // Odorat
            spellID == 192 || //ronce apaisante
            spellID == 4 || // Renvoi de sort
            spellID == 1 || // Armure incandescente
            spellID == 6 || //Armure terrestre
            spellID == 14 || //Armrue venteuse
            spellID == 18 || //Armrue aqueuse
            spellID == 7 || // Boucleir feca
            spellID == 284 || // Accélération poupesque
            spellID == 197 || //Puissance sylvestre
            spellID == 704 || // Pandanlku
            spellID == 168 || // Oeil de taupe
            spellID == 45 || // Clé reductrice
            spellID == 159 || //Colère de iop
            spellID == 171 || // punitive
            spellID == 167 || // Expiation
            spellID == 72 || //Invisibilité
            spellID == 22 || //Déplacement du felin
            spellID == 27 || //Piqure motivante
            spellID == 32 || // Résistance naturelle 
            spellID == 28 || // Crapaud
            spellID == 155 || //Vitalité
            spellID == 47 ||// Boite de pandore
            spellID == 82 || // contre
            spellID == 94 ||// Protection aveuglante
            spellID == 228 //Etourderie
            ) {
            debuff = true;
        }
        //Si c'est le jouer actif qui s'autoBuff, on ajoute 1 a la durée
//        _fightBuffs.add(new _SpellEffect(effect, val, (_canPlay ? duration + 1 : duration), turns, debuff, caster, args, spellID));
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToLog("Ajout du Buff " + effect + " sur le personnage Fighter ID = " + this.getSpriteId() + " val : " + val + " duration : " + duration + " turns : " + turns + " debuff : " + debuff + " spellid : " + spellID + " args : " + args);
        }

        switch (effect) {
            case RETURNS_SPELL://Renvoie de sort
                SocketManager.GAME_SEND_FIGHT_GIE_TO_FIGHT(_fight, 7, effect.getId(), getSpriteId(), -1, val + "", "10", "", duration, spellID);
                break;

            case CHANCE_ECA://Chance éca
                val = Integer.parseInt(args.split(";")[0]);
                String valMax = args.split(";")[1];
                String chance = args.split(";")[2];
                SocketManager.GAME_SEND_FIGHT_GIE_TO_FIGHT(_fight, 7, effect.getId(), getSpriteId(), val, valMax, chance, "", duration, spellID);
                break;

            case CHATIMENT://Fait apparaitre message le temps de buff sacri Chatiment de X sur Y tours
                val = Integer.parseInt(args.split(";")[1]);
                String valMax2 = args.split(";")[2];
                if (Integer.parseInt(args.split(";")[0]) == 108) {
                    return;
                }
                SocketManager.GAME_SEND_FIGHT_GIE_TO_FIGHT(_fight, 7, effect.getId(), getSpriteId(), val, "" + val, "" + valMax2, "", duration, spellID);

                break;

            case DOMA_AIR://Poison insidieux
            case RETURNS_DOMA://Mot d'épine (2à3), Contre(3)
            case DOMA_NEU://Flèche Empoisonnée, Tout ou rien
            case HEAL://Mot de Régénération, Tout ou rien
            case ADD_MAITRISE://Maîtrises
                val = Integer.parseInt(args.split(";")[0]);
                String valMax1 = args.split(";")[1];
                if (valMax1.compareTo("-1") == 0 || spellID == 82 || spellID == 94) {
                    SocketManager.GAME_SEND_FIGHT_GIE_TO_FIGHT(_fight, 7, effect.getId(), getSpriteId(), val, "", "", "", duration, spellID);
                } else if (valMax1.compareTo("-1") != 0) {
                    SocketManager.GAME_SEND_FIGHT_GIE_TO_FIGHT(_fight, 7, effect.getId(), getSpriteId(), val, valMax1, "", "", duration, spellID);
                }
                break;

            default:
                SocketManager.GAME_SEND_FIGHT_GIE_TO_FIGHT(_fight, 7, effect.getId(), getSpriteId(), val, "", "", "", duration, spellID);
                break;
        }
    }

    abstract public int getInitiative();

    public int getPDVMAX() {
        return _PDVMAX + getBuffValue(Effect.ADD_VITA);
    }

    abstract public int get_lvl();

    abstract public String xpString(String str);

    abstract public String getPacketsName();

    public Monster getMonster() {
        return null;
    }

    public FightTeam getTeam() {
        return _team;
    }

    public boolean canPlay() {
        return _canPlay;
    }

    public void setCanPlay(boolean b) {
        _canPlay = b;
    }

    public int getPA() {
        return getTotalStats().getEffect(Effect.ADD_PA);
    }

    public int getPM() {
        return getTotalStats().getEffect(Effect.ADD_PM);
    }

    public int getCurPA() {
        return curPA;
    }

    public int getCurPM() {
        return curPM;
    }

    public void setCurPM(int pm) {
        curPM = pm;
    }
    
    public void removeCurPM(int nb){
        curPM -= nb;
    }

    public void setCurPA(int pa) {
        curPA = pa;
    }
    
    public void removeCurPA(int nb){
        curPA -= nb;
    }

    public void setInvocator(Fighter caster) {
        _invocator = caster;
    }

    public Fighter getInvocator() {
        return _invocator;
    }

    public boolean isInvocation() {
        return (_invocator != null);
    }

    public boolean isPerco() {
        return (getCollector() != null);
    }

    public boolean isDouble() {
        return false;
    }

    public boolean isPrisme() {
        return (getPrism() != null);
    }
    
    public Collection<Buff> removeBuffsByCaster(Fighter caster){
        Collection<Buff> removed = new ArrayList<>();
        
        Iterator<Buff> it = buffs.iterator();
        
        while(it.hasNext()){
            Buff buff = it.next();
            
            if(buff.getCaster().equals(caster)){
                it.remove();
                removed.add(buff);
            }
        }
        
        return removed;
    }
    
    public Collection<Buff> refreshBuffs(){
        Collection<Buff> removed = new ArrayList<>();
        
        Iterator<Buff> it = buffs.iterator();
        
        while(it.hasNext()){
            Buff buff = it.next();
            buff.decrementTruns();
            
            if(!buff.isValid()){
                it.remove();
                removed.add(buff);
            }
        }
        
        return removed;
    }

    public void fullPDV() {
        _PDV = _PDVMAX;
    }

    public void setIsDead(boolean b) {
        _isDead = b;
    }
    
    public boolean isReallyDead(){
        return _isDead;
    }

    abstract public int getPdvMaxOutFight();

    public Map<Integer, Integer> get_chatiValue() {
        return _chatiValue;
    }

    abstract public int getDefaultGfx();

    abstract public long getXpGive();

    public void addPDV(int max, boolean growMax) {
        if (growMax) {
            _PDVMAX = (_PDVMAX + max);
        }
        _PDV = (_PDV + max);
    }
    
    public void addLifePoints(int nb, Fighter caster){
        if(nb < 0)
            return;
        
        if(nb + _PDV > _PDVMAX)
            nb = _PDVMAX - _PDV;
        
        _PDV += nb;
        
        _fight.sendToFight(GameActionResponse.addLifePoints(caster, this, nb));
    }

    public Fight getFight() {
        return _fight;
    }

    public Collection<Buff> getBuffs() {
        return buffs;
    }
    
    public void addBuff(Buff buff){
        buffs.add(buff);
        _fight.sendToFight(new AddFightBuff(buff));
        
        if(canPlay()){
            buff.incrementTurns();
        }
    }
    
    abstract public boolean isReady();

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
    
    public void addState(FighterState state, Fighter caster){
        if(hasState(state))
            return;
        
        states.add(state);
        _fight.sendToFight(GameActionResponse.alterState(caster, this, state, true));
    }
    
    public void addState(FighterState state){
        addState(state, this);
    }
    
    public boolean hasState(FighterState state){
        return states.contains(state);
    }
    
    public boolean hasAllStats(Collection<FighterState> states){
        return this.states.containsAll(states);
    }
    
    public void removeState(FighterState state){
        removeState(state, this);
    }
    
    public void removeState(FighterState state, Fighter caster){
        if(!hasState(state))
            return;
        
        states.remove(state);
        _fight.sendToFight(GameActionResponse.alterState(caster, this, state, false));
    }
    
    public void attach(Object key, Object value){
        attachments.put(key, value);
    }
    
    public Object detach(Object key){
        return attachments.remove(key);
    }
    
    public Object getAttachment(Object key){
        return attachments.get(key);
    }
}
