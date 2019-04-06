package game;

public class BasicAI implements AIMovement
{
    public enum TurnDirection {
        LEFT, RIGHT, BACK;
    }

    private TurnDirection turnDirection;

    public BasicAI(final TurnDirection turnDirection) {
	this.turnDirection = turnDirection;
    }

    @Override public void move(final Moveable movingObject) {
        movingObject.move();
    }

    @Override public void handleCollision(final Moveable movingObject) {
	movingObject.resetPositionAndTarget();

	if (turnDirection == TurnDirection.BACK) {
	    movingObject.direction = Direction.getOppositeDirection(movingObject.direction);
	}

	movingObject.targetPosition.addX(movingObject.direction.deltaX);
	movingObject.targetPosition.addY(movingObject.direction.deltaY);
    }
}
