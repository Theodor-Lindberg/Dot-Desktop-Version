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

    public EnemyFactory(final Game game) {
        this.game = game;
    }

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
}