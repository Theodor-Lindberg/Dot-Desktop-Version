package game;

public interface LevelGrid
{
    Block getBlockAt(final int x, final int y);

    int getWidth();

    int getHeight();
}
