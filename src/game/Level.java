package game;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Level extends Publisher implements LevelGrid
{
    private Block[][] blocks;

    public Level(final String fileName) {
	try {
	    blocks = FileHandler.readLevel(fileName);
	} catch (IOException e) {
	}
    }

    public Level(final int width, final int height) {
	blocks = new Block[height][width];

	for (int y = 0; y < blocks.length; y++) {
	    for (int x = 0; x < blocks[0].length; x++) {
		if (x == 0 || y == 0 || x == blocks[0].length - 1 || y == blocks.length - 1) {
		    insertBlockAt(x, y, new Block(BlockType.WALL));
		} else if (y == 20) {
		    insertBlockAt(x, y, new Block(BlockType.WALL1));
		} else if (y == 21) {
		    insertBlockAt(x, y, new Block(BlockType.WALL2));
		} else {
		    insertBlockAt(x, y, new Block(BlockType.EMPTY));
		}
	    }
	}

	insertBlockAt(15, 15, new Block(BlockType.WALL));
	insertBlockAt(16, 15, new Block(BlockType.WALL));
	insertBlockAt(17, 15, new Block(BlockType.WALL));
	insertBlockAt(18, 15, new Block(BlockType.WALL));
	insertBlockAt(19, 15, new Block(BlockType.WALL));
	insertBlockAt(20, 15, new Block(BlockType.WALL));
	insertBlockAt(21, 15, new Block(BlockType.WALL));
	insertBlockAt(22, 15, new Block(BlockType.WALL));
	insertBlockAt(23, 15, new Block(BlockType.WALL));

	insertBlockAt(25, 25, new EndBlock(null, null));

	insertBlockAt(7, 5, new KeyBlock(BlockType.WALL1, null, null));
	insertBlockAt(12, 12, new KeyBlock(BlockType.WALL2, null, null));

	insertBlockAt(10, 10, new Player(new Point2D(10, 10), Movable.Speed.NORMAL, null));
	EnemyFactory ef = new EnemyFactory(null);

	insertBlockAt(17, 17, ef.createEnemy(new Point2D(17, 17), Direction.RIGHT, Movable.Speed.NORMAL, EnemyFactory.EnemyAI.BASIC_TURN_LEFT));
	insertBlockAt(25, 17, ef.createEnemy(new Point2D(25, 17), Direction.UP, Movable.Speed.NORMAL, EnemyFactory.EnemyAI.BASIC_TURN_BACK));
    }

    @Override public int getWidth() {
	return blocks[0].length;
    }

    @Override public int getHeight() {
	return blocks.length;
    }

    public void insertBlockAt(final int x, final int y, final Block block) {
	blocks[y][x] = block;
	notifyListeners();
    }

    @Override public Block getBlockAt(final int x, final int y) {
	return blocks[y][x];
    }

    public void saveToFile(String fileName) {
	try {
	    FileHandler.saveLevel(fileName, blocks);
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	}
    }
}
