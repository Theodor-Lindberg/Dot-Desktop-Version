package game;

/**
 * Enum listing all available block types and their characteristics.
 */
public enum BlockType
{
    EMPTY(false), WALL(true), WALL1(true), WALL2(true), START(false),
    END(false), KEY(false), PLAYER(false), ENEMY(false);

    public final boolean isSolid;

    BlockType(final boolean isSolid) {
	this.isSolid = isSolid;
    }
}
