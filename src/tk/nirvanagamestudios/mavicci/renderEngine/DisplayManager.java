package tk.nirvanagamestudios.mavicci.renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager {

	private static final int WIDTH = 800;
	private static final int HEIGHT = WIDTH / 16 * 9;
	private static final int FPS_CAP = 120;
	
	private static long lastFrameTime;
	private static float delta;
	
	public void createDisplay(){
		
		try{
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.setTitle("Mavicci");
			Display.create(new PixelFormat());
			//Mouse.setGrabbed(true);
		}catch(LWJGLException e){
			e.printStackTrace();
		}
		
		GL11.glViewport(0,0,WIDTH,HEIGHT);
		lastFrameTime = getCurrentTime();
	}
	
	public static int getWidth(){
		return WIDTH;
	}
	
	public static int getHeight(){
		return HEIGHT;
	}
	
	public void updateDisplay(){
		Display.sync(FPS_CAP);
		Display.update();
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) System.exit(0);;
		long currentFrameTime = getCurrentTime();
		delta = (currentFrameTime - lastFrameTime) / 1000f;
		lastFrameTime = currentFrameTime;
	}
	
	public void stopDisplay(){
		Display.destroy();
	}
	
	public static float getFrameTimeSeconds(){
		return delta;
	}
	
	public static long getCurrentTime(){
		return Sys.getTime()*1000/Sys.getTimerResolution();
	}
	
}
