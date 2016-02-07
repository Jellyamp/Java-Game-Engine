package engineTester;

import org.lwjgl.opengl.Display;

import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;

public class MainGameLoop {

	public static void main(String[] args) {
		
		// Create the Display
		DisplayManager.createDisplay();
		
		// Creates a new loader, renderer, and a new static shader
		Loader loader = new Loader();
		Renderer renderer = new Renderer();
		StaticShader shader = new StaticShader();
		
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
		
		float[] textureCoords = {
				0, 0,	//V0
				0, 1,	//V1
				1, 1,	//V2
				1, 0 	//V3
		};
		
		// Create the model
		RawModel model = loader.loadToVAO(vertices, textureCoords, indices);
		ModelTexture texture = new ModelTexture(loader.loadTexture("image"));
		TexturedModel texturedModel = new TexturedModel(model, texture);
		
		// Main game loop
		while(!Display.isCloseRequested()) {
			renderer.prepare();
			// game logic
			// Display the model
			shader.start();
			renderer.render(texturedModel);
			shader.stop();
			DisplayManager.updateDisplay();
			
		}
		
		// Delete all of the vaos, vbos, shaders, and destroy the display
		shader.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
		
	}

}
