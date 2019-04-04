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
	player = new Player(BlockType.PLAYER, new Point2D(10, 10), 0.2f, this);
	blocks = new Block[height][width];
	levelKey = new LevelChanger();
	levelListeners = new ArrayList<>();
	tickables = new ArrayList<>();
	tickables.add(player);
	movingObjects = new ArrayList<>();
	movingObjects.add(player);
	for (int y = 0; y < height; y++) {
	    for (int x = 0; x < width; x++) {
	        if (x == 0 || y == 0 || x == width - 1 || y == height - 1) {
	            blocks[y][x] = new Block(BlockType.WALL);
		}
	        else if (y == 20) {
		    blocks[y][x] = new Block(BlockType.WALL1);
		}
	        else if (y == 21) {
		    blocks[y][x] = new Block(BlockType.WALL2);
		}
	        else {
		    blocks[y][x] = new Block(BlockType.EMPTY);
		}
	    }
	}
	blocks[15][15] = new Block(BlockType.WALL);
	blocks[15][16] = new Block(BlockType.WALL);
	blocks[15][17] = new Block(BlockType.WALL);
	blocks[15][18] = new Block(BlockType.WALL);
	blocks[15][19] = new Block(BlockType.WALL);
	blocks[15][20] = new Block(BlockType.WALL);
	blocks[15][21] = new Block(BlockType.WALL);
	blocks[15][22] = new Block(BlockType.WALL);

	blocks[5][5] = new KeyBlock(BlockType.KEY, BlockType.WALL1, this, levelKey);
	blocks[12][12] = new KeyBlock(BlockType.KEY, BlockType.WALL2, this, levelKey);

    }

    public void subscribeListener(LevelListener levelListener) {
        levelListeners.add(levelListener);
    }

    @Override public void tick() {
	for (Tickable tickable : tickables) {
	    tickable.tick();
	}
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

    public Block getBlockAt(int x, int y) {
	return blocks[y][x];
    }

    public void removeBlockAt(LevelChanger levelChanger, int x, int y) {
        blocks[y][x] = new Block(BlockType.EMPTY);
    }

    public void movePlayer(final Direction direction) {
        player.move(direction);
    }

    public void removeDirection(final Direction direction) { player.releaseDirection(direction); }
}