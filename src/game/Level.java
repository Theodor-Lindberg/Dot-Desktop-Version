package game;

import java.util.ArrayList;
import java.util.List;

public class Level
{
    private final int width, height;
    private Block[][] blocks;
    private LevelChanger levelKey;
    private List<LevelListener> levelListeners;

    // Works a proxy to grant access to certain methods.
    class LevelChanger {
    }

    public Level(final int width, final int height) {
	this.width = width;
	this.height = height;
	blocks = new Block[height][width];
	levelKey = new LevelChanger();
	levelListeners = new ArrayList<>();
	for (int y = 0; y < getHeight(); y++)
	    for (int x = 0; x < getWidth(); x++)
	        blocks[y][x] = new Block(BlockType.EMPTY);

    }

    public void subscribeListener(LevelListener levelListener) {
        levelListeners.add(levelListener);
    }

    private void notifyListeners() {
	for (LevelListener listener : levelListeners) {
	    listener.levelChanged();
	}
    }

    public int getWidth() {
	return width;
    }

    public int getHeight() {
	return height;
    }

    public BlockType getBlockAt(int x, int y) {
        return blocks[y][x].getBlockType();
    }
}