package trip;

import graph.DirectedGraph;
import graph.LabeledGraph;
import graph.SimpleShortestPaths;

import java.io.FileNotFoundException;
import java.io.FileReader;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static trip.Main.error;

/** Encapsulates a map containing sites, positions, and road distances between
 *  them.
 *  Discussed Trip structure with Michelle Ling
 *  @author Netra Sathe
 */
class Trip {

    /** Read map file named NAME into out map graph. */
    void readMap(String name) {
        int k;
        k = 0;
        try {
            Scanner inp = new Scanner(new FileReader(name));
            while (inp.hasNext()) {
                k += 1;
                switch (inp.next()) {
                case "L":
                    addLocation(inp.next(), inp.nextDouble(),
                            inp.nextDouble());
                    break;
                case "R":
                    addRoad(inp.next(), inp.next(), inp.nextDouble(),
                                Direction.parse(inp.next()), inp.next());
                    break;
                default:
                    error("map entry #%d: unknown type", k);
                    break;
                }
            }
        } catch (NullPointerException excp) {
            error(excp.getMessage());
        } catch (InputMismatchException excp) {
            error("bad entry #%d", k);
        } catch (NoSuchElementException excp) {
            error("entry incomplete at end of file");
        } catch (FileNotFoundException excp) {
            error("File not found");
        }
    }

    /** Produce a report on the standard output of a shortest journey from
     *  DESTS.get(0), then DESTS.get(1), .... */
    void makeTrip(List<String> dests) {
        if (dests.size() < 2) {
            error("must have at least two locations for a trip");
        }

        System.out.printf("From %s:%n%n", dests.get(0));
        int trippy;

        trippy = 1;
        for (int i = 1; i < dests.size(); i += 1) {
            Integer
                    from = sitez .get(dests.get(i - 1)),
                    to = sitez .get(dests.get(i));
            if (from == null) {
                error("No location named %s", dests.get(i - 1));
            } else if (to == null) {
                error("No location named %s", dests.get(i));
            }
            TripPlan plan = new TripPlan(from, to);
            plan.setPaths();
            List<Integer> seg = plan.pathTo(to);
            trippy = reportSegment(trippy, from, seg);
        }
    }

    /** Print out a written description of the location sequence SEGMENT,
     *  starting at FROM, and numbering the lines of the description starting
     *  at SEQ.  That is, FROM and each item in SEGMENT are the
     *  numbers of vertices representing locations.  Together, they
     *  specify the starting point and vertices along a path where
     *  each vertex is joined to the next by an edge.  Returns the
     *  next sequence number.  The format is as described in the
     *  project specification.  That is, each line but the last in the
     *  segment is formated like this example:
     *      1. Take University_Ave west for 0.1 miles.
     *  and the last like this:
     *      5. Take I-80 west for 8.4 miles to San_Francisco.
     *  Adjacent roads with the same name and direction are combined.
     *  */
    int reportSegment(int seq, int from, List<Integer> segment) {
        int i = 0;
        Road curr;
        if (from == segment.get(i)) {
            i += 1;
        }
        curr = m.getLabel(from, segment.get(i));
        double distance = curr.length();
        String name = curr.toString();
        String direcc = curr.direction().fullName();
        while (i < segment.size() - 1) {
            from = i; i++;
            curr = m.getLabel(segment.get(from), segment.get(i));
            if (curr.toString().equals(name)
                    && direcc.equals(curr.direction().fullName())) {
                distance += curr.length();
            } else {
                System.out.printf("%d. Take %s %s for %.1f miles. %n",
                        seq, name, direcc, distance);
                distance = curr.length();
                name = curr.toString();
                direcc = curr.direction().fullName();
                seq++;
            }
        }
        System.out.printf("%d. Take %s %s for %.1f miles to %s. %n", seq,
                name, direcc, distance,
                m.getLabel(segment.get(segment.size() - 1)));
        seq++;
        return seq;
    }

    /** Add a new location named NAME at (X, Y). */
    private void addLocation(String name, double x, double y) {
        if (sitez .containsKey(name)) {
            error("multiple entries for %s", name);
        }
        int v = m.add(new Location(name, x, y));
        sitez .put(name, v);
    }

    /** Add a stretch of road named NAME from the Location named FROM
     *  to the location named TO, running in direction DIR, and
     *  LENGTH miles long.  Add a reverse segment going back from TO
     *  to FROM. */
    private void addRoad(String from, String name, double length,
                         Direction dir, String to) {
        Integer v0 = sitez .get(from),
                v1 = sitez .get(to);

        if (v0 == null) {
            error("location %s not defined", from);
        } else if (v1 == null) {
            error("location %s not defined", to);
        }

        Road rd = new Road(name, dir, length);
        Road rd2 = new Road(name, dir.reverse(), length);
        m.add(v0, v1, rd);
        m.add(v1, v0, rd2);
    }

    /** Represents the network of Locations and Roads. */
    private RoadMap m = new RoadMap();
    /** Mapping of Location names to corresponding map vertices. */
    private HashMap<String, Integer> sitez = new HashMap<>();

    /** A labeled directed graph of Locations whose edges are labeled by
     *  Roads. */
    private static class RoadMap extends LabeledGraph<Location, Road> {
        /** An empty RoadMap. */
        RoadMap() {
            super(new DirectedGraph());
        }
    }

    /** Paths in _map from a given location. */
    private class TripPlan extends SimpleShortestPaths {

        /** Location of destination. */
        private final Location locn;


        /** A plan for travel from START to DEST according to _map. */
        TripPlan(int start, int dest) {
            super(m, start, dest);
            locn = m.getLabel(dest);
        }

        @Override
        protected double getWeight(int u, int v) {
            return m.getLabel(u, v).length();
        }

        @Override
        protected double estimatedDistance(int v) {
            return m.getLabel(v).dist(locn);
        }


    }

}
