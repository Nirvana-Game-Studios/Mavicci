package tk.nirvanagamestudios.mavicci.animation;

import java.io.File;

public class ColladaConverter {

	public static void loadAnimation(File animationFile, boolean looping, File output) {
		Animation animation = new Animation();
		animation.setLooping(looping);
		ColladaFileReader fileReader = new ColladaFileReader(animationFile);
		fileReader.openFile();
		fileReader.readInData(animation);
		AnimationSection head = animation.getSections()[0];
		DataProcessor.matrixStuff(head);
		DataProcessor.tryInversingStuff(animation);
		DataProcessor.rotate90Degrees(animation);

		AnimationSection[] sections = animation.getSections();
		for (AnimationSection section : sections) {
			section.convertAllRawFrames();
			section.printAll();
		}
		if(animation.isUseDefaultMaxChanges()){
			DataProcessor.setDefaultMaxChangeValues(animation);
		}
		if(animation.isUseDefaultLengths()){
			DataProcessor.setDefaultAnimationLength(animation);
		}
		AnimationFileWriter writer = new AnimationFileWriter(output);
		writer.writeAnimationToFile(animation);
	}
}
