package game;

public enum BlockType
{
    EMPTY(false, false), WALL(true, false), START(false, false),
    END(false, false), KEY(false, true), PLAYER(false, false), ENEMY(false, true);

    public final boolean isSolid;
    public final boolean isTriggerable;

    BlockType(final boolean isSolid, final boolean isTriggerable) {
	this.isSolid = isSolid;
	this.isTriggerable = isTriggerable;
    }
}
