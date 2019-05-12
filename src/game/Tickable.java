package game;

/**
 * All objects that should be updated on game tick implements this interface.
 */
public interface Tickable
{
    /**
     * Update object.
     */
    void tick();
}
