package tk.nirvanagamestudios.mavicci.animation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class AnimationFileWriter {

	private static final int TRUE = 1;

	private PrintWriter writer;

	public AnimationFileWriter(File output) {
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter(output, false)));
		} catch (IOException e) {
			System.err.println("Unable to create new file!");
			System.exit(-1);
		}
	}

	public void writeAnimationToFile(Animation animation) {
		try {
			writeAnimationHeader(animation);
			AnimationSection[] sections = animation.getSections();
			writer.println(sections.length);
			for (AnimationSection section : sections) {
				writeSection(section);
			}
			writer.println("END");
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println();
			System.err.println("Error writing to file!");
			System.exit(-1);
		}
	}

	private void writeAnimationHeader(Animation animation) throws Exception {
		int id = animation.getID();
		int length = animation.getLength();
		int looping = 0;
		if (animation.isLooping()) {
			looping = TRUE;
		}
		float maxPosChange = animation.getMaxPosChange();
		float maxRotChange = animation.getMaxRotChange();
		float maxScaleChange = animation.getMaxScaleChange();
		writer.println(id + ";" + length + ";" + looping + ";" + maxPosChange + ";" + maxRotChange
				+ ";" + maxScaleChange);

	}

	private void writeSection(AnimationSection section) {
		writeSectionHeader(section);
		Frame[] frames = section.getFrames();
		for (Frame frame : frames) {
			writeFrame(frame, section.isHasPosChange(), section.isHasRotChange(),
					section.hasScaleChange());
		}
	}

	private void writeSectionHeader(AnimationSection section) {
		int partID = section.getId();
		int numberOfFrames = section.getNumberOfFrames();
		int hasPos = 0;
		int hasRot = 0;
		int hasScale = 0;
		if (section.isHasPosChange()) {
			hasPos = TRUE;
		}
		if (section.isHasRotChange()) {
			hasRot = TRUE;
		}
		if (section.hasScaleChange()) {
			hasScale = TRUE;
		}
		writer.println(partID + ";" + numberOfFrames + ";" + hasPos + ";" + hasRot + ";" + hasScale);
	}

	private void writeFrame(Frame frame, boolean pos, boolean rot, boolean scale) {
		writer.print(frame.getTime());
		if (pos) {
			writer.print(";" + frame.getX() + ";" + frame.getY() + ";" + frame.getZ());
		}
		if (rot) {
			writer.print(";" + frame.getRotW() + ";" + frame.getRotX() + ";" + frame.getRotY()
					+ ";" + frame.getRotZ());
		}
		if (scale) {
			writer.print(";" + frame.getScale());
		}
		writer.println();
	}
}
