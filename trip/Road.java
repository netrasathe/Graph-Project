package trip;

import static trip.Main.error;

/** Represents a road between two Locations.
 *  @author P. N. Hilfinger
 */
class Road {

    /** Segment name. */
    private final String segname;
    /** Direction to Dest. */
    private final Direction direcc;
    /** Segment length. */
    private final double seglength;

    /** A Road whose name is NAME, going in DIRECTION, and of given
     *  LENGTH. */
    Road(String name, Direction direction, double length) {
        if (length < 0) {
            error("Road %s given negative length.", length);
        }
        segname = name;
        direcc = direction;
        seglength = length;
    }

    /** Returns the direction of road. */
    Direction direction() {
        return direcc;
    }

    @Override
    public String toString() {
        return segname;
    }

    /** Returns the length of road segment. */
    public double length() {
        return seglength;
    }

}
