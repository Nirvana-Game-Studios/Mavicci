package tk.nirvanagamestudios.mavicci.renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager {

	private static final int WIDTH = 800;
	private static final int HEIGHT = WIDTH / 16 * 9;
	private static final int FPS_CAP = 120;
	
	public void createDisplay(){
		
		ContextAttribs attribs = new ContextAttribs(3, 3).withForwardCompatible(true).withProfileCore(true);
		try{
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.setTitle("Mavicci");
			Display.create(new PixelFormat(), attribs);
		}catch(LWJGLException e){
			e.printStackTrace();
		}
		
		GL11.glViewport(0,0,WIDTH,HEIGHT);
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
	}
	
	public void stopDisplay(){
		Display.destroy();
	}
	
}
