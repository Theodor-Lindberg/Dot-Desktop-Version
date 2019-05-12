package game.objects.movables;

import game.Game;
import game.objects.Block;
import game.objects.BlockType;
import game.Interactable;
import game.Tickable;
import util.Point2D;

/**
 * Base class for all moving objects.
 */
public abstract class Movable extends Block implements Tickable, Interactable
{
    public enum Speed {
	// The names are self explanatory so Javadoc is not needed, warnings ignored.
        SLOW(0.09f), NORMAL(0.15f), FAST(0.2f);

	/**
	 * The speed.
	 */
        public final float value;

        Speed(final float speed) {
	    value = speed;
	}
    }

    private final Speed speed;
    private Point2D position;
    private Direction direction;
    private boolean moving;
    private boolean blockReached;
    private Point2D targetPosition;
    private final Game game;

    /**
     * @param blockType 	The type of Block to create.
     * @param position 		The start position of the object.
     * @param speed 		The movement speed.
     * @param startDirection 	The direction to face when starting.
     * @param game 		The reference to the Game object.
     */
    protected Movable(final BlockType blockType, final Point2D position, final Speed speed, final Direction startDirection, final Game game) {
	super(blockType);
	this.position = position;
	this.speed = speed;
	this.game = game;
	moving = false;
	blockReached = true;
	targetPosition = new Point2D(position);
	direction = startDirection;
    }

    public float getX() {
	return position.getX();
    }

    public float getY() {
	return position.getY();
    }

    public Direction getDirection() {
	return direction;
    }

    public Game getGame() {
        return game;
    }

    public void setDirection(final Direction direction) {
	this.direction = direction;
    }

    public boolean isMoving() {
	return moving;
    }

    public void setMoving(final boolean moving) {
	this.moving = moving;
    }

    public boolean isBlockReached() {
	return blockReached;
    }

    public Speed getSpeed() {
        return speed;
    }

    public float getTargetX() {
	return targetPosition.getX();
    }

    public float getTargetY() {
	return targetPosition.getY();
    }

    /**
     * Update the target position to a new position.
     *
     * @param direction The facing direction.
     */
    public void updateTargetPosition(final Direction direction) {
	targetPosition.addX(direction.deltaX);
	targetPosition.addY(direction.deltaY);
    }

    /**
     * Check if the next movement results in a collision.
     *
     * @return True if there will be a collision.
     */
    public boolean willCollide() {
        if (direction == null){
	    return true;
        }

        final int x = (direction == Direction.RIGHT || direction == Direction.DOWN) ? (int)(position.getX() + direction.deltaX) : (int)Math.floor((position.getX() + direction.deltaX * speed.value));
        final int y = (direction == Direction.RIGHT || direction == Direction.DOWN) ? (int)(position.getY() + direction.deltaY) : (int)Math.floor((position.getY() + direction.deltaY * speed.value));
        return game.getCollidingEntity(this, x, y).isSolid();
    }

    /**
     * Reset the position to a integer value and reset the target position.
     */
    public void resetPositionAndTarget() {
	position.setX((int)position.getX());
	position.setY((int)position.getY());
        targetPosition.setX(position.getX());
        targetPosition.setY(position.getY());
    }

    /**
     * Calculate if the target block has been reached.
     *
     * @return True if the block is reached.
     */
    private boolean hasReachedBlock() {
	if (direction == Direction.RIGHT) if (position.getX() > targetPosition.getX()) return true;
	if (direction == Direction.DOWN) if (position.getY() > targetPosition.getY()) return true;
	if (direction == Direction.LEFT) if (Math.floor(position.getX()) < targetPosition.getX()) return true;
	if (direction == Direction.UP) return Math.floor(position.getY()) < targetPosition.getY();
	return false;
    }

    /**
     * Try to move one step forward.
     */
    public void move() {
	if (willCollide()) {
	    handleCollision();
	}

	if (moving) {
	    position.setX(position.getX() + direction.deltaX * speed.value);
	    position.setY(position.getY() + direction.deltaY * speed.value);

	    blockReached = hasReachedBlock();
	    if (blockReached) {

		if (direction == Direction.RIGHT || direction == Direction.DOWN) {
		    position.setX((int) position.getX());
		    position.setY((int) position.getY());
		} else {
		    position.setX((int) targetPosition.getX());
		    position.setY((int) targetPosition.getY());
		}

	    }

		final Block block = game.getCollidingEntity(this, position.getX(), position.getY());
		if (block instanceof Interactable) {
		    ((Interactable) block).interact(this);
		}
	}
    }

    /**
     * Handle an upcoming collision.
     */
    protected abstract void handleCollision();
}