package game;

public class Player extends Moveable
{
    private boolean isAlive;

    public Player(final Point2D position, final float speed, final Level level) {
	super(position, speed, level);
	isAlive = true;
    }

    public boolean isAlive() {
	return isAlive;
    }

    public void move(Direction direction) {
        if (!isMoving)
            direction = direction;
    }

    @Override public void handleCollision() {
        isMoving = false;
    }
}