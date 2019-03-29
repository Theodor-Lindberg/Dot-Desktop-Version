package game;

public enum Direction
{
    DOWN(0, 1), UP(0, -1), RIGHT(1, 0), LEFT(-1, 0);

    public final int deltaX;
    public final int deltaY;

    Direction(final int deltaX, final int deltaY) {
        this.deltaX = deltaX;
        this.deltaY = deltaY;
    }
}
