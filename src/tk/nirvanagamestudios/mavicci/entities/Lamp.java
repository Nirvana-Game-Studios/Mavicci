package tk.nirvanagamestudios.mavicci.entities;

import org.lwjgl.util.vector.Vector3f;

import tk.nirvanagamestudios.mavicci.models.RawModel;
import tk.nirvanagamestudios.mavicci.models.TexturedModel;
import tk.nirvanagamestudios.mavicci.renderEngine.Loader;
import tk.nirvanagamestudios.mavicci.renderEngine.OBJLoader;
import tk.nirvanagamestudios.mavicci.textures.ModelTexture;

public class Lamp{
	
	private Vector3f position;
	private Vector3f lightPosition;
	private Vector3f colour;
	private Vector3f attenuation = new Vector3f(1,0,0);
	private Entity lamp;
	private Light light;

	private Loader loader;
	
	public Lamp(Vector3f pos, Vector3f colour, Vector3f attenuation){
		this.position = pos;
		this.lightPosition = new Vector3f(pos.x,pos.y+12f,pos.z);
		this.colour = colour;
		this.attenuation = attenuation;
		this.loader = new Loader();
		ModelTexture texture = new ModelTexture(loader.loadTexture("lamp"));
		RawModel model = OBJLoader.loadObjModel("lamp", loader);
		TexturedModel texturedModel = new TexturedModel(model, texture);
		lamp = new Entity(texturedModel, position, 0, 0, 0, 1);
		lamp.getModel().getTexture().setUseFakeLighting(true);
		light = new Light(lightPosition, colour, attenuation);
	}
	
	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getColour() {
		return colour;
	}

	public Vector3f getAttenuation() {
		return attenuation;
	}

	public Entity getEntity() {
		return lamp;
	}

	public Light getLight() {
		return light;
	}
	
	public void increasePosition(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
		this.lightPosition.x += dx;
		this.lightPosition.y += dy;
		this.lightPosition.z += dz;
	}

	public void increasetColor(float dx, float dy, float dz) {
		this.colour.x += dx;
		this.colour.y += dy;
		this.colour.z += dz;
	}

	
}
