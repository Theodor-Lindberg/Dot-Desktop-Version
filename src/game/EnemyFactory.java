package game;

import game.Movable.Speed;

import static game.BasicAI.*;

public class EnemyFactory
{
    public enum EnemyAI
    {
	BASIC_TURN_LEFT, BASIC_TURN_RIGHT, BASIC_TURN_BACK
    }

    private final Game game;

    public EnemyFactory(Game game) {
        this.game = game;
    }

    public Enemy createEnemy(Point2D position, Direction startDirection, Speed speed, EnemyAI ai) {
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