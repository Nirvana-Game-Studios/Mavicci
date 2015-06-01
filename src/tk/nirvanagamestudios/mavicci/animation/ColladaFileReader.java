package tk.nirvanagamestudios.mavicci.animation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ColladaFileReader {

	private BufferedReader reader;
	private AnimationSection currentParent;
	private File file;

	public ColladaFileReader(File file) {
		currentParent = null;
		this.file = file;
	}

	public void openFile() {
		while (true) {
			try {
				FileReader isr = new FileReader(file);
				reader = new BufferedReader(isr);
				return;
			} catch (Exception e) {
				System.out.println("File not found!");
				continue;
			}
		}
	}

	public void readInData(Animation animation) {

		List<AnimationSection> sections = readInAnimations();
		List<AnimationSection> orderedSections = orderSections(sections);
		readInRestPositions(orderedSections);
		AnimationSection[] sectionsArray = turnListToArray(sections);
		animation.setSections(sectionsArray);
	}

	private List<AnimationSection> readInAnimations() {
		List<AnimationSection> sections = new ArrayList<AnimationSection>();
		try {
			String check = null;
			while ((check = reader.readLine()) != null) {
				if (check.contains("<animation ")) {
					sections.add(createSection());
				} else if (check.contains("</library_animations>")) {
					break;
				}
			}
		} catch (Exception e) {
			closeWithError("File format problem!", e);
		}
		return sections;
	}

	private List<AnimationSection> orderSections(List<AnimationSection> sections) {
		List<AnimationSection> orderedList = new ArrayList<AnimationSection>();
		for (int i = 0; i < sections.size(); i++) {
			for (AnimationSection section : sections) {
				if (section.getId() == i) {
					orderedList.add(section);
					break;
				}
			}
		}
		if (orderedList.size() < sections.size()) {
			closeWithError("Problem with the ordering of sections!", new Exception());
		}
		return orderedList;

	}

	private void readInRestPositions(List<AnimationSection> sections) {
		String check;
		try {
			while ((check = reader.readLine()) != null) {
				if (check.contains("<node id=\"Armature\"")) {
					processHeirachy(sections);
				}
			}
		} catch (Exception e) {
			closeWithError("Problem reading in the rest positions!", e);
		}
	}

	private void readStartMatrixForSection(AnimationSection section) throws Exception {
		String matrix = reader.readLine().split("[<>]")[2];
		String[] indieValues = matrix.split(" ");
		float[] matrixArray = new float[16];
		for (int i = 0; i < matrixArray.length; i++) {
			matrixArray[i] = Float.parseFloat(indieValues[i]);
		}
		section.setStartMatrix(matrixArray);
	}

	private AnimationSection createSection() {
		AnimationSection section = null;
		try {
			String iDLine = reader.readLine();
			String idName = iDLine.split("\"")[1];
			int id = Integer.parseInt(idName.split("_")[2]);
			section = new AnimationSection(id);
		} catch (Exception e) {
			closeWithError("Bone IDs must be in form Bone_X, where X is a number!", e);
		}
		RawKeyFrame[] frames = createFrameArray();
		section.setRawFrames(frames);
		return section;
	}

	private RawKeyFrame[] createFrameArray() {
		RawKeyFrame[] frames = null;
		try {
			String timesLine = reader.readLine();
			int count = Integer.parseInt(timesLine.split("\"")[3]);
			frames = new RawKeyFrame[count];
			String times = timesLine.split("[<>]")[2];
			String[] individualTimes = times.split(" ");
			for (int i = 0; i < frames.length; i++) {
				float time = Float.parseFloat(individualTimes[i]);
				frames[i] = new RawKeyFrame(time);
			}
			readInMatrices(frames);
		} catch (Exception e) {
			closeWithError("File format error!", e);
		}
		return frames;
	}

	private void readInMatrices(RawKeyFrame[] frames) throws Exception {
		String check;
		while ((check = reader.readLine()) != null) {
			if (check.contains("float_array")) {
				break;
			}
		}
		String allMatrixValues = check.split("[<>]")[2];
		String[] indieValues = allMatrixValues.split(" ");
		int pointer = 0;
		for (RawKeyFrame frame : frames) {
			float[] matrix = new float[16];
			for (int i = 0; i < 16; i++) {
				matrix[i] = Float.parseFloat(indieValues[pointer++]);
			}
			frame.setMatrix(matrix);
		}
	}

	private AnimationSection[] turnListToArray(List<AnimationSection> sections) {
		AnimationSection[] sectionsArray = new AnimationSection[sections.size()];
		for (int i = 0; i < sectionsArray.length; i++) {
			sectionsArray[i] = sections.get(i);
		}
		return sectionsArray;
	}

	private void processHeirachy(List<AnimationSection> sections) throws Exception {
		System.out.println("Number of sections: "+sections.size());
		String line;
		while (!(line = reader.readLine()).contains("</visual_scene>")) {
			if (line.contains("JOINT")) {
				processNode(line, sections);
			} else if (line.contains("NODE")) {
				skip();
			} else if (line.contains("</node>")) {
				if (currentParent != null) {
					currentParent = currentParent.getParent();
				} else {
					break;
				}
			}
		}
	}

	private void processNode(String nodeHeader, List<AnimationSection> sections) throws Exception {
		System.out.println("Node processed");
		String sectionName = nodeHeader.split("\"")[1];
		int sectionID = Integer.parseInt(sectionName.split("_")[1]);
		AnimationSection section = sections.get(sectionID);
		if (currentParent != null) {
			currentParent.addChild(section);
			section.setParent(currentParent);
		}
		currentParent = section;
		readStartMatrixForSection(section);
	}

	private void skip() throws Exception {
		while (!reader.readLine().contains("</node>")) {
		}
	}

	private void closeWithError(String errorMessage, Exception e) {
		e.printStackTrace();
		System.out.println();
		System.out.println();
		System.err.println(errorMessage);
		try {
			reader.close();
		} catch (IOException e1) {
		}
		System.exit(-1);
	}

}
