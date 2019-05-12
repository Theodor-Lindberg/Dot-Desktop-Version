package util;

/**
 * This class stores two floats representing a position.
 */
public class Point2D
{
    private float x, y;

    /**
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    public Point2D(final float x, final float y) {
	this.x = x;
	this.y = y;
    }

    /**
     * Constructs a copy of another point.
     *
     * @param point The point to copy.
     */
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

    /**
     * Add a value to the x coordinate.
     *
     * @param dx The amount to add.
     */
    public void addX(final float dx) {
        this.x += dx;
    }

    /**
     * Add a value to the y coordinate.
     *
     * @param dy The amount to add.
     */
    public void addY(final float dy) {
        this.y += dy;
    }
}