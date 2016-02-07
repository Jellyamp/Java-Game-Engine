package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager {

	// Constants for windows size and max fps
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	private static final int FPS_CAP = 120;
	
	// Creates the Display for the game
	public static void createDisplay() {
		
		// Creates the variable that will hold the OpenGL context with version 3.2 in forward compatibility mode
		ContextAttribs attribs = new ContextAttribs(3, 2)
				.withForwardCompatible(true)
				.withProfileCore(true);
		
		// Creates the display with the given width and height as well as the title and OpenGl context
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create(new PixelFormat(), attribs);
			Display.setTitle("Our First Display!");
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		
	}
	
	// Updates the display
	public static void updateDisplay() {
		
		Display.sync(FPS_CAP);
		Display.update();
		
	}
	
	// Destroys the Display
	public static void closeDisplay() {
		
		Display.destroy();
		
	}
	
}
