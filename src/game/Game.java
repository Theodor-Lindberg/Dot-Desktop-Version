package game;

import util.Point2D;
import util.Publisher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Game class
 */
public class Game extends Publisher implements Tickable, LevelGrid
{
    private Block[][] blocks;
    private final Level level;
    private Player player = null;
    private final GameKey gameKey;
    private List<Tickable> tickables;
    private List<Movable> movingObjects;
    private boolean paused;
    private boolean levelCompleted;

    // Works a proxy to grant access to certain methods, the warning is ignored.
    public class GameKey
    {
    }

    public Game(final Level level) {
        super();
        this.level = level;
	gameKey = new GameKey();
	initializeLevel();
    }

    private void initializeLevel() {
	levelCompleted = false;
        blocks = new Block[level.getHeight()][level.getHeight()];
	movingObjects = new ArrayList<>();

	for (int y = 0; y < level.getHeight(); y++) {
	    for (int x = 0; x < level.getWidth(); x++) {
	        final Block block = level.getBlockAt(x, y);
	        if (block.getBlockType() == BlockType.PLAYER) {
	            player = new Player(new Point2D(x, y), ((Player)block).getSpeed(), this, gameKey);
	            movingObjects.add(player);
		    insertBlockAt(x, y, new Block(BlockType.EMPTY));
		}
	        else if (block.getBlockType() == BlockType.ENEMY) {
		    movingObjects.add(new Enemy((Enemy)block, this));
		    insertBlockAt(x, y, new Block(BlockType.EMPTY));
		}
	        else if (block.getBlockType() == BlockType.KEY) {
	            insertBlockAt(x, y, new KeyBlock(((KeyBlock)block).getTargetBlock(), this, gameKey));
		}
	        else if (block.getBlockType() == BlockType.END) {
		    insertBlockAt(x, y, new EndBlock(this, gameKey));
		}
	        else {
		    insertBlockAt(x, y, new Block(block.getBlockType()));
		}
	    }
	}

	tickables = new ArrayList<>();
	tickables.addAll(movingObjects); // tickables and movingObjects currently contains the same elements but tickables is used for better scalability
    }

    @Override public void tick() {
	if (!paused) {
	    for (Tickable tickable : tickables) {
		tickable.tick();
	    }
	}

	notifyListeners();
    }

    @Override public int getWidth() {
	return level.getWidth();
    }

    @Override public int getHeight() {
	return level.getHeight();
    }

    public boolean isPaused() {
	return paused;
    }

    public void setPaused(final boolean paused) {
	this.paused = paused;
    }

    public void restartLevel() {
	initializeLevel();
    }

    public Iterator<Movable> getMovingObjectsIterator() {
        return movingObjects.iterator();
    }

    @Override public Block getBlockAt(final int x, final int y) {
        if (x >= 0 && x < getWidth() && y >= 0 && y < getHeight()) {
	    return blocks[y][x];
	}
	return new Block(BlockType.WALL);
    }

    public Block getCollidingEntity(final Block block, final float x, final float y) {
	for (Movable movingObject : movingObjects) {
	    if (block != movingObject && Math.abs(movingObject.getX() - x) < 1 && Math.abs(movingObject.getY() - y) < 1) { // Ignore the warning since reference comparison is intended.
	        return movingObject;
	    }
	}

        return getBlockAt((int)x, (int)y);
    }

    public void removeBlockAt(final GameKey gameKey, final int x, final int y) {
        if (gameKey == this.gameKey) { // Only accept the instance that the level itself created, warning is ignored.
	    insertBlockAt(x, y, new Block(BlockType.EMPTY));
	}
    }

    public void completeLevel(final GameKey gameKey) {
        if (gameKey == this.gameKey) { // Only accept the instance that the level itself created, warning is ignored.
	    levelCompleted = true;
	}
    }

    public boolean isLevelCompleted() {
        return levelCompleted;
    }

    private void insertBlockAt(final int x, final int y, Block block) {
	blocks[y][x] = block;
    }

    public void playerDied(final GameKey gameKey) {
        if (gameKey == this.gameKey) { // Only accept the instance that the level itself created, warning is ignored.
	    restartLevel();
	}
    }

    public void movePlayer(final Direction direction) {
        player.move(direction);
    }

    public void removeDirection(final Direction direction) { player.releaseDirection(direction); }
}