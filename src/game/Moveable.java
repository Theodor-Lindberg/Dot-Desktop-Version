package game;

public abstract class Moveable implements CollisionHandler
{
    protected Point2D position;
    protected float speed;
    protected Direction direction;
    protected boolean isMoving;
    protected Level level;

    public Moveable(final Point2D position, final float speed, final Level level) {
	this.position = position;
	this.speed = speed;
	this.level = level;
    }

    public float getX() {
        return position.getX();
    }

    public float getY() {
        return position.getY();
    }

    private boolean isCollision(final Point2D pos, final float dx, final float dy) {
        final int newX = (int)(pos.getX() + dx);
        final int newY = (int)(pos.getY() + dy);
        return level.getBlockAt(newX, newY).isSolid;
    }

    public void move() {
        if (isMoving) {
            if (isCollision(position, direction.deltaX * speed, direction.deltaY * speed))
                handleCollision();
            if (isMoving) {
		position.setX(position.getX() + direction.deltaX * speed);
		position.setY(position.getY() + direction.deltaY * speed);
	    }
	}
    }
}
