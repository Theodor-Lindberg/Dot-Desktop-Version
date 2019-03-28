package game;

public class KeyBlock extends Block implements ITriggerable
{
    public KeyBlock(final BlockType blockType) {
	super(blockType);
    }

    @Override public void trigger(final Level.LevelChanger levelChanger) {
    }
}
