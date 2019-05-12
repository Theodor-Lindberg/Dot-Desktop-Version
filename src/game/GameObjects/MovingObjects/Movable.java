package game.GameObjects.MovingObjects;

import game.Game;
import game.GameObjects.Block;
import game.GameObjects.BlockType;
import game.Interactable;
import game.Tickable;
import util.Point2D;

/**
 * Base class for all moving objects.
 */
public abstract class Movable extends Block implements Tickable, Interactable
{
    public enum Speed {
        SLOW(0.09f), NORMAL(0.15f), FAST(0.2f);

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

    public void addXTargetPosition(final float dx) {
	targetPosition.addX(dx);
    }

    public void addYTargetPosition(final float dy) {
	targetPosition.addY(dy);
    }

    public boolean willCollide() {
        if (direction == null){
	    return true;
        }

        final int x = (direction == Direction.RIGHT || direction == Direction.DOWN) ? (int)(position.getX() + direction.deltaX) : (int)Math.floor((position.getX() + direction.deltaX * speed.value));
        final int y = (direction == Direction.RIGHT || direction == Direction.DOWN) ? (int)(position.getY() + direction.deltaY) : (int)Math.floor((position.getY() + direction.deltaY * speed.value));
        return game.getCollidingEntity(this, x, y).isSolid();
    }

    public void resetPositionAndTarget() {
	position.setX((int)position.getX());
	position.setY((int)position.getY());
        targetPosition.setX(position.getX());
        targetPosition.setY(position.getY());
    }

    private boolean hasReachedBlock() {
	if (direction == Direction.RIGHT) if (position.getX() > targetPosition.getX()) return true;
	if (direction == Direction.DOWN) if (position.getY() > targetPosition.getY()) return true;
	if (direction == Direction.LEFT) if (Math.floor(position.getX()) < targetPosition.getX()) return true;
	if (direction == Direction.UP) return Math.floor(position.getY()) < targetPosition.getY();
	return false;
    }

    public void move() {
	if (willCollide()) {
	    handleCollision();
	}

	if (moving) {
	    position.setX(position.getX() + direction.deltaX * speed.value);
	    position.setY(position.getY() + direction.deltaY * speed.value);

	    if (hasReachedBlock()) {
		blockReached = true;

		if (direction == Direction.RIGHT || direction == Direction.DOWN) {
		    position.setX((int) position.getX());
		    position.setY((int) position.getY());
		} else {
		    position.setX((int) targetPosition.getX());
		    position.setY((int) targetPosition.getY());
		}

	    } else {
		blockReached = false;
	    }

		final Block block = game.getCollidingEntity(this, position.getX(), position.getY());
		if (block instanceof Interactable) {
		    ((Interactable) block).interact(this);
		}

	}
    }

    protected abstract void handleCollision();
}