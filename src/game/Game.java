package game;

import game.objects.Block;
import game.objects.BlockType;
import game.objects.EndBlock;
import game.objects.KeyBlock;
import game.objects.movables.Direction;
import game.objects.movables.Enemy;
import game.objects.movables.EnemyFactory;
import game.objects.movables.Movable;
import game.objects.movables.Player;
import util.Observable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Game class
 */
public class Game extends Observable implements Tickable, LevelGrid
{
    private Block[][] blocks = null;
    private final Level level;
    private Player player = null;
    private final GameKey gameKey;
    private List<Tickable> tickables = null;
    private List<Movable> movingObjects = null;
    private static final List<BlockType> UNIQUE_BLOCK_TYPES;
    private boolean paused;
    private boolean levelCompleted;
    private boolean levelRestarted;

    static {
	UNIQUE_BLOCK_TYPES = new ArrayList<>(Collections.singletonList(BlockType.PLAYER));
    }

    // Works a proxy to grant access to certain methods, the warning is ignored.
    public class GameKey
    {
    }

    /**
     * @param level The level to play.
     */
    public Game(final Level level) {
        super();
        this.level = level;
	gameKey = new GameKey();
	restart();
    }

    /**
     * Restarts the game and resets the level.
     */
    public void restart() {
	levelRestarted = true;
	levelCompleted = false;
	blocks = new Block[level.getHeight()][level.getHeight()];
	movingObjects = new ArrayList<>();
	final EnemyFactory enemyFactory = new EnemyFactory(this);

	for (int y = 0; y < level.getHeight(); y++) {
	    for (int x = 0; x < level.getWidth(); x++) {
		final Block block = level.getBlockAt(x, y);
		if (block.getBlockType() == BlockType.PLAYER) {
		    player = new Player((Player)block, this);
		    movingObjects.add(player);
		    insertBlockAt(x, y, new Block(BlockType.EMPTY));
		} else if (block.getBlockType() == BlockType.ENEMY) {
		    movingObjects.add(enemyFactory.copyEnemy((Enemy)block));
		    insertBlockAt(x, y, new Block(BlockType.EMPTY));
		} else if (block.getBlockType() == BlockType.KEY) {
		    insertBlockAt(x, y, new KeyBlock(((KeyBlock) block).getTargetBlock(), this, gameKey));
		} else if (block.getBlockType() == BlockType.END) {
		    insertBlockAt(x, y, new EndBlock(this, gameKey));
		} else {
		    insertBlockAt(x, y, new Block(block.getBlockType()));
		}
	    }
	}

	tickables = new ArrayList<>();
	tickables.addAll(movingObjects); // tickables and movables currently contains the same elements but tickables is used for better scalability
    }

    @Override public void tick() {
	if (!paused) {
	    levelRestarted = false;
	    for (Tickable tickable : tickables) {
		if (!levelRestarted) {
		    tickable.tick();
		}
	    }
	}

	notifyObservers();
    }

    @Override public int getWidth() {
	return level.getWidth();
    }

    @Override public int getHeight() {
	return level.getHeight();
    }

    @Override public Block getBlockAt(final int x, final int y) {
        if (x >= 0 && x < getWidth() && y >= 0 && y < getHeight()) {
	    return blocks[y][x];
	}
	return new Block(BlockType.WALL);
    }

    public boolean isPaused() {
	return paused;
    }

    public void setPaused(final boolean paused) {
	this.paused = paused;
    }

    /**
     * Get the iterator for all moving objects in the game.
     *
     * @return	Iterator moving objects.
     */
    public Iterator<Movable> getMovingObjectsIterator() {
        return movingObjects.iterator();
    }

    /**
     * Check which entity the moving object collides with.
     *
     * @param block 	The moving object.
     * @param 		x The x coordinate of the collision.
     * @param		y TThe y coordinate of the collision.
     *
     * @return 		The block that it collides with.
     */
    public Block getCollidingEntity(final Movable block, final float x, final float y) {
	for (Movable movingObject : movingObjects) {
	    if (block != movingObject && Math.abs(movingObject.getX() - x) < 1 && Math.abs(movingObject.getY() - y) < 1) { // Ignore the warning since reference comparison is intended.
	        return movingObject;
	    }
	}

        return getBlockAt((int)x, (int)y);
    }

    /**
     * Replace a block at a coordinate with an empty block.
     *
     * @param gameKey	The reference to the GameKey of the game.
     * @param x 	The x coordinate.
     * @param y 	The y coordinate.
     */
    public void removeBlockAt(final GameKey gameKey, final int x, final int y) {
        if (gameKey == this.gameKey) { // Only accept the instance that the level itself created, warning is ignored.
	    insertBlockAt(x, y, new Block(BlockType.EMPTY));
	}
    }

    /**
     * Set the level to complete.
     *
     * @param gameKey	he reference to the GameKey of the game.
     */
    public void setLevelCompleted(final GameKey gameKey) {
        if (gameKey == this.gameKey) { // Only accept the instance that the level itself created, warning is ignored.
	    levelCompleted = true;
	}
    }

    public boolean isLevelCompleted() {
        return levelCompleted;
    }

    /**
     * Insert a block on a coordinate.
     *
     * @param x		Coordinate on the x-axis
     * @param y		Coordinate on the y-axis
     * @param block	The block to insert
     */
    private void insertBlockAt(final int x, final int y, final Block block) {
	blocks[y][x] = block;
    }

    /**
     * Prepare the Player to move on the next tick.
     *
     * @param direction	The direction to move.
     */
    public void movePlayer(final Direction direction) {
        if (!paused) {
	    player.move(direction);
	}
    }


    /**
     * Remove a direction from being evaluated when moving the player.
     *
     * @param direction	The direction to release.
     */
    public void removeDirection(final Direction direction) { player.releaseDirection(direction); }

    /**
     * Check if a block type is unique in the game.
     *
     * @param blockType	The block type to check.
     *
     * @return		True if the block type is unique.
     */
    public static boolean isBlockTypeUnique(final BlockType blockType) {
        return UNIQUE_BLOCK_TYPES.contains(blockType);
    }
}