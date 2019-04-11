package game;

/**
 * Base class for all moving objects.
 */
public abstract class Movable extends Block implements Tickable, Interactable, Movement
{
    public enum Speed {
        SLOW(0.1f), NORMAL(0.15f), FAST(0.2f);

        public final float value;

        Speed(final float speed) {
	    value = speed;
	}
    }

    private Speed speed;
    protected Point2D position;
    protected Direction direction;
    protected boolean isMoving;
    protected boolean reachedBlock;
    protected Point2D targetPosition;
    protected Level level;

    protected Movable(final BlockType blockType, final Point2D position, final Speed speed, final Direction startDirection, final Level level) {
	super(blockType);
	this.position = position;
	this.speed = speed;
	this.level = level;
	isMoving = false;
	reachedBlock = true;
	targetPosition = new Point2D(position);
	direction = startDirection;
    }

    public float getX() {
        return position.getX();
    }

    public float getY() {
        return position.getY();
    }

    protected boolean willCollide() {
        if (direction == null){
	    return true;
        }

        final int x = (direction == Direction.RIGHT || direction == Direction.DOWN) ? (int)(position.getX() + direction.deltaX) : (int)(position.getX() + direction.deltaX * speed.value);
        final int y = (direction == Direction.RIGHT || direction == Direction.DOWN) ? (int)(position.getY() + direction.deltaY) : (int)(position.getY() + direction.deltaY * speed.value);
        return level.getBlockAt(x, y).isSolid();
    }

    protected void resetPositionAndTarget() {
	position.setX((int)position.getX());
	position.setY((int)position.getY());
        targetPosition.setX(position.getX());
        targetPosition.setY(position.getY());
    }

    protected boolean hasReachedBlock() {
	if (direction == Direction.RIGHT) if (position.getX() > targetPosition.getX()) return true;
	if (direction == Direction.DOWN) if (position.getY() > targetPosition.getY()) return true;
	if (direction == Direction.LEFT) if ((int) position.getX() < targetPosition.getX()) return true;
	if (direction == Direction.UP) return (int) position.getY() < targetPosition.getY();
	return false;
    }

    @Override public void move(final Movable movingObject) {
	if (willCollide()) handleCollision(this);

	level.completeLevel(null);

	if (isMoving) {
	    position.setX(position.getX() + direction.deltaX * speed.value);
	    position.setY(position.getY() + direction.deltaY * speed.value);

	    if (hasReachedBlock()) {
		reachedBlock = true;

		if (direction == Direction.RIGHT || direction == Direction.DOWN) {
		    position.setX((int) position.getX());
		    position.setY((int) position.getY());
		} else {
		    position.setX((int) targetPosition.getX());
		    position.setY((int) targetPosition.getY());

		}

	    } else {
		reachedBlock = false;
	    }

	    Block block = level.getCollidingEntity(this, targetPosition.getX(), targetPosition.getY());
	    if (block.isInteractive()) {
		((Interactable)block).interact(this);
	    }
	}
    }
}
