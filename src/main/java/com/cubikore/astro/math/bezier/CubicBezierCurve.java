package com.cubikore.astro.math.bezier;

/**
 * Class BezierCurve
 *
 * @author Otsoko
 * <a href="https://github.com/Otsoko/bezier">...</a>
 *
 */
public class CubicBezierCurve extends BezierCurve {
    private Point c1 = null;
    private Point c2 = null;

    public CubicBezierCurve(Point start, Point c1, Point c2, Point end) {
        this.start = start;
        this.c1 = c1;
        this.c2 = c2;
        this.end = end;
    }

    /**
     * Gets the first control point
     *
     * @return the first control point
     */
    public Point getC1() {
        return c1;
    }

    /**
     * Sets the first control point
     *
     * @param c1
     *        the first control point
     */
    public void setC1(Point c1) {
        this.c1 = c1;
    }

    /**
     * Gets the second control point
     *
     * @return the second control point
     */
    public Point getC2() {
        return c2;
    }

    /**
     * Sets the second control point
     *
     * @param c2
     *        the second control point
     */
    public void setC2(Point c2) {
        this.c2 = c2;
    }

    /**
     * Calculates the point on the curve in step t
     *
     * @param t
     *        the step on the curve
     * @return the point in the step t
     */
    public Point getPointAtCurve(float t) {
        float t_squared = t * t;
        float t_cubed = t_squared * t;
        float minus_t_squared = (1 - t) * (1 - t);
        float minus_t_cubed = minus_t_squared * (1 - t);

        float x = start.getX() * minus_t_cubed + 3 * c1.getX() * t * minus_t_squared
                + 3 * c2.getX() * t_squared * (1 - t) + end.getX() * t_cubed;
        float y = start.getY() * minus_t_cubed + 3 * c1.getY() * t * minus_t_squared
                + 3 * c2.getY() * t_squared * (1 - t) + end.getY() * t_cubed;

        float rx = minus_t_squared * (c1.getX() - start.getX()) + 2 * t * (1 - t) * (c2.getX() - c1.getX())
                + t_squared * (end.getX() - c2.getX());
        float ry = minus_t_squared * (c1.getY() - start.getY()) + 2 * t * (1 - t) * (c2.getY() - c1.getY())
                + t_squared * (end.getY() - c2.getY());

        float angle = (float) Math.toDegrees(Math.atan2(ry, rx));

        return new Point(x, y, angle);
    }

}
