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
    private boolean levelCompleted;

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

	player = new Player(new Point2D(10, 10), Moveable.Speed.FAST, this);
	movingObjects.add(player);
	tickables.add(player);

	Enemy enemy = new Enemy(new Point2D(17,17), Direction.RIGHT, Moveable.Speed.NORMAL, this, new BasicAI(BasicAI.TurnDirection.LEFT));
	movingObjects.add(enemy);
	tickables.add(enemy);

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

	createBlockAt(25,1, new EndBlock(this, levelKey));

	createBlockAt(7, 5, new KeyBlock(BlockType.WALL1, this, levelKey));
	createBlockAt(12, 12, new KeyBlock(BlockType.WALL2, this, levelKey));
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

    public Block getBlockAt(final int x, final int y) {
	return blocks[y][x];
    }

    public Block getCollidingEntity(final Block block, final float x, final float y) {
	for (Moveable movingObject : movingObjects) {
	    if (block != movingObject && Math.abs(movingObject.getX() - x) < 1 && Math.abs(movingObject.getY() - y) < 1) {
	        return movingObject;
	    }
	}

        return getBlockAt((int)x, (int)y);
    }

    public void removeBlockAt(LevelChanger levelChanger, int x, int y) {
        blocks[y][x] = new Block(BlockType.EMPTY);
    }

    public void completeLevel(LevelChanger levelChanger) {
	levelCompleted = true;
    }

    public boolean isLevelCompleted() {
        return levelCompleted;
    }

    private void createBlockAt(int x, int y, Block block) {
        blocks[y][x] = block;
    }

    public void movePlayer(final Direction direction) {
        player.move(direction);
    }

    public void removeDirection(final Direction direction) { player.releaseDirection(direction); }
}