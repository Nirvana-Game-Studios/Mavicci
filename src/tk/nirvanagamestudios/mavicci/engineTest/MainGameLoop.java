package tk.nirvanagamestudios.mavicci.engineTest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import tk.nirvanagamestudios.mavicci.entities.Camera;
import tk.nirvanagamestudios.mavicci.entities.Entity;
import tk.nirvanagamestudios.mavicci.entities.Light;
import tk.nirvanagamestudios.mavicci.entities.Player;
import tk.nirvanagamestudios.mavicci.guis.Button;
import tk.nirvanagamestudios.mavicci.guis.GuiRenderer;
import tk.nirvanagamestudios.mavicci.guis.GuiTexture;
import tk.nirvanagamestudios.mavicci.guis.hud.Hotbar;
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
import tk.nirvanagamestudios.mavicci.water.WaterFrameBuffers;
import tk.nirvanagamestudios.mavicci.water.WaterRenderer;
import tk.nirvanagamestudios.mavicci.water.WaterShader;
import tk.nirvanagamestudios.mavicci.water.WaterTile;
import tk.nirvanagamestudios.mavicci.world.WorldReader;

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
 * Day and Night Cycle - 03/06/15
 * Skybox - Done - 03/06/15
 * Mouse Picking - Done - 26/17/15
 * Animation - In Completion...
 * Water Engine - In Completion...
 */

public class MainGameLoop {

	public static List<Entity> entities = new ArrayList<Entity>();
	public static List<Terrain> terrains = new ArrayList<Terrain>();
	public static List<Light> lights = new ArrayList<Light>();
	public static List<GuiTexture> guis = new ArrayList<GuiTexture>();
	public static MasterRenderer renderer;
	public static Loader loader;
	public static WaterShader waterShader;
	public static GuiRenderer guiRenderer;
	public static WaterFrameBuffers waterFBOs;
	public static Random random = new Random(676452);
	public static boolean inPause = false;
	public static boolean exiting = false;
	public static Player player;

