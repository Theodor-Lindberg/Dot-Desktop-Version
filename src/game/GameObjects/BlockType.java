package game.GameObjects;

/**
 * Enum listing all available block types and their characteristics.
 */
public enum BlockType
{
    EMPTY(false), WALL(true), WALL1(true), WALL2(true),
    END(false), KEY(false), PLAYER(false), ENEMY(false);

    public final boolean isSolid;

    /**
     * @param isSolid Set to True if these types of blocks should be solid, set to False otherwise.
     */
    BlockType(final boolean isSolid) {
	this.isSolid = isSolid;
    }
}
