package com.cubikore.astro.math.bezier;

/**
 * Class BezierCurve
 *
 * @author Otsoko
 * <a href="https://github.com/Otsoko/bezier">...</a>
 *
 */
public class BezierCurve {
    protected Point start = null;
    protected Point end   = null;

    /**
     * Gets the start point
     *
     * @return the start point
     */
    public Point getStart() {
        return start;
    }

    /**
     * Sets the start point
     *
     * @param start
     *        the start point
     */
    public void setStart(Point start) {
        this.start = start;
    }

    /**
     * Gets de end point
     *
     * @return the end point
     */
    public Point getEnd() {
        return end;
    }

    /**
     * Sets the end point
     *
     * @param end
     *        the end point
     */
    public void setEnd(Point end) {
        this.end = end;
    }
}
