package game.AIMovement;

import game.GameObjects.MovingObjects.Direction;
import game.GameObjects.MovingObjects.Movable;

/**
 * Contains logic for basic ai movement, i.e turn right, left or back on collision.
 */
public class BasicAI implements AIMovement
{
    public enum TurnDirection {
        LEFT, RIGHT, BACK
    }

    private TurnDirection turnDirection;

    /**
     * Constructs a BasicAI object.
     *
     * @param turnDirection The direction to turn when colliding.
     */
    public BasicAI(final TurnDirection turnDirection) {
	this.turnDirection = turnDirection;
    }

    @Override public void move(final Movable movingObject) {
        movingObject.setMoving(true);
        movingObject.move();
    }

    @Override public void handleCollision(final Movable movingObject) {
	movingObject.resetPositionAndTarget();

	if (turnDirection == TurnDirection.BACK) {
	    movingObject.setDirection(Direction.getOppositeDirection(movingObject.getDirection()));
	}
	else {
	    movingObject.setDirection(Direction.turn(movingObject.getDirection(), turnDirection == TurnDirection.LEFT));
	}

	movingObject.updateTargetPosition(movingObject.getDirection());

	if (movingObject.willCollide()) {
	    movingObject.setMoving(false);
	}
    }

    @Override public AIMovement copy() {
	return new BasicAI(turnDirection);
    }
}
