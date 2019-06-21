package org.peakemu.objects;

public class Fireworks {
	
	private int ID;
	private int AnimationId;
	private String AnimationName;
	private int AnimationArea;
	private int AnimationAction;
	private int AnimationSize;
	
	public Fireworks(int Id, int AnimId, String Name, int Area, int Action, int Size) 
	{
		this.ID = Id;
		this.AnimationId = AnimId;
		this.AnimationName = Name;
		this.AnimationArea = Area;
		this.AnimationAction = Action;
		this.AnimationSize = Size;
	}
	
	public int getId() 
	{
		return ID;
	}
	
	public String getName() 
	{
		return AnimationName;
	}
	
	public int getArea() 
	{
		return AnimationArea;
	}
	
	public int getAction() 
	{
		return AnimationAction;
	}
	
	public int getSize() 
	{
		return AnimationSize;
	}
	
	public int getAnimationId() 
	{
		return AnimationId;
	}
	
	public static String PrepareToGA(Fireworks animation) {
	String Packet;
	Packet = animation.getAnimationId() + "," + animation.getArea() + "," + animation.getAction() + "," + animation.getSize();
	return Packet;
	}
	
}