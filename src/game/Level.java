package game;

public class Level
{
    private final int width, height;
    private Block[][] blocks;
    private LevelChanger levelKey;

    // Works a proxy to grant access to certain methods.
    class LevelChanger {
    }

    public Level(final int width, final int height) {
	this.width = width;
	this.height = height;
	blocks = new Block[height][width];
	levelKey = new LevelChanger();
    }

    public int getWidth() {
	return width;
    }

    public int getHeight() {
	return height;
    }

    public Block getBlockAt(int x, int y) {
        return blocks[y][x];
    }
}