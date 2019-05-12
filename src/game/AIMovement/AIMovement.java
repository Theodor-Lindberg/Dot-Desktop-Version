package game.AIMovement;

import game.GameObjects.MovingObjects.Movable;

/**
 * All classes that classes handles the movement of another object implements this interface.
 */
public interface AIMovement
{
    void move(final Movable movingObject);

    void handleCollision(final Movable movingObject);

    AIMovement copy();
}
