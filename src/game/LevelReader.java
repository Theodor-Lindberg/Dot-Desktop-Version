package game;

import game.Game.LevelChanger;
import game.Movable.Speed;

import java.util.ArrayList;
import java.util.List;

import static game.EnemyFactory.*;

/**
 * Read
 */
public class LevelReader
{
    private final String fileName;
    private Player player;
    private List<Movable> movingObjects;

    public LevelReader(final String fileName) {
	this.fileName = fileName;
	movingObjects = new ArrayList<>();
	player = null;
    }

    public Block[][] readLevel(Game game, final LevelChanger levelChanger) {
	player = new Player(new Point2D(10, 10), Speed.NORMAL, game);
	movingObjects.add(player);

	EnemyFactory ef = new EnemyFactory(game);

	Enemy enemy = ef.createEnemy(new Point2D(17, 17), Direction.RIGHT, Speed.NORMAL, EnemyAI.BASIC_TURN_LEFT);
	movingObjects.add(enemy);

	Enemy enemy2 = ef.createEnemy(new Point2D(25, 17), Direction.UP, Speed.NORMAL, EnemyAI.BASIC_TURN_BACK);
	movingObjects.add(enemy2);

	Block[][] blocks = new Block[30][30];
	for (int y = 0; y < blocks.length; y++) {
	    for (int x = 0; x < blocks[0].length; x++) {
		if (x == 0 || y == 0 || x == blocks[0].length - 1 || y == blocks.length - 1) {
		    insertBlockAt(x, y, new Block(BlockType.WALL), blocks);
		} else if (y == 20) {
		     insertBlockAt(x, y, new Block(BlockType.WALL1), blocks);
		} else if (y == 21) {
		     insertBlockAt(x, y, new Block(BlockType.WALL2), blocks);
		} else {
		     insertBlockAt(x, y, new Block(BlockType.EMPTY), blocks);
		}
	    }
	}
	insertBlockAt(15,15, new Block(BlockType.WALL), blocks);
	insertBlockAt(16,15, new Block(BlockType.WALL), blocks);
	insertBlockAt(17,15, new Block(BlockType.WALL), blocks);
	insertBlockAt(18,15, new Block(BlockType.WALL), blocks);
	insertBlockAt(19,15, new Block(BlockType.WALL), blocks);
	insertBlockAt(20,15, new Block(BlockType.WALL), blocks);
	insertBlockAt(21,15, new Block(BlockType.WALL), blocks);
	insertBlockAt(22,15, new Block(BlockType.WALL), blocks);
	insertBlockAt(23,15, new Block(BlockType.WALL), blocks);

	insertBlockAt(25, 25, new EndBlock(game, levelChanger), blocks);

	insertBlockAt(7, 5, new KeyBlock(BlockType.WALL1, game, levelChanger), blocks);
	insertBlockAt(12, 12, new KeyBlock(BlockType.WALL2, game, levelChanger), blocks);

	return blocks;
    }

    public Player getPlayer() {
	return player;
    }

    public List<Movable> getMovingObjects() {
	return movingObjects;
    }

    private void insertBlockAt(final int x, final int y, final Block block, Block[][] blocks) {
         blocks[y][x] = block;
    }
}
