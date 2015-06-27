package tk.nirvanagamestudios.mavicci.items;

import java.util.List;

import tk.nirvanagamestudios.mavicci.entities.Entity;
import tk.nirvanagamestudios.mavicci.items.enchantments.Enchantment;

public class Item {

	public Entity entity;
	public int textureIcon;
	public String name;
	public List<Enchantment> enchantments;
	public int value;
	public int weight;
	public int category;
	
	public enum type{
		WEAPON, APPAREL, MISC, POTION, METAL, BOOK, GEMSTONE, HERB, FOOD, DEVELOPERS; 
	}

	public Item(Entity e, String n, List<Enchantment> en, int t, int weight, int value, int textureID){
		this.entity = e;
		this.name = n; 
		this.value = value;
		this.weight = weight;
		this.textureIcon = textureID;
		this.enchantments = en;
		this.category = t;
	}
	
	
}
