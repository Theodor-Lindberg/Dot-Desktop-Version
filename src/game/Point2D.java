package game;

/**
 * This class stores two floats representing a position.
 */
public class Point2D
{
    private float x, y;

    public Point2D(final float x, final float y) {
	this.x = x;
	this.y = y;
    }

    public Point2D(final Point2D point) {
        this.x = point.x;
        this.y = point.y;
    }

    public float getX() {
	return x;
    }

    public void setX(final float x) {
	this.x = x;
    }

    public float getY() {
	return y;
    }

    public void setY(final float y) {
	this.y = y;
    }

    public void addX(final float dx) {
        this.x += dx;
    }

    public void addY(final float dy) {
        this.y += dy;
    }
}