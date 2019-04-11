package game;

import java.util.Stack;

/**
 * Handles the move priority for the player when multiple keys are pressed.
 */
public class MovePriority
{
    private Stack<Direction> directions;

    public MovePriority() {
	directions = new Stack<>();
    }

    public void addDirection(Direction direction) {
        if (!directions.contains(direction))
            directions.add(direction);
    }

    public void releaseDirection(Direction direction) {
        directions.remove(direction);
    }

    public Direction getFirstPriority() {
        if (directions.isEmpty())
            return null;
	return directions.peek();
    }

    public Direction getSecondPriority() {
	if (directions.isEmpty())
	    return null;
	return directions.firstElement();
    }
}
