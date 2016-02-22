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
		
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy2"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("mossPath256"));
		
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, 
				gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		
		//************************************
		
		ModelData person = OBJFileLoader.loadOBJ("person");
		RawModel personModel = loader.loadToVAO(person.getVertices(), person.getTextureCoords(), person.getNormals(),
				person.getIndices());
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

		ModelTexture fernTextureAtlas = new ModelTexture(loader.loadTexture("fern"));
		fernTextureAtlas.setNumberOfRows(2);
		
		TexturedModel personTextured = new TexturedModel(personModel, new ModelTexture(
				loader.loadTexture("playerTexture")));
		TexturedModel treeTextured = new TexturedModel(treeModel, new ModelTexture(
				loader.loadTexture("tree")));
		TexturedModel grassTextured = new TexturedModel(grassModel, 
				new ModelTexture(loader.loadTexture("grassTexture")));
		TexturedModel flowerTextured = new TexturedModel(grassModel, 
				new ModelTexture(loader.loadTexture("flower")));
		TexturedModel fernTextured = new TexturedModel(fernModel, fernTextureAtlas);
		TexturedModel lowPolyTreeTextured = new TexturedModel(lowPolyTreeModel, 
				new ModelTexture(loader.loadTexture("lowPolyTree")));
		
		grassTextured.getTexture().setHasTransparency(true);
		grassTextured.getTexture().setUseFakeLighting(true);
		flowerTextured.getTexture().setHasTransparency(true);
		flowerTextured.getTexture().setUseFakeLighting(true);
		fernTextured.getTexture().setHasTransparency(true);
		
		
		Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "heightmap");
		Terrain terrain2 = new Terrain(-1, -1, loader, texturePack, blendMap, "heightmap");
		
		List<Entity> entities = new ArrayList<>();
		Random random = new Random(676452);
		
		for (int i = 0; i < 400; i++) {
			if (i % 2 == 0) {
				float x = random.nextFloat() * 800 - 400;
				float z = random.nextFloat() * -600;
				float y = terrain.getHeightOfTerrain(x, z);
				entities.add(new Entity(fernTextured, random.nextInt(4), new Vector3f(x, y, z), 0, random.nextFloat() * 360,
						0, 0.9f));
//				entities.add(new Entity(flowerTextured, new Vector3f(random.nextFloat() * 400 - 200, 0,
//						random.nextFloat() * -400),	0, 0, 0, 2.3f));
//				entities.add(new Entity(grassTextured, new Vector3f(random.nextFloat() * 400 - 200, 0,
//						random.nextFloat() * -400),	0, 0, 0, 1.8f));
			}
			
			if (i % 5 == 0) {
				float x = random.nextFloat() * 800 - 400;
				float z = random.nextFloat() * -600;
				float y = terrain.getHeightOfTerrain(x, z);
				entities.add(new Entity(lowPolyTreeTextured, new Vector3f(x, y, z), 0, random.nextFloat() * 360,
						0, random.nextFloat() * 0.1f + 0.6f));
				x = random.nextFloat() * 800 - 400;
				z = random.nextFloat() * -600;
				y = terrain.getHeightOfTerrain(x, z);
				entities.add(new Entity(treeTextured, new Vector3f(x, y, z), 0, 0, 0, random.nextFloat() * 1 + 4));				
			}
		}

		Light light = new Light(new Vector3f(3000, 2000, 2000), new Vector3f(1, 1, 1));

		

		MasterRenderer renderer = new MasterRenderer();

		Player player = new Player(personTextured, new Vector3f(100, 0, -50), 0, 180, 0, 0.6f);
		Camera camera = new Camera(player);
		
		// Main game loop
		while (!Display.isCloseRequested()) {
			player.move(terrain);
			camera.move();
			renderer.processEntity(player);
			renderer.processTerrain(terrain);
			//renderer.processTerrain(terrain2);
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
