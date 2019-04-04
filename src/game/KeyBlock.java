package game;

import game.Level.LevelChanger;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class KeyBlock extends Block implements Interactable
{
    private final BlockType targetBlock;
    private Level level;
    private final LevelChanger levelChanger;

    public KeyBlock(final BlockType blockType, final BlockType targetBlock, final Level level, final LevelChanger levelChanger) {
	super(blockType);
	this.targetBlock = targetBlock;
	this.level = level;
	this.levelChanger = levelChanger;
    }

    @Override public void interact(Moveable movingObject) {
        if (movingObject.getBlockType() == BlockType.PLAYER) {
            level.removeBlockAt(levelChanger, (int)movingObject.getX(), (int)movingObject.getY());
	    floodFill(movingObject.position);
	}
    }

    private void floodFill(final Point2D start) {
        int x = (int)start.getX();
        int y = (int)start.getY();
        boolean[][] hits = new boolean[level.getHeight()][level.getWidth()];

	LinkedList<Point> queue = new LinkedList<Point>();
        queue.add(new Point(x, y));

        while (!queue.isEmpty())
        {
            Point p = queue.remove();

            if(floodFillImageDo(hits,p.x,p.y))
            {
                queue.add(new Point(p.x,p.y - 1));
                queue.add(new Point(p.x,p.y + 1));
                queue.add(new Point(p.x - 1,p.y));
                queue.add(new Point(p.x + 1,p.y));
            }
        }
    }

    private boolean floodFillImageDo(boolean[][] hits,int x, int y) {
        if (y < 0) return false;
        if (x < 0) return false;
        if (y > level.getHeight()-1) return false;
        if (x > level.getWidth()-1) return false;

        if (hits[y][x]) return false;

        if (level.getBlockTypeAt(x, y).isSolid && level.getBlockTypeAt(x, y) != targetBlock)
            return false;

        // valid, paint it

	if (level.getBlockTypeAt(x, y) == targetBlock) {
	    level.removeBlockAt(levelChanger, x, y);
	}
        hits[y][x] = true;
        return true;
    }
}
