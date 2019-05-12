package game.objects;

import game.objects.movables.Direction;
import game.Game;
import game.Game.GameKey;
import game.Interactable;
import game.objects.movables.Movable;

import java.awt.Point;
import java.util.LinkedList;

/**
 * KeyBlock removes its target blocks when a player interacts with it.
 */
public class KeyBlock extends Block implements Interactable
{
    private final BlockType targetBlock;
    private final Game game;
    private final GameKey gameKey;

    /**
     * @param targetBlock 	The type of blocks to remove.
     * @param game 		The reference to the Game.
     * @param gameKey 		The reference to the GameKey of the game.
     */
    public KeyBlock(final BlockType targetBlock, final Game game, final GameKey gameKey) {
	super(BlockType.KEY);
	this.targetBlock = targetBlock;
	this.game = game;
	this.gameKey = gameKey;
    }

    public BlockType getTargetBlock() {
	return targetBlock;
    }

    @Override public void interact(final Movable movingObject) {
        if (movingObject.getBlockType() == BlockType.PLAYER) {
            game.removeBlockAt(gameKey, (int)movingObject.getTargetX(), (int)movingObject.getTargetY());
	    borrowedcode_removeTargetBlocks((int)movingObject.getX(), (int)movingObject.getY());
	}
    }

    /**
     * Remove all reachable target blocks.
     *
     * @param x Starting x-position.
     * @param y Starting y-position.
     */
    private void borrowedcode_removeTargetBlocks(final int x, final int y) {  // The warning is ignored because it's named according to instructions.
        final boolean[][] visited = new boolean[game.getHeight()][game.getWidth()];

	LinkedList<Point> blocksToVisit = new LinkedList<>();
	blocksToVisit.add(new Point(x, y));

        while (!blocksToVisit.isEmpty())
        {
            final Point p = blocksToVisit.remove();

            if(borrowedcode_removeBlock(visited, p.x, p.y))
            {
		blocksToVisit.add(new Point(p.x, p.y + Direction.UP.deltaY));
		blocksToVisit.add(new Point(p.x,p.y + Direction.DOWN.deltaY));
		blocksToVisit.add(new Point(p.x + Direction.LEFT.deltaX, p.y));
		blocksToVisit.add(new Point(p.x + Direction.RIGHT.deltaX, p.y));
            }
        }
    }

    /**
     * Try to remove a block from the level.
     *
     * @param visited 	All visited position.
     * @param x 	Current x-position.
     * @param y 	Current y-position.
     *
     * @return 		True if a block was removed.
     */
    private boolean borrowedcode_removeBlock(final boolean[][] visited, final int x, final int y) {  // The warning is ignored because it's named according to instructions.
        if (y < 0 || x < 0 || y > game.getHeight() - 1 || x > game.getWidth() - 1) {
            return false;
	}

        if (visited[y][x]) {
	    return false;
	}

        if (game.getBlockAt(x, y).getBlockType().isSolid && game.getBlockAt(x, y).getBlockType() != targetBlock) {
	    return false;
	}

	if (game.getBlockAt(x, y).getBlockType() == targetBlock) {
	    game.removeBlockAt(gameKey, x, y);
	}

	visited[y][x] = true;
        return true;
    }
}
