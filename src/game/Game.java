package game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 */
public class Game extends Publisher implements Tickable, LevelGrid
{
    private Block[][] blocks;
    private Level level;
    private Player player;
    private final LevelChanger levelKey;
    private List<Tickable> tickables;
    private List<Movable> movingObjects;
    private boolean paused;
    private boolean levelCompleted;

    // Works a proxy to grant access to certain methods, the warning is ignored.
    public class LevelChanger {
    }

    public Game(final Level level) {
        super();
        this.level = level;
	levelKey = new LevelChanger();
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
	            player = new Player(new Point2D(x, y), ((Player)block).getSpeed(), this);
	            movingObjects.add(player);
		    createBlockAt(x, y, new Block(BlockType.EMPTY));
		}
	        else if (block.getBlockType() == BlockType.ENEMY) {
		    movingObjects.add(new Enemy((Enemy)block, this));
		    createBlockAt(x, y, new Block(BlockType.EMPTY));
		}
	        else if (block.getBlockType() == BlockType.KEY) {
	            createBlockAt(x, y, new KeyBlock(((KeyBlock)block).getTargetBlock(), this, levelKey));
		}
	        else if (block.getBlockType() == BlockType.END) {
		    createBlockAt(x, y, new EndBlock(this, levelKey));
		}
	        else {
		    createBlockAt(x, y, new Block(block.getBlockType()));
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

	    if (!player.isAlive() && !levelCompleted) {
		playerDied();
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

    public Iterator<Movable> getMovingObstaclesIterator() {
        return movingObjects.iterator();
    }

    @Override public Block getBlockAt(final int x, final int y) {
	return blocks[y][x];
    }

    public Block getCollidingEntity(final Block block, final float x, final float y) {
	for (Movable movingObject : movingObjects) {
	    if (block != movingObject && Math.abs(movingObject.getX() - x) < 1 && Math.abs(movingObject.getY() - y) < 1) { // Ignore the warning since reference comparison is intended.
	        return movingObject;
	    }
	}

        return getBlockAt((int)x, (int)y);
    }

    public void removeBlockAt(final LevelChanger levelChanger, final int x, final int y) {
        if (levelChanger == levelKey) { // Only accept the instance that the level itself created, warning is ignored.
	    createBlockAt(x, y, new Block(BlockType.EMPTY));
	}
    }

    public void completeLevel(final LevelChanger levelChanger) {
        if (levelChanger == levelKey) { // Only accept the instance that the level itself created, warning is ignored.
	    levelCompleted = true;
	}
    }

    public boolean isLevelCompleted() {
        return levelCompleted;
    }

    private void createBlockAt(final int x, final int y, Block block) {
        if (block.getBlockType() == BlockType.ENEMY) {
            movingObjects.add((Enemy)block);
            tickables.add((Enemy)block);
	}
        else {
	    blocks[y][x] = block;
	}
    }

    private void playerDied() {
        restartLevel();
    }

    public void movePlayer(final Direction direction) {
        player.move(direction);
    }

    public void removeDirection(final Direction direction) { player.releaseDirection(direction); }
}