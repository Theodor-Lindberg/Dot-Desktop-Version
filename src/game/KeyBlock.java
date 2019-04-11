package game;

import game.Level.LevelChanger;

import java.awt.Point;
import java.util.LinkedList;

/**
 * KeyBlock removes its target blocks when a player interacts with it.
 */
public class KeyBlock extends Block implements Interactable
{
    private final BlockType targetBlock;
    private Level level;
    private final LevelChanger levelChanger;

    public KeyBlock(final BlockType targetBlock, final Level level, final LevelChanger levelChanger) {
	super(BlockType.KEY);
	this.targetBlock = targetBlock;
	this.level = level;
	this.levelChanger = levelChanger;
    }

    public BlockType getTargetBlock() {
	return targetBlock;
    }

    @Override public void interact(Movable movingObject) {
        if (movingObject.getBlockType() == BlockType.PLAYER) {
            level.removeBlockAt(levelChanger, (int)movingObject.targetPosition.getX(), (int)movingObject.targetPosition.getY());
	    floodFill(movingObject.position);
	}
    }

    private void floodFill(final Point2D start) {
        int x = (int)start.getX();
        int y = (int)start.getY();
        boolean[][] visited = new boolean[level.getHeight()][level.getWidth()];

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
        if (y > level.getHeight()-1) return false;
        if (x > level.getWidth()-1) return false;

        if (visited[y][x]) return false;

        if (level.getBlockAt(x, y).getBlockType().isSolid && level.getBlockAt(x, y).getBlockType() != targetBlock)
            return false;

	if (level.getBlockAt(x, y).getBlockType() == targetBlock) {
	    level.removeBlockAt(levelChanger, x, y);
	}

	visited[y][x] = true;
        return true;
    }
}
