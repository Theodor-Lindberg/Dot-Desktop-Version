package game.ai;

import game.objects.movables.Movable;

/**
 * All classes that classes handles the movement and collisions of another object implements this interface.
 */
public interface AIMovement
{
    /**
     * Move a Movable object.
     *
     * @param movingObject The object to be moved.
     */
    void move(final Movable movingObject);

    /**
     * Handle the collision of a Movable object.
     *
     * @param movingObject The object that collided.
     */
    void handleCollision(final Movable movingObject);


    /**
     * Get a copy of the object that implemented this interface.
     *
     * @return A copy of the class that implemented this interface.
     */
    AIMovement copy();
}
