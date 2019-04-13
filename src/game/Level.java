package game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 */
public class Level implements Tickable
{
    private Block[][] blocks;
    private Player player;
    private final LevelChanger levelKey;
    private List<LevelListener> levelListeners;
    private List<Tickable> tickables;
    private List<Movable> movingObjects;
    private boolean paused;
    private boolean levelCompleted;

    // Works a proxy to grant access to certain methods, the warning is ignored.
    public class LevelChanger {
    }

    public Level() {
	levelKey = new LevelChanger();
	levelListeners = new ArrayList<>();

	initializeLevel();
    }

    private void initializeLevel() {

        levelCompleted = false;

	movingObjects = new ArrayList<>();

        final LevelReader levelReader = new LevelReader(null);
        blocks = levelReader.readLevel(this, levelKey);
        movingObjects = levelReader.getMovingObjects();
        player = levelReader.getPlayer();

	tickables = new ArrayList<>(); // tickables and movingObjects currently contains the same elements but tickables is used for better scalability
	tickables.addAll(movingObjects);
    }

    public void subscribeListener(LevelListener levelListener) {
        levelListeners.add(levelListener);
    }

    @Override public void tick() {
        if (!paused) {
	    for (Tickable tickable : tickables) {
		tickable.tick();
	    }

	    if (!player.isAlive() && !levelCompleted) {
		playerDied();
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
	return blocks[0].length;
    }

    public int getHeight() {
	return blocks.length;
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

    public Block getBlockAt(final int x, final int y) {
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

    public void completeLevel(LevelChanger levelChanger) {
        if (levelChanger == levelKey) { // Only accept the instance that the level itself created, warning is ignored.
	    levelCompleted = true;
	}
    }

    public boolean isLevelCompleted() {
        return levelCompleted;
    }

    private void createBlockAt(final int x, final int y, Block block) {
        blocks[y][x] = block;
    }

    private void playerDied() {
        restartLevel();
    }

    public void movePlayer(final Direction direction) {
        player.move(direction);
    }

    public void removeDirection(final Direction direction) { player.releaseDirection(direction); }
}