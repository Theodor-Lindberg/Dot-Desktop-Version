package game;

import util.FileHandler;
import util.Observable;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * This class acts as a wrapper for the level and works as a template when the game is generated.
 */
public class Level extends Observable implements LevelGrid
{
    private Block[][] blocks;

    public Level(final String fileName) throws IOException {
	blocks = FileHandler.readLevel(fileName);
    }

    @Override public int getWidth() {
	return blocks[0].length;
    }

    @Override public int getHeight() {
	return blocks.length;
    }

    public void insertBlockAt(final int x, final int y, final Block block) {
	blocks[y][x] = block;
	notifyObservers();
    }

    @Override public Block getBlockAt(final int x, final int y) {
	return blocks[y][x];
    }

    public void saveToFile(final String fileName) throws FileNotFoundException {
	FileHandler.saveLevel(fileName, blocks);
    }
}
