package game;

import game.GameObjects.Block;
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

    /**
     * @param fileName 		The name of the file to read from.
     *
     * @throws IOException 	Throws if the file could not be read.
     */
    public Level(final String fileName) throws IOException {
	blocks = FileHandler.readLevel(fileName);
    }

    @Override public int getWidth() {
	return blocks[0].length;
    }

    @Override public int getHeight() {
	return blocks.length;
    }

    /**
     * Insert a block on a coordinate.
     *
     * @param x 	Coordinate on the x-axis
     * @param y 	Coordinate on the y-axis
     * @param block 	The block to insert
     */
    public void insertBlockAt(final int x, final int y, final Block block) {
	blocks[y][x] = block;
	notifyObservers();
    }

    @Override public Block getBlockAt(final int x, final int y) {
	return blocks[y][x];
    }

    /**
     * Save level information to a file.
     *
     * @param fileName 			The name of the file to save to.
     *
     * @throws FileNotFoundException 	Throws if the file name is not found.
     */
    public void saveToFile(final String fileName) throws FileNotFoundException {
	FileHandler.saveLevel(fileName, blocks);
    }
}