	public static void main(String[] args) {
		System.setProperty("org.lwjgl.librarypath",
				new File("natives/windows/").getAbsolutePath());
		DisplayManager displayManager = new DisplayManager();
		displayManager.createDisplay();

		loader = new Loader();
		renderer = new MasterRenderer(loader);

		ModelTexture fernTextureAtlas = new ModelTexture(
				loader.loadTexture("fern"));
		fernTextureAtlas.setNumberOfRows(2);
		fernTextureAtlas.setHasTransparency(true);
		TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern",
				loader), fernTextureAtlas);

		ModelTexture grassTexture = new ModelTexture(
				loader.loadTexture("grassTexture"));
		TexturedModel grass = new TexturedModel(OBJLoader.loadObjModel(
				"grassModel", loader), grassTexture);

		ModelTexture pineTexture = new ModelTexture(loader.loadTexture("pine"));
		TexturedModel pine = new TexturedModel(OBJLoader.loadObjModel("pine",
				loader), pineTexture);

		ModelTexture lampTexture = new ModelTexture(loader.loadTexture("lamp"));
		lampTexture.setUseFakeLighting(true);
		TexturedModel lamp = new TexturedModel(OBJLoader.loadObjModel("lamp",
				loader), lampTexture);
		lamp.getTexture().setUseFakeLighting(true);

		TerrainTexture backgroundTexture = new TerrainTexture(
				loader.loadTexture("grass"));
		TerrainTexture rTexture = new TerrainTexture(
				loader.loadTexture("grassFlowers"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("rock"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

		TerrainTexturePack texturePack = new TerrainTexturePack(
				backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(
				loader.loadTexture("blendMap"));

		Terrain terrain = createTerrain(-1, -1, loader, texturePack, blendMap,
				"heightmap");
		// Terrain terrain1 = new Terrain(-1, -1, loader, texturePack, blendMap,
		// "heightmap");

		List<Button> buttons = new ArrayList<Button>();
		List<Button> pauseButtons = new ArrayList<Button>();

		Hotbar hotbar = new Hotbar();

		// Pause Menu GUI Initialisation Variable Code
		GuiTexture pause = new GuiTexture(Loader.loadGUITexture("menuOverlay"),
				new Vector2f(0f, 0f), new Vector2f(5.0f, 5.0f));
		GuiTexture play = new GuiTexture(Loader.loadGUITexture("play"),
				new Vector2f(0.0f, 0.8f), new Vector2f(0.2f, 0.2f));
		GuiTexture playHover = new GuiTexture(
				Loader.loadGUITexture("playHover"), new Vector2f(0.0f, 0.8f),
				new Vector2f(0.2f, 0.2f));
		GuiTexture quit = new GuiTexture(Loader.loadGUITexture("quit"),
				new Vector2f(0.0f, 0.4f), new Vector2f(0.2f, 0.2f));
		GuiTexture quitHover = new GuiTexture(
				Loader.loadGUITexture("quitHover"), new Vector2f(0.0f, 0.4f),
				new Vector2f(0.2f, 0.2f));
		GuiTexture menu = new GuiTexture(Loader.loadGUITexture("menu"),
				new Vector2f(-0.5f, -1.0f), new Vector2f(0.5f, 0.5f));
		GuiTexture menuHover = new GuiTexture(
				Loader.loadGUITexture("menuHover"), new Vector2f(-0.5f, -1.0f),
				new Vector2f(0.5f, 0.5f));

		Button pausePlay = new Button(play, playHover, "play");
		Button pauseQuit = new Button(quit, quitHover, "quit");

		Button gameMenu = new Button(menu, menuHover, "menu");

		pauseButtons.add(pausePlay);
		pauseButtons.add(pauseQuit);

		buttons.add(gameMenu);

		// Player Inititialisation

		RawModel playerRaw = OBJLoader.loadObjModel("player", loader);
		ModelTexture playerTexture = new ModelTexture(
				loader.loadTexture("playerTexture"));
		TexturedModel playerModel = new TexturedModel(playerRaw, playerTexture);
		player = new Player(playerModel, new Vector3f(-369f,
				terrain.getHeightOfTerrain(terrain.getX(), terrain.getZ()),
				-329f), 0, 0, 0, 0.9f);

		Light light = new Light(new Vector3f(0f, 10000f, -7000f), new Vector3f(
				1f, 1f, 1f));
		lights.add(light);
		for (int i = 0; i < 4; i++) {
			float x, y, z;
			if (i >= 3) {
				x = random.nextFloat() * 800 - 400;
				z = random.nextFloat() * -600;
				y = terrain.getHeightOfTerrain(x, z);
			} else {
				x = -156.94044f;
				z = -292.38776f;
				y = terrain.getHeightOfTerrain(x, z);
			}

			Light lampLight = new Light(new Vector3f(x + 7, y + 12f, z),
					new Vector3f(1f, 1f, 1f), new Vector3f(1f, 0.01f, 0.002f));
			Entity lampEntity = new Entity(lamp, new Vector3f(x, y, z), 0,
					random.nextFloat(), 0, 1, 1);
			lights.add(lampLight);
			entities.add(lampEntity);
		}
		// lights.add(new Light(new Vector3f(-200, 10, -200), new
		// Vector3f(1,1,1)));

		Camera camera = new Camera(player);

		waterShader = new WaterShader();
		waterFBOs = new WaterFrameBuffers();
		WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader,
				renderer.getProjectionMatrix(), waterFBOs);
		List<WaterTile> waterTiles = new ArrayList<WaterTile>();
		WaterTile tile1 = new WaterTile(-428, -365, 6);
		waterTiles.add(tile1);

		guiRenderer = new GuiRenderer(loader);
		// Button b = new Button(new GuiTexture(loader.loadTexture("texture"),
		// new Vector2f(0.25f, 0.25f), new Vector2f(0.25f, 0.25f), new
		// Vector2f(256, 256)), new GuiTexture(loader.loadTexture("texture1"),
		// new Vector2f(0.25f, 0.25f), new Vector2f(0.25f, 0.25f), new
		// Vector2f(256, 256)), "menu");
		// buttons.add(b);

		// Item Initialisation

		for (int i = 0; i < 400; i++) {
			if (i % 2 == 0) {
				float x = random.nextFloat() * 800 - 400;
				float z = random.nextFloat() * -600;
				float y = terrain.getHeightOfTerrain(x, z);

				entities.add(new Entity(fern, new Vector3f(x, y, z), 0, random
						.nextFloat(), 0, 0.9f, random.nextInt(4)));
			}
			if (i % 4 == 0) {
				float x = random.nextFloat() * 800 - 400;
				float z = random.nextFloat() * -600;
				float y = terrain.getHeightOfTerrain(x, z);

				entities.add(new Entity(pine, new Vector3f(x, y, z), 0, random
						.nextFloat(), 0, 1f, random.nextInt(4)));
			}
			if (i % 3 == 0) {
				float x = random.nextFloat() * 800 - 400;
				float z = random.nextFloat() * -600;
				float y = terrain.getHeightOfTerrain(x, z);

				entities.add(new Entity(grass, new Vector3f(x, y, z), 0, random
						.nextFloat(), 0, 1f, random.nextInt(4)));
			}
			if (i % 6 == 0) {
				float x = random.nextFloat() * 800 - 400;
				float z = random.nextFloat() * -600;
				float y = terrain.getHeightOfTerrain(x, z);

				lights.add(new Light(new Vector3f(x, y + 12f, z), new Vector3f(
						0, 0, 1), new Vector3f(1f, 0.01f, 0.002f)));
				entities.add(new Entity(lamp, new Vector3f(x, y, z), 0, random
						.nextFloat(), 0, 1f, random.nextInt(4)));
			}
		}

		MousePicker picker = new MousePicker(camera,
				renderer.getProjectionMatrix(), terrain);
		List<Entity> worldEntities = WorldReader.loadWorld("world", loader);

		for (Entity e : worldEntities) {
			e.setPosition(new Vector3f(e.getPosition().x, terrain
					.getHeightOfTerrain(e.getPosition().x, e.getPosition().z),
					e.getPosition().z));
			entities.add(e);
		}
		Entity entity = new Entity(pine, new Vector3f(0, 0, 0), 0, 0, 0, 1f);
		entities.add(entity);
		while (!Display.isCloseRequested()) {
			// entity.increaseRotation(0, 5, 0);
			if (!inPause) {
				for (Button but : hotbar.getHotbarButtons()) {
					buttons.add(but);
				}
				for (Button bu : buttons) {
					bu.update();
				}
				player.move(terrain);
				camera.move();
				picker.update();
			}
			// System.out.println(player.getPosition());

			/*
			 * List<Entity> entitiesInOrder = new ArrayList<Entity>();
			 * for(Entity e:entities){ while(!entitiesInOrder.contains(e)){
			 * if((entities.indexOf(e) - 1) != -1){ if(e.getPosition().getX() >
			 * entities.get(entities.indexOf(e) - 1).getPosition().getX()){
			 * if(e.getPosition().getY() > entities.get(entities.indexOf(e) -
			 * 1).getPosition().getY()){ if(e.getPosition().getZ() >
			 * entities.get(entities.indexOf(e) - 1).getPosition().getZ()){
			 * entitiesInOrder.add(e); System.out.println(e.getPosition());
			 * break; } } } } } }
			 */

			if (!inPause) {
				GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
				renderer.processEntity(player);
				float distance = 2 * (camera.getPosition().y - tile1
						.getHeight());
				camera.getPosition().y -= distance;
				camera.invertPitch();
				waterFBOs.bindReflectionFrameBuffer();
				renderer.renderScene(entities, terrains, lights, camera,
						new Vector4f(0, 1, 0, -tile1.getHeight()));
				camera.getPosition().y += distance;
				camera.invertPitch();

				renderer.processEntity(player);
				waterFBOs.bindRefractionFrameBuffer();
				renderer.renderScene(entities, terrains, lights, camera,
						new Vector4f(0, -1, 0, tile1.getHeight()));

				GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
				waterFBOs.unbindCurrentFrameBuffer();
			}
			renderer.processEntity(player);
			renderer.renderScene(entities, terrains, lights, camera,
					new Vector4f(0, 0, 0, Integer.MAX_VALUE));
			waterRenderer.render(waterTiles, camera);
			if (inPause) {
				for (Button pb : pauseButtons) {
					pb.update();
				}
				guiRenderer.renderButtons(pauseButtons);
				guiRenderer.renderGUI(pause);
				Mouse.setGrabbed(false);
			} else {
				guiRenderer.renderButtons(buttons);
				guiRenderer.render(guis);
				Mouse.setGrabbed(true);
			}
			displayManager.updateDisplay();
			if (exiting) {
				break;
			}
		}

		cleanUp();
		displayManager.stopDisplay();
	}

	private static void cleanUp() {
		waterFBOs.cleanUp();
		waterShader.cleanUp();
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
	}

	public static Entity createEntity(TexturedModel model, Vector3f pos,
			float rotX, float rotY, float rotZ, float scale, float amount) {
		Entity entity = new Entity(model, pos, rotX, rotY, rotZ, scale);
		for (int i = 0; i < amount; i++) {
			entity = new Entity(model, pos, rotX, rotY, rotZ, scale);
			entities.add(entity);
		}
		return entity;
	}

	public static void pause() {
		inPause = true;
	}

	public static Player getPlayer() {
		return player;
	}

	public static void unpause() {
		inPause = false;
	}

	public static void exitGame() {
		exiting = true;
	}

	public static Terrain createTerrain(int gridX, int gridZ, Loader loader,
			TerrainTexturePack texturePack, TerrainTexture blendMap,
			String heightMap) {
		Terrain terrain = new Terrain(gridX, gridZ, loader, texturePack,
				blendMap, heightMap);
		terrains.add(terrain);
		return terrain;
	}

}
