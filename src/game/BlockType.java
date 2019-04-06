package game;

public enum BlockType
{
    EMPTY(false, false), WALL(true, false), WALL1(true, false), WALL2(true, false), START(false, false),
    END(false, true), KEY(false, true), PLAYER(false, true), ENEMY(false, true);

    public final boolean isSolid;
    public final boolean isInteractive;

    BlockType(final boolean isSolid, final boolean isInteractive) {
	this.isSolid = isSolid;
	this.isInteractive = isInteractive;
    }
}
