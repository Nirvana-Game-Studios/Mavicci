package tk.nirvanagamestudios.mavicci.animation;

import org.lwjgl.util.vector.Matrix4f;

public class RawKeyFrame {
	
	private float time;
	private Matrix4f frameMatrix;
	
	public RawKeyFrame(float time){
		this.time = time;
		frameMatrix = new Matrix4f();
	}
	
	public void setMatrix(float[] matrix){
		int pointer = 0;
		frameMatrix.m00 = matrix[pointer++];
		frameMatrix.m10 = matrix[pointer++];
		frameMatrix.m20 = matrix[pointer++];
		frameMatrix.m30 = matrix[pointer++];
		frameMatrix.m01 = matrix[pointer++];
		frameMatrix.m11 = matrix[pointer++];
		frameMatrix.m21 = matrix[pointer++];
		frameMatrix.m31 = matrix[pointer++];
		frameMatrix.m02 = matrix[pointer++];
		frameMatrix.m12 = matrix[pointer++];
		frameMatrix.m22 = matrix[pointer++];
		frameMatrix.m32 = matrix[pointer++];
		frameMatrix.m03 = matrix[pointer++];
		frameMatrix.m13 = matrix[pointer++];
		frameMatrix.m23 = matrix[pointer++];
		frameMatrix.m33 = matrix[pointer++];
	}

	public float getTime() {
		return time;
	}

	public Matrix4f getTransformation() {
		return frameMatrix;
	}
	
	public void setMatrix(Matrix4f matrix){
		this.frameMatrix = matrix;
	}
	
	public void printAll(){
		System.out.println("Time: "+this.time);
		//System.out.println(frameMatrix.toString());
		System.out.println("angle: "
				+ Math.toDegrees(Math
						.acos((frameMatrix.m00 + frameMatrix.m11 + frameMatrix.m22 - 1) / 2)));
		System.out.println("x: "
				+ (frameMatrix.m12 - frameMatrix.m21)
				/ Math.sqrt(Math.pow((frameMatrix.m12 - frameMatrix.m21), 2)
						+ Math.pow((frameMatrix.m20 - frameMatrix.m02), 2)
						+ Math.pow((frameMatrix.m01 - frameMatrix.m10), 2)));
		System.out.println("y: "
				+ (frameMatrix.m20 - frameMatrix.m02)
				/ Math.sqrt(Math.pow((frameMatrix.m12 - frameMatrix.m21), 2)
						+ Math.pow((frameMatrix.m20 - frameMatrix.m02), 2)
						+ Math.pow((frameMatrix.m01 - frameMatrix.m10), 2)));
		System.out.println("z: "
				+ (frameMatrix.m01 - frameMatrix.m10)
				/ Math.sqrt(Math.pow((frameMatrix.m12 - frameMatrix.m21), 2)
						+ Math.pow((frameMatrix.m20 - frameMatrix.m02), 2)
						+ Math.pow((frameMatrix.m01 - frameMatrix.m10), 2)));
		System.out.println();
		//double[] rotation = DataProcessor.matrixToQuaternion(frameMatrix);
		float[] translation = DataProcessor.getTranslation(frameMatrix);
		
		System.out.println("translation: "+translation[0]+", "+translation[1]+", "+translation[2]);
		//System.out.println("Rotation: "+rotation[0]+", "+rotation[1]+", "+rotation[2]+", "+rotation[3]);
		System.out.println();
	}
	
}
