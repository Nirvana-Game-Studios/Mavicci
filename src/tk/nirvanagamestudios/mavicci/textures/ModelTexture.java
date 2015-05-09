package tk.nirvanagamestudios.mavicci.textures;

public class ModelTexture {

	private int textureID;
	
	private int shineDamper = 1;
	private int reflectivity = 0;
	
	private boolean hasTransparency = false;
	
	public ModelTexture(int id){
		this.textureID = id;
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
	
}
