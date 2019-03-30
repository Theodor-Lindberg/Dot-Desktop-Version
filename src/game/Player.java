package game;

public class Player extends Moveable
{
    private boolean isAlive;

    public Player(final BlockType blockType, final Point2D position, final float speed, final Level level) {
	super(blockType, position, speed, level);
	isAlive = true;
    }

    public boolean isAlive() {
	return isAlive;
    }

    public void move(Direction direction) {
        if (!isMoving) {
	    this.direction = direction;
	    isMoving = true;
	}
    }

    @Override protected void handleCollision() {
        isMoving = false;
    }

    @Override public void tick() {
        if (isMoving) {
	    int oldX = (int) position.getX();
	    int oldY = (int) position.getY();

	    move();

	    if (((direction == Direction.UP || direction == Direction.LEFT) &&(oldX-1 != (int) position.getX() || oldY-1 != (int) position.getY()) || ((direction == Direction.RIGHT || direction == Direction.DOWN) &&(oldX != (int) position.getX() || oldY != (int) position.getY())))){
	        isMoving = false;
	    	position.setX((int)position.getX());
		position.setY((int)position.getY());
	    }
	}
    }

    @Override public void interact(Moveable movingObject) {
        if (movingObject.getBlockType() == BlockType.ENEMY) {
            isAlive = false;
	}
    }
}