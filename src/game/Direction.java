package game;

/**
 * Enum listing all available directions for a movable object.
 */
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

    public static Direction turn(final Direction facingDirection, final boolean turnLeft) {
        Direction newDirection = null;

        switch (facingDirection) {
            case UP:
                newDirection = RIGHT;
                break;
            case RIGHT:
                newDirection = DOWN;
                break;
            case DOWN:
                newDirection = LEFT;
                break;
            case LEFT:
                newDirection = UP;
                break;
        }
        if (turnLeft) {
            return getOppositeDirection(newDirection);
        }
        return newDirection;
    }

    public static Direction toDirection(final int deltaX, final int deltaY) {
        if (deltaX == Direction.DOWN.deltaX && deltaY == Direction.DOWN.deltaY) {
            return Direction.DOWN;
        } else if (deltaX == Direction.UP.deltaX && deltaY == Direction.UP.deltaY) {
            return Direction.UP;
        } else if (deltaX == Direction.RIGHT.deltaX && deltaY == Direction.RIGHT.deltaY) {
            return Direction.RIGHT;
        } else if (deltaX == Direction.LEFT.deltaX && deltaY == Direction.LEFT.deltaY) {
            return Direction.LEFT;
        } else {
            return null;
        }
    }
}
