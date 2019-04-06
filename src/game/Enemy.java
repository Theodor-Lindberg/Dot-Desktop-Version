package game;

public class Enemy extends Moveable
{
    private AIMovement ai;

    public Enemy(final Point2D position, final Direction startDirection, final Speed speed, final Level level, final AIMovement ai) {
	super(BlockType.ENEMY, position, speed, level);

	direction = startDirection;
	isMoving = true;
	this.ai = ai;
    }

    @Override protected void handleCollision() {
        ai.handleCollision(this);
    }

    @Override public void interact(final Moveable movingObject) {
    }

    @Override public void tick() {
	if (reachedBlock) {
	    targetPosition.addX(direction.deltaX);
	    targetPosition.addY(direction.deltaY);
	}
	ai.move(this);
    }
}
