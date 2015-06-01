package tk.nirvanagamestudios.mavicci.animation;

public class Frame {

	private int time;
	private float x;
	private float y;
	private float z;
	private double rotW;
	private double rotX;
	private double rotY;
	private double rotZ;
	private float scale;

	public Frame(int time, float x, float y, float z, double rotW, double rotX, double rotY,
			double rotZ, float scale) {
		this.time = time;
		this.x = x;
		this.y = y;
		this.z = z;
		this.rotW = rotW;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
	}

	public int getTime() {
		return time;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}

	public double getRotW() {
		return this.rotW;
	}

	public double getRotX() {
		return this.rotX;
	}

	public double getRotY() {
		return this.rotY;
	}

	public double getRotZ() {
		return this.rotZ;
	}

	public float getScale() {
		return scale;
	}

	public void printAll() {
		double[] axisAngle = getAxisAngle(new double[]{rotW,rotX,rotY,rotZ});
		System.out.println("Time: " + time);
		System.out.println("Pos: " + x + ", " + y + ", " + z);
		System.out.println("Rot: " + Math.toDegrees(axisAngle[0]) + ", " + axisAngle[1] + ", " + axisAngle[2] + ", " + axisAngle[3]);
		System.out.println();
	}

	private double[] getAxisAngle(double[] quaternion) {
		double[] axisAngle = new double[4];
		axisAngle[0] = 2 * Math.acos(quaternion[0]);
		axisAngle[1] = quaternion[1] / Math.sqrt(1 - quaternion[0] * quaternion[0]);
		axisAngle[2] = quaternion[2] / Math.sqrt(1 - quaternion[0] * quaternion[0]);
		axisAngle[3] = quaternion[3] / Math.sqrt(1 - quaternion[0] * quaternion[0]);
		return axisAngle;
	}
}
