package tk.nirvanagamestudios.mavicci.engineTest;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import tk.nirvanagamestudios.mavicci.entities.Camera;
import tk.nirvanagamestudios.mavicci.entities.Entity;
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
 * 3D Testing - Complete on Friday.
 * Per-pixel Lighting
 * Specular Lighting
 * Terrain
 * Write Storyline
 * Complete Matrix Stuff - Done
 * 
 */

public class MainGameLoop {

	static Loader loader;
	static StaticShader shader;
	
	public static void main(String[] args){
		DisplayManager displayManager = new DisplayManager();
		displayManager.createDisplay();
		
		loader = new Loader();
		shader = new StaticShader();
		Renderer renderer = new Renderer(shader);
		
		RawModel model = OBJLoader.loadObjModel("stall", loader);
	
		ModelTexture texture = new ModelTexture(loader.loadTexture("stallTexture"));
		TexturedModel staticModel = new TexturedModel(model, texture);
		Entity entity = new Entity(staticModel, new Vector3f(0,0,-50),0,0,0,1);
		
		Camera camera = new Camera();
		
		while(!Display.isCloseRequested()){
			//entity.increaseRotation(1, 1, 0);
			camera.move();
			renderer.prepare();
			shader.start();
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
