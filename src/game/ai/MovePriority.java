package game.ai;

import game.objects.movables.Direction;

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

    /**
     * Add another direction in consideration when choosing prioritized direction.
     *
     * @param direction The direction to add.
     */
    public void addDirection(final Direction direction) {
        if (!directions.contains(direction))
            directions.add(direction);
    }

    /**
     * Remove a direction from being evaluated when choosing prioritized direction.
     *
     * @param direction The direction to remove.
     */
    public void releaseDirection(final Direction direction) {
        directions.remove(direction);
    }

    /**
     * Get the direction with the highest priority.
     *
     * @return The direction with the highest priority.
     */
    public Direction getFirstPriority() {
        if (directions.isEmpty())
            return null;
	return directions.peek();
    }

    /**
     * Get the direction with the second most highest priority.
     *
     * @return The direction with the second most highest priority.
     */
    public Direction getSecondPriority() {
	if (directions.isEmpty())
	    return null;
	return directions.firstElement();
    }
}
