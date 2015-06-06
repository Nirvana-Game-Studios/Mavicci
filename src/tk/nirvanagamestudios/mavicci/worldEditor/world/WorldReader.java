package tk.nirvanagamestudios.mavicci.worldEditor.world;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import tk.nirvanagamestudios.mavicci.worldEditor.entities.Entity;
import tk.nirvanagamestudios.mavicci.worldEditor.models.RawModel;
import tk.nirvanagamestudios.mavicci.worldEditor.models.TexturedModel;
import tk.nirvanagamestudios.mavicci.worldEditor.renderEngine.Loader;
import tk.nirvanagamestudios.mavicci.worldEditor.renderEngine.OBJLoader;
import tk.nirvanagamestudios.mavicci.worldEditor.textures.ModelTexture;

public class WorldReader {

	public static List<Entity> loadWorld(String fileName, Loader loader) {
		FileReader fr = null;
		try {
			fr = new FileReader(new File("res/" + fileName + ".wngs"));
		} catch (FileNotFoundException e) {
			return null;
		}
		BufferedReader reader = new BufferedReader(fr);
		String line;
		List<Vector3f> positions = new ArrayList<Vector3f>();
		List<Vector3f> rotations = new ArrayList<Vector3f>();
		List<Float> scales = new ArrayList<Float>();
		List<String> models = new ArrayList<String>();
		List<String> textures = new ArrayList<String>();
		List<Entity> entities = new ArrayList<Entity>();
		int entityCount = 0;
		Vector3f position = null;
		Vector3f rotation = null;
		float scale = 0;
		String model = null;
		String texture = null;
		try {
			while (true) {
				line = reader.readLine();
				//System.out.println(line);
				String[] currentLine;
				if(line == null){
					currentLine = null;
				}else{
					currentLine = line.split(" ");					
				}
				if (line.startsWith("POS ")) {
					position = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
				} else if (line.startsWith("ROT ")) {
					rotation = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
				} else if (line.startsWith("S ")) {
					scale = Float.parseFloat(currentLine[1]);
				} else if (line.startsWith("M")) {
					model = currentLine[1];
				} else if (line.startsWith("T ")) {
					texture = currentLine[1];
				} else if (line.matches("en")) {
					if (entityCount > 0) {
						positions.add(position);
						rotations.add(rotation);
						scales.add(scale);
						models.add(model);
						textures.add(texture);
					}
					entityCount++;
				} else if (line.matches("end")) {
					for (int i = 0; i < entityCount - 1; i++) {
						ModelTexture modelTexture = new ModelTexture(
								loader.loadTexture(textures.get(i)));
						RawModel rawModel = OBJLoader.loadObjModel(
								models.get(i), loader);
						TexturedModel texturedModel = new TexturedModel(
								rawModel, modelTexture);
						Entity entity = new Entity(texturedModel,
								positions.get(i), rotations.get(i).x,
								rotations.get(i).y, rotations.get(i).z,
								Float.parseFloat(scales.get(i).toString()));
						entities.add(entity);
					}
					break;
				}
			}
			
			while(line != null){
				if(!line.startsWith("end")){
					line = reader.readLine();
					continue;
				}
				break;
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entities;
	}

}
