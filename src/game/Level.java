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
    private List<Tickable> tickables;
    private List<Moveable> movingObjects;
    private boolean isPaused;

    // Works a proxy to grant access to certain methods.
    class LevelChanger {
    }

    public Level(final int width, final int height) {
	this.width = width;
	this.height = height;
	levelKey = new LevelChanger();
	levelListeners = new ArrayList<>();

	initializeLevel();
    }

    private void initializeLevel() {
	tickables = new ArrayList<>();
	movingObjects = new ArrayList<>();

	player = new Player(BlockType.PLAYER, new Point2D(10, 10), 0.2f, this);
	movingObjects.add(player);
	tickables.add(player);

	createGrid();
    }
    private void createGrid() {
	blocks = new Block[height][width];
	for (int y = 0; y < height; y++) {
	    for (int x = 0; x < width; x++) {
		if (x == 0 || y == 0 || x == width - 1 || y == height - 1) {
		    createBlockAt(x, y, new Block(BlockType.WALL));
		} else if (y == 20) {
		    createBlockAt(x, y, new Block(BlockType.WALL1));
		} else if (y == 21) {
		    createBlockAt(x, y, new Block(BlockType.WALL2));
		} else {
		    createBlockAt(x, y, new Block(BlockType.EMPTY));
		}
	    }
	}
	createBlockAt(15,15, new Block(BlockType.WALL));
	createBlockAt(16,15, new Block(BlockType.WALL));
	createBlockAt(17,15, new Block(BlockType.WALL));
	createBlockAt(18,15, new Block(BlockType.WALL));
	createBlockAt(19,15, new Block(BlockType.WALL));
	createBlockAt(20,15, new Block(BlockType.WALL));
	createBlockAt(21,15, new Block(BlockType.WALL));
	createBlockAt(22,15, new Block(BlockType.WALL));
	createBlockAt(23,15, new Block(BlockType.WALL));

	createBlockAt(7, 5, new KeyBlock(BlockType.KEY, BlockType.WALL1, this, levelKey));
	createBlockAt(12, 12, new KeyBlock(BlockType.KEY, BlockType.WALL2, this, levelKey));
    }

    public void subscribeListener(LevelListener levelListener) {
        levelListeners.add(levelListener);
    }

    @Override public void tick() {
        if (!isPaused) {
	    for (Tickable tickable : tickables) {
		tickable.tick();
	    }
	    notifyListeners();
	}
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

    public void restartLevel() {
	initializeLevel();
    }

    public Iterator<Moveable> getMovingObstaclesIterator() {
        return movingObjects.iterator();
    }

    public BlockType getBlockTypeAt(int x, int y) {
        return blocks[y][x].getBlockType();
    }

    public Block getBlockAt(int x, int y) {
	return blocks[y][x];
    }

    public void removeBlockAt(LevelChanger levelChanger, int x, int y) {
        blocks[y][x] = new Block(BlockType.EMPTY);
    }

    private void createBlockAt(int x, int y, Block block) {
        blocks[y][x] = block;
    }

    public void movePlayer(final Direction direction) {
        player.move(direction);
    }

    public void removeDirection(final Direction direction) { player.releaseDirection(direction); }
}