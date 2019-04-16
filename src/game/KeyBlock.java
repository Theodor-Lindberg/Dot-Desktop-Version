package game;

import game.Game.LevelChanger;

import java.awt.Point;
import java.util.LinkedList;

/**
 * KeyBlock removes its target blocks when a player interacts with it.
 */
public class KeyBlock extends Block implements Interactable
{
    private final BlockType targetBlock;
    private Game game;
    private final LevelChanger levelChanger;

    public KeyBlock(final BlockType targetBlock, final Game game, final LevelChanger levelChanger) {
	super(BlockType.KEY);
	this.targetBlock = targetBlock;
	this.game = game;
	this.levelChanger = levelChanger;
    }

    public BlockType getTargetBlock() {
	return targetBlock;
    }

    @Override public void interact(Movable movingObject) {
        if (movingObject.getBlockType() == BlockType.PLAYER) {
            game.removeBlockAt(levelChanger, (int)movingObject.getTargetX(), (int)movingObject.getTargetY());
	    floodFill((int)movingObject.getX(), (int)movingObject.getY());
	}
    }

    // The algorithm was remade from Stackoverflow answer nr 2 https://stackoverflow.com/questions/2783204/flood-fill-using-a-stack/2783341#2783341
    private void floodFill(final int x, final int y) {
        boolean[][] visited = new boolean[game.getHeight()][game.getWidth()];

	LinkedList<Point> blocksToVisit = new LinkedList<>();
	blocksToVisit.add(new Point(x, y));

        while (!blocksToVisit.isEmpty())
        {
            Point p = blocksToVisit.remove();

            if(fillBlock(visited, p.x,p.y))
            {
		blocksToVisit.add(new Point(p.x,p.y + Direction.UP.deltaY));
		blocksToVisit.add(new Point(p.x,p.y + Direction.DOWN.deltaY));
		blocksToVisit.add(new Point(p.x + Direction.LEFT.deltaX,p.y));
		blocksToVisit.add(new Point(p.x + Direction.RIGHT.deltaX,p.y));
            }
        }
    }

    private boolean fillBlock(boolean[][] visited,int x, int y) {
        if (y < 0) return false;
        if (x < 0) return false;
        if (y > game.getHeight() - 1) return false;
        if (x > game.getWidth() - 1) return false;

        if (visited[y][x]) return false;

        if (game.getBlockAt(x, y).getBlockType().isSolid && game.getBlockAt(x, y).getBlockType() != targetBlock)
            return false;

	if (game.getBlockAt(x, y).getBlockType() == targetBlock) {
	    game.removeBlockAt(levelChanger, x, y);
	}

	visited[y][x] = true;
        return true;
    }
}
