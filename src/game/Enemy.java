package game;

/**
 * Generic Enemy class, can have different types of ai.
 */
public class Enemy extends Movable
{
    private AIMovement ai;

    public Enemy(final Point2D position, final Direction startDirection, final Speed speed, final Game game, final AIMovement ai)
    {
	super(BlockType.ENEMY, position, speed, startDirection, game);

	this.ai = ai;
    }

    public Enemy(final Enemy clone, final Game game) {
	super(clone.getBlockType(), new Point2D(clone.getX(), clone.getY()), clone.getSpeed(),clone.getDirection(), game);

	ai = clone.ai.copy();
    }

    @Override protected void handleCollision() {
	ai.handleCollision(this);
    }

    @Override public void interact(final Movable movingObject) {
	if (movingObject.getBlockType() == BlockType.PLAYER) {
	    movingObject.interact(this);
	}
    }

    @Override public void tick() {
	if (isReachedBlock()) {
	    addXTargetPosition(getDirection().deltaX);
	    addYTargetPosition(getDirection().deltaY);
	}
	ai.move(this);
    }
}
