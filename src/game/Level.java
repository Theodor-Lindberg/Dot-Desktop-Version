package game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Level implements Tickable
{
    private final int width, height;
    private Block[][] blocks;
    private Player player;
    private LevelChanger levelKey;
    private List<LevelListener> levelListeners;
    private List<Moveable> movingObjects;
    private boolean isPaused;

    // Works a proxy to grant access to certain methods.
    class LevelChanger {
    }

    public Level(final int width, final int height) {
	this.width = width;
	this.height = height;
	player = new Player(BlockType.PLAYER, new Point2D(10, 10), 0.2f, this);
	blocks = new Block[height][width];
	levelKey = new LevelChanger();
	levelListeners = new ArrayList<>();
	movingObjects = new ArrayList<>();
	movingObjects.add(player);
	for (int y = 0; y < getHeight(); y++)
	    for (int x = 0; x < getWidth(); x++)
		blocks[y][x] = new Block(BlockType.EMPTY);

	blocks[15][15] = new Block(BlockType.WALL);
	blocks[15][16] = new Block(BlockType.WALL);
	blocks[15][17] = new Block(BlockType.WALL);

    }

    public void subscribeListener(LevelListener levelListener) {
        levelListeners.add(levelListener);
    }

    @Override public void tick() {
	player.tick();
	notifyListeners();
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

    public boolean isPaused() {
	return isPaused;
    }

    public void setPaused(final boolean paused) {
	isPaused = paused;
    }

    public Iterator<Moveable> getMovingObstaclesIterator() {
        return movingObjects.iterator();
    }

    public BlockType getBlockTypeAt(int x, int y) {
        return blocks[y][x].getBlockType();
    }

    public Block getBlockAt(int x, int y)  {
            return blocks[y][x];
        }

    public void movePlayer(final Direction direction) {
        player.move(direction);
    }

    public void removeDirection(final Direction direction) { player.releaseDirection(direction); }
}