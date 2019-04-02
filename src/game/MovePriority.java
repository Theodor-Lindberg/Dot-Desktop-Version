package game;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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
	return directions.lastElement();
    }
}
