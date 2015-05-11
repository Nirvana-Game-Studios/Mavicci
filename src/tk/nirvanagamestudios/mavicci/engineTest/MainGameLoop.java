package tk.nirvanagamestudios.mavicci.engineTest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import tk.nirvanagamestudios.mavicci.entities.Camera;
import tk.nirvanagamestudios.mavicci.entities.Entity;
import tk.nirvanagamestudios.mavicci.entities.Light;
import tk.nirvanagamestudios.mavicci.entities.Player;
import tk.nirvanagamestudios.mavicci.guis.GuiRenderer;
import tk.nirvanagamestudios.mavicci.guis.GuiTexture;
import tk.nirvanagamestudios.mavicci.models.RawModel;
import tk.nirvanagamestudios.mavicci.models.TexturedModel;
import tk.nirvanagamestudios.mavicci.renderEngine.DisplayManager;
import tk.nirvanagamestudios.mavicci.renderEngine.Loader;
import tk.nirvanagamestudios.mavicci.renderEngine.MasterRenderer;
import tk.nirvanagamestudios.mavicci.renderEngine.OBJLoader;
import tk.nirvanagamestudios.mavicci.terrains.Terrain;
import tk.nirvanagamestudios.mavicci.textures.ModelTexture;
import tk.nirvanagamestudios.mavicci.textures.TerrainTexture;
import tk.nirvanagamestudios.mavicci.textures.TerrainTexturePack;

/*
 * To-do list
 * 
 * Make Window(16:9 ASPECT RATIO) - Done - 09/05/15
 * OpenGL Research - Done - 09/05/15
 * Graphics Research - Done - 09/05/15
 * LWJGL Research - Done - 09/05/15
 * Trigonometry Research - Done - 09/05/15
 * 2D Testing - Done - 09/05/15
 * Shader Colouring - Done - 09/05/15
 * 3D Testing - Done - 09/05/15
 * Per-pixel Lighting - Done - 09/05/15
 * Specular Lighting - Done - 09/05/15
 * Terrain - Done - 09/05/15
 * 3D Optimisation - Done - 09/05/15
 * Write Storyline
 * Complete Matrix Stuff - Done - 09/05/15
 * Transparent Compatibility - Done - 09/05/15
 * Multitexturing Terrain - Done - 09/05/15
 * Fog - Done - 09/05/15
 * Mipmapping - Done  - 10/05/15
 * Terrain Collision - Done - 10/05/15
 * Heightmaps - Done - 10/05/15
 * Player Movement - Done - 10/05/15
 * 3rd Person Camera - Done - 10/05/15
 */

public class MainGameLoop {

	public static List<Entity> entities = new ArrayList<Entity>();
	public static List<Terrain> terrains = new ArrayList<Terrain>();
	public static MasterRenderer renderer;
	public static Random random = new Random();
	
	public static void main(String[] args) {
		System.setProperty("org.lwjgl.librarypath", new File("natives/windows/").getAbsolutePath());
		DisplayManager displayManager = new DisplayManager();
		displayManager.createDisplay();

		Loader loader = new Loader();
		renderer = new MasterRenderer();

		RawModel model = OBJLoader.loadObjModel("stall", loader);
		TexturedModel staticModel = new TexturedModel(model, new ModelTexture(
				loader.loadTexture("stallTexture")));
		ModelTexture texture = staticModel.getTexture();
		texture.setReflectivity(1);
		texture.setShineDamper(10);
		Entity entity = createEntity(staticModel, new Vector3f(0, 0, -25), 0,
				0, 0, 1, 5);
		Entity grass = createEntity(new TexturedModel(OBJLoader.loadObjModel(
				"grassModel", loader), new ModelTexture(
						loader.loadTexture("grassTexture"))), new Vector3f(
								random.nextFloat() * 800 - 400, 1, random.nextFloat() * -600), 0, 0, 0,
				1, 100);
		
		RawModel player = OBJLoader.loadObjModel("player", loader);
		ModelTexture playerTexture = new ModelTexture(loader.loadTexture("playerTexture"));
		TexturedModel playerModel = new TexturedModel(player, playerTexture);
		Player playerEntity = new Player(playerModel, new Vector3f(-100, 200, -50), 0, 0, 0, 1);
		
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grass"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("path"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grass"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
		
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		
		Terrain terrain = new Terrain(-1, -1, loader, texturePack, blendMap, "heightmap");
		grass.getModel().getTexture().setHasTransparency(true);
		// Terrain terrain1 = new Terrain(1,-1, loader, new
		// ModelTexture(loader.loadTexture("grass")));
		Light light = new Light(new Vector3f(0, 20000, 20000), new Vector3f(1, 1, 1));

		Camera camera = new Camera(playerEntity);

		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		GuiTexture gui = new GuiTexture(loader.loadTexture("logo"), new Vector2f(0.5f, 0.5f), new Vector2f(0.25f, 0.25f));
		guis.add(gui);
		
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		
		entity.setRotY(180);
		entity.setPosition(new Vector3f(-30, 1, -16));
		while (!Display.isCloseRequested()) {
			// entity.increaseRotation(0, 5, 0);
			camera.move();
			playerEntity.move(terrain);
			renderer.processEntity(grass);
			renderer.processEntity(entity);
			renderer.processEntity(playerEntity);
			renderer.processTerrain(terrain);
			// renderer.processTerrain(terrain1);
			// processAll();
			renderer.render(light, camera);
			guiRenderer.render(guis);
			displayManager.updateDisplay();
		}

		loader.cleanUp();

		displayManager.stopDisplay();
	}

	public static Entity createEntity(TexturedModel model, Vector3f pos,
			float rotX, float rotY, float rotZ, float scale, float amount) {
			Entity entity = new Entity(model, pos, rotX, rotY, rotZ, scale); 
		for(int i = 0; i < amount; i++){
			entity = new Entity(model, pos, rotX, rotY, rotZ, scale);
			entities.add(entity);
		}
		return entity;
	}


}
