package engineTester;

import org.lwjgl.opengl.Display;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.RawModel;
import renderEngine.Renderer;

public class MainGameLoop {

	public static void main(String[] args) {
		
		// Create the Display
		DisplayManager.createDisplay();
		
		// Creates a new loader and renderer
		Loader loader = new Loader();
		Renderer renderer = new Renderer();
		
		float[] vertices = {
				-0.5f,  0.5f, 0,	//v0
				-0.5f, -0.5f, 0,	//v1
				 0.5f, -0.5f, 0,	//v2
				 0.5f,  0.5f, 0		//v3
		};
		
		int[] indices = {
				0, 1, 3,	// Top left triangle (V0, V1, V3)
				3, 1, 2		// Bottom right triangle (V3, V1, V2)
		};
		
		// Create the model
		RawModel model = loader.loadToVAO(vertices, indices);
		
		// Main game loop
		while(!Display.isCloseRequested()) {
			renderer.prepare();
			// game logic
			// Display the model
			renderer.render(model);
			DisplayManager.updateDisplay();
			
		}
		
		// Delete all of the vaos and vbos and destroy the display
		loader.cleanUP();
		DisplayManager.closeDisplay();
		
	}

}
