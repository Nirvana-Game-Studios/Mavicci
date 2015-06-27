package tk.nirvanagamestudios.mavicci.guis;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

import tk.nirvanagamestudios.mavicci.engineTest.MainGameLoop;
import tk.nirvanagamestudios.mavicci.renderEngine.DisplayManager;

public class Button {

	private GuiTexture texture1;
	private GuiTexture texture2;
	private GuiTexture overall;
	private boolean isMouseOver;
	private Vector2f size;
	private Vector2f scale;
	private Vector2f position;
	private String type;

	public Button(GuiTexture defaultTexture, GuiTexture hoverTexture, String type) {
		this.overall = defaultTexture;
		this.texture1 = defaultTexture;
		this.texture2 = hoverTexture;
		this.size = texture1.getSize();
		this.position = texture1.getPosition();
		this.scale = texture1.getScale();
		this.type = type;
	}

	public void update() {
		float cartesianX = (float) (-1.0 + 2.0 * Mouse.getX()
				/ DisplayManager.getWidth());
		float cartesianY = (float) (-1.0 + 2.0 * Mouse.getY()
				/ DisplayManager.getHeight());
		if (cartesianX > position.x - 1 * scale.x
				&& cartesianX < position.x + 1 * scale.x) {
			if (cartesianY > position.y - (1 * scale.y)
					&& cartesianY < position.y + (1 * scale.y)) {
				isMouseOver = true;
				setTexture(texture2);
				if (Mouse.isButtonDown(0)) {
					if(!MainGameLoop.inPause && type == "menu"){
						MainGameLoop.pause();
					}else if(type == "play"){
						MainGameLoop.unpause();
					}else if(type == "options"){
						
					}else if(type == "statistics"){
						
					}else if(type == "save"){
						
					}else if(type == "load"){
						
					}else if(type == "quit"){
						MainGameLoop.exitGame();
					}else if(type == "hotbar"){
						//MainGameLoop.getPlayer().equip
					}
				} else {
					while (Keyboard.next()) {
						if (Keyboard.getEventKeyState()) {
						} else {
							if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
								if(MainGameLoop.inPause){
									MainGameLoop.unpause();
								}
							}
						}
					}
				}
			} else {
				setTexture(texture1);
				isMouseOver = false;
			}
		} else {
			setTexture(texture1);
			isMouseOver = false;
		}
	}

	public GuiTexture getTexture() {
		return overall;
	}

	public void setTexture(GuiTexture gui) {
		this.overall = gui;
	}
}
