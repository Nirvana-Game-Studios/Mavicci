package tk.nirvanagamestudios.mavicci.engineTest;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import tk.nirvanagamestudios.mavicci.entities.Camera;
import tk.nirvanagamestudios.mavicci.entities.Entity;
import tk.nirvanagamestudios.mavicci.entities.Light;
import tk.nirvanagamestudios.mavicci.models.RawModel;
import tk.nirvanagamestudios.mavicci.models.TexturedModel;
import tk.nirvanagamestudios.mavicci.renderEngine.DisplayManager;
import tk.nirvanagamestudios.mavicci.renderEngine.Loader;
import tk.nirvanagamestudios.mavicci.renderEngine.MasterRenderer;
import tk.nirvanagamestudios.mavicci.renderEngine.OBJLoader;
import tk.nirvanagamestudios.mavicci.textures.ModelTexture;

/*
 * To-do list
 * 
 * Make Window(16:9 ASPECT RATIO) - Done
 * OpenGL Research - Done
 * Graphics Research - Done
 * LWJGL Research - Done
 * 2D Testing - Done
 * Shader Colouring - Done
 * 3D Testing - Done
 * Per-pixel Lighting - Done 
 * Specular Lighting - Done
 * Terrain
 * 3D Optimisation
 * Write Storyline
 * Complete Matrix Stuff - Done
 * 
 */

public class MainGameLoop {
	
	public static List<Entity> entities = new ArrayList<Entity>();
	
	public static void main(String[] args){
		DisplayManager displayManager = new DisplayManager();
		displayManager.createDisplay();
		
		Loader loader = new Loader();
		MasterRenderer renderer = new MasterRenderer();
		
		RawModel model = OBJLoader.loadObjModel("stall", loader);
		TexturedModel staticModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("stallTexture")));
		ModelTexture texture = staticModel.getTexture();
		texture.setReflectivity(1);
		texture.setShineDamper(10);
		Entity entity = createEntity(staticModel, new Vector3f(0,0,-25),0,0,0,1);
		Light light = new Light(new Vector3f(0,10,-20), new Vector3f(1,1,1));
		
		Camera camera = new Camera();
		
		entity.setRotY(180);
		entity.setRotX(12);
		while(!Display.isCloseRequested()){
			//entity.increaseRotation(0, 5, 0);
			camera.move();
			for(Entity e:entities){
				renderer.processEntity(e);
			}
			renderer.render(light, camera);
			displayManager.updateDisplay();
		}
		
		loader.cleanUp();

		displayManager.stopDisplay();
	}
	
	public static Entity createEntity(TexturedModel model, Vector3f pos, float rotX, float rotY, float rotZ, float scale){
		Entity entity = new Entity(model, pos, rotX, rotY, rotZ, scale);
		entities.add(entity);
		return entity;
	}
	
}
