package tk.nirvanagamestudios.mavicci.worldEditor.engineTest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import tk.nirvanagamestudios.mavicci.worldEditor.entities.Camera;
import tk.nirvanagamestudios.mavicci.worldEditor.entities.Entity;
import tk.nirvanagamestudios.mavicci.worldEditor.entities.Light;
import tk.nirvanagamestudios.mavicci.worldEditor.entities.Player;
import tk.nirvanagamestudios.mavicci.worldEditor.guis.GuiRenderer;
import tk.nirvanagamestudios.mavicci.worldEditor.guis.GuiTexture;
import tk.nirvanagamestudios.mavicci.worldEditor.models.RawModel;
import tk.nirvanagamestudios.mavicci.worldEditor.models.TexturedModel;
import tk.nirvanagamestudios.mavicci.worldEditor.renderEngine.DisplayManager;
import tk.nirvanagamestudios.mavicci.worldEditor.renderEngine.Loader;
import tk.nirvanagamestudios.mavicci.worldEditor.renderEngine.MasterRenderer;
import tk.nirvanagamestudios.mavicci.worldEditor.renderEngine.OBJLoader;
import tk.nirvanagamestudios.mavicci.worldEditor.terrains.Terrain;
import tk.nirvanagamestudios.mavicci.worldEditor.test.Write;
import tk.nirvanagamestudios.mavicci.worldEditor.textures.ModelTexture;
import tk.nirvanagamestudios.mavicci.worldEditor.textures.TerrainTexture;
import tk.nirvanagamestudios.mavicci.worldEditor.textures.TerrainTexturePack;
import tk.nirvanagamestudios.mavicci.worldEditor.util.MousePicker;
import tk.nirvanagamestudios.mavicci.worldEditor.world.WorldReader;

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
 * Point Lights - Done - 26/05/15
 * Day and Night Cycle
 * Skybox
 * Mouse Picking - Done - 26/17/15
 */


public class MainGameLoop {

	public static List<Entity> entities = new ArrayList<Entity>();
	public static List<Terrain> terrains = new ArrayList<Terrain>();
	public static List<Light> lights = new ArrayList<Light>();
	public static List<TexturedModel> texturedModels = new ArrayList<TexturedModel>();
	public static List<Entity> toWriteEntities = new ArrayList<Entity>();
	
