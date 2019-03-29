package game;

public abstract class AnimatedBlock extends Block
{
    protected int alpha;
    protected boolean animationDone;

    public AnimatedBlock(final BlockType blockType) {
	super(blockType);
	animationDone = false;
    }

    public abstract void tick();

    public final int getAlpha() {
	return alpha;
    }

    public final boolean isAnimationDone() {
	return animationDone;
    }
}
