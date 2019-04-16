package game;

/**
 * All objects that handles movement implements this interface.
 */
public interface AIMovement
{
    void move(final Movable movingObject);

    void handleCollision(final Movable movingObject);

    AIMovement copy();
}
