package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.RawModel;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import terrains.Terrain;
import textures.ModelTexture;

public class MainGameLoop {

	public static void main(String[] args) {

		// Create the Display
		DisplayManager.createDisplay();

		// Creates a new loader, renderer, and a new static shader
		Loader loader = new Loader();

		ModelData tree = OBJFileLoader.loadOBJ("tree");
		RawModel treeModel = loader.loadToVAO(tree.getVertices(), tree.getTextureCoords(), tree.getNormals(),
				tree.getIndices());
		ModelData grass = OBJFileLoader.loadOBJ("grassModel");
		RawModel grassModel = loader.loadToVAO(grass.getVertices(), grass.getTextureCoords(), grass.getNormals(),
				grass.getIndices());
		ModelData fern = OBJFileLoader.loadOBJ("fern");
		RawModel fernModel = loader.loadToVAO(fern.getVertices(), fern.getTextureCoords(), fern.getNormals(),
				fern.getIndices());

		// Create the model
		// RawModel model = OBJLoader.loadObjModel("Tree1", loader);
		// RawModel grassModel = OBJLoader.loadObjModel("grassModel", loader);
		// RawModel fernModel = OBJLoader.loadObjModel("fern", loader);

		TexturedModel treeTextured = new TexturedModel(treeModel, new ModelTexture(loader.loadTexture("tree")));
		TexturedModel grassTextured = new TexturedModel(grassModel, new ModelTexture(loader.loadTexture("flower")));
		grassTextured.getTexture().setHasTransparency(true);
		grassTextured.getTexture().setUseFakeLighting(true);
		TexturedModel fernTextured = new TexturedModel(fernModel, new ModelTexture(loader.loadTexture("flower")));
		fernTextured.getTexture().setHasTransparency(true);

		List<Entity> entities = new ArrayList<>();
		Random random = new Random();
		for (int i = 0; i < 500; i++) {
			entities.add(new Entity(treeTextured,
					new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600), 0, 0, 0, 3));
			entities.add(new Entity(grassTextured, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600),
					0, 0, 0, 1));
			entities.add(new Entity(fernTextured, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600), 0,
					0, 0, 0.6f));
		}

		Light light = new Light(new Vector3f(3000, 2000, 2000), new Vector3f(1, 1, 1));

		Terrain terrain = new Terrain(0, -1, loader, new ModelTexture(loader.loadTexture("grass")));
		Terrain terrain2 = new Terrain(-1, -1, loader, new ModelTexture(loader.loadTexture("grass")));

		Camera camera = new Camera();
		MasterRenderer renderer = new MasterRenderer();

		// Main game loop
		while (!Display.isCloseRequested()) {
			camera.move();

			renderer.processTerrain(terrain);
			renderer.processTerrain(terrain2);
			for (Entity entity : entities) {
				renderer.processEntity(entity);
			}

			renderer.render(light, camera);
			DisplayManager.updateDisplay();

		}

		// Delete all of the vaos, vbos, shaders, and destroy the display

		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}

}
