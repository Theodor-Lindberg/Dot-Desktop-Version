package game;

import java.util.Timer;

public class Player extends Moveable
{
    private boolean isAlive;
    private MovePriority movePriority;
    Timer timer = new Timer();

    public Player(final Point2D position, final Speed speed, final Level level) {
	super(BlockType.PLAYER, position, speed, level);
	isAlive = true;
	movePriority = new MovePriority();
    }

    public boolean isAlive() {
	return isAlive;
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

    @Override protected void handleCollision() {
        isMoving = false;
	reachedBlock = true;
	resetPositionAndTarget();
    }


    @Override public void tick() {
        if (isMoving) {
            setDirection();
            move();
            if (reachedBlock && movePriority.getFirstPriority() == null)
                isMoving = false;
	}
    }

    @Override public void interact(Moveable movingObject) {
        if (movingObject.getBlockType() == BlockType.ENEMY) {
            isAlive = false;
	    level.restartLevel();
	}
    }
}