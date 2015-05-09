package tk.nirvanagamestudios.mavicci.shaders;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import tk.nirvanagamestudios.mavicci.entities.Camera;
import tk.nirvanagamestudios.mavicci.entities.Light;
import tk.nirvanagamestudios.mavicci.util.Maths;

public class StaticShader extends ShaderProgram{

	private static final String VERTEX_FILE = "src/tk/nirvanagamestudios/mavicci/shaders/vertexShader.txt";
	private static final String FRAGMENT_FILE = "src/tk/nirvanagamestudios/mavicci/shaders/fragmentShader.txt";
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPosition;
	private int location_lightColour;
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_skyColour;
	
	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(1, "normal");
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_lightPosition = super.getUniformLocation("lightPosition");
		location_lightColour = super.getUniformLocation("lightColour");
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		location_skyColour = super.getUniformLocation("skyColour");
	}
	
	public void loadSkyColour(float r, float g, float b){
		super.loadVector(location_skyColour, new Vector3f(r,g,b));
	}
	
	public void loadShineVariables(float damper, float reflect){
		super.loadFloat(location_shineDamper, damper);
		super.loadFloat(location_reflectivity, reflect);
	}
	
	public void loadTransformationMatrix(Matrix4f matrix){
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	public void loadLight(Light light){
		super.loadVector(location_lightPosition, light.getPosition());
		super.loadVector(location_lightColour, light.getColour());
	}
	
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}

	public void loadProjectionMatrix(Matrix4f matrix){
		super.loadMatrix(location_projectionMatrix, matrix);
	}
	
}
