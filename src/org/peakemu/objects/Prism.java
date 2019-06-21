package org.peakemu.objects;

import org.peakemu.world.GameMap;
import java.util.Map;

import org.peakemu.world.World;
import org.peakemu.world.Area;
import org.peakemu.world.MapCell;
import org.peakemu.world.SpellLevel;
import org.peakemu.world.Sprite;
import org.peakemu.world.Stats;
import org.peakemu.world.SubArea;
import org.peakemu.world.enums.Effect;
import org.peakemu.world.enums.SpriteTypeEnum;
import org.peakemu.world.fight.ConquestFight;


public class Prism implements Sprite{
    final static public String SPELLS = "56@6;24@6;157@6;63@6;8@6;81@6";
    
	final private int id;
	final private int alignement;
	private int level;
	final private GameMap map;
	private MapCell cell;
	private int _xY;
	private int _nom;
	private int _gfx;
	private ConquestFight fight;
	private int _honor = 0;
    private boolean isOnArea;
	private Stats _stats = new Stats();
    final private Map<Integer, SpellLevel> spells;
    
    private int spriteId = 0;
	
	public Prism(int id, int alignement, int lvl, GameMap map, MapCell cell, int honor, boolean isOnArea, Map<Integer, SpellLevel> spells) {
		this.id = id;
		this.alignement = alignement;
		this.level = lvl;
		this.map = map;
		this.cell = cell;
		this._xY = 1;
		if (alignement == 1) {
			_nom = 1111;
			_gfx = 8101;
		} else {
			_nom = 1112;
			_gfx = 8100;
		}
		_honor = honor;
        this.isOnArea = isOnArea;
        this.spells = spells;
	}
    
    public Prism copy(int newId){
        return new Prism(newId, alignement, level, map, cell, _honor, isOnArea, spells);
    }
	
    @Override
	public int getSpriteId() {
		return spriteId;
	}

    public void setSpriteId(int spriteId) {
        this.spriteId = spriteId;
    }
    
    public int getPrismUniqueId(){
        return id;
    }
	
	public int getAlignement() {
		return alignement;
	}
	
	public int getLevel() {
		return level;
	}
	
	public GameMap getMap() {
		return map;
	}
	
    @Override
	public MapCell getCell() {
		return cell;
	}
	
	public Stats getStats() {
		return _stats;
	}
	
	public Map<Integer, SpellLevel> getSpells() {
		return spells;
	}
	
	public void refreshStats() {
		int force = 1000 + (500 * level);
		int intel = 1000 + (500 * level);
		int agi = 1000 + (500 * level);
		int sagesse = 1000 + (500 * level);
		int chance = 1000 + (500 * level);
		int resistance = 9 * level;
		_stats = new Stats();
		_stats.addOneStat(Effect.ADD_FORC, force);
		_stats.addOneStat(Effect.ADD_INTE, intel);
		_stats.addOneStat(Effect.ADD_AGIL, agi);
		_stats.addOneStat(Effect.ADD_SAGE, sagesse);
		_stats.addOneStat(Effect.ADD_CHAN, chance);
		_stats.addOneStat(Effect.ADD_RP_NEU, resistance);
		_stats.addOneStat(Effect.ADD_RP_FEU, resistance);
		_stats.addOneStat(Effect.ADD_RP_EAU, resistance);
		_stats.addOneStat(Effect.ADD_RP_AIR, resistance);
		_stats.addOneStat(Effect.ADD_RP_TER, resistance);
		_stats.addOneStat(Effect.ADD_AFLEE, resistance);
		_stats.addOneStat(Effect.ADD_MFLEE, resistance);
		_stats.addOneStat(Effect.ADD_PA, 6);
		_stats.addOneStat(Effect.ADD_PM, 0);
	}
	
    public boolean isOnFight(){
        return fight != null;
    }

    public ConquestFight getFight() {
        return fight;
    }

    public void setFight(ConquestFight fight) {
        this.fight = fight;
    }
	
	public int getTurnTime() {
        if(fight == null)
            return -1;
        
		return (int)fight.getRemaningTime();
	}
	
	public int getX() {
		return map.getX();
	}
	
	public int getY() {
		return map.getY();
	}
	
	public SubArea getSubArea() {
		return map.getSubArea();
	}
	
	public Area getArea() {
		return getSubArea().getArea();
	}
	
	public int getAlinSubArea() {
		return getSubArea().getAlignement();
	}
	
	public int getAlinArea() {
		return getArea().getalignement();
	}
	
	public int getHonor() {
		return _honor;
	}
	
	public void addHonor(int honor) {
		_honor += honor;
	}

    public void setLevel(int level) {
        this.level = level;
        refreshStats();
    }
	
	public void setCell(MapCell cell) {
		this.cell = cell;
	}

    @Override
    public SpriteTypeEnum getSpriteType() {
        return SpriteTypeEnum.PRISM;
    }
	
    @Override
	public String getSpritePacket() {
		String str = cell.getID() + ";";
		str += _xY + ";0;" + getSpriteId() + ";" + _nom + ";" + getSpriteType().toInt() + ";" + _gfx + "^100;" + level + ";" + 10 + ";" + alignement;
		return str;
	}

    public boolean isOnArea() {
        return isOnArea;
    }

    public void setIsOnArea(boolean isOnArea) {
        this.isOnArea = isOnArea;
    }
}
