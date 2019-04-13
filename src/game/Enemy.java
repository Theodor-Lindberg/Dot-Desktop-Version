package game;

/**
 * Enemy class.
 */
public class Enemy extends Movable
{
    private AIMovement ai;

    public Enemy(final Point2D position, final Direction startDirection, final Speed speed, final Level level, final AIMovement ai) {
	super(BlockType.ENEMY, position, speed, startDirection, level);

	this.ai = ai;
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
