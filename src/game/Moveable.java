package game;

public abstract class Moveable extends Block implements Tickable, Interactable
{
    protected Point2D position;
    private float speed;
    protected Direction direction;
    protected boolean isMoving;
    private Level level;

    public Moveable(final BlockType blockType, final Point2D position, final float speed, final Level level) {
	super(blockType);
	this.position = position;
	this.speed = speed;
	this.level = level;
	isMoving = false;
    }

    public float getX() {
        return position.getX();
    }

    public float getY() {
        return position.getY();
    }

    private boolean isCollision() {
        final int x = (int)position.getX() + direction.deltaX;
        final int y = (int)position.getY() + direction.deltaY;
        return level.getBlockTypeAt(x, y).isSolid;
    }

    protected abstract void handleCollision();

    protected void move() {
	if (isMoving) {
	    if (isCollision())
	        handleCollision();

	    if (isMoving) {
		position.setX(position.getX() + direction.deltaX * speed);
		position.setY(position.getY() + direction.deltaY * speed);

		Block block = level.getBlockAt((int)position.getX(), (int)position.getY());
		if (block.isInteractable())
		    ((Interactable)block).interact(this);
	    }
	}
    }
}
