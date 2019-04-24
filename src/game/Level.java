package game;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * This class acts as a wrapper for the level and works as a template when the game is generated.
 */
public class Level extends Publisher implements LevelGrid
{
    private Block[][] blocks = null;

    public Level(final String fileName) {
	try {
	    blocks = FileHandler.readLevel(fileName);
	} catch (IOException ignored) {
	}
    }

    @Override public int getWidth() {
	return blocks[0].length;
    }

    @Override public int getHeight() {
	return blocks.length;
    }

    public void insertBlockAt(final int x, final int y, final Block block) {
	blocks[y][x] = block;
	notifyListeners();
    }

    @Override public Block getBlockAt(final int x, final int y) {
	return blocks[y][x];
    }

    public void saveToFile(final String fileName) {
	try {
	    FileHandler.saveLevel(fileName, blocks);
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	}
    }
}
