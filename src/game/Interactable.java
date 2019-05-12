package game;

import game.GameObjects.MovingObjects.Movable;

/**
 * Interface that all game objects that can be interacted with implements.
 */
public interface Interactable
{
    /**
     * Interact with a moving object.
     *
     * @param movingObject The moving object to interact with.
     */
    void interact(final Movable movingObject);
}
