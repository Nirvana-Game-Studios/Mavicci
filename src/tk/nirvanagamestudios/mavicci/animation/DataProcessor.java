package tk.nirvanagamestudios.mavicci.animation;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;

public class DataProcessor {

	public static void tryInversingStuff(Animation anim) {
		AnimationSection[] sections = anim.getSections();
		for (AnimationSection section : sections) {
			invertMultiplyAllFrames(section);
		}
	}

	public static void matrixStuff(AnimationSection section) {
		List<AnimationSection> children = section.getChildren();
		for (AnimationSection child : children) {
			Matrix4f childMatrix = child.getAccumulatedMatrix();
			Matrix4f.mul(section.getAccumulatedMatrix(), childMatrix,
					childMatrix);
			matrixStuff(child);
		}
	}

	public static void rotate90Degrees(Animation animation) {
		AnimationSection[] sections = animation.getSections();
		for (AnimationSection section : sections) {
			RawKeyFrame[] frames = section.getRawFrames();
			for (RawKeyFrame frame : frames) {
				Matrix4f matrix = frame.getTransformation();
				Matrix4f translate = extractTranslationMatrix(matrix);
				double[] axisAngleRotated = matrixToRotatedAxisAngle(matrix);
				Matrix4f rotated = axisAngleToMatrix(axisAngleRotated);
				rotated.m30 = -translate.m30;
				rotated.m31 = translate.m32;
				rotated.m32 = -translate.m31;
				frame.setMatrix(rotated);
			}
		}
	}

	private static double[] matrixToRotatedAxisAngle(Matrix4f frameMatrix) {
		double[] axisAngle = new double[4];

		if ((Math.abs(frameMatrix.m01 + frameMatrix.m10) < 0.001)
				&& (Math.abs(frameMatrix.m20 + frameMatrix.m02) < 0.001)
				&& (Math.abs(frameMatrix.m12 + frameMatrix.m21) < 0.001)
				&& (Math.abs(frameMatrix.m00 + frameMatrix.m11
						+ frameMatrix.m22 - 3) < 0.001)) {
			return new double[] { 0, 1, 0, 0 };
		}

		axisAngle[0] = Math.acos((frameMatrix.m00 + frameMatrix.m11
				+ frameMatrix.m22 - 1) / 2);
		axisAngle[1] = -(frameMatrix.m12 - frameMatrix.m21)
				/ Math.sqrt(Math.pow((frameMatrix.m12 - frameMatrix.m21), 2)
						+ Math.pow((frameMatrix.m20 - frameMatrix.m02), 2)
						+ Math.pow((frameMatrix.m01 - frameMatrix.m10), 2));
		axisAngle[3] = ((frameMatrix.m20 - frameMatrix.m02) / Math.sqrt(Math
				.pow((frameMatrix.m12 - frameMatrix.m21), 2)
				+ Math.pow((frameMatrix.m20 - frameMatrix.m02), 2)
				+ Math.pow((frameMatrix.m01 - frameMatrix.m10), 2)));
		axisAngle[2] = -(frameMatrix.m01 - frameMatrix.m10)
				/ Math.sqrt(Math.pow((frameMatrix.m12 - frameMatrix.m21), 2)
						+ Math.pow((frameMatrix.m20 - frameMatrix.m02), 2)
						+ Math.pow((frameMatrix.m01 - frameMatrix.m10), 2));
		return axisAngle;
	}

	private static Matrix4f axisAngleToMatrix(double[] axisAngle) {
		Matrix4f matrix = new Matrix4f();
		double c = Math.cos(axisAngle[0]);
		double s = Math.sin(axisAngle[0]);
		double t = 1 - c;
		double x = axisAngle[1];
		double y = axisAngle[2];
		double z = axisAngle[3];
		matrix.m00 = (float) (t * x * x + c);
		matrix.m11 = (float) (t * y * y + c);
		matrix.m22 = (float) (t * z * z + c);
		matrix.m10 = (float) (t * x * y - z * s);
		matrix.m20 = (float) (t * x * z + y * s);
		matrix.m01 = (float) (t * x * y + z * s);
		matrix.m02 = (float) (t * x * z - y * s);
		matrix.m21 = (float) (t * y * z - x * s);
		matrix.m12 = (float) (t * y * z + x * s);
		return matrix;
	}

