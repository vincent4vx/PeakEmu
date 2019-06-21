package org.peakemu.objects.item;
import java.util.ArrayList;

import org.peakemu.world.World.Couple;
import org.peakemu.world.ItemTemplate;
import org.peakemu.world.StatsTemplate;
import org.peakemu.world.enums.InventoryPosition;

public class PierreAme extends Item{
	
	private ArrayList<Couple<Integer, Integer>> _monsters;
	
	public PierreAme (String owner, int Guid, int qua, ItemTemplate template, InventoryPosition pos, StatsTemplate statsTemplate)
	{
        super(owner, Guid, template, qua, pos, statsTemplate);
		_monsters = new ArrayList<Couple<Integer, Integer>>();	//Couple<MonstreID,Level>
	}
	
	public void parseStringToStats(String monsters) //Dans le format "monstreID,lvl|monstreID,lvl..."
	{
		String[] split = monsters.split("\\|");
		for(String s : split)
		{	
			try
			{
				int monstre = Integer.parseInt(s.split(",")[0]);
				int level = Integer.parseInt(s.split(",")[1]);
				
				_monsters.add(new Couple<Integer, Integer>(monstre, level));
				
			}catch(Exception e){continue;};
		}
	}
	
	public String parseStatsString()
	{
		StringBuilder stats = new StringBuilder();
		boolean isFirst = true;
		for(Couple<Integer, Integer> coupl : _monsters)
		{
			if(!isFirst)
				stats.append(",");
			
			try
			{
				stats.append("26f#0#0#").append(Integer.toHexString(coupl.first));
			}catch(Exception e)
			{
				e.printStackTrace();
				continue;
			};
			
			isFirst = false;
		}
		return stats.toString();
	}
	
	public String parseGroupData()//Format : id,lvlMin,lvlMax;id,lvlMin,lvlMax...
	{
		StringBuilder toReturn = new StringBuilder();
		boolean isFirst = true;
		for(Couple<Integer, Integer> curMob : _monsters)
		{
			if(!isFirst)
				toReturn.append(";");
			
			toReturn.append(curMob.first).append(",").append(curMob.second).append(",").append(curMob.second);
			
			isFirst = false;
		}
		return toReturn.toString();
	}
	
	public String parseToSave()
	{
		StringBuilder toReturn = new StringBuilder();
		boolean isFirst = true;
		for(Couple<Integer, Integer> curMob : _monsters)
		{
			if(!isFirst)
				toReturn.append("|");
			toReturn.append(curMob.first).append(",").append(curMob.second);
			isFirst = false;
		}
		return toReturn.toString();
	}
}
