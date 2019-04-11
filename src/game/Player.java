package game;

/**
 * The player class.
 */
public class Player extends Movable
{
    private MovePriority movePriority;

    public Player(final Point2D position, final Speed speed, final Level level) {
	super(BlockType.PLAYER, position, speed, null, level);
	movePriority = new MovePriority();
    }

    public void move(Direction direction) {
        movePriority.addDirection(direction);
        isMoving = true;
    }

    public void releaseDirection(Direction direction) {
        movePriority.releaseDirection(direction);
    }

    private void setDirection() {
        if (reachedBlock) {
	    final Direction old = direction;
	    direction = movePriority.getFirstPriority();
	    if (willCollide()) {
		direction = movePriority.getSecondPriority();
	    }
	    if (direction == null) direction = old;
	    targetPosition.addX(direction.deltaX);
	    targetPosition.addY(direction.deltaY);
	}
    }

    @Override public void handleCollision(final Movable movingObject) {
        isMoving = false;
	reachedBlock = true;
	resetPositionAndTarget();
    }


    @Override public void tick() {
        if (isMoving) {
            setDirection();
            move(this);
            if (reachedBlock && movePriority.getFirstPriority() == null)
                isMoving = false;
	}
    }

    @Override public void interact(Movable movingObject) {
        if (movingObject.getBlockType() == BlockType.ENEMY && !level.isLevelCompleted()) {
	    level.restartLevel();
	}
    }
}