package game.objects;

/**
 * Base class for all blocks.
 */
public class Block
{
    private final BlockType blockType;

    /**
     * @param blockType The type of block to create.
     */
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
