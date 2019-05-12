package game;

import game.objects.Block;

/**
 * All classes that acts as grid of Blocks implements this interface.
 */
public interface LevelGrid
{
    /**
     * Get the block at a coordinate.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     *
     * @return  The block on that coordinate.
     */
    Block getBlockAt(final int x, final int y);

    int getWidth();

    int getHeight();
}
