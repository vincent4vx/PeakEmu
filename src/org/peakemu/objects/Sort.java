//package org.peakemu.objects;
//
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Map;
//import java.util.TreeMap;
//
//import org.peakemu.common.util.StringUtil;
//import org.peakemu.database.parser.SpellParser;
//import org.peakemu.world.SpellEffect;
//
//public class Sort {
//
//    private int spellID;
//    private int spriteID;
//    private String spriteInfos;
//    private Map<Integer, SortStats> sortStats = new TreeMap<Integer, SortStats>();
//    private ArrayList<Integer> effectTargets = new ArrayList<Integer>();
//    private ArrayList<Integer> CCeffectTargets = new ArrayList<Integer>();
//
//    public static class SortStats {
//
//        private Sort spell;
//        private int level;
//        private int PACost;
//        private int minPO;
//        private int maxPO;
//        private int TauxCC;
//        private int TauxEC;
//        private boolean isLineLaunch;
//        private boolean hasLDV;
//        private boolean isEmptyCell;
//        private boolean isModifPO;
//        private int maxLaunchbyTurn;
//        private int maxLaunchbyByTarget;
//        private int coolDown;
//        private int reqLevel;
//        private boolean isEcEndTurn;
//        private String porteeType;
//
//        final private Collection<SpellEffect> spellEffects = new ArrayList<>();
//        final private Collection<SpellEffect> ccSpellEffects = new ArrayList<>();
//
//        public SortStats(Sort spell, int Alevel, int cost, int minPO, int maxPO, int tauxCC, int tauxEC, boolean isLineLaunch, boolean hasLDV,
//            boolean isEmptyCell, boolean isModifPO, int maxLaunchbyTurn, int maxLaunchbyByTarget, int coolDown,
//            int reqLevel, boolean isEcEndTurn, String effects, String ceffects, String typePortee) {
//            this.spell = spell;
//            this.level = Alevel;
//            this.PACost = cost;
//            this.minPO = minPO;
//            this.maxPO = maxPO;
//            this.TauxCC = tauxCC;
//            this.TauxEC = tauxEC;
//            this.isLineLaunch = isLineLaunch;
//            this.hasLDV = hasLDV;
//            this.isEmptyCell = isEmptyCell;
//            this.isModifPO = isModifPO;
//            this.maxLaunchbyTurn = maxLaunchbyTurn;
//            this.maxLaunchbyByTarget = maxLaunchbyByTarget;
//            this.coolDown = coolDown;
//            this.reqLevel = reqLevel;
//            this.isEcEndTurn = isEcEndTurn;
//            this.porteeType = typePortee;
//            
//            parseEffect(effects, spellEffects);
//            parseEffect(ceffects, ccSpellEffects);
//        }
//
//        private void parseEffect(String str, Collection<SpellEffect> effects) {
//            if(str.equals("-1"))
//                return;
//            
//            int i = 0;
//            for (String e : StringUtil.split(str, "|")) {
//                if (e.isEmpty()) {
//                    continue;
//                }
//
//                String area = "Ca";
//
//                if (porteeType.length() >= 2 * i + 2) {
//                    area = porteeType.substring(2 * i, 2 * i + 2);
//                }
//
//                int target = 0;
//
//                if (spell.getEffectTargets().size() > i) {
//                    target = spell.getEffectTargets().get(i);
//                }
//
//                SpellEffect nse = SpellParser.parseSpellEffect(e, area, this, target);
//                ++i;
//
//                if (nse != null) {
//                    effects.add(nse);
//                }
//            }
//        }
//
//        public int getSpellID() {
//            return spell.getSpellID();
//        }
//
//        public Sort getSpell() {
//            return spell;
//        }
//
//        public int getSpriteID() {
//            return getSpell().getSpriteID();
//        }
//
//        public String getSpriteInfos() {
//            return getSpell().getSpriteInfos();
//        }
//
//        public int getLevel() {
//            return level;
//        }
//
//        public int getPACost() {
//            return PACost;
//        }
//
//        public int getMinPO() {
//            return minPO;
//        }
//
//        public int getMaxPO() {
//            return maxPO;
//        }
//
//        public int getTauxCC() {
//            return TauxCC;
//        }
//
//        public int getTauxEC() {
//            return TauxEC;
//        }
//
//        public boolean isLineLaunch() {
//            return isLineLaunch;
//        }
//
//        public boolean hasLDV() {
//            return hasLDV;
//        }
//
//        public boolean isEmptyCell() {
//            return isEmptyCell;
//        }
//
//        public boolean isModifPO() {
//            return isModifPO;
//        }
//
//        public int getMaxLaunchbyTurn() {
//            return maxLaunchbyTurn;
//        }
//
//        public int getMaxLaunchbyByTarget() {
//            return maxLaunchbyByTarget;
//        }
//
//        public int getCoolDown() {
//            return coolDown;
//        }
//
//        public int getReqLevel() {
//            return reqLevel;
//        }
//
//        public boolean isEcEndTurn() {
//            return isEcEndTurn;
//        }
//
//        public Collection<SpellEffect> getSpellEffects() {
//            return spellEffects;
//        }
//
//        public Collection<SpellEffect> getCcSpellEffects() {
//            return ccSpellEffects;
//        }
//
//        public String getPorteeType() {
//            return porteeType;
//        }
//    }
//
//    public Sort(int aspellID, int aspriteID, String aspriteInfos, String ET) {
//        spellID = aspellID;
//        spriteID = aspriteID;
//        spriteInfos = aspriteInfos;
//        String nET = ET.split(":")[0];
//        String ccET = "";
//        if (ET.split(":").length > 1) {
//            ccET = ET.split(":")[1];
//        }
//        for (String num : nET.split(";")) {
//            try {
//                effectTargets.add(Integer.parseInt(num));
//            } catch (Exception e) {
//                effectTargets.add(0);
//                continue;
//            };
//        }
//        for (String num : ccET.split(";")) {
//            try {
//                CCeffectTargets.add(Integer.parseInt(num));
//            } catch (Exception e) {
//                CCeffectTargets.add(0);
//                continue;
//            };
//        }
//    }
//
//    public ArrayList<Integer> getEffectTargets() {
//        return effectTargets;
//    }
//
//    public int getSpriteID() {
//        return spriteID;
//    }
//
//    public String getSpriteInfos() {
//        return spriteInfos;
//    }
//
//    public int getSpellID() {
//        return spellID;
//    }
//
//    public SortStats getStatsByLevel(int lvl) {
//        return sortStats.get(lvl);
//    }
//
//    public void addSortStats(Integer lvl, SortStats stats) {
//        if (sortStats.get(lvl) != null) {
//            return;
//        }
//        sortStats.put(lvl, stats);
//    }
//
//}
