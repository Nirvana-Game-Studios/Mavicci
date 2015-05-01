package tk.nirvanagamestudios.mavicci.models;

public class RawModel {

	private int vaoID;
	private int vertexCount;
	
	public RawModel(int id, int vertices){
		this.vaoID = id;
		this.vertexCount = vertices;
	}
	
	public int getVaoID() {
		return vaoID;
	}
	
	public int getVertexCount() {
		return vertexCount;
	}
	
}
