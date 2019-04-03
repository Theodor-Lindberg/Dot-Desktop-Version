package game;

public enum BlockType
{
    EMPTY(false, false), WALL(true, false), START(false, false),
    END(false, false), KEY(false, true), PLAYER(false, false), ENEMY(false, true);

    public final boolean isSolid;
    public final boolean isInteractive;

    BlockType(final boolean isSolid, final boolean isInteractive) {
	this.isSolid = isSolid;
	this.isInteractive = isInteractive;
    }
}
