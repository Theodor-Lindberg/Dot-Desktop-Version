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

    public static Direction getOppositeDirection(final Direction direction) {
        return toDirection(direction.deltaX * -1, direction.deltaY * -1);
    }

    public static Direction toDirection(final int deltaX, final int deltaY) {
        if (deltaX == 0 && deltaY == 1) {
            return Direction.DOWN;
        } else if (deltaX == 0 && deltaY == -1) {
            return Direction.UP;
        } else if (deltaX == 1 && deltaY == 0) {
            return Direction.RIGHT;
        } else if (deltaX == -1 && deltaY == 0) {
            return Direction.LEFT;
        } else {
            return null;
        }
    }
}
