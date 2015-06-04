package tk.nirvanagamestudios.mavicci.skybox;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import tk.nirvanagamestudios.mavicci.engineTest.MainGameLoop;
import tk.nirvanagamestudios.mavicci.entities.Camera;
import tk.nirvanagamestudios.mavicci.models.RawModel;
import tk.nirvanagamestudios.mavicci.renderEngine.DisplayManager;
import tk.nirvanagamestudios.mavicci.renderEngine.Loader;

public class SkyboxRenderer {

	private static final float SIZE = 500f;
	
	private static final float[] VERTICES = {        
	    -SIZE,  SIZE, -SIZE,
	    -SIZE, -SIZE, -SIZE,
	    SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,

	    -SIZE, -SIZE,  SIZE,
	    -SIZE, -SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE,  SIZE,
	    -SIZE, -SIZE,  SIZE,

	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,

	    -SIZE, -SIZE,  SIZE,
	    -SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE, -SIZE,  SIZE,
	    -SIZE, -SIZE,  SIZE,

	    -SIZE,  SIZE, -SIZE,
	     SIZE,  SIZE, -SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	    -SIZE,  SIZE,  SIZE,
	    -SIZE,  SIZE, -SIZE,

	    -SIZE, -SIZE, -SIZE,
	    -SIZE, -SIZE,  SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	    -SIZE, -SIZE,  SIZE,
	     SIZE, -SIZE,  SIZE
	};
	
	private static String[] TEXTURE_FILES = {"right","left","top","bottom","back","front"};
	private static String[] NIGHT_TEXTURE_FILES = {"nightRight","nightLeft","nightTop","nightBottom","nightBack","nightFront"};
	
	private RawModel cube;
	private int texture;
	private int nightTexture;
	private SkyboxShader shader;
	private float time; 
	
	public SkyboxRenderer(Loader loader, Matrix4f projectionMatrix){
		cube = loader.loadToVao(VERTICES, 3);
		texture = loader.loadCubeMap(TEXTURE_FILES, true);
		time = 0;
		nightTexture = loader.loadCubeMap(NIGHT_TEXTURE_FILES, true);
		shader = new SkyboxShader();
		shader.start();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	public void render(Camera camera, float r, float g, float b){
		shader.start();
		shader.loadViewMatrix(camera);
		shader.loadFog(r,g,b);
		GL30.glBindVertexArray(cube.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		bindTextures();
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}
	
	private void bindTextures(){
		time += DisplayManager.getFrameTimeSeconds() * 1000;
		time %= 240000;
		int texture1;
		int texture2;
		float blendFactor;
		if(time >= 0 && time < 120000){
			texture1 = texture;
			texture2 = texture;
			MainGameLoop.lights.get(0).setColour(new Vector3f(1f,1f,1f));
			blendFactor = (time - 0)/(12000 - 0);
		}else if(time >= 120000 && time < 170000){
			texture1 = texture;
			texture2 = nightTexture;
			MainGameLoop.lights.get(0).setColour(new Vector3f(0.8f,0.8f,0.8f));
			blendFactor = (time - 120000)/(170000 - 120000);
		}else if(time >= 170000 && time < 200000){
			texture1 = nightTexture;
			texture2 = nightTexture;
			MainGameLoop.lights.get(0).setColour(new Vector3f(0.6f,0.6f,0.6f));
			blendFactor = (time - 170000)/(200000 - 170000);
		}else{
			texture1 = nightTexture;
			texture2 = texture;
			MainGameLoop.lights.get(0).setColour(new Vector3f(0.8f,0.8f,0.8f));
			blendFactor = (time - 200000)/(240000 - 200000);
		}

		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture1);
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture2);
		shader.loadBlendFactor(blendFactor);
	}
}
