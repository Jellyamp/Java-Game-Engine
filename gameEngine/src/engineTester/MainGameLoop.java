package engineTester;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;

public class MainGameLoop {

	public static void main(String[] args) {
		
		// Create the Display
		DisplayManager.createDisplay();
		
		// Creates a new loader, renderer, and a new static shader
		Loader loader = new Loader();
		
		// Create the model
		RawModel model = OBJLoader.loadObjModel("tree", loader);
		ModelTexture loadedTexture = new ModelTexture(loader.loadTexture("tree"));
		TexturedModel staticModel = new TexturedModel(model, loadedTexture);
		ModelTexture texture = staticModel.getTexture();
		
		Entity entity = new Entity(staticModel, new Vector3f(0, 0, -25), 0, 0, 0, 1);
		Light light = new Light(new Vector3f(3000, 2000, 2000), new Vector3f(1, 1, 1));
		
		Terrain terrain = new Terrain(0, -1, loader, new ModelTexture(loader.loadTexture("grass")));
		Terrain terrain2 = new Terrain(-1, -1, loader, new ModelTexture(loader.loadTexture("grass")));
		
		Camera camera = new Camera();		
		MasterRenderer renderer = new MasterRenderer();
		
		// Main game loop
		while(!Display.isCloseRequested()) {
			entity.increaseRotation(0, 1, 0);
			camera.move();
			
			renderer.processTerrain(terrain);
			renderer.processTerrain(terrain2);
			renderer.processEntity(entity);
			
			renderer.render(light, camera);
			DisplayManager.updateDisplay();
			
		}
		
		// Delete all of the vaos, vbos, shaders, and destroy the display

		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
		
	}

}
