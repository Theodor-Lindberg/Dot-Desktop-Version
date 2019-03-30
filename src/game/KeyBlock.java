package game;

public class KeyBlock extends Block implements Interactable
{
    private Level level;
    private final Level.LevelChanger levelChanger;

    public KeyBlock(final BlockType blockType, final Level level, final Level.LevelChanger levelChanger) {
	super(blockType);
	this.level = level;
	this.levelChanger = levelChanger;
    }

    @Override public void interact(Moveable movingObject) {
        if (movingObject.getBlockType() == BlockType.PLAYER) {

	}
    }
}
