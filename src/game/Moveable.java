package game;

public abstract class Moveable extends Block implements Tickable, Interactable
{
    private Level level;
    private float speed;
    protected Point2D position;
    protected Direction direction;
    protected boolean isMoving;
    protected boolean reachedBlock;
    protected Point2D targetPosition;

    public Moveable(final BlockType blockType, final Point2D position, final float speed, final Level level) {
	super(blockType);
	this.position = position;
	this.speed = speed;
	this.level = level;
	isMoving = false;
	reachedBlock = true;
	targetPosition = new Point2D(position);
    }

    public float getX() {
        return position.getX();
    }

    public float getY() {
        return position.getY();
    }

    protected boolean isCollision() {
        if (direction == null){
	    return true;
        }

        final int x = (direction == Direction.RIGHT || direction == Direction.DOWN) ? (int)(position.getX() + direction.deltaX) : (int)(position.getX() + direction.deltaX * speed);
        final int y = (direction == Direction.RIGHT || direction == Direction.DOWN) ? (int)(position.getY() + direction.deltaY) : (int)(position.getY() + direction.deltaY * speed);
        return level.getBlockTypeAt(x, y).isSolid;
    }

    protected abstract void handleCollision();

    private boolean hasReachedBlock() {
        return (direction == Direction.RIGHT && position.getX() > targetPosition.getX() ||
        	    		direction == Direction.DOWN && position.getY() > targetPosition.getY() ||
        	    		direction == Direction.LEFT && (int)position.getX() < targetPosition.getX() ||
        	    	    	direction == Direction.UP && (int)position.getY() < targetPosition.getY());
    }

    protected void move() {
	if (isCollision()) handleCollision();

	if (isMoving) {
	    position.setX(position.getX() + direction.deltaX * speed);
	    position.setY(position.getY() + direction.deltaY * speed);

	    if (hasReachedBlock()) {
		reachedBlock = true;
		if (direction == Direction.RIGHT || direction == Direction.DOWN) {
		    position.setX((int) position.getX());
		    position.setY((int) position.getY());
		    targetPosition.setX(position.getX());
		    targetPosition.setY(position.getY());
		}
		else {
		    position.setX((int)targetPosition.getX());
		    position.setY((int)targetPosition.getY());
		}
	    }
	    else {
	        reachedBlock = false;
	    }

	    Block block = level.getBlockAt((int) position.getX(), (int) position.getY());
	    if (block.isInteractive()) ((Interactable) block).interact(this);
	}
    }
}
