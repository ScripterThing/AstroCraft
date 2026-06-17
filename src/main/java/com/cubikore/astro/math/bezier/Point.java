package com.cubikore.astro.math.bezier;

/**
 * Class BezierCurve
 *
 * @author Otsoko
 * <a href="https://github.com/Otsoko/bezier">...</a>
 *
 */
public class Point {
    private float x     = 0.0f;
    private float y     = 0.0f;
    private float angle = 0.0f;

    /**
     * Constructs a new Point
     *
     * @param x
     *        the x coordinate
     * @param y
     *        the y coordinate
     */
    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructs a new Point
     *
     * @param x
     *        the x coordinate
     * @param y
     *        the y coordinate
     * @param angle
     *        the angle
     */
    public Point(float x, float y, float angle) {
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    /**
     * Gets the x coordinate
     *
     * @return the x coordinate
     */
    public float getX() {
        return x;
    }

    /**
     * Sets the x coordinate
     *
     * @param x
     *        the x coordinate
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Gets the y coordinate
     *
     * @return the y coordinate
     */
    public float getY() {
        return y;
    }

    /**
     * Sets the y coordinate
     *
     * @param y
     *        the y coordinate
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Gets the angle
     *
     * @return the angle
     */
    public float getAngle() {
        return angle;
    }

    /**
     * Sets the angle
     *
     * @param angle
     *        the angle
     */
    public void setAngle(float angle) {
        this.angle = angle;
    }

    @Override
    public String toString() {
        return "Point [x=" + x + ", y=" + y + ", angle=" + angle + "]";
    }
}
