package game;

import game.Level.LevelChanger;

/**
 * End block, determines when the player has won.
 */
public class EndBlock extends Block implements Interactable
{
    private Level level;
    private final LevelChanger levelChanger;

    public EndBlock(Level level, final LevelChanger levelChanger) {
	super(BlockType.END);

	this.level = level;
	this.levelChanger = levelChanger;
    }

    @Override public void interact(final Movable movingObject) {
	if (movingObject.getBlockType() == BlockType.PLAYER) {
	    level.completeLevel(levelChanger);
	}
    }
}
