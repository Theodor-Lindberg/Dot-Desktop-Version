package game;

public interface AIMovement
{
    void move(final Moveable movingObject);

    void handleCollision(final Moveable movingObject);
}
