package tk.nirvanagamestudios.mavicci.animation;

public class Animation {

	private int id = 0;
	private int lengthInMillis;
	private boolean isLooping;
	private float maxPosChange;
	private float maxRotChange;
	private float maxScaleChange;
	private boolean useDefaultMaxChanges = true;
	private boolean useDefaultLengths = true;
	private AnimationSection[] sections;
	private String fileName;
	
	public Animation(){}

	public void setId(int id) {
		this.id = id;
	}

	public void setLengthInMillis(int lengthInMillis) {
		this.lengthInMillis = lengthInMillis;
	}

	public void setLooping(boolean isLooping) {
		this.isLooping = isLooping;
	}

	public void setMaxPosChange(float maxPosChange) {
		this.maxPosChange = maxPosChange;
	}

	public void setMaxRotChange(float maxRotChange) {
		this.maxRotChange = maxRotChange;
	}

	public void setMaxScaleChange(float maxScaleChange) {
		this.maxScaleChange = maxScaleChange;
	}

	public void setUseDefaultMaxChanges(boolean useDefaultMaxChanges) {
		this.useDefaultMaxChanges = useDefaultMaxChanges;
	}
	
	public int getID(){
		return this.id;
	}
	
	public int getLength(){
		return lengthInMillis;
	}

	public boolean isLooping() {
		return isLooping;
	}

	public boolean isUseDefaultMaxChanges() {
		return useDefaultMaxChanges;
	}
	
	public AnimationSection[] getSections(){
		return sections;
	}
	
	public void setSections(AnimationSection[] sections){
		this.sections = sections;
	}

	public float getMaxPosChange() {
		return maxPosChange;
	}

	public float getMaxRotChange() {
		return maxRotChange;
	}

	public float getMaxScaleChange() {
		return maxScaleChange;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public boolean isUseDefaultLengths() {
		return useDefaultLengths;
	}

	public void setUseDefaultLengths(boolean useDefaultLengths) {
		this.useDefaultLengths = useDefaultLengths;
	}
	
	
	
}

