package game.GameObjects.MovingObjects;

import game.Game;
import game.Game.GameKey;
import game.GameObjects.BlockType;
import game.AIMovement.MovePriority;
import util.Point2D;

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

    public void move(final Direction direction) {
        movePriority.addDirection(direction);
        setMoving(true);
        if (getDirection() == null) {
            setDirection(direction);
	}
    }

    public void releaseDirection(final Direction direction) {
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