	private static void invertMultiplyAllFrames(AnimationSection section) {
		Matrix4f inverse = new Matrix4f();
		Matrix4f.invert(section.getStartMatrix(), inverse);

		Matrix4f rot = new Matrix4f();
		Matrix4f inverted = new Matrix4f();
		if (true) {
			rot = section.getAccumulatedMatrix();
			inverted = new Matrix4f();
			Matrix4f.invert(rot, inverted);
		}

		RawKeyFrame[] frames = section.getRawFrames();
		for (RawKeyFrame frame : frames) {
			Matrix4f frameMatrix = frame.getTransformation();

			// The code below works. Iunno how, it just does.
			Matrix4f justTranslate = extractTranslationMatrix(frameMatrix);
			Matrix4f translateMatrix = new Matrix4f(section.getStartMatrix());
			translateMatrix.m30 = justTranslate.m30;
			translateMatrix.m31 = justTranslate.m31;
			translateMatrix.m32 = justTranslate.m32;
			Matrix4f.mul(inverse, translateMatrix, translateMatrix);
			Matrix4f.mul(rot, translateMatrix, translateMatrix);
			Matrix4f.mul(translateMatrix, inverted, translateMatrix);
			// pos

			// rot
			Matrix4f.mul(inverse, frameMatrix, frameMatrix);
			Matrix4f.mul(rot, frameMatrix, frameMatrix);
			Matrix4f.mul(frameMatrix, inverted, frameMatrix);
			// rot

			frameMatrix.m30 = translateMatrix.m30;
			frameMatrix.m31 = translateMatrix.m31;
			frameMatrix.m32 = translateMatrix.m32;

		}
	}

	// private static Matrix4f extractRotationMatrix(Matrix4f matrix) {
	// Matrix4f rotation = new Matrix4f(matrix);
	// rotation.m30 = 0;
	// rotation.m31 = 0;
	// rotation.m32 = 0;
	// return rotation;
	// }

	private static Matrix4f extractTranslationMatrix(Matrix4f matrix) {
		Matrix4f translation = new Matrix4f();
		translation.m30 = matrix.m30;
		translation.m31 = matrix.m31;
		translation.m32 = matrix.m32;
		return translation;
	}

	public static double[] matrixToQuaternion(Matrix4f matrix) {
		double[] quaternion = new double[4];
		quaternion[0] = Math.sqrt(1.0 + matrix.m00 + matrix.m11 + matrix.m22) / 2.0;
		double w4 = quaternion[0] * 4;
		quaternion[1] = (matrix.m12 - matrix.m21) / w4;
		quaternion[2] = (matrix.m20 - matrix.m02) / w4;
		quaternion[3] = (matrix.m01 - matrix.m10) / w4;
		return quaternion;
	}

	public static Matrix4f convertQuaternionToMatrix4f(double[] q) {
		Matrix4f matrix = new Matrix4f();
		q = new double[4];
		matrix.m00 = (float) (1.0f - 2.0f * (q[1] * q[1] + q[2] * q[2]));
		matrix.m01 = (float) (2.0f * (q[0] * q[1] + q[2] * q[3]));
		matrix.m02 = (float) (2.0f * (q[0] * q[2] - q[1] * q[3]));
		matrix.m03 = 0.0f;

		matrix.m10 = (float) (2.0f * (q[0] * q[1] - q[2] * q[3]));
		matrix.m11 = (float) (1.0f - 2.0f * (q[0] * q[0] + q[2] * q[2]));
		matrix.m12 = (float) (2.0f * (q[2] * q[1] + q[0] * q[3]));
		matrix.m13 = 0.0f;

		matrix.m20 = (float) (2.0f * (q[0] * q[2] + q[1] * q[3]));
		matrix.m21 = (float) (2.0f * (q[1] * q[2] - q[0] * q[3]));
		matrix.m22 = (float) (1.0f - 2.0f * (q[1] * q[1] + q[0] + q[0]));
		matrix.m23 = 0.0f;

		matrix.m30 = 0;
		matrix.m31 = 0;
		matrix.m32 = 0;
		matrix.m33 = 1.0f;

		return matrix;
	}

