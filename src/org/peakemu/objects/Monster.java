/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.objects;

import java.util.ArrayList;
import java.util.Map;
import org.peakemu.world.MonsterTemplate;
import org.peakemu.world.SpellLevel;
import org.peakemu.world.Stats;
import org.peakemu.world.World;
import org.peakemu.world.enums.Effect;
import org.peakemu.world.fight.fighter.Fighter;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class Monster {
    final private MonsterTemplate template;
    final private int grade;
    final private int level;
    final private int baseXp;
    final private Stats stats;
    final private Map<Integer, SpellLevel> spells;

    public Monster(MonsterTemplate aTemp, int Agrade, int Alevel, Stats stats, Map<Integer, SpellLevel> spells, int xp) {
        template = aTemp;
        grade = Agrade;
        level = Alevel;
        baseXp = xp;
        this.stats = stats;
        this.spells = spells;
    }

    private Monster(Monster other) {
        this.template = other.template;
        this.grade = other.grade;
        this.level = other.level;
        this.stats = new Stats(other.stats);
        this.spells = other.spells;
        this.baseXp = other.baseXp;
    }

    public ArrayList<World.Drop> getDrops() {
        return template.getDrops();
    }

    public int getBaseXp() {
        return baseXp;
    }

    public int getInit() {
        return stats.getEffect(Effect.ADD_INIT);
    }

    public Stats getStats() {
        return stats;
    }

    public int getLevel() {
        return level;
    }

    public Map<Integer, SpellLevel> getSpells() {
        return spells;
    }

    public MonsterTemplate getTemplate() {
        return template;
    }

    public int getPDVMAX() {
        return stats.getEffect(Effect.ADD_VITA);
    }

    public int getGrade() {
        return grade;
    }

    public int getPA() {
        return stats.getEffect(Effect.ADD_PA);
    }

    public int getPM() {
        return stats.getEffect(Effect.ADD_PM);
    }

    public Monster modifStatByInvocator(Fighter caster) {
        Monster monster = new Monster(this);
        double coef = (caster.get_lvl()) / 100;
        double vita = stats.getEffect(Effect.ADD_VITA) * coef;
        double force = stats.getEffect(Effect.ADD_FORC) * coef;
        double intel = stats.getEffect(Effect.ADD_INTE) * coef;
        double agili = stats.getEffect(Effect.ADD_AGIL) * coef;
        double sages = stats.getEffect(Effect.ADD_SAGE) * coef;
        double chanc = stats.getEffect(Effect.ADD_CHAN) * coef;
        monster.stats.addOneStat(Effect.ADD_FORC, (int) force);
        monster.stats.addOneStat(Effect.ADD_INTE, (int) intel);
        monster.stats.addOneStat(Effect.ADD_AGIL, (int) agili);
        monster.stats.addOneStat(Effect.ADD_SAGE, (int) sages);
        monster.stats.addOneStat(Effect.ADD_CHAN, (int) chanc);
        monster.stats.addOneStat(Effect.ADD_VITA, (int) vita);
        return monster;
    }

    @Override
    public String toString() {
        return "Monster{" + "template=" + template + ", grade=" + grade + ", level=" + level + '}';
    }

}
