package game.objects.movables;

import game.ai.AIMovement;
import game.Game;
import game.objects.BlockType;
import util.Point2D;

/**
 * Generic Enemy class, can have different types of ai.
 */
public class Enemy extends Movable
{
    private final AIMovement ai;

    /**
     * @param position 		The start position
     * @param startDirection 	The direction to face when starting
     * @param speed 		The movement speed
     * @param game 		The reference to the Game object
     * @param ai 		The type of AI to use
     */
    public Enemy(final Point2D position, final Direction startDirection, final Speed speed, final Game game, final AIMovement ai)
    {
	super(BlockType.ENEMY, position, speed, startDirection, game);

	this.ai = ai;
    }

    /**
     * Construct a new Enemy object based on another one but with a new reference to Game.
     *
     * @param clone 	The enemy to clone
     * @param game 	The reference to the new Game object.
     */
    public Enemy(final Enemy clone, final Game game) {
	super(clone.getBlockType(), new Point2D(clone.getX(), clone.getY()), clone.getSpeed(),clone.getDirection(), game);

	ai = clone.ai.copy();
    }

    @Override protected void handleCollision() {
	ai.handleCollision(this);
    }

    @Override public void interact(final Movable movingObject) {
	if (movingObject.getBlockType() == BlockType.PLAYER) {
	    movingObject.interact(this);
	}
    }

    @Override public void tick() {
	if (isBlockReached()) {
	    updateTargetPosition(getDirection());
	}
	ai.move(this);
    }
}
