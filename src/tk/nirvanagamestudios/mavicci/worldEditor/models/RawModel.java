package tk.nirvanagamestudios.mavicci.worldEditor.models;

public class RawModel {

	private int vaoID;
	private int vertexCount;
	private String fileName;
	
	public RawModel(int id, int vertices){
		this.vaoID = id;
		this.vertexCount = vertices;
	}
	
	public RawModel(int id, int vertices, String fileName){
		this.vaoID = id;
		this.vertexCount = vertices;
		this.fileName = fileName;
	}
	
	public int getVaoID() {
		return vaoID;
	}
	
	public int getVertexCount() {
		return vertexCount;
	}

	public String getFileName() {
		return fileName;
	}
	
}
