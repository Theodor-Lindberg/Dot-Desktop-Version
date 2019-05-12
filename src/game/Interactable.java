package game;

import game.GameObjects.MovingObjects.Movable;

/**
 * Interface that all game objects that can be interacted with implements.
 */
public interface Interactable
{
    void interact(final Movable movingObject);
}
