package game.GameObjects.MovingObjects;

import game.AIMovement.BasicAI;
import game.Game;
import game.GameObjects.MovingObjects.Movable.Speed;
import util.Point2D;

import static game.AIMovement.BasicAI.*;

/**
 * Factory for creating enemies.
 */
public class EnemyFactory
{
    /**
     * All available types of ai.
     */
    public enum EnemyAI
    {
	BASIC_TURN_LEFT, BASIC_TURN_RIGHT, BASIC_TURN_BACK
    }

    private final Game game;

    /**
     * @param game The reference to the Game object for Enemies to have when created or copied.
     */
    public EnemyFactory(final Game game) {
        this.game = game;
    }

    /**
     * Create an Enemy.
     *
     * @param position 		The start position.
     * @param startDirection 	The direction to face when starting.
     * @param speed 		The movement speed.
     * @param ai 		The type of AI.
     *
     * @return An Enemy object
     */
    public Enemy createEnemy(final Point2D position, final Direction startDirection, final Speed speed, final EnemyAI ai) {
	switch (ai) {
	    case BASIC_TURN_LEFT:
		return new Enemy(position, startDirection, speed, game, new BasicAI(TurnDirection.LEFT));
	    case BASIC_TURN_RIGHT:
		return new Enemy(position, startDirection, speed, game, new BasicAI(TurnDirection.RIGHT));
	    case BASIC_TURN_BACK:
		return new Enemy(position, startDirection, speed, game, new BasicAI(TurnDirection.BACK));
	    default:
		return null;
	}
    }

    /**
     * Create a copy of an enemy object with a new reference to the Game object.
     * @param enemy The enemy to copy.
     *
     * @return A copy of the enemy.
     */
    public Enemy copyEnemy(final Enemy enemy) {
        return new Enemy(enemy, game);
    }
}