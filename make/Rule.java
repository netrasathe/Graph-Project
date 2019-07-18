package make;

import java.util.ArrayList;
import java.util.List;

import static make.Main.error;

/** Represents the rules concerning a single target in a makefile.
 *  @author P. N. Hilfinger
 *  Modified with help from Lab assistants and Turese Anderson
 */
class Rule {

    /** The Maker that created me. */
    private Maker makr;
    /** The Maker's dependency graph. */
    private Depends depz;
    /** My target. */
    private String targt;
    /** The vertex corresponding to my target. */
    private int vert;
    /** My change time, or null if I don't exist. */
    private Integer tiem;
    /** True iff I have been brought up to date. */
    private boolean fini;

    /** A new Rule for TARGET. Adds corresponding vertex to MAKER's dependence
     *  graph. */
    Rule(Maker maker, String target) {
        makr = maker;
        depz = makr.getGraph();
        targt = target;
        vert = depz.add(this);
        tiem = makr.getInitialAge(target);
        fini = false;
    }

    /** Add the target of DEPENDENT to my dependencies. */
    void addDependency(Rule dependent) {
        depz.add(getVertex(), dependent.getVertex());
    }

    /** Add COMMANDS to my command set.  Signals IllegalStateException if
     *  COMMANDS is non-empty, but I already have a non-empty command set.
     */
    void addCommands(List<String> commands) {
        if (!rulez.isEmpty() && !commands.isEmpty()) {
            throw new IllegalStateException();
        }
        rulez.addAll(commands);
    }

    /** Return the vertex representing me. */
    int getVertex() {
        return vert;
    }

    /** Return my target. */
    String getTarget() {
        return targt;
    }

    /** Return my target's current change time. */
    Integer getTime() {
        return tiem;
    }

    /** Return true iff I have not yet been brought up to date. */
    boolean isUnfinished() {
        return !fini;
    }

    /** Check that dependencies are in fact built before it's time to rebuild
     *  a node. */
    private void checkFinishedDependencies() {
        for (int i : depz.successors(vert)) {
            if (depz.getLabel(i).isUnfinished()) {
                error("Error: %s is missing dependencies",
                        depz.getLabel(i));
            }
        }
    }

    /** Return true iff I am out of date and need to be rebuilt (including the
     *  case where I do not exist).  Assumes that my dependencies are all
     *  successfully rebuilt. */
    private boolean outOfDate() {
        if (getTime() == null) {
            return true;
        }
        for (int i : depz.successors(vert)) {
            if (depz.getLabel(i) == null) {
                continue;
            } else if (depz.getLabel(i).getTime() > tiem) {
                return true;
            }
        }
        return false;
    }

    /** Rebuild me, if needed, after checking that all dependencies are rebuilt
     *  (error otherwise). */
    void rebuild() {
        checkFinishedDependencies();

        if (outOfDate()) {
            if (rulez.isEmpty()) {
                error("Error: %s needs to be rebuilt, but has no commands",
                        targt);
            }
            for (String i : rulez) {
                System.out.println(i);
            }
            tiem = makr.getCurrentTime();

        }
        fini = true;
    }
    /** My command list. */
    private ArrayList<String> rulez = new ArrayList<>();
}
