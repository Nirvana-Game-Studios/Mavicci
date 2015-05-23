package tk.nirvanagamestudios.mavicci.engineTest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
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
import tk.nirvanagamestudios.mavicci.util.MousePicker;

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
 * Texture Atlas - Done - 16/05/15
 * Multiple Lights - Done - 17/05/15
 * Point Lights - 17/05/15
 * Day and Night Cycle
 * Mouse Picking
 */


public class MainGameLoop {

	public static List<Entity> entities = new ArrayList<Entity>();
	public static List<Terrain> terrains = new ArrayList<Terrain>();
	public static List<Light> lights = new ArrayList<Light>();
	public static MasterRenderer renderer;
	
	public static void main(String[] args) {
		System.setProperty("org.lwjgl.librarypath", new File("natives/windows/").getAbsolutePath());
		DisplayManager displayManager = new DisplayManager();
		displayManager.createDisplay();

		Loader loader = new Loader();
		renderer = new MasterRenderer();
		
		ModelTexture fernTextureAtlas = new ModelTexture(loader.loadTexture("fern"));
		fernTextureAtlas.setNumberOfRows(2);
		fernTextureAtlas.setHasTransparency(true);
		TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern", loader), fernTextureAtlas);
		
		ModelTexture treeTexture = new ModelTexture(loader.loadTexture("tree"));
		TexturedModel tree = new TexturedModel(OBJLoader.loadObjModel("tree", loader), treeTexture);
		
		ModelTexture pineTexture = new ModelTexture(loader.loadTexture("pine"));
		TexturedModel pine = new TexturedModel(OBJLoader.loadObjModel("pine", loader), pineTexture);

		ModelTexture lampTexture = new ModelTexture(loader.loadTexture("lamp"));
		lampTexture.setUseFakeLighting(true);
		TexturedModel lamp = new TexturedModel(OBJLoader.loadObjModel("lamp", loader), lampTexture);
		lamp.getTexture().setUseFakeLighting(true);
		Entity lampEntity = createEntity(lamp, new Vector3f(0,0,0),0f,0f,0f,1f,1);
		
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grass"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("path"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grass"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
		
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		
		Terrain terrain = new Terrain(-1, -1, loader, texturePack, blendMap, "heightmap");
		//Terrain terrain1 = new Terrain(-1, -1, loader, texturePack, blendMap, "heightmap");
		
		Random random = new Random(676452);
		for(int i = 0; i < 400; i++){
			if(i % 2 == 0){
				float x = random.nextFloat() * 800 - 400;
				float z = random.nextFloat() * -600;
				float y = terrain.getHeightOfTerrain(x, z);
				
				entities.add(new Entity(fern, new Vector3f(x, y, z), 0, random.nextFloat(), 0, 0.9f, random.nextInt(4)));
			}
			if(i % 4 == 0){
				float x = random.nextFloat() * 800 - 400;
				float z = random.nextFloat() * -600;
				float y = terrain.getHeightOfTerrain(x, z);
				
				entities.add(new Entity(pine, new Vector3f(x, y, z), 0, random.nextFloat(), 0, 1f, random.nextInt(4)));
			}
			if(i % 5 == 0){
				float x = random.nextFloat() * 800 - 400;
				float z = random.nextFloat() * -600;
				float y = terrain.getHeightOfTerrain(x, z);
				
				lights.add(new Light(new Vector3f(x, y + 12f, z), new Vector3f(1,0,0), new Vector3f(1, 0.01f, 0.002f)));
				entities.add(new Entity(lamp, new Vector3f(x, y, z), 0, random.nextFloat(), 0, 1f, random.nextInt(4)));
			}
			if(i % 6 == 0){
				float x = random.nextFloat() * 800 - 400;
				float z = random.nextFloat() * -600;
				float y = terrain.getHeightOfTerrain(x, z);
				
				entities.add(new Entity(tree, new Vector3f(x, y, z), 0, random.nextFloat(), 0, 1f, random.nextInt(4)));
			}
		}
		
		
		RawModel player = OBJLoader.loadObjModel("player", loader);
		ModelTexture playerTexture = new ModelTexture(loader.loadTexture("playerTexture"));
		TexturedModel playerModel = new TexturedModel(player, playerTexture);
		Player playerEntity = new Player(playerModel, new Vector3f(-250, terrain.getHeightOfTerrain(terrain.getX(), terrain.getZ()), -290), 0, 0, 0, 1);
		
		Light light = new Light(new Vector3f(0f, 10000f, -7000f), new Vector3f(1f, 1f, 1f));
		Light lampLight = new Light(new Vector3f(lampEntity.getPosition().x, lampEntity.getPosition().y + 12f, lampEntity.getPosition().z), new Vector3f(1f,1f,1f), new Vector3f(1f, 0.6f, 0.009f));
		lights.add(light);
		lights.add(lampLight);
		//lights.add(new Light(new Vector3f(-200, 10, -200), new Vector3f(1,1,1)));
		
		Camera camera = new Camera(playerEntity);

		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		//GuiTexture gui = new GuiTexture(loader.loadTexture("logo"), new Vector2f(1f, 1f), new Vector2f(0.25f, 0.25f));
		//guis.add(gui);
		
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		
		MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix(), terrain);
		
		while (!Display.isCloseRequested()) {
			// entity.increaseRotation(0, 5, 0);
			camera.move();
			playerEntity.move(terrain);
			
			picker.update();
			Vector3f terrainPoint = picker.getCurrentTerrainPoint();
			if(terrainPoint!=null){
				if(Mouse.isButtonDown(1)){
					lampLight.setPosition(new Vector3f(lampEntity.getPosition().x, lampEntity.getPosition().y + 12f, lampEntity.getPosition().z));
					lampEntity.setPosition(lampEntity.getPosition());
				}else{
					lampLight.setPosition(new Vector3f(terrainPoint.x, terrainPoint.y + 12f, terrainPoint.z));
					lampEntity.setPosition(terrainPoint);
				}
			}
			
			for(Entity e:entities){				
				renderer.processEntity(e);
			}
			renderer.processEntity (playerEntity);
			renderer.processTerrain(terrain);
			//renderer.processTerrain(terrain1);
			renderer.render(lights, camera);
			guiRenderer.render(guis);
			displayManager.updateDisplay();
		}

		guiRenderer.cleanUp();
		renderer.cleanUp();
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
