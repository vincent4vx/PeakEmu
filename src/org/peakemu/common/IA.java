package org.peakemu.common;

import org.peakemu.maputil.OldPathfinding;
import org.peakemu.Ancestra;
import org.peakemu.world.fight.Fight;
import org.peakemu.game.GameServer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import org.peakemu.Peak;
import org.peakemu.maputil.CellChecker;
import org.peakemu.maputil.PartialPathException;
import org.peakemu.maputil.Pathfinding;






import org.peakemu.world.MapCell;
import org.peakemu.world.Spell;
import org.peakemu.world.SpellEffect;
import org.peakemu.world.SpellLevel;
import org.peakemu.world.enums.Effect;
import org.peakemu.world.fight.fighter.Fighter;
import org.peakemu.world.fight.LaunchedSort;

public class IA {

    public static class IAThread implements Runnable {

        private Fight _fight;
        private Fighter _fighter;
        private static boolean stop = false;
        private Thread _t;

        public IAThread(Fighter fighter, Fight fight) {
            _fighter = fighter;
            _fight = fight;
            _t = new Thread(this);
            _t.setDaemon(true);
            _t.start();
        }

        public Thread getThread() {
            return _t;
        }

        public void run() {
            stop = false;
            if (_fighter.getMonster() == null) {
                if (_fighter.isDouble()) {
                    try {
                        apply_type5(_fighter, _fight);
                    } catch (Exception localException) {
                    }
                    try {
                        Thread.sleep(2000L);
                    } catch (InterruptedException localInterruptedException) {
                    }
                    _fight.endTurn();
                } else if (_fighter.isPerco()) {
                    apply_typePerco(_fighter, _fight);
                    try {
                        Thread.sleep(2000L);
                    } catch (InterruptedException localInterruptedException1) {
                    }
                    _fight.endTurn();
                } else {
                    try {
                        Thread.sleep(2000L);
                    } catch (InterruptedException localInterruptedException2) {
                    }
                    _fight.endTurn();
                }
            } else if (_fighter.getMonster().getTemplate() == null) {
                _fight.endTurn();
            } else {
                int IA_Type = _fighter.getMonster().getTemplate().getIAType();
                switch (IA_Type) {
                    case 0:
                        apply_type0(_fighter, _fight);
                        break;
                    case 1:
                        try {
                            apply_type1(_fighter, _fight);
                        } catch (Exception localException1) {
                        }
                    case 2:
                        try {
                            apply_type2(_fighter, _fight);
                        } catch (Exception localException2) {
                        }
                    case 3:
                        try {
                            apply_type3(_fighter, _fight);
                        } catch (Exception localException3) {
                        }
                    case 4:
                        try {
                            apply_type4(_fighter, _fight);
                        } catch (Exception localException4) {
                        }
                    case 5:
                        try {
                            apply_type5(_fighter, _fight);
                        } catch (Exception localException5) {
                        }
                    case 6:
                        try {
                            apply_type6(_fighter, _fight);
                        } catch (Exception localException6) {
                        }
                    case 7:
                        try {
                            apply_type7(_fighter, _fight);
                        } catch (Exception e) {
                        }
                    case 8:
                        try {
                            apply_type8(_fighter, _fight);
                        } catch (Exception e) {
                        }
                    default: //si type d'IA introuvable, on utilisise l'IA par défaut
                        try {
                            apply_type1(_fighter, _fight);
                        } catch (Exception e) {
                        }
                }
                try {
                    if (IA_Type == 0) {
                        Thread.sleep(250L);
                    } else {
                        Thread.sleep(2000L);
                    }
                } catch (InterruptedException localInterruptedException3) {
                }
                if (!_fighter.isDead()) {
                    _fight.endTurn();
                }
            }
        }

        private static void apply_type0(Fighter F, Fight fight) {
            stop = true;
        }

        //IA agressive : normale (bouftou)
        private static void apply_type1(Fighter F, Fight fight) {
            while (!stop && F.canPlay()) {
                //int PDVPER = (F.getPDV() * 100) / F.getPDVMAX();
                Fighter T = getNearestEnnemy(fight, F); // Ennemis
                Fighter T2 = getNearestFriend(fight, F); // Amis
                /*if (T == null) {
                    return;
                }*/
                //if (PDVPER > 15) {
                    int attack = attackIfPossible(fight, F);
                    if (attack != 0)//Attaque
                    {
                        if (attack == 5) {
                            stop = true;//EC
                            break;
                        }
                        if (!moveToAttackIfPossible(fight, F)) {
                            if (!buffIfPossible(fight, F, F))//auto-buff
                            {
                                if (!HealIfPossible(fight, F, false))//soin alli�
                                {
                                    if (!buffIfPossible(fight, F, T2))//buff alli�
                                    {
                                            if (T == null || !moveNearIfPossible(fight, F, T))//avancer
                                            {
                                                if (!invocIfPossible(fight, F))//invoquer
                                                {
                                                    stop = true;
                                                    break;
                                                }
                                            }
                                    }
                                }
                            }
                        }
                    }
                /*} else {
                    if (!HealIfPossible(fight, F, true))//auto-soin
                    {

                        int attack = attackIfPossible(fight, F);
                        if (attack != 0)//Attaque
                        {
                            if (attack == 5) {
                                stop = true;//EC
                                break;
                            }
                            if (!moveToAttackIfPossible(fight, F)) {
                                attack = attackIfPossible(fight, F);
                                if (attack != 0) { //retente l'attaque
                                    if (attack == 5) {
                                        stop = true; //EC
                                        break;
                                    }
                                    if (!buffIfPossible(fight, F, F))//auto-buff
                                    {
                                        if (!HealIfPossible(fight, F, false))//soin alli�
                                        {
                                            if (!buffIfPossible(fight, F, T2))//buff alli�
                                            {
                                                if (!invocIfPossible(fight, F)) {
                                                    if (!moveFarIfPossible(fight, F))//fuite
                                                    {
                                                        stop = true;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }*/
            }
        }

