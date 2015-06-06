package tk.nirvanagamestudios.mavicci.worldEditor.test;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.lwjgl.util.vector.Vector3f;

import tk.nirvanagamestudios.mavicci.worldEditor.engineTest.MainGameLoop;
import tk.nirvanagamestudios.mavicci.worldEditor.entities.Entity;

public class Write {
	
	private static Vector3f vectorr = new Vector3f(0,0,0);
	private static Writer writer;
	
	public static void createFile(){
		writer = null;
		try{
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("res/world.wngs"), "utf-8"));	
			for(Entity e:MainGameLoop.toWriteEntities){
				write(e.getPosition(), vectorr, e.getScale(), e.getModel().getRawModel().getFileName(), e.getModel().getTexture().getFileName());
			}	
			writer.write("en\n");
			writer.write("end\n");
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try{
				writer.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	private static void write(Vector3f pos, Vector3f rot, float scale, String modelFile, String textureFile) throws IOException{
		writer.write("en\n"); //Declares new Entity
		
		writer.write("POS " + String.valueOf(pos.x) + " " + String.valueOf(pos.y) + " " + String.valueOf(pos.z) + "\n");
		
		writer.write("ROT " + String.valueOf(rot.x) + " " + String.valueOf(rot.y) + " " + String.valueOf(rot.z) + "\n"); //Writes X Rotation to file
		
		writer.write("S " + String.valueOf(scale) + "\n");
		
		writer.write("M " + modelFile + "\n"); //Writes Model Path to file
		writer.write("T " + textureFile + "\n"); //Writes Texture Path to file
		
	}
	
}