	public static float[] getTranslation(Matrix4f matrix) {
		float[] translate = new float[3];
		translate[0] = matrix.m30;
		translate[1] = matrix.m31;
		translate[2] = matrix.m32;
		return translate;
	}

	public static void setDefaultAnimationLength(Animation animation) {
		int longestTime = 0;
		AnimationSection[] sections = animation.getSections();
		for (AnimationSection section : sections) {
			Frame[] frames = section.getFrames();
			for (Frame frame : frames) {
				int time = frame.getTime();
				if (time > longestTime) {
					longestTime = time;
				}

			}
		}
		animation.setLengthInMillis(longestTime);
	}

	public static void setDefaultMaxChangeValues(Animation animation) {
		AnimationSection[] sections = animation.getSections();
		double maxRotChange = 0;
		float maxPosChange = 0;
		for (AnimationSection section : sections) {
			double rotChange = findMaxRotChangeForFrames(section);
			float posChange = findMaxPosChangeForFrames(section);
			if (rotChange > maxRotChange) {
				maxRotChange = rotChange;
			} else if (rotChange < -maxRotChange) {
				maxRotChange = -rotChange;
			}
			if (posChange > maxPosChange) {
				maxPosChange = posChange;
			} else if (posChange < -maxPosChange) {
				maxPosChange = -posChange;
			}
		}
		animation.setMaxPosChange(maxPosChange);
		animation.setMaxRotChange((float) maxRotChange);
		animation.setMaxScaleChange(0);
	}

	private static double findMaxRotChangeForFrames(AnimationSection section) {
		if (!section.isHasRotChange()) {
			return 0;
		}
		double maxChange = 0;
		Frame[] frames = section.getFrames();
		Frame previous = frames[0];
		for (int i = 1; i < frames.length; i++) {
			Frame next = frames[i];
			double[] qA = { previous.getRotW(), previous.getRotX(),
					previous.getRotY(), previous.getRotZ() };
			double[] qB = { next.getRotW(), next.getRotX(), next.getRotY(),
					next.getRotZ() };
			double angleBetween = getAngleBetweenQuaternions(qA, qB);
			int timePassed = next.getTime() - previous.getTime();
			double changePerMilli = angleBetween / timePassed;
			if (changePerMilli > maxChange) {
				maxChange = changePerMilli;
			} else if (changePerMilli < -maxChange) {
				maxChange = -changePerMilli;
			}
			previous = next;
		}
		return maxChange;
	}

	private static float findMaxPosChangeForFrames(AnimationSection section) {
		if (!section.isHasPosChange()) {
			return 0;
		}
		float maxChange = 0;
		Frame[] frames = section.getFrames();
		Frame previous = frames[0];
		for (int i = 1; i < frames.length; i++) {
			Frame next = frames[i];
			float[] pos1 = { previous.getX(), previous.getY(), previous.getZ() };
			float[] pos2 = { next.getX(), next.getY(), next.getZ() };
			float change = getBiggestChange(pos1, pos2);
			int timePassed = next.getTime() - previous.getTime();
			float changePerMilli = change / timePassed;
			if (changePerMilli > maxChange) {
				maxChange = changePerMilli;
			} else if (changePerMilli < -maxChange) {
				maxChange = -changePerMilli;
			}
			previous = next;
		}
		return maxChange;

	}

	private static double getAngleBetweenQuaternions(double[] qA, double[] qB) {
		double dot = qA[0] * qB[0] + qA[1] * qB[1] + qA[2] * qB[2] + qA[3]
				* qB[3];
		return Math.acos(dot);
	}

	private static float getBiggestChange(float[] position1, float[] position2) {
		float changeX = position1[0] - position2[0];
		float changeY = position1[1] - position2[1];
		float changeZ = position1[2] - position2[2];
		float biggest = 0;
		if (changeX > biggest) {
			biggest = changeX;
		} else if (changeX < -biggest) {
			biggest = -changeX;
		}
		if (changeY > biggest) {
			biggest = changeY;
		} else if (changeY < -biggest) {
			biggest = -changeY;
		}
		if (changeZ > biggest) {
			biggest = changeZ;
		} else if (changeZ < -biggest) {
			biggest = -changeZ;
		}
		return biggest;
	}

}