        //IA coffre animé
        private static void apply_type2(Fighter F, Fight fight) {
            while (!stop && F.canPlay()) {
                Fighter T = getNearestFriend(fight, F);
                if (!HealIfPossible(fight, F, false))//soin alli�
                {
                    if (!buffIfPossible(fight, F, T))//buff alli�
                    {
                        if (!moveNearIfPossible(fight, F, T))//Avancer vers alli�
                        {
                            if (!HealIfPossible(fight, F, true))//auto-soin
                            {
                                if (!buffIfPossible(fight, F, F))//auto-buff
                                {
                                    if (!invocIfPossible(fight, F)) {
                                        T = getNearestEnnemy(fight, F);
                                        int attack = attackIfPossible(fight, F);
                                        if (attack != 0)//Attaque
                                        {
                                            if (attack == 5) {
                                                stop = true;//EC
                                                break;
                                            }
                                            if (!moveFarIfPossible(fight, F))//fuite
                                            {
                                                stop = true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //IA invocations
        private static void apply_type3(Fighter F, Fight fight) {
            while (!stop && F.canPlay()) {
                Fighter T = getNearestFriend(fight, F);
                if (!moveNearIfPossible(fight, F, T))//Avancer vers alli�
                {
                    if (!HealIfPossible(fight, F, false))//soin alli�
                    {
                        if (!buffIfPossible(fight, F, T))//buff alli�
                        {
                            if (!HealIfPossible(fight, F, true))//auto-soin
                            {
                                if (!invocIfPossible(fight, F)) {
                                    if (!buffIfPossible(fight, F, F))//auto-buff
                                    {
                                        stop = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        private static void apply_type4(Fighter F, Fight fight) //IA propre La Folle
        {
            while (!stop && F.canPlay()) {
                Fighter T = getNearestEnnemy(fight, F);
                if (T == null) {
                    return;
                }
                int attack = attackIfPossible(fight, F);
                if (attack != 0)//Attaque
                {
                    if (attack == 5) {
                        stop = true;//EC
                        break;
                    }
                    if (!moveFarIfPossible(fight, F))//fuite
                    {
                        if (!HealIfPossible(fight, F, false))//soin alli�
                        {
                            if (!buffIfPossible(fight, F, T))//buff alli�
                            {
                                if (!HealIfPossible(fight, F, true))//auto-soin
                                {
                                    if (!invocIfPossible(fight, F)) {
                                        if (!buffIfPossible(fight, F, F))//auto-buff
                                        {
                                            stop = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        private static void apply_type5(Fighter F, Fight fight) //IA propre aux bloqueurs (bloqueuse + double)
        {
            while (!stop && F.canPlay()) {
                Fighter T = getNearestEnnemy(fight, F);
                if (T == null) {
                    return;
                }

                if (!moveNearIfPossible(fight, F, T))//Avancer vers enemis
                {
                    stop = true;
                }
            }
        }

        private static void apply_type6(Fighter F, Fight fight) {
            while (!stop && F.canPlay()) {
                if (!invocIfPossible(fight, F)) {
                    Fighter T = getNearestFriend(fight, F);
                    if (!HealIfPossible(fight, F, false))//soin alli�
                    {
                        if (!buffIfPossible(fight, F, T))//buff alli�
                        {
                            if (!buffIfPossible(fight, F, F))//buff alli�
                            {
                                if (!HealIfPossible(fight, F, true)) {
                                    int attack = attackIfPossible(fight, F);
                                    if (attack != 0)//Attaque
                                    {
                                        if (attack == 5) {
                                            stop = true;//EC
                                            break;
                                        }
                                        if (!moveFarIfPossible(fight, F))//fuite
                                        {
                                            stop = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //IA sac animé
        private static void apply_type7(Fighter F, Fight fight) {
            while (!stop && F.canPlay()) {
                Fighter T = getNearestFriend(fight, F);
                if (!buffIfPossible(fight, F, T)) {
                    if (!moveNearIfPossible(fight, F, T)) {
                        stop = true;
                    }
                }
            }
        }

        //IA fuyarde : exemple tofu
        private static void apply_type8(Fighter F, Fight fight) {
            int PDVPER = (F.getPDV() * 100) / F.getPDVMAX();
            int attack;
            while(!stop && F.canPlay()){
                attack = attackIfPossible(fight, F);//attaque
                if(attack != 0){
                    if(attack == 5){
                        stop = true;
                        break;
                    }
                    if(PDVPER > 15){
                        if(!moveToAttackIfPossible(fight, F)){
                            attack = attackIfPossible(fight, F); //retente l'attaque
                            if(attack != 0){
                                if(attack == 5){
                                    stop = true;
                                    break;
                                }
                                if(!moveFarIfPossible(fight, F)){ //fuit
                                    if(!buffIfPossible(fight, F, F)){ //auto buff
                                        if(!HealIfPossible(fight, F, true)){ //auto soin
                                            stop = true;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }else{
                        if (!HealIfPossible(fight, F, true)) { //se soigne
                            if(!buffIfPossible(fight, F, F)){ //auto buff
                                if(!moveFarIfPossible(fight, F)){ //fuit
                                    stop = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        private static void apply_typePerco(Fighter F, Fight fight) {
            try {
                int noBoucle = 0;
                do {
                    noBoucle++;
                    if (noBoucle >= 12) {
                        stop = true;
                    }
                    if (noBoucle > 15) {
                        return;
                    }
                    Fighter T = getNearestEnnemy(fight, F);
                    if (T == null) {
                        return;
                    }
                    int attack = attackIfPossiblePerco(fight, F);
                    if (attack != 0) {
                        if (attack == 5) {
                            stop = true;
                        }
                        if (!moveFarIfPossible(fight, F)) {
                            if (!HealIfPossiblePerco(fight, F, false)) {
                                if (!buffIfPossiblePerco(fight, F, T)) {
                                    if (!HealIfPossiblePerco(fight, F, true)) {
                                        if (!buffIfPossiblePerco(fight, F, F)) {
                                            stop = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (stop) {
                        break;
                    }
                } while (F.canPlay());
            } catch (Exception e) {
                return;
            }
        }

        private static boolean moveFarIfPossible(Fight fight, Fighter F) {
            int dist[] = {1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000}, cell[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            for (int i = 0; i < 10; i++) {
                for (Fighter f : fight.getFighters(3)) {

                    if (f.isDead()) {
                        continue;
                    }
                    if (f == F || f.getTeam() == F.getTeam()) {
                        continue;
                    }
                    int cellf = f.getCell().getID();
                    if (cellf == cell[0] || cellf == cell[1] || cellf == cell[2] || cellf == cell[3] || cellf == cell[4] || cellf == cell[5] || cellf == cell[6] || cellf == cell[7] || cellf == cell[8] || cellf == cell[9]) {
                        continue;
                    }
                    int d = 0;
                    d = OldPathfinding.getDistanceBetween(fight.get_map(), F.getCell().getID(), f.getCell().getID());
                    if (d == 0) {
                        continue;
                    }
                    if (d < dist[i]) {
                        dist[i] = d;
                        cell[i] = cellf;
                    }
                    if (dist[i] == 1000) {
                        dist[i] = 0;
                        cell[i] = F.getCell().getID();
                    }
                }
            }
            if (dist[0] == 0) {
                return false;
            }
            int dist2[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            int PM = F.getCurPM(), caseDepart = F.getCell().getID(), destCase = F.getCell().getID();
            for (int i = 0; i <= PM; i++) {
                if (destCase > 0) {
                    caseDepart = destCase;
                }
                int curCase = caseDepart;
                curCase += 15;
                int infl = 0, inflF = 0;
                for (int a = 0; a < 10 && dist[a] != 0; a++) {
                    dist2[a] = OldPathfinding.getDistanceBetween(fight.get_map(), curCase, cell[a]);
                    if (dist2[a] > dist[a]) {
                        infl++;
                    }
                }

                if (infl > inflF && curCase > 0 && curCase < 478 && testCotes(destCase, curCase)) {
                    inflF = infl;
                    destCase = curCase;
                }

                curCase = caseDepart + 14;
                infl = 0;

                for (int a = 0; a < 10 && dist[a] != 0; a++) {
                    dist2[a] = OldPathfinding.getDistanceBetween(fight.get_map(), curCase, cell[a]);
                    if (dist2[a] > dist[a]) {
                        infl++;
                    }
                }

                if (infl > inflF && curCase > 0 && curCase < 478 && testCotes(destCase, curCase)) {
                    inflF = infl;
                    destCase = curCase;
                }

                curCase = caseDepart - 15;
                infl = 0;
                for (int a = 0; a < 10 && dist[a] != 0; a++) {
                    dist2[a] = OldPathfinding.getDistanceBetween(fight.get_map(), curCase, cell[a]);
                    if (dist2[a] > dist[a]) {
                        infl++;
                    }
                }

                if (infl > inflF && curCase > 0 && curCase < 478 && testCotes(destCase, curCase)) {
                    inflF = infl;
                    destCase = curCase;
                }

                curCase = caseDepart - 14;
                infl = 0;
                for (int a = 0; a < 10 && dist[a] != 0; a++) {
                    dist2[a] = OldPathfinding.getDistanceBetween(fight.get_map(), curCase, cell[a]);
                    if (dist2[a] > dist[a]) {
                        infl++;
                    }
                }

                if (infl > inflF && curCase > 0 && curCase < 478 && testCotes(destCase, curCase)) {
                    inflF = infl;
                    destCase = curCase;
                }
            }
            //Ancestra.printDebug("Test MOVEFAR : cell = " + destCase);
            if (destCase < 0 || destCase > 478 || destCase == F.getCell().getID() || !fight.get_map().getCell(destCase).isWalkable(false)) {
                return false;
            }
            if (F.getPM() <= 0) {
                return false;
            }
            List<MapCell> path;// = OldPathfinding.getShortestPathBetween(fight.get_map(), F.getCell().getID(), destCase, 0);
            
            try{
                path = Pathfinding.findPath(fight.get_map(), F.getCell(), fight.get_map().getCell(destCase), false, CellChecker.forFightPathing(fight));
            }catch(Exception e){
                Peak.errorLog.addToLog(e);
                return false;
            }
            
            if (path == null) {
                return false;
            }

            // DEBUG PATHFINDING
			/*Ancestra.printDebug("DEBUG PATHFINDING:");
             Ancestra.printDebug("startCell: "+F.getCell().getID());
             Ancestra.printDebug("destinationCell: "+cellID);
			
             for(Case c : path)
             {
             Ancestra.printDebug("Passage par cellID: "+c.getID()+" walk: "+c.isWalkable(true));
             }*/

            ArrayList<MapCell> finalPath = new ArrayList<>();
            for (int a = 0; a < F.getPM() + 1; a++) {
                if (path.size() == a) {
                    break;
                }
                finalPath.add(path.get(a));
            }
            
            boolean result = fight.fighterDeplace(F, finalPath);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
            return result;

        }

        private static boolean testCotes(int cell1, int cell2) {
            if (cell1 == 15 || cell1 == 44 || cell1 == 73 || cell1 == 102 || cell1 == 131 || cell1 == 160 || cell1 == 189 || cell1 == 218 || cell1 == 247 || cell1 == 276 || cell1 == 305 || cell1 == 334 || cell1 == 363 || cell1 == 392 || cell1 == 421 || cell1 == 450) {
                if (cell2 == cell1 + 14 || cell2 == cell1 - 15) {
                    return false;
                }
            }
            if (cell1 == 28 || cell1 == 57 || cell1 == 86 || cell1 == 115 || cell1 == 144 || cell1 == 173 || cell1 == 202 || cell1 == 231 || cell1 == 260 || cell1 == 289 || cell1 == 318 || cell1 == 347 || cell1 == 376 || cell1 == 405 || cell1 == 434 || cell1 == 463) {
                if (cell2 == cell1 + 15 || cell2 == cell1 - 14) {
                    return false;
                }
            }
            return true;
        }

        private static boolean invocIfPossible(Fight fight, Fighter fighter) {
            Fighter nearest = getNearestEnnemy(fight, fighter);
            if (nearest == null) {
                return false;
            }
            int nearestCell = OldPathfinding.getNearestCellAround(fight.get_map(), fighter.getCell().getID(), nearest.getCell().getID(), null);
            if (nearestCell == -1) {
                return false;
            }
            SpellLevel spell = getInvocSpell(fight, fighter, nearestCell);
            if (spell == null) {
                return false;
            }
            int invoc = fight.tryCastSpell(fighter, spell, nearestCell);
            if (invoc != 0) {
                return false;
            }

            return true;
        }

        private static SpellLevel getInvocSpell(Fight fight, Fighter fighter, int nearestCell) {
            if (fighter.getMonster() == null) {
                return null;
            }
            for (Entry<Integer, SpellLevel> SS : fighter.getMonster().getSpells().entrySet()) {
                if (!fight.canCastSpell(fighter, SS.getValue(), fight.get_map().getCell(nearestCell), -1)) {
                    continue;
                }
                for (SpellEffect SE : SS.getValue().getSpellEffects()) {
                    if (SE.getEffect() == Effect.INVOC) {
                        return SS.getValue();
                    }
                }
            }
            return null;
        }

        private static boolean HealIfPossible(Fight fight, Fighter f, boolean autoSoin)//boolean pour choisir entre auto-soin ou soin alli�
        {
            if (autoSoin && (f.getPDV() * 100) / f.getPDVMAX() > 85) { //inutile avant 85%
                return false;
            }
            Fighter target = null;
            SpellLevel SS = null;
            if (autoSoin) {
                target = f;
                SS = getHealSpell(fight, f, target);
            } else//s�lection joueur ayant le moins de pv
            {
                Fighter curF = null;
                int PDVPERmin = 100;
                SpellLevel curSS = null;
                for (Fighter F : fight.getFighters(3)) {
                    if (f.isDead()) {
                        continue;
                    }
                    if (F == f) {
                        continue;
                    }
                    if (F.getTeam() == f.getTeam()) {
                        int PDVPER = (F.getPDV() * 100) / F.getPDVMAX();
                        if (PDVPER < PDVPERmin && PDVPER < 95) {
                            int infl = 0;
                            for (Entry<Integer, SpellLevel> ss : f.getMonster().getSpells().entrySet()) {
                                if (infl < calculInfluenceHeal(ss.getValue()) && calculInfluenceHeal(ss.getValue()) != 0 && fight.canCastSpell(f, ss.getValue(), F.getCell(), -1))//Si le sort est plus interessant
                                {
                                    infl = calculInfluenceHeal(ss.getValue());
                                    curSS = ss.getValue();
                                }
                            }
                            if (curSS != SS && curSS != null) {
                                curF = F;
                                SS = curSS;
                                PDVPERmin = PDVPER;
                            }
                        }
                    }
                }
                target = curF;
            }
            if (target == null) {
                return false;
            }
            if (SS == null) {
                return false;
            }
            int heal = fight.tryCastSpell(f, SS, target.getCell().getID());
            if (heal != 0) {
                return false;
            }

            return true;
        }

        @SuppressWarnings("rawtypes")
        private static boolean HealIfPossiblePerco(Fight fight, Fighter f, boolean autoSoin) {
            if ((autoSoin) && (f.getPDV() * 100 / f.getPDVMAX() > 95)) {
                return false;
            }
            Fighter target = null;
            SpellLevel SS = null;
            if (autoSoin) {
                target = f;
                SS = getHealSpell(fight, f, target);
            } else {
                Fighter curF = null;
                int PDVPERmin = 100;
                SpellLevel curSS = null;
                for (Fighter F : fight.getFighters(3)) {
                    if ((f.isDead())
                            || (F == f)
                            || (F.getTeam() != f.getTeam())) {
                        continue;
                    }
                    int PDVPER = F.getPDV() * 100 / F.getPDVMAX();
                    if ((PDVPER >= PDVPERmin) || (PDVPER >= 95)) {
                        continue;
                    }
                    int infl = 0;
                    for (Map.Entry ss : F.getCollector().getGuild().getSpells().entrySet()) {
                        if ((ss.getValue() == null)
                                || (infl >= calculInfluenceHeal((SpellLevel) ss.getValue())) || (calculInfluenceHeal((SpellLevel) ss.getValue()) == 0) || (!fight.canCastSpell(f, (SpellLevel) ss.getValue(), F.getCell(), infl))) {
                            continue;
                        }
                        infl = calculInfluenceHeal((SpellLevel) ss.getValue());
                        curSS = (SpellLevel) ss.getValue();
                    }

                    if ((curSS == SS) || (curSS == null)) {
                        continue;
                    }
                    curF = F;
                    SS = curSS;
                    PDVPERmin = PDVPER;
                }

                target = curF;
            }
            if (target == null) {
                return false;
            }
            if (SS == null) {
                return false;
            }
            int heal = fight.tryCastSpell(f, SS, target.getCell().getID());

            return heal == 0;
        }

        private static boolean buffIfPossible(Fight fight, Fighter fighter, Fighter target) {
            if (target == null) {
                return false;
            }
            SpellLevel SS = getBuffSpell(fight, fighter, target);
            if (SS == null) {
                return false;
            }
            int buff = fight.tryCastSpell(fighter, SS, target.getCell().getID());
            if (buff != 0) {
                return false;
            }

            return true;
        }

        private static boolean buffIfPossiblePerco(Fight fight, Fighter fighter, Fighter target) {
            if (target == null) {
                return false;
            }
            SpellLevel SS = getBuffSpellPerco(fight, fighter, target);
            if (SS == null) {
                return false;
            }
            int buff = fight.tryCastSpell(fighter, SS, target.getCell().getID());
            return buff == 0;
        }

        private static SpellLevel getBuffSpell(Fight fight, Fighter F, Fighter T) {
            int infl = 0;
            SpellLevel ss = null;
            for (Entry<Integer, SpellLevel> SS : F.getMonster().getSpells().entrySet()) {
                if (infl < calculInfluence(fight, SS.getValue(), F, T) && calculInfluence(fight, SS.getValue(), F, T) > 0 && fight.canCastSpell(F, SS.getValue(), T.getCell(), -1))//Si le sort est plus interessant
                {
                    infl = calculInfluence(fight, SS.getValue(), F, T);
                    ss = SS.getValue();
                }
            }
            return ss;
        }

        @SuppressWarnings("rawtypes")
        private static SpellLevel getBuffSpellPerco(Fight fight, Fighter F, Fighter T) {
            int infl = 0;
            SpellLevel ss = null;
            for (Map.Entry SS : F.getCollector().getGuild().getSpells().entrySet()) {
                if ((SS.getValue() == null)
                        || (infl >= calculInfluence((SpellLevel) SS.getValue(), F, T)) || (calculInfluence((SpellLevel) SS.getValue(), F, T) <= 0) || (!fight.canCastSpell(F, (SpellLevel) SS.getValue(), T.getCell(), infl))) {
                    continue;
                }
                infl = calculInfluence((SpellLevel) SS.getValue(), F, T);
                ss = (SpellLevel) SS.getValue();
            }

            return ss;
        }

        private static SpellLevel getHealSpell(Fight fight, Fighter F, Fighter T) {
            int infl = 0;
            SpellLevel ss = null;
            for (Entry<Integer, SpellLevel> SS : F.getMonster().getSpells().entrySet()) {
                if (infl < calculInfluenceHeal(SS.getValue()) && calculInfluenceHeal(SS.getValue()) != 0 && fight.canCastSpell(F, SS.getValue(), T.getCell(), -1))//Si le sort est plus interessant
                {
                    infl = calculInfluenceHeal(SS.getValue());
                    ss = SS.getValue();
                }
            }
            return ss;
        }

        private static boolean moveNearIfPossible(Fight fight, Fighter F, Fighter T) {
            if (F.getCurPM() <= 0) {
                return false;
            }
            if (OldPathfinding.isNextTo(F.getCell().getID(), T.getCell().getID())) {
                return false;
            }

            if (Ancestra.CONFIG_DEBUG) {
                GameServer.addToLog("Tentative d'approche par " + F.getPacketsName() + " de " + T.getPacketsName());
            }

            int cellID = OldPathfinding.getNearestCellAround(fight.get_map(), T.getCell().getID(), F.getCell().getID(), null);
            //On demande le chemin plus court
            if (cellID == -1) {
                Map<Integer, Fighter> ennemys = getLowHpEnnemyList(fight, F);
                if (ennemys == null) {
                    return false;
                }
                if (ennemys.isEmpty()) {
                    return false;
                }
                for (Entry<Integer, Fighter> target : ennemys.entrySet()) {
                    int cellID2 = OldPathfinding.getNearestCellAround(fight.get_map(), target.getValue().getCell().getID(), F.getCell().getID(), null);
                    if (cellID2 != -1) {
                        cellID = cellID2;
                        break;
                    }
                }
            }
            
            List<MapCell> path;//OldPathfinding.getShortestPathBetween(fight.get_map(), F.getCell().getID(), cellID, 0);
            
            try{
                path = Pathfinding.findPath(fight.get_map(), F.getCell(), fight.get_map().getCell(cellID), false, CellChecker.forFightPathing(fight));
            }catch(PartialPathException e){
                Peak.worldLog.addToLog(Logger.Level.DEBUG, "Partial path found");
                path = e.getPartialPath();
            }catch(Exception e){
                Peak.errorLog.addToLog(e);
                return false;
            }
            
            if (path == null || path.isEmpty()) {
                return false;
            }
            // DEBUG PATHFINDING
			/*Ancestra.printDebug("DEBUG PATHFINDING:");
             Ancestra.printDebug("startCell: "+F.getCell().getID());
             Ancestra.printDebug("destinationCell: "+cellID);
			
             for(Case c : path)
             {
             Ancestra.printDebug("Passage par cellID: "+c.getID()+" walk: "+c.isWalkable(true));
             }*/

            ArrayList<MapCell> finalPath = new ArrayList<>();
            for (int a = 0; a < F.getCurPM() + 1; a++) {
                if (path.size() == a) {
                    break;
                }
                finalPath.add(path.get(a));
            }
            boolean result = fight.fighterDeplace(F, finalPath);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
            return result;
        }

        private static Fighter getNearestFriend(Fight fight, Fighter fighter) {
            int dist = 1000;
            Fighter curF = null;
            for (Fighter f : fight.getFighters(3)) {
                if (f.isDead() || f.isHidden()) { //invisible
                    continue;
                }
                if (f == fighter) {
                    continue;
                }
                if (f.getTeam() == fighter.getTeam())//Si c'est un ami
                {
                    int d = OldPathfinding.getDistanceBetween(fight.get_map(), fighter.getCell().getID(), f.getCell().getID());
                    if (d < dist) {
                        dist = d;
                        curF = f;
                    }
                }
            }
            return curF;
        }

        private static Fighter getNearestEnnemy(Fight fight, Fighter fighter) {
            int dist = 1000;
            Fighter curF = null;
            for (Fighter f : fight.getFighters(3)) {
                if (f.isDead() || f.isHidden()) { //si invisible, on passe
                    continue;
                }
                if (f.getTeam() != fighter.getTeam())//Si c'est un ennemis
                {
                    int d = OldPathfinding.getDistanceBetween(fight.get_map(), fighter.getCell().getID(), f.getCell().getID());
                    if (d < dist) {
                        dist = d;
                        curF = f;
                    }
                }
            }
            return curF;
        }

        private static Map<Integer, Fighter> getLowHpEnnemyList(Fight fight, Fighter fighter) {
            Map<Integer, Fighter> list = new TreeMap<Integer, Fighter>();
            Map<Integer, Fighter> ennemy = new TreeMap<Integer, Fighter>();
            for (Fighter f : fight.getFighters(3)) {
                if (f == null) {
                    continue;
                }
                if (f.isDead()) {
                    continue;
                }
                if (f == fighter) {
                    continue;
                }
                if (f.getTeam() != fighter.getTeam()) {
                    ennemy.put(f.getPDV(), f);
                }
            }
            int i = 0, i2 = ennemy.size();
            int curHP = 1000000000;
            while (i < i2) {
                try {
                    curHP = 1000000000;
                    for (Entry<Integer, Fighter> t : ennemy.entrySet()) {
                        if (t.getValue() == null) {
                            continue;
                        }
                        if (t.getValue().getPDV() < curHP) {
                            curHP = t.getValue().getPDV();
                        }
                        //TODO: J'ai cherch�, j'ai rien trouver � faire :o
                    }
                    Fighter test = ennemy.get(curHP);
                    if (test == null) {
                        break;
                    }
                    list.put(test.getPDV(), test);
                    ennemy.remove(curHP);
                    i++;
                } catch (NullPointerException e) {
                    break;
                }//Avec mon calcul on arriverait � cette ligne...
            }
            return list;
        }

        private static int attackIfPossible(Fight fight, Fighter fighter)// 0 = Rien, 5 = EC, 666 = NULL, 10 = SpellNull ou ActionEnCour ou Can'tCastSpell, 0 = AttaqueOK
        {
            try {
                if (fight == null || fighter == null) {
                    return 0;
                }
                Map<Integer, Fighter> ennemyList = getLowHpEnnemyList(fight, fighter);
                SpellLevel SS = null;
                Fighter target = null;
                boolean invisible = false;
                if(ennemyList != null && !ennemyList.isEmpty()){
                    for (Entry<Integer, Fighter> t : ennemyList.entrySet()) // pour chaque ennemi on cherche le meilleur sort
                    {
                        if(t.getValue().isHidden()){ //si invisible, on passe
                            invisible = true;
                            continue;
                        }
                        SS = getBestSpellForTarget(fight, fighter, t.getValue());
                        if (SS != null) // s'il existe un sort pour un ennemi, on le prend pour cible
                        {
                            target = t.getValue();
                            break;
                        }
                    }
                }
                int curTarget = 0, cell = 0;
                SpellLevel SS2 = null;
                for (Entry<Integer, SpellLevel> S : fighter.getMonster().getSpells().entrySet()) // pour chaque sort du mob
                {
                    int targetVal = getBestTargetZone(fight, fighter, S.getValue(), fighter.getCell().getID()); // on d�termine le meilleur
                    if (targetVal == -1 || targetVal == 0) // endroit pour lancer le sort de zone (ou non)
                    {
                        continue;
                    }
                    int nbTarget = targetVal / 1000;
                    int cellID = targetVal - nbTarget * 1000;
                    if (nbTarget > curTarget) {
                        curTarget = nbTarget;
                        cell = cellID;
                        SS2 = S.getValue();
                    }
                }
                if (curTarget > 0 && cell > 0 && cell < 480 && SS2 != null) // si la case s�lectionn�e est valide et qu'il y a au moins une cible
                {
                    int attack = fight.tryCastSpell(fighter, SS2, cell);
                    if (attack != 0) {
                        return attack;
                    }
                } else {
                    if (target == null || SS == null) {
                        if(invisible){ //si invisible
                            if(Ancestra.CONFIG_DEBUG){
                                System.out.println("sélection d'un sort de zone pour attaque aléatoire");
                            }
                                int area = -1;
                                int curArea = -1;
                                int cellTarget = 0;
                                for(SpellLevel SS3 : getLaunchableSort(fighter, fight, 0)){ //on sélection le sort (lancable) avec plus grosse zone
                                    if(SS3.getPorteeType().isEmpty()){
                                        continue; //pas de porteeType
                                    }
                                    String p = SS3.getPorteeType();
                                    int size = CryptManager.getIntByHashedValue(p.charAt(1)); //calcul la taille de la zone (en cases)
                                    switch(p.charAt(0)){
                                        case 'C': //en cercle
                                            curArea = 1;
                                            for(int n = 0; n < size; n++){
                                                curArea += 4 * n;
                                            }
                                            break;
                                        case 'X': //en croix
                                            curArea = 4 * size + 1;
                                            break;
                                        case 'L': //en ligne
                                            curArea = size + 1;
                                            break;
                                        case 'P': //case simple
                                            curArea = 1;
                                            break;
                                        default:
                                            curArea = -1;
                                    }
                                    
                                    String args = SS3.isLineLaunch() ? "X" : "C";
                                    char[] table = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v'};
                                    if (SS3.getMaxPO() > 20) {
                                        args += "u";
                                    } else {
                                        args += table[SS3.getMaxPO()];
                                    }
                
                                    if(curArea > area){ //si zone plus grande
                                        //sélection des cases possibles
                                        ArrayList<MapCell> possibleLaunch = OldPathfinding.getCellListFromAreaString(fight.get_map(), fighter.getCell().getID(), fighter.getCell().getID(), args, 0, false);
                                        Collections.shuffle(possibleLaunch); //ajoute un peu d'aléatoire
                                        for(MapCell possibleCell : possibleLaunch){
                                            if(possibleCell.getFirstFighter() != null && possibleCell.getFirstFighter().getTeam() == fighter.getTeam()){
                                                continue; //on ne va quand même pas attaquer ses alliers
                                            }
                                            if(!fight.canCastSpell(fighter, SS3, fight.get_map().getCell(possibleCell.getID()), -1)){ //vérifie si il est lançable
                                                if(Ancestra.CONFIG_DEBUG){
                                                    System.out.println("Cellule " + possibleCell.getID() + " non valide pour lancer le sort");
                                                }
                                                continue;
                                            }
                                            SS = SS3;
                                            area = curArea;
                                            cellTarget = possibleCell.getID(); //on met en mémoire la cellule de lancé
                                            if(Ancestra.CONFIG_DEBUG){
                                                System.out.println("Sort " + SS.getSpellID() + " sélectionné");
                                            }
                                            break;
                                        }
                                    }
                                } //END FOREACH
                                return fight.tryCastSpell(fighter, SS, cellTarget); //lance le sort (dans le vide)
                        }
                        return 666;
                    }
                    int attack = fight.tryCastSpell(fighter, SS, target.getCell().getID());
                    if (attack != 0) {
                        return attack;
                    }
                }
                return 0;
            } catch (NullPointerException e) {
                return 666;
            }
        }

        @SuppressWarnings("rawtypes")
        private static int attackIfPossiblePerco(Fight fight, Fighter fighter) {
            Map<Integer, Fighter> ennemyList = getLowHpEnnemyList(fight, fighter);
            SpellLevel SS = null;
            Fighter target = null;
            for (Entry<Integer, Fighter> t : ennemyList.entrySet()) {
                SS = getBestSpellForTargetPerco(fight, fighter, (Fighter) t.getValue());
                if (SS == null) {
                    continue;
                }
                target = (Fighter) t.getValue();
                break;
            }

            int curTarget = 0;
            int cell = 0;
            SpellLevel SS2 = null;
            for (Map.Entry S : fighter.getCollector().getGuild().getSpells().entrySet()) {
                if (S.getValue() != null) {
                    int targetVal = getBestTargetZone(fight, fighter, (SpellLevel) S.getValue(), fighter.getCell().getID());
                    if ((targetVal == -1) || (targetVal == 0)) {
                        continue;
                    }
                    int nbTarget = targetVal / 1000;
                    int cellID = targetVal - nbTarget * 1000;
                    if (nbTarget <= curTarget) {
                        continue;
                    }
                    curTarget = nbTarget;
                    cell = cellID;
                    SS2 = (SpellLevel) S.getValue();
                }
            }
            if ((curTarget > 0) && (cell > 0) && (cell < 480) && (SS2 != null)) {
                int attack = fight.tryCastSpell(fighter, SS2, cell);
                if (attack != 0) {
                    return attack;
                }
            } else {
                if ((target == null) || (SS == null)) {
                    return 666;
                }
                int attack = fight.tryCastSpell(fighter, SS, target.getCell().getID());
                if (attack != 0) {
                    return attack;
                }
            }
            return 0;
        }

        private static boolean moveToAttackIfPossible(Fight fight, Fighter fighter) {
            ArrayList<Integer> cells = OldPathfinding.getListCaseFromFighter(fight, fighter);
            if (cells == null) {
                return false;
            }
            int distMin = OldPathfinding.getDistanceBetween(fight.get_map(), fighter.getCell().getID(), getNearestEnnemy(fight, fighter).getCell().getID());
            ArrayList<SpellLevel> sorts = getLaunchableSort(fighter, fight, distMin);
            if (sorts == null) {
                return false;
            }
            ArrayList<Fighter> targets = getPotentialTarget(fight, fighter, sorts);
            if (targets == null) {
                return false;
            }

            int CellDest = 0;
            boolean found = false;
            boolean invisible = false;
            for (int i : cells) {
                for (SpellLevel S : sorts) {
                    for (Fighter T : targets) {
                        if(T.isHidden()){ //si il est invisible
                            invisible = true;
                            continue;
                        }
                        if (fight.canCastSpell(fighter, S, T.getCell(), i)) {
                            CellDest = i;
                            found = true;
                        }
                        int targetVal = getBestTargetZone(fight, fighter, S, i);
                        if (targetVal > 0) {
                            int nbTarget = targetVal / 1000;
                            int cellID = targetVal - nbTarget * 1000;
                            if (fight.get_map().getCell(cellID) != null) {
                                if (fight.canCastSpell(fighter, S, fight.get_map().getCell(cellID), i)) {
                                    CellDest = i;
                                    found = true;
                                }
                            }
                        }
                        if (found) {
                            break;
                        }
                    }
                    if (found) {
                        break;
                    }
                }
                if (found) {
                    break;
                }
            }
            //si aucuns joueurs valides et qu'il y en a un invisible, on se déplace aléatoirement
            if(!found && invisible){
                cells = OldPathfinding.getFullPMListCase(fight, fighter); //pour utiliser tout les PM
                if(cells == null)
                    return false;
                int i = Formulas.getRandomValue(0, cells.size() - 1);
                CellDest = cells.get(i);
                if(Ancestra.CONFIG_DEBUG){
                    System.out.println("Tentative de déplacement aléatoire");
                }
            }
            if (CellDest == 0) {
                return false;
            }
            List<MapCell> path;// = OldPathfinding.getShortestPathBetween(fight.get_map(), fighter.getCell().getID(), CellDest, 0);
            
            try{
                path = Pathfinding.findPath(fight.get_map(), fighter.getCell(), fight.get_map().getCell(CellDest), false, CellChecker.forFightPathing(fight));
            }catch(Exception e){
                Peak.errorLog.addToLog(e);
                return false;
            }
            
            if (path == null) {
                return false;
            }
            boolean result = fight.fighterDeplace(fighter, path);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
            return result;

        }

        private static ArrayList<SpellLevel> getLaunchableSort(Fighter fighter, Fight fight, int distMin) {
            ArrayList<SpellLevel> sorts = new ArrayList<SpellLevel>();
            if (fighter.getMonster() == null) {
                return null;
            }
            for (Entry<Integer, SpellLevel> S : fighter.getMonster().getSpells().entrySet()) {
                if (S.getValue().getPACost() > fighter.getCurPA())//si PA insuffisant
                {
                    continue;
                }
                //if(S.getValue().getMaxPO() + fighter.getCurPM(fight) < distMin && S.getValue().getMaxPO() != 0)// si po max trop petite
                //continue;
                if (!LaunchedSort.coolDownGood(fighter, S.getValue().getSpellID()))// si cooldown ok
                {
                    continue;
                }
                if (S.getValue().getMaxLaunchbyTurn() - LaunchedSort.getNbLaunch(fighter, S.getValue().getSpellID()) <= 0 && S.getValue().getMaxLaunchbyTurn() > 0)// si nb tours ok
                {
                    continue;
                }
                if (calculInfluence(fight, S.getValue(), fighter, fighter) >= 0)// si sort pas d'attaque
                {
                    continue;
                }
                sorts.add(S.getValue());
            }
            ArrayList<SpellLevel> finalS = TriInfluenceSorts(fight, fighter, sorts);

            return finalS;
        }

        private static ArrayList<SpellLevel> TriInfluenceSorts(Fight fight, Fighter fighter, ArrayList<SpellLevel> sorts) {
            if (sorts == null) {
                return null;
            }

            ArrayList<SpellLevel> finalSorts = new ArrayList<SpellLevel>();
            Map<Integer, SpellLevel> copie = new TreeMap<Integer, SpellLevel>();
            for (SpellLevel S : sorts) {
                copie.put(S.getSpellID(), S);
            }

            int curInfl = 0;
            int curID = 0;

            while (copie.size() > 0) {
                curInfl = 0;
                curID = 0;
                for (Entry<Integer, SpellLevel> S : copie.entrySet()) {
                    int infl = -calculInfluence(fight, S.getValue(), fighter, fighter);
                    if (infl > curInfl) {
                        curID = S.getValue().getSpellID();
                        curInfl = infl;
                    }
                }
                if (curID == 0 || curInfl == 0) {
                    break;
                }
                finalSorts.add(copie.get(curID));
                copie.remove(curID);
            }

            return finalSorts;
        }

        private static ArrayList<Fighter> getPotentialTarget(Fight fight, Fighter fighter, ArrayList<SpellLevel> sorts) {
            try {
                ArrayList<Fighter> targets = new ArrayList<Fighter>();
                int distMax = 0;
                for (SpellLevel S : sorts) {
                    if (S.getMaxPO() > distMax) {
                        distMax = S.getMaxPO();
                    }
                }
                distMax += fighter.getCurPM();
                Map<Integer, Fighter> potentialsT = getLowHpEnnemyList(fight, fighter);
                if (potentialsT == null || potentialsT.isEmpty()) {
                    return new ArrayList<Fighter>();
                }
                for (Entry<Integer, Fighter> T : potentialsT.entrySet()) {
                    int dist = OldPathfinding.getDistanceBetween(fight.get_map(), fighter.getCell().getID(), T.getValue().getCell().getID());
                    if (dist < distMax) {
                        targets.add(T.getValue());
                    }
                }

                return targets;
            } catch (Exception e) {
                return new ArrayList<Fighter>();
            }
        }

        private static SpellLevel getBestSpellForTarget(Fight fight, Fighter F, Fighter T) {
            int inflMax = 0;
            SpellLevel ss = null;
            for (Entry<Integer, SpellLevel> SS : F.getMonster().getSpells().entrySet()) {
                int curInfl = 0, Infl1 = 0, Infl2 = 0;
                int PA = F.getMonster().getPA();
                int usedPA[] = {0, 0};
                if (!fight.canCastSpell(F, SS.getValue(), T.getCell(), -1)) {
                    continue;
                }
                curInfl = calculInfluence(fight, SS.getValue(), F, T);
                if (curInfl == 0) {
                    continue;
                }
                if (curInfl > inflMax) {
                    ss = SS.getValue();
                    usedPA[0] = ss.getPACost();
                    Infl1 = curInfl;
                    inflMax = Infl1;
                }

                for (Entry<Integer, SpellLevel> SS2 : F.getMonster().getSpells().entrySet()) {
                    if ((PA - usedPA[0]) < SS2.getValue().getPACost()) {
                        continue;
                    }
                    if (!fight.canCastSpell(F, SS2.getValue(), T.getCell(), -1)) {
                        continue;
                    }
                    curInfl = calculInfluence(fight, SS2.getValue(), F, T);
                    if (curInfl == 0) {
                        continue;
                    }
                    if ((Infl1 + curInfl) > inflMax) {
                        ss = SS.getValue();
                        usedPA[1] = SS2.getValue().getPACost();
                        Infl2 = curInfl;
                        inflMax = Infl1 + Infl2;
                    }
                    for (Entry<Integer, SpellLevel> SS3 : F.getMonster().getSpells().entrySet()) {
                        if ((PA - usedPA[0] - usedPA[1]) < SS3.getValue().getPACost()) {
                            continue;
                        }
                        if (!fight.canCastSpell(F, SS3.getValue(), T.getCell(), -1)) {
                            continue;
                        }
                        curInfl = calculInfluence(fight, SS3.getValue(), F, T);
                        if (curInfl == 0) {
                            continue;
                        }
                        if ((curInfl + Infl1 + Infl2) > inflMax) {
                            ss = SS.getValue();
                            inflMax = curInfl + Infl1 + Infl2;
                        }
                    }
                }
            }
            return ss;
        }

        @SuppressWarnings("rawtypes")
        private static SpellLevel getBestSpellForTargetPerco(Fight fight, Fighter F, Fighter T) {
            int inflMax = 0;
            SpellLevel ss = null;
            //MomEmu.printDebug("SIZE SPELL : " + MomWorld.getGuild(F.getCollector().GetPercoGuildID()).getSpells().size());
            for (Map.Entry SS : F.getCollector().getGuild().getSpells().entrySet()) {
                if (SS.getValue() != null) {
                    int curInfl = 0;
                    int Infl1 = 0;
                    int Infl2 = 0;
                    int PA = 6;
                    int[] usedPA = new int[2];
                    if (fight.canCastSpell(F, (SpellLevel) SS.getValue(), F.getCell(), T.getCell().getID())) {
                        curInfl = calculInfluence((SpellLevel) SS.getValue(), F, T);
                        if (curInfl != 0) {
                            if (curInfl > inflMax) {
                                ss = (SpellLevel) SS.getValue();
                                usedPA[0] = ss.getPACost();
                                Infl1 = curInfl;
                                inflMax = Infl1;
                            }

                            for (Map.Entry SS2 : F.getCollector().getGuild().getSpells().entrySet()) {
                                if ((PA - usedPA[0] < ((SpellLevel) SS2.getValue()).getPACost())
                                        || (!fight.canCastSpell(F, (SpellLevel) SS2.getValue(), F.getCell(), T.getCell().getID()))) {
                                    continue;
                                }
                                curInfl = calculInfluence((SpellLevel) SS2.getValue(), F, T);
                                if (curInfl != 0) {
                                    if (Infl1 + curInfl > inflMax) {
                                        ss = (SpellLevel) SS.getValue();
                                        usedPA[1] = ((SpellLevel) SS2.getValue()).getPACost();
                                        Infl2 = curInfl;
                                        inflMax = Infl1 + Infl2;
                                    }
                                    for (Map.Entry SS3 : F.getCollector().getGuild().getSpells().entrySet()) {
                                        if ((PA - usedPA[0] - usedPA[1] < ((SpellLevel) SS3.getValue()).getPACost())
                                                || (!fight.canCastSpell(F, (SpellLevel) SS3.getValue(), F.getCell(), T.getCell().getID()))) {
                                            continue;
                                        }
                                        curInfl = calculInfluence((SpellLevel) SS3.getValue(), F, T);
                                        if ((curInfl == 0)
                                                || (curInfl + Infl1 + Infl2 <= inflMax)) {
                                            continue;
                                        }
                                        ss = (SpellLevel) SS.getValue();
                                        inflMax = curInfl + Infl1 + Infl2;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return ss;
        }

        private static int getBestTargetZone(Fight fight, Fighter fighter, SpellLevel spell, int launchCell) {
            if (spell.getPorteeType().isEmpty() || (spell.getPorteeType().charAt(0) == 'P' && spell.getPorteeType().charAt(1) == 'a')) {
                return 0;
            }
            ArrayList<MapCell> possibleLaunch = new ArrayList<>();
            int CellF = -1;
            if (spell.getMaxPO() != 0) {
                char arg1 = 'a';
                if (spell.isLineLaunch()) {
                    arg1 = 'X';
                } else {
                    arg1 = 'C';
                }
                char[] table = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v'};
                char arg2 = 'a';
                if (spell.getMaxPO() > 20) {
                    arg2 = 'u';
                } else {
                    arg2 = table[spell.getMaxPO()];
                }
                String args = Character.toString(arg1) + Character.toString(arg2);
                possibleLaunch = OldPathfinding.getCellListFromAreaString(fight.get_map(), launchCell, launchCell, args, 0, false);
            } else {
                possibleLaunch.add(fight.get_map().getCell(launchCell));
            }

            if (possibleLaunch == null) {
                return -1;
            }
            int nbTarget = 0;
            for (MapCell cell : possibleLaunch) {
                try {
                    if (!fight.canCastSpell(fighter, spell, cell, launchCell)) {
                        continue;
                    }
                    int num = 0;
                    int curTarget = 0;
                    ArrayList<SpellEffect> test = new ArrayList<>();
                    test.addAll(spell.getSpellEffects());

                    for (SpellEffect SE : test) {
                        try {
                            if (SE == null) {
                                continue;
                            }
//                            if (SE.getValue() == -1) {
//                                continue;
//                            }
                            int POnum = num * 2;
                            ArrayList<MapCell> cells = OldPathfinding.getCellListFromAreaString(fight.get_map(), cell.getID(), launchCell, spell.getPorteeType(), POnum, false);
                            for (MapCell c : cells) {
                                if (c.getFirstFighter() == null || c.getFirstFighter().isHidden()) { //si pas d'ennemie, ou invisible
                                    continue;
                                }
                                if (c.getFirstFighter().getTeam() != fighter.getTeam()) {
                                    curTarget++;
                                }
                            }
                        } catch (Exception e) {
                        };
                        num++;
                    }
                    if (curTarget > nbTarget) {
                        nbTarget = curTarget;
                        CellF = cell.getID();
                    }
                } catch (Exception E) {
                }
            }
            if (nbTarget > 0 && CellF != -1) {
                return CellF + nbTarget * 1000;
            } else {
                return 0;
            }
        }

        private static int calculInfluenceHeal(SpellLevel ss) {
            int inf = 0;
            for (SpellEffect SE : ss.getSpellEffects()) {
                if (SE.getEffect() != Effect.HEAL) {
                    return 0;
                }
                inf += 50 * SE.getMin() + SE.getMax();
            }

            return inf;
        }

        private static int calculInfluence(Fight fight, SpellLevel ss, Fighter C, Fighter T) {
            //FIXME TODO
            int infTot = 0;
            int num = 0, POnum = 0;
            int allies = 0, ennemies = 0;
            for (SpellEffect SE : ss.getSpellEffects()) {
                allies = 0;
                ennemies = 0;
                POnum = 2 * num;
                /**
                 * D�termine � qui s'applique l'effet*
                 */
                ArrayList<MapCell> cells = OldPathfinding.getCellListFromAreaString(fight.get_map(), T.getCell().getID(), C.getCell().getID(), ss.getPorteeType(), POnum, false);
                ArrayList<MapCell> finalCells = new ArrayList<>();
                
                Spell S = ss.getSpell();
                int TE = S.getEffectTarget(num);
                

                for (MapCell C1 : cells) {
                    if (C1 == null) {
                        continue;
                    }
                    Fighter F = C1.getFirstFighter();
                    if (F == null) {
                        continue;
                    }
                    //Ne touche pas les alli�s
                    if (((TE & 1) == 1) && (F.getTeam() == C.getTeam())) {
                        continue;
                    }
                    //Ne touche pas le lanceur
                    if ((((TE >> 1) & 1) == 1) && (F.getSpriteId() == C.getSpriteId())) {
                        continue;
                    }
                    //Ne touche pas les ennemies
                    if ((((TE >> 2) & 1) == 1) && (F.getTeam() != C.getTeam())) {
                        continue;
                    }
                    //Ne touche pas les combatants (seulement invocations)
                    if ((((TE >> 3) & 1) == 1) && (!F.isInvocation())) {
                        continue;
                    }
                    //Ne touche pas les invocations
                    if ((((TE >> 4) & 1) == 1) && (F.isInvocation())) {
                        continue;
                    }
                    //N'affecte que le lanceur
                    if ((((TE >> 5) & 1) == 1) && (F.getSpriteId() != C.getSpriteId())) {
                        continue;
                    }
                    //Si pas encore eu de continue, on ajoute la case
                    finalCells.add(C1);
                }
                //Si le sort n'affecte que le lanceur et que le lanceur n'est pas dans la zone
                if (((TE >> 5) & 1) == 1) {
                    if (!finalCells.contains(C.getCell())) {
                        finalCells.add(C.getCell());
                    }
                }
                for (MapCell cell : finalCells) {
                    Fighter fighter = cell.getFirstFighter();
                    
                    if(fighter == null)
                        continue;
                    
                    if (fighter.getTeam() == C.getTeam()) {
                        allies++;
                    } else {
                        ennemies++;
                    }
                }
                num++;
                int inf = getInfluenceBySpellEffect(SE, C);
                if (C.getTeam() == T.getTeam())//Si Amis
                {
                    infTot -= inf * (allies - ennemies);
                } else//Si ennemis
                {
                    infTot += inf * (ennemies - allies);
                }
            }
            return (int)(infTot / ss.getPACost());
        }
        
        private static int getInfluenceBySpellEffect(SpellEffect SE, Fighter F){
            int inf = 0;
                switch (SE.getEffect().getId()) {
                    case 5://repousse de X cases
                        inf = 500 * SE.getAverageJet();
                        break;
                    case 89://dommages % vie neutre
                        inf = 250 * SE.getAverageJet();
                        break;
                    case 91://Vol de Vie Eau
                        inf = (int)(150 * SE.getAverageJet() * (F.getBaseStats().getEffect(Effect.ADD_CHAN) * .01 + 1));
                        break;
                    case 92://Vol de Vie Terre
                        inf = (int)(150 * SE.getAverageJet() * (F.getBaseStats().getEffect(Effect.ADD_FORC) * .01 + 1));
                        break;
                    case 93://Vol de Vie Air
                        inf = (int)(150 * SE.getAverageJet() * (F.getBaseStats().getEffect(Effect.ADD_AGIL) * .01 + 1));
                        break;
                    case 94://Vol de Vie feu
                        inf = (int)(150 * SE.getAverageJet() * (F.getBaseStats().getEffect(Effect.ADD_INTE) * .01 + 1));
                        break;
                    case 95://Vol de Vie neutre
                        inf = (int)(150 * SE.getAverageJet() * (F.getBaseStats().getEffect(Effect.ADD_FORC) * .01 + 1));
                        break;
                    case 96://Dommage Eau
                        inf = (int)(100 * SE.getAverageJet() * (F.getBaseStats().getEffect(Effect.ADD_CHAN) * .01 + 1));
                        break;
                    case 97://Dommage Terre
                        inf = (int)(100 * SE.getAverageJet() * (F.getBaseStats().getEffect(Effect.ADD_FORC) * .01 + 1));
                        break;
                    case 98://Dommage Air
                        inf = (int)(100 * SE.getAverageJet() * (F.getBaseStats().getEffect(Effect.ADD_AGIL) * .01 + 1));
                        break;
                    case 99://Dommage feu
                        inf = (int)(100 * SE.getAverageJet() * (F.getBaseStats().getEffect(Effect.ADD_INTE) * .01 + 1));
                        break;
                    case 100://Dommage neutre
                        inf = (int)(100 * SE.getAverageJet() * (F.getBaseStats().getEffect(Effect.ADD_FORC) * .01 + 1));
                        break;
                    case 101://retrait PA
                        inf = 2500 * SE.getAverageJet();
                        break;
                    case 127://retrait PM
                        inf = 1500 * SE.getAverageJet();
                        break;
                    case 84://vol PA
                        inf = 5000 * SE.getAverageJet();
                        break;
                    case 77://vol PM
                        inf = 3000 * SE.getAverageJet();
                        break;
                    case 108:// soin
                        inf = (int)(-100 * SE.getAverageJet() * (F.getBaseStats().getEffect(Effect.ADD_INTE) * .01 + 1));
                        break;
                    case 111://+ PA
                        inf = -2500 * SE.getAverageJet();
                        break;
                    case 128://+ PM
                        inf = -1500 * SE.getAverageJet();
                        break;
                    case 121://+ Dom
                        inf = -200 * SE.getAverageJet();
                        break;
                    case 131://poison X pdv par PA
                        inf = 300 * SE.getAverageJet();
                        break;
                    case 132://d�senvoute
                        inf = 6000;
                        break;
                    case 138://+ %Dom
                        inf = -150 * SE.getAverageJet();
                        break;
                    case 150://invisibilit�
                        inf = -5000;
                        break;
                    case 168://retrait PA non esquivable
                        inf = 3000 * SE.getAverageJet();
                        break;
                    case 169://retrait PM non esquivable
                        inf = 2000 * SE.getAverageJet();
                        break;
                    case 210://r�sistance
                        inf = -100 * SE.getAverageJet();
                        break;
                    case 211://r�sistance
                        inf = -100 * SE.getAverageJet();
                        break;
                    case 212://r�sistance
                        inf = -100 * SE.getAverageJet();
                        break;
                    case 213://r�sistance
                        inf = -100 * SE.getAverageJet();
                        break;
                    case 214://r�sistance
                        inf = -100 * SE.getAverageJet();
                        break;
                    case 215://faiblesse
                        inf = 100 * SE.getAverageJet();
                        break;
                    case 216://faiblesse
                        inf = 100 * SE.getAverageJet();
                        break;
                    case 217://faiblesse
                        inf = 100 * SE.getAverageJet();
                        break;
                    case 218://faiblesse
                        inf = 100 * SE.getAverageJet();
                        break;
                    case 219://faiblesse
                        inf = 100 * SE.getAverageJet();
                        break;
                    case 265://r�duction dommage
                        inf = -250 * SE.getAverageJet();
                        break;
                    case 765://sacrifice
                        inf = -50000;
                        break;
                    default:
                        inf = 100 * SE.getAverageJet();

                }
                return inf;
        }

        private static int calculInfluence(SpellLevel ss, Fighter C, Fighter T) {
            int infTot = 0;
            for (SpellEffect SE : ss.getSpellEffects()) {
                int inf = getInfluenceBySpellEffect(SE, C);

                if (C.getTeam() == T.getTeam()) {
                    infTot -= inf;
                } else {
                    infTot += inf;
                }
            }
            return (int)(infTot / ss.getPACost());
        }
    }
    
      public static void launchIA(Fighter fighter, Fight fight)
  {
          if(fighter.getMonster() == null)
          {
                  return;
          }
          switch(fighter.getMonster().getTemplate().getIAType())
          {
                  case 0:
                  		IAThread.apply_type0(fighter, fight);
                          return;
                  case 1:
                  		IAThread.apply_type1(fighter, fight);
                          break;
                  case 2:
                  		IAThread.apply_type2(fighter, fight);
                          break;
                  case 3:
                  		IAThread.apply_type3(fighter, fight);
                      	break;
                  case 4:
                  		IAThread.apply_type4(fighter, fight);
                      	break;
                  case 5:
                  		IAThread.apply_type5(fighter, fight);
	                    	break;
                  case 6:
                  		IAThread.apply_type6(fighter, fight);
	                    	break;
                  case 7:
                      IAThread.apply_type7(fighter, fight);
                  case 8:
                      IAThread.apply_type8(fighter, fight);
                  default:
                  		IAThread.apply_type1(fighter, fight);
                          break;
          }
  }
}
