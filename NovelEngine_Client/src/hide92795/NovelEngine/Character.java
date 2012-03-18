package hide92795.novelengine;

import hide92795.novelengine.client.NovelEngine;

import java.util.HashMap;

import org.newdawn.slick.opengl.Texture;

public class Character {
	private final int characterId;
	private final String name;
	private int currentFace;

	/**
	 * Key:表情ID Val:画像ID
	 */
	private HashMap<Integer, Integer> faces;

	public Character(int characterId, String name) {
		this.characterId = characterId;
		this.name = name;
		this.faces = new HashMap<Integer, Integer>();
	}

	public void addFace(int faceId, int imageId) {
		faces.put(faceId, imageId);
	}

	public int getCharacterId() {
		return characterId;
	}

	public String getName() {
		return name;
	}

	public void render(NovelEngine engine, int xpos, int ypos) {
		System.out.println("Character.render()");
		System.out.println(faces.containsKey(currentFace));
		int imgId = faces.get(currentFace);
		System.out.println(imgId);
		Texture tex = engine.imageManager.getImage(imgId);
		System.out.println(tex);
		Renderer.renderImage(tex, xpos, ypos, xpos + tex.getTextureWidth(),
				ypos + tex.getTextureHeight());
	}

	public void setCurrentFace(int faceId) {
		this.currentFace = faceId;
	}
}
