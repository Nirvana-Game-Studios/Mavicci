package tk.nirvanagamestudios.mavicci.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import tk.nirvanagamestudios.mavicci.renderEngine.DisplayManager;

public class Camera {

	private float distanceFromPlayer = 50;
	private float angleAroundPlayer = 0;
	
	private Vector3f position = new Vector3f(-100, 35 ,50);
	private float pitch = 30;
	private float yaw = 0;
	private float roll;

	private Player player;
	
	public Camera(Player player){
		this.player = player;
	}
	
	public void move(){	
		calculateZoom();
		calculatePitch();
		float horizDistance = calculateHorizDistance();
		float verticalDistance = calculateVerticalDistance();
		calculateCameraPosition(horizDistance, verticalDistance);
		this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
		if(pitch >= 90){
			pitch = 90;
		}else if(pitch <= 0){
			pitch = 0;
		}
	}
	
	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}
	
	public void invertPitch(){
		pitch = -pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
	
	private void calculateCameraPosition(float horizDist, float vertDist){
		float theta = player.getRotY() + angleAroundPlayer;
		float dx = (float) (horizDist * Math.sin(Math.toRadians(theta)));
		float dz = (float) (horizDist * Math.cos(Math.toRadians(theta)));
		position.y = player.getPosition().y + vertDist;
		position.x = player.getPosition().x - dx;
		position.z = player.getPosition().z - dz;
	}
	
	private float calculateHorizDistance(){
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}
	
	private float calculateVerticalDistance(){
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}
	
	private void calculateZoom(){
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		distanceFromPlayer -= zoomLevel;
	}
	
	private void calculatePitch(){
		if(Mouse.isButtonDown(1)){
			float pitchChange = Mouse.getDY() * 0.1f;
			pitch -= pitchChange;
		}
	}
	
}
