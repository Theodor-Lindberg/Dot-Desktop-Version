package game;

/**
 * All objects that handles movement implements this interface.
 */
public interface Movement
{
    void move(final Movable movingObject);

    void handleCollision(final Movable movingObject);
}
