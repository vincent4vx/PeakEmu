package org.peakemu.world;

import org.peakemu.objects.Monster;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.peakemu.world.World.*;

public class MonsterTemplate {

    final private int ID;
    final private int gfxID;
    final private int align;
    final private String colors;
    final private int IAType;
    final private int minKamas;
    final private int maxKamas;
    private List<Monster> grades = null;
    final private ArrayList<Drop> drops = new ArrayList<>();
    final private boolean isCapturable;
    final private int assaultDistance = -1;

    public MonsterTemplate(int Aid, int agfx, int Aalign, String Acolors, int mK, int MK, String xpstr, int IAtype, boolean capturable) {
        ID = Aid;
        gfxID = agfx;
        align = Aalign;
        colors = Acolors;
        minKamas = mK;
        maxKamas = MK;
        IAType = IAtype;
        isCapturable = capturable;
    }

    public void setGrades(List<Monster> grades) {
        if(this.grades != null)
            throw new IllegalStateException("Grades are already set");
        
        this.grades = Collections.unmodifiableList(grades);
    }

    public int getID() {
        return ID;
    }

    public void addDrop(Drop D) {
        drops.add(D);
    }

    public ArrayList<Drop> getDrops() {
        return drops;
    }

    public int getGfxID() {
        return gfxID;
    }

    public int getMinKamas() {
        return minKamas;
    }

    public int getMaxKamas() {
        return maxKamas;
    }

    public int getAlign() {
        return align;
    }

    public String getColors() {
        return colors;
    }

    public int getIAType() {
        return IAType;
    }

    public List<Monster> getGrades() {
        return grades;
    }

    public Monster getGradeByLevel(int lvl) {
        for (Monster monster : grades) {
            if (monster.getLevel() == lvl) {
                return monster;
            }
        }
        return null;
    }
    
    public Monster getFirstGrade(){
        return grades.get(0);
    }

    public Monster getRandomGrade() {
        int randomgrade = (int) (Math.random() * (6 - 1)) + 1;
        int graderandom = 1;
        for (Monster grade : grades) {
            if (graderandom == randomgrade) {
                return grade;
            } else {
                graderandom++;
            }
        }
        return null;
    }

    public boolean isCapturable() {
        return this.isCapturable;
    }

    public int getAssaultDistance() {
        return assaultDistance;
    }

    @Override
    public String toString() {
        return "MonsterTemplate{" + "ID=" + ID + ", gfxID=" + gfxID + '}';
    }
    
}
