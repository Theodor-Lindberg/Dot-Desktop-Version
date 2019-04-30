package game;

import game.Game.GameKey;

/**
 * The player class.
 */
public class Player extends Movable
{
    private final MovePriority movePriority;
    private final GameKey gameKey;

    public Player(final Point2D position, final Speed speed, final Game game, final GameKey gameKey) {
	super(BlockType.PLAYER, position, speed, null, game);
	this.gameKey = gameKey;
	movePriority = new MovePriority();
    }

    public void move(Direction direction) {
        movePriority.addDirection(direction);
        setMoving(true);
        if (getDirection() == null) {
            setDirection(direction);
	}
    }

    public void releaseDirection(Direction direction) {
        movePriority.releaseDirection(direction);
    }

    private void setDirection() {
        if (isBlockReached()) {
	    final Direction old = getDirection();
	    setDirection(movePriority.getFirstPriority());
	    if (willCollide()) {
		setDirection(movePriority.getSecondPriority());
	    }
	    if (getDirection() == null) {
	        setDirection(old);
	    }
	    addXTargetPosition(getDirection().deltaX);
	    addYTargetPosition(getDirection().deltaY);
	}
    }

    @Override protected void handleCollision() {
	resetPositionAndTarget();
	setBlockReached(true);
    }


    @Override public void tick() {
        if (isMoving()) {
            setDirection();
            move();
            if (isBlockReached() && movePriority.getFirstPriority() == null) {
		setMoving(false);
	    }
	}
    }

    @Override public void interact(final Movable movingObject) {
        if (!getGame().isLevelCompleted()) {
	    if (movingObject.getBlockType() == BlockType.ENEMY) {
		getGame().playerDied(gameKey);
	    }
	}
    }
}