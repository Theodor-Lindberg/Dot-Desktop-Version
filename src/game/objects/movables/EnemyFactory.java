package game.objects.movables;

import game.ai.BasicAI;
import game.Game;
import game.objects.movables.Movable.Speed;
import util.Point2D;

import static game.ai.BasicAI.*;

/**
 * Factory for creating enemies.
 */
public class EnemyFactory
{
    /**
     * All available types of ai.
     */
    public enum EnemyAI
    {	// For some reason IntelliJ doesn't map the Javadoc comments to the enums properly, so the warnings are ignored.
        /**
	 * Maps to BasicAI with always turn left configuration.
	 */
	BASIC_TURN_LEFT
	/**
	* Maps to BasicAI with always turn right configuration.
	*/
, BASIC_TURN_RIGHT,
	/**
	 * Maps to BasicAI with always turn back configuration.
	 */
	BASIC_TURN_BACK
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