	public static MasterRenderer renderer;
	public static Random random = new Random(676452);
	public static int entityCount = 0;
	public static void main(String[] args) {
		System.setProperty("org.lwjgl.librarypath", new File("natives/windows/").getAbsolutePath());
		DisplayManager displayManager = new DisplayManager();
		displayManager.createDisplay();

		Loader loader = new Loader();
		renderer = new MasterRenderer();
		
		ModelTexture fernTextureAtlas = loader.loadModelTexture("fern");
		fernTextureAtlas.setNumberOfRows(2);
		fernTextureAtlas.setHasTransparency(true);
		TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern", loader), fernTextureAtlas);
		texturedModels.add(fern);
		
		ModelTexture grassTexture = loader.loadModelTexture("grassTexture");
		TexturedModel grass = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader), grassTexture);
		texturedModels.add(grass);
		
		ModelTexture stallTexture = loader.loadModelTexture("stallTexture");
		TexturedModel stall = new TexturedModel(OBJLoader.loadObjModel("stall", loader), stallTexture);
		texturedModels.add(stall);
		
		ModelTexture treeTexture = loader.loadModelTexture("tree");
		TexturedModel tree = new TexturedModel(OBJLoader.loadObjModel("tree", loader), treeTexture);
		texturedModels.add(tree);
		
		ModelTexture pineTexture = loader.loadModelTexture("pine");
		TexturedModel pine = new TexturedModel(OBJLoader.loadObjModel("pine", loader), pineTexture);
		texturedModels.add(pine);
		
		ModelTexture lampTexture = loader.loadModelTexture("lamp");
		lampTexture.setUseFakeLighting(true);
		TexturedModel lamp = new TexturedModel(OBJLoader.loadObjModel("lamp", loader), lampTexture);
		lamp.getTexture().setUseFakeLighting(true);
		texturedModels.add(lamp);
		
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grass"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("path"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grass"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
		
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		
		Terrain terrain = new Terrain(-1, -1, loader, texturePack, blendMap, "heightmap");
		//Terrain terrain1 = new Terrain(-1, -1, loader, texturePack, blendMap, "heightmap");
		
		RawModel player = OBJLoader.loadObjModel("player", loader);
		ModelTexture playerTexture = new ModelTexture(loader.loadTexture("playerTexture"));
		TexturedModel playerModel = new TexturedModel(player, playerTexture);
		Player playerEntity = new Player(playerModel, new Vector3f(-250, terrain.getHeightOfTerrain(terrain.getX(), terrain.getZ()), -290), 0, 0, 0, 1);
		
		Light light = new Light(new Vector3f(0f, 10000f, -7000f), new Vector3f(0.4f, 0.4f, 0.4f));
		lights.add(light);
		for(int i = 0; i < 4; i++){
			float x, y, z;
			if(i >= 3){
				x = random.nextFloat() * 800 - 400;
				z = random.nextFloat() * -600;
				y = terrain.getHeightOfTerrain(x, z);
			}else{
				x = -156.94044f;
				z = -292.38776f;
				y = terrain.getHeightOfTerrain(x, z);
			}

			Light lampLight = new Light(new Vector3f(x + 7, y + 12f, z), new Vector3f(1f,1f,1f), new Vector3f(1f, 0.01f, 0.002f));
			Entity lampEntity = new Entity(lamp, new Vector3f(x, y, z), 0, random.nextFloat(), 0, 1, 1);
			lights.add(lampLight);
			entities.add(lampEntity);
		}
		//lights.add(new Light(new Vector3f(-200, 10, -200), new Vector3f(1,1,1)));
		
		Camera camera = new Camera(playerEntity);

		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		//GuiTexture gui = new GuiTexture(loader.loadTexture("logo"), new Vector2f(1f, 1f), new Vector2f(0.25f, 0.25f));
		//guis.add(gui);
		
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		
		
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
			if(i % 6 == 0){
				float x = random.nextFloat() * 800 - 400;
				float z = random.nextFloat() * -600;
				float y = terrain.getHeightOfTerrain(x, z);
				
				entities.add(new Entity(tree, new Vector3f(x, y, z), 0, random.nextFloat(), 0, 1f, random.nextInt(4)));
			}
		}
		
		MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix(), terrain);
		List<Entity> worldEntities = WorldReader.loadWorld("world", loader);
		if(worldEntities != null){
			for(Entity e:worldEntities){
				entities.add(e);
				toWriteEntities.add(e);
			}
		}
		Entity entity = new Entity(pine, new Vector3f(0,0,0), 0, 0, 0, 1f);
		entities.add(entity);
		while (!Display.isCloseRequested()) {
			// entity.increaseRotation(0, 5, 0);
			camera.move();
			playerEntity.move(terrain);
			picker.update();
			Vector3f terrainPoint = picker.getCurrentTerrainPoint();
			if(terrainPoint != null){
				entity.setPosition(terrainPoint);
				if(Mouse.isButtonDown(1)){
					entities.add(new Entity(entity));
					toWriteEntities.add(new Entity(entity));
					entityCount++;
				}else if(Mouse.isButtonDown(2)){
					if(!((texturedModels.indexOf(entity.getModel()) + 1) >= texturedModels.size())){
						entity.setModel(texturedModels.get(texturedModels.indexOf(entity.getModel()) + 1));						
					}else{
						entity.setModel(texturedModels.get(1));
					}
				}else if(Mouse.hasWheel()){
					entity.setScale(entity.getScale() + (Mouse.getDWheel() * 0.01f));
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
		
		Write.createFile();
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
