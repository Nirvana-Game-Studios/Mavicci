package tk.nirvanagamestudios.mavicci.worldEditor.textures;

public class ModelTexture {

	private int textureID;
	
	private int shineDamper = 1;
	private int reflectivity = 0;
	
	private boolean hasTransparency = false;
	private boolean useFakeLighting = false;
	
	private String fileName; 
	
	public boolean isUseFakeLighting() {
		return useFakeLighting;
	}

	public void setUseFakeLighting(boolean useFakeLighting) {
		this.useFakeLighting = useFakeLighting;
	}

	private int numberOfRows = 1;
	
	public ModelTexture(int id){
		this.textureID = id;
	}
	
	public ModelTexture(int id, String fileName){
		this.textureID = id;
		this.setFileName(fileName);
	}
	
	public void setNumberOfRows(int numberOfRows){
		this.numberOfRows = numberOfRows;
	}
	
	public int getNumberOfRows(){
		return numberOfRows;
	}
	
	public int getTextureID(){
		return textureID;
	}

	public void setTextureID(int textureID) {
		this.textureID = textureID;
	}

	public void setShineDamper(int shineDamper) {
		this.shineDamper = shineDamper;
	}

	public void setReflectivity(int reflectivity) {
		this.reflectivity = reflectivity;
	}

	public int getShineDamper() {
		return shineDamper;
	}

	public int getReflectivity() {
		return reflectivity;
	}

	public boolean isHasTransparency() {
		return hasTransparency;
	}

	public void setHasTransparency(boolean hasTransparency) {
		this.hasTransparency = hasTransparency;
	}

	public String getFileName() {
		return fileName;
	}

	private void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}
