package game;

/**
 * Base class for all blocks.
 */
public class Block
{
    private final BlockType blockType;

    public Block(final BlockType blockType) {
	this.blockType = blockType;
    }

    public BlockType getBlockType() {
	return blockType;
    }

    public boolean isSolid() {
        return blockType.isSolid;
    }
}
