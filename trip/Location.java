package trip;

import static java.lang.Math.sqrt;

/** Represents a location on a map.
 *  @author P. N. Hilfinger
 */
class Location {

    /** A location identified by NAME, at coordinates (X, Y). */
    Location(String name, double x, double y) {
        stringname = name;
        locx = x;
        locy = y;
    }

    /** Return the distance between me and Y. */
    double dist(Location y) {
        double dx = locx - y.locx;
        double dy = locy - y.locy;
        return sqrt(dx * dx + dy * dy);
    }

    /** Returns the distance between locations X and Y. */
    public double dist(Location x, Location y) {
        return x.dist(y);
    }

    @Override
    public String toString() {
        return stringname;
    }

    /** Returns my estimated distance from the origin. */
    public double distance() {
        return d;
    }

    /** Set distance() to W. */
    public void setDistance(double w) {
        d = w;
    }

    /** The identifying name of this location. */
    private String stringname;
    /** Coordinates of this location. */
    private double locx, locy;
    /** Estimated distance from starting point. */
    private double d;
}
