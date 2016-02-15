package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import models.RawModel;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

public class MainGameLoop {

	public static void main(String[] args) {

		// Create the Display
		DisplayManager.createDisplay();

		// Creates a new loader, renderer, and a new static shader
		Loader loader = new Loader();

		
		//*******TERRAIN TEXTURES*************
		
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, 
				gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		
		//************************************
		
		ModelData bunny = OBJFileLoader.loadOBJ("stanfordBunny");
		RawModel bunnyModel = loader.loadToVAO(bunny.getVertices(), bunny.getTextureCoords(), bunny.getNormals(),
				bunny.getIndices());
		ModelData tree = OBJFileLoader.loadOBJ("tree");
		RawModel treeModel = loader.loadToVAO(tree.getVertices(), tree.getTextureCoords(), tree.getNormals(),
				tree.getIndices());
		ModelData lowPolyTree = OBJFileLoader.loadOBJ("lowPolyTree");
		RawModel lowPolyTreeModel = loader.loadToVAO(lowPolyTree.getVertices(), lowPolyTree.getTextureCoords(), lowPolyTree.getNormals(),
				lowPolyTree.getIndices());
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

		TexturedModel bunnyTextured = new TexturedModel(bunnyModel, new ModelTexture(
				loader.loadTexture("white")));
		TexturedModel treeTextured = new TexturedModel(treeModel, new ModelTexture(
				loader.loadTexture("tree")));
		TexturedModel grassTextured = new TexturedModel(grassModel, 
				new ModelTexture(loader.loadTexture("grassTexture")));
		TexturedModel flowerTextured = new TexturedModel(grassModel, 
				new ModelTexture(loader.loadTexture("flower")));
		TexturedModel fernTextured = new TexturedModel(fernModel, 
				new ModelTexture(loader.loadTexture("fern")));
		TexturedModel lowPolyTreeTextured = new TexturedModel(lowPolyTreeModel, 
				new ModelTexture(loader.loadTexture("lowPolyTree")));
		
		grassTextured.getTexture().setHasTransparency(true);
		grassTextured.getTexture().setUseFakeLighting(true);
		flowerTextured.getTexture().setHasTransparency(true);
		flowerTextured.getTexture().setUseFakeLighting(true);
		fernTextured.getTexture().setHasTransparency(true);
		
		List<Entity> entities = new ArrayList<>();
		Random random = new Random();
		
		for (int i = 0; i < 400; i++) {
			if (i % 7 == 0) {
				entities.add(new Entity(grassTextured, new Vector3f(random.nextFloat() * 400 - 200, 0,
						random.nextFloat() * -400),	0, 0, 0, 1.8f));
				entities.add(new Entity(flowerTextured, new Vector3f(random.nextFloat() * 400 - 200, 0,
						random.nextFloat() * -400),	0, 0, 0, 2.3f));
			}
			
			if (i % 3 == 0) {
				entities.add(new Entity(fernTextured, new Vector3f(random.nextFloat() * 400 - 200, 0,
						random.nextFloat() * -400), 0, random.nextFloat() * 360, 0, 0.9f));
				entities.add(new Entity(lowPolyTreeTextured, new Vector3f(random.nextFloat() * 800 - 400, 0,
						random.nextFloat() * -600), 0, random.nextFloat() * 360, 0, 
						random.nextFloat() * 0.1f + 0.6f));
				entities.add(new Entity(treeTextured, new Vector3f(random.nextFloat() * 800 - 400, 0,
						random.nextFloat() * -600), 0, 0, 0, random.nextFloat() * 1 + 4));				
			}
		}

		Light light = new Light(new Vector3f(3000, 2000, 2000), new Vector3f(1, 1, 1));

		Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap);
		Terrain terrain2 = new Terrain(-1, -1, loader, texturePack, blendMap);

		Camera camera = new Camera();
		MasterRenderer renderer = new MasterRenderer();

		Player player = new Player(bunnyTextured, new Vector3f(100, 0, -50), 0, 0, 0, 1);
		
		// Main game loop
		while (!Display.isCloseRequested()) {
			camera.move();
			player.move();
			renderer.processEntity(player);
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
