package tk.nirvanagamestudios.mavicci.guis.hud;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import tk.nirvanagamestudios.mavicci.guis.Button;
import tk.nirvanagamestudios.mavicci.guis.GuiTexture;
import tk.nirvanagamestudios.mavicci.renderEngine.Loader;

public class Hotbar {

	private Button b1;
	private Button b2;
	private Button b3;
	private Button b4;
	private Button b5;
	
	public Hotbar(){
		b1 = new Button(new GuiTexture(Loader.loadGUITexture("hotbarIcon"), new Vector2f(0.9f, 0.8f), new Vector2f(0.07f, 0.125f)), new GuiTexture(Loader.loadGUITexture("hotbarIconHover"), new Vector2f(0.8f, 0.4f), new Vector2f(1f, 1f)), "hotbar");
		b2 = new Button(new GuiTexture(Loader.loadGUITexture("hotbarIcon"), new Vector2f(0.9f, 0.4f), new Vector2f(0.07f, 0.125f)), new GuiTexture(Loader.loadGUITexture("hotbarIconHover"), new Vector2f(0.8f, 0.4f), new Vector2f(1f, 1f)), "hotbar");
		b3 = new Button(new GuiTexture(Loader.loadGUITexture("hotbarIcon"), new Vector2f(0.9f, 0.0f), new Vector2f(0.07f, 0.125f)), new GuiTexture(Loader.loadGUITexture("hotbarIconHover"), new Vector2f(0.8f, 0.4f), new Vector2f(1f, 1f)), "hotbar");
		b4 = new Button(new GuiTexture(Loader.loadGUITexture("hotbarIcon"), new Vector2f(0.9f, -0.4f), new Vector2f(0.07f, 0.125f)), new GuiTexture(Loader.loadGUITexture("hotbarIconHover"), new Vector2f(0.8f, 0.4f), new Vector2f(1f, 1f)), "hotbar");
		b5 = new Button(new GuiTexture(Loader.loadGUITexture("hotbarIcon"), new Vector2f(0.9f, -0.8f), new Vector2f(0.07f, 0.125f)), new GuiTexture(Loader.loadGUITexture("hotbarIconHover"), new Vector2f(0.8f, 0.4f), new Vector2f(1f, 1f)), "hotbar");
	}
	
	public List<Button> getHotbarButtons(){
		List<Button> buttons = new ArrayList<Button>();
		buttons.add(b1);
		buttons.add(b2);
		buttons.add(b3);
		buttons.add(b4);
		buttons.add(b5);
		return buttons;
	}
	
	
	
}
