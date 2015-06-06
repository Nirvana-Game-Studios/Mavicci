package tk.nirvanagamestudios.mavicci.water;

import org.lwjgl.util.vector.Matrix4f;

import tk.nirvanagamestudios.mavicci.entities.Camera;
import tk.nirvanagamestudios.mavicci.shaders.ShaderProgram;
import tk.nirvanagamestudios.mavicci.util.Maths;

public class WaterShader extends ShaderProgram {

	private final static String VERTEX_FILE = "res/shaders/waterVertexShader.vert";
	private final static String FRAGMENT_FILE = "res/shaders/waterFragmentShader.frag";

	private int location_modelMatrix;
	private int location_viewMatrix;
	private int location_projectionMatrix;
	private int location_reflection;
	private int location_refraction;

	public WaterShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = getUniformLocation("projectionMatrix");
		location_viewMatrix = getUniformLocation("viewMatrix");
		location_modelMatrix = getUniformLocation("modelMatrix");
		location_reflection = getUniformLocation("reflectionTexture");
		location_refraction = getUniformLocation("refractionTexture");
	}

	public void connectTextureUnits(){
		super.loadInt(location_reflection, 0);
		super.loadInt(location_refraction, 1);
	}
	
	public void loadProjectionMatrix(Matrix4f projection) {
		loadMatrix(location_projectionMatrix, projection);
	}
	
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		loadMatrix(location_viewMatrix, viewMatrix);
	}

	public void loadModelMatrix(Matrix4f modelMatrix){
		loadMatrix(location_modelMatrix, modelMatrix);
	}

}
