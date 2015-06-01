package tk.nirvanagamestudios.mavicci.animation;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Matrix4f;

public class AnimationSection {

	private int id;
	private RawKeyFrame[] rawFrames;
	private Frame[] frames;
	private Matrix4f startMatrix;
	private Matrix4f accumulatedMatrix;
	private boolean hasPosChange;
	private boolean hasRotChange;
	private boolean hasScaleChange;
	private AnimationSection parent;
	private List<AnimationSection> children;

	public AnimationSection(int id) {
		this.id = id;
		children = new ArrayList<AnimationSection>();
		this.hasPosChange = false;
		this.hasRotChange = false;
		this.hasScaleChange = false;
	}

	public void setRawFrames(RawKeyFrame[] rawFrames) {
		this.rawFrames = rawFrames;
	}

	public int getId() {
		return id;
	}

	public RawKeyFrame[] getRawFrames() {
		return rawFrames;
	}

	public void setStartMatrix(float[] matrix) {
		startMatrix = new Matrix4f();
		int pointer = 0;
		startMatrix.m00 = matrix[pointer++];
		startMatrix.m10 = matrix[pointer++];
		startMatrix.m20 = matrix[pointer++];
		startMatrix.m30 = matrix[pointer++];
		startMatrix.m01 = matrix[pointer++];
		startMatrix.m11 = matrix[pointer++];
		startMatrix.m21 = matrix[pointer++];
		startMatrix.m31 = matrix[pointer++];
		startMatrix.m02 = matrix[pointer++];
		startMatrix.m12 = matrix[pointer++];
		startMatrix.m22 = matrix[pointer++];
		startMatrix.m32 = matrix[pointer++];
		startMatrix.m03 = matrix[pointer++];
		startMatrix.m13 = matrix[pointer++];
		startMatrix.m23 = matrix[pointer++];
		startMatrix.m33 = matrix[pointer++];
		accumulatedMatrix = new Matrix4f(startMatrix);
	}

	public Frame[] getFrames() {
		return frames;
	}

	public void setFrames(Frame[] frames) {
		this.frames = frames;
	}

	public Matrix4f getStartMatrix() {
		return startMatrix;
	}

	public boolean isHasPosChange() {
		return hasPosChange;
	}

	public void setHasPosChange(boolean hasPosChange) {
		this.hasPosChange = hasPosChange;
	}

	public boolean isHasRotChange() {
		return hasRotChange;
	}

	public void setHasRotChange(boolean hasRotChange) {
		this.hasRotChange = hasRotChange;
	}

	public boolean hasScaleChange() {
		return hasScaleChange;
	}

	public void setHasScaleChange(boolean hasScaleChange) {
		this.hasScaleChange = hasScaleChange;
	}

	public int getNumberOfFrames() {
		return frames.length;
	}

	public AnimationSection getParent() {
		return parent;
	}

	public void setParent(AnimationSection parent) {
		this.parent = parent;
	}

	public void addChild(AnimationSection child) {
		children.add(child);
	}

	public List<AnimationSection> getChildren() {
		return children;
	}

	public List<Matrix4f> getMatrices() {
		List<Matrix4f> matrices = new ArrayList<Matrix4f>();
		for (RawKeyFrame frame : rawFrames) {
			matrices.add(frame.getTransformation());
		}
		return matrices;
	}

	public void actOnMatrices(List<Matrix4f> parentMatrices) {
		for (int i = 0; i < parentMatrices.size(); i++) {
			Matrix4f childMatrix = rawFrames[i].getTransformation();
			Matrix4f parentMatrix = parentMatrices.get(i);
			Matrix4f inverted = new Matrix4f();
			Matrix4f.transpose(parentMatrix, inverted);
			Matrix4f.mul(childMatrix, inverted, childMatrix);
		}
	}

	public void convertAllRawFrames() {
		frames = new Frame[rawFrames.length];
		for (int i = 0; i < rawFrames.length; i++) {
			RawKeyFrame rawFrame = rawFrames[i];
			float[] translate = DataProcessor.getTranslation(rawFrame.getTransformation());
			if (translate[0] > 0.001 || translate[0] < -0.001 || translate[1] > 0.001
					|| translate[1] < -0.001 || translate[2] > 0.001 || translate[2] < -0.001) {
				hasPosChange = true;
			}
			Matrix4f frameMatrix = rawFrame.getTransformation();
			double angle = Math.acos((frameMatrix.m00 + frameMatrix.m11 + frameMatrix.m22 - 1) / 2);
			if(angle>0.0001||angle<-0.0001){
				hasRotChange = true;
			}
			double[] rotation = DataProcessor.matrixToQuaternion(rawFrame.getTransformation());
			int realTime = Math.round(rawFrame.getTime() * 1000);
			frames[i] = new Frame(realTime, translate[0], translate[1], translate[2], rotation[0],
					rotation[1], rotation[2], rotation[3], 1);
		}
	}

	public void printAll() {
		System.out.println("Section " + id);
		System.out.println("Pos: "+ hasPosChange+" Rot: "+hasRotChange);
		System.out.print("Children: ");
		for (AnimationSection section : children) {
			System.out.print(section.id + ", ");
		}
		System.out.println();
		for (Frame frame : frames) {
			frame.printAll();
		}
	}
	
	public Matrix4f getAccumulatedMatrix(){
		return this.accumulatedMatrix;
	}

}
