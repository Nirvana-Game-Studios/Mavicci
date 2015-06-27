package tk.nirvanagamestudios.mavicci.items;

import java.util.ArrayList;
import java.util.List;

public class ItemHandler {
	
	List<Item> items;
	
	static List<Item> apparelItems = new ArrayList<Item>();
	static List<Item> bookItems = new ArrayList<Item>();
	static List<Item> foodItems = new ArrayList<Item>();
	static List<Item> gemstoneItems = new ArrayList<Item>();
	static List<Item> herbItems = new ArrayList<Item>();
	static List<Item> metalItems = new ArrayList<Item>();
	static List<Item> miscItems = new ArrayList<Item>();
	static List<Item> potionItems = new ArrayList<Item>();
	static List<Item> weaponItems = new ArrayList<Item>();
	static List<Item> developerItems = new ArrayList<Item>();
	
	public void sortItems(List<Item> items){
		this.items = items;
		for(Item i:items){
			if(i.category == Item.type.APPAREL.ordinal()){
				apparelItems.add(i);
			}else if(i.category == Item.type.BOOK.ordinal()){
				bookItems.add(i);
			}else if(i.category == Item.type.FOOD.ordinal()){
				foodItems.add(i);
			}else if(i.category == Item.type.GEMSTONE.ordinal()){
				gemstoneItems.add(i);
			}else if(i.category == Item.type.HERB.ordinal()){
				herbItems.add(i);
			}else if(i.category == Item.type.METAL.ordinal()){
				metalItems.add(i);
			}else if(i.category == Item.type.MISC.ordinal()){
				miscItems.add(i);
			}else if(i.category == Item.type.POTION.ordinal()){
				potionItems.add(i);
			}else if(i.category == Item.type.WEAPON.ordinal()){
				weaponItems.add(i);
			}else{
				developerItems.add(i);
			}
		}
	}
	
	public static List<Item> getApparel(){
		return apparelItems;
	}

	public static List<Item> getBooks(){
		return bookItems;
	}
	
	public static List<Item> getFood(){
		return foodItems;
	}
	
	public static List<Item> getGemstones(){
		return gemstoneItems;
	}
	
	public static List<Item> getHerbs(){
		return herbItems;
	}
	
	public static List<Item> getMetals(){
		return metalItems;
	}
	
	public static List<Item> getMisc(){
		return miscItems;
	}
	
	public static List<Item> getPotions(){
		return potionItems;
	}
	
	public static List<Item> getWeapons(){
		return weaponItems;
	}
	
	public static List<Item> getDevelopers(){
		return developerItems;
	}
	
}
