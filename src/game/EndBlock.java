package game;

import game.Game.GameKey;

/**
 * Determines when the player has won.
 */
public class EndBlock extends Block implements Interactable
{
    private final Game game;
    private final GameKey gameKey;

    public EndBlock(final Game game, final GameKey gameKey) {
	super(BlockType.END);

	this.game = game;
	this.gameKey = gameKey;
    }

    @Override public void interact(final Movable movingObject) {
	if (movingObject.getBlockType() == BlockType.PLAYER) {
	    game.completeLevel(gameKey);
	}
    }
}
