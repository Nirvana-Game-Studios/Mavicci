package tk.nirvanagamestudios.mavicci.guis;

import org.lwjgl.util.vector.Vector2f;

public class GuiTexture {

	private int texture;
	private Vector2f position;
	private Vector2f scale;
	private Vector2f size;
	
	public GuiTexture(int texture, Vector2f position, Vector2f scale) {
		super();
		this.texture = texture;
		this.position = position;
		this.scale = scale;
	}
	
	public GuiTexture(int texture, Vector2f position, Vector2f scale, Vector2f size) {
		super();
		this.texture = texture;
		this.position = position;
		this.scale = scale;
		this.size = size;
	}

	public int getTexture() {
		return texture;
	}

	public Vector2f getSize(){
		return size;
	}
	
	public Vector2f getPosition() {
		return position;
	}

	public Vector2f getScale() {
		return scale;
	}
	
}
