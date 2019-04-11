package game;

/**
 * Enemy class.
 */
public class Enemy extends Movable
{
    private Movement ai;

    public Enemy(final Point2D position, final Direction startDirection, final Speed speed, final Level level, final Movement ai) {
	super(BlockType.ENEMY, position, speed, startDirection, level);

	this.ai = ai;
    }

    @Override public void handleCollision(final Movable movingObject) {
        ai.handleCollision(movingObject);
    }

    @Override public void interact(final Movable movingObject) {
        if (movingObject.getBlockType() == BlockType.PLAYER) {
            movingObject.interact(this);
	}
    }

    @Override public void tick() {
	if (reachedBlock) {
	    targetPosition.addX(direction.deltaX);
	    targetPosition.addY(direction.deltaY);
	}
	ai.move(this);
    }
}
