package game;

import game.Game.LevelChanger;

/**
 * End block, determines when the player has won.
 */
public class EndBlock extends Block implements Interactable
{
    private Game game;
    private final LevelChanger levelChanger;

    public EndBlock(Game game, final LevelChanger levelChanger) {
	super(BlockType.END);

	this.game = game;
	this.levelChanger = levelChanger;
    }

    @Override public void interact(final Movable movingObject) {
	if (movingObject.getBlockType() == BlockType.PLAYER) {
	    game.completeLevel(levelChanger);
	}
    }
}
