package game;

/**
 * All classes that acts as grid of Blocks implements this interface.
 */
public interface LevelGrid
{
    Block getBlockAt(final int x, final int y);

    int getWidth();

    int getHeight();
}
