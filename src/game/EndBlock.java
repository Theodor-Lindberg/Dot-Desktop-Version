package game;

public class EndBlock extends Block implements Interactable
{
    private Level level;
    private final Level.LevelChanger levelChanger;

    public EndBlock(Level level, final Level.LevelChanger levelChanger) {
	super(BlockType.END);

	this.level = level;
	this.levelChanger = levelChanger;
    }

    @Override public void interact(final Moveable movingObject) {
	if (movingObject.getBlockType() == BlockType.PLAYER) {
	    level.completeLevel(levelChanger);
	}
    }
}
