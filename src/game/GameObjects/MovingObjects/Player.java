package game.GameObjects.MovingObjects;

import game.Game;
import game.GameObjects.BlockType;
import game.AIMovement.MovePriority;
import util.Point2D;

/**
 * The player class.
 */
public class Player extends Movable
{
    private final MovePriority movePriority;

    /**
     * @param position The start position of the player.
     * @param speed The movement speed.
     * @param game The reference to the Game.
     */
    public Player(final Point2D position, final Speed speed, final Game game) {
	super(BlockType.PLAYER, position, speed, null, game);
	movePriority = new MovePriority();
    }

    /**
     * Construct a new Player object based on another one but with a new reference to Game.
     *
     * @param clone 	The Player object to clone.
     * @param game	The reference to the Game.
     */
    public Player(final Player clone, final Game game) {
        super(clone.getBlockType(), new Point2D(clone.getX(), clone.getY()), clone.getSpeed(), null, game);
	movePriority = new MovePriority();
    }

    /**
     * Prepare the Player to move on the next tick.
     *
     * @param direction The direction to move.
     */
    public void move(final Direction direction) {
        movePriority.addDirection(direction);
        setMoving(true);
    }

    /**
     * Remove a direction from being evaluated when choosing prioritized direction.
     *
     * @param direction The direction to release.
     */
    public void releaseDirection(final Direction direction) {
        movePriority.releaseDirection(direction);
    }

    /**
     * Set the direction to move.
     */
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
	    updateTargetPosition(getDirection());
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
		getGame().restart();
	    }
	}
    }
}