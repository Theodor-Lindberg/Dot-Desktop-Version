package game;

/**
 * Contains logic for basic ai movement, i.e turn right, left or back on collision.
 */
public class BasicAI implements Movement
{
    public enum TurnDirection {
        LEFT, RIGHT, BACK
    }

    private TurnDirection turnDirection;

    public BasicAI(final TurnDirection turnDirection) {
	this.turnDirection = turnDirection;
    }

    @Override public void move(final Movable movingObject) {
        movingObject.isMoving = true;
        movingObject.move(movingObject);
    }

    @Override public void handleCollision(final Movable movingObject) {
	movingObject.resetPositionAndTarget();

	if (turnDirection == TurnDirection.BACK) {
	    movingObject.direction = Direction.getOppositeDirection(movingObject.direction);
	}
	else {
	    movingObject.direction = Direction.turn(movingObject.direction, turnDirection == TurnDirection.LEFT);
	}

	movingObject.targetPosition.addX(movingObject.direction.deltaX);
	movingObject.targetPosition.addY(movingObject.direction.deltaY);

	if (movingObject.willCollide()) {
	    movingObject.isMoving = false;
	}
    }
}
