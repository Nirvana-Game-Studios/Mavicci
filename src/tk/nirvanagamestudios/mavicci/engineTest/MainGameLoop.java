package tk.nirvanagamestudios.mavicci.engineTest;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import tk.nirvanagamestudios.mavicci.entities.Camera;
import tk.nirvanagamestudios.mavicci.entities.Entity;
import tk.nirvanagamestudios.mavicci.entities.Light;
import tk.nirvanagamestudios.mavicci.models.RawModel;
import tk.nirvanagamestudios.mavicci.models.TexturedModel;
import tk.nirvanagamestudios.mavicci.renderEngine.DisplayManager;
import tk.nirvanagamestudios.mavicci.renderEngine.Loader;
import tk.nirvanagamestudios.mavicci.renderEngine.OBJLoader;
import tk.nirvanagamestudios.mavicci.renderEngine.Renderer;
import tk.nirvanagamestudios.mavicci.shaders.StaticShader;
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
	
	public static void main(String[] args){
		DisplayManager displayManager = new DisplayManager();
		displayManager.createDisplay();
		
		Loader loader = new Loader();
		StaticShader shader = new StaticShader();
		Renderer renderer = new Renderer(shader);
		
		RawModel model = OBJLoader.loadObjModel("stall", loader);
		TexturedModel staticModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("stallTexture")));
		ModelTexture texture = staticModel.getTexture();
		texture.setReflectivity(1);
		texture.setShineDamper(10);
		Entity entity = new Entity(staticModel, new Vector3f(0,0,-25),0,0,0,1);
		Light light = new Light(new Vector3f(0,10,-20), new Vector3f(1,1,1));
		
		Camera camera = new Camera();
		
		entity.setRotY(180);
		entity.setRotX(12);
		while(!Display.isCloseRequested()){
			//entity.increaseRotation(0, 5, 0);
			camera.move();
			renderer.prepare();
			shader.start();
			shader.loadLight(light);
			shader.loadViewMatrix(camera);
			renderer.render(entity, shader);
			shader.stop();
			displayManager.updateDisplay();
		}
		
		loader.cleanUp();
		shader.cleanUp();
		displayManager.stopDisplay();
	}
	
}
