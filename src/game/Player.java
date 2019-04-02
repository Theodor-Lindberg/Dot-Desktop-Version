package game;

public class Player extends Moveable
{
    private boolean isAlive;
    private MovePriority movePriority;

    public Player(final BlockType blockType, final Point2D position, final float speed, final Level level) {
	super(blockType, position, speed, level);
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
	    if (isCollision()) direction = movePriority.getSecondPriority();
	    if (direction == null) direction = old;
	    targetPosition.addX(direction.deltaX);
	    targetPosition.addY(direction.deltaY);
	}
    }

    @Override protected void handleCollision() {
        isMoving = false;
	reachedBlock = true;
	position.setX((int)position.getX());
	position.setY((int)position.getY());
        targetPosition.setX(position.getX());
        targetPosition.setY(position.getY());
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
	}
    }
}