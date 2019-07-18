package make;

import graph.DepthFirstTraversal;

import java.io.FileNotFoundException;
import java.io.FileReader;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;
import static make.Main.error;

/** Represents a makefile.
 *  @author P. N. Hilfinger
 */
class Maker {


    /** Mapping the target names to the rules. */
    private HashMap<String, Rule> targetz = new HashMap<>();
    /** Depth-first traversal of my vertices. */
    private MakeTraversal travz;
    /** Current time. */
    private int timee;
    /** Makefile dependency graph. */
    private Depends depz = new Depends();
    /** Map target names to ages. */
    private HashMap<String, Integer> targage = new HashMap<>();



    /** Describes Makefile lines that are ignored. */
    static final Pattern IGNORED = Pattern.compile("\\s*(#.*)?");
    /** Describes a rule header in a makefile:  TARGET: DEPENDENCIES. */
    static final Pattern HEADER =
            Pattern.compile("([^:\\s]+)\\s*:\\s*(.*?)\\s*");
    /** Describes a sequence of valid targets and whitespace. */
    static final Pattern TARGETS = Pattern.compile("[^:=#\\\\]*$");
    /** Describes an indented command line. */
    static final Pattern COMMAND = Pattern.compile("(\\s+.*)");
    /** Describes the separator on dependencies lines. */
    static final Pattern SPACES = Pattern.compile("\\p{Blank}+");

    /** Read and store the ages of existing targets from the
     *  file named FILEINFONAME. */
    void readFileAges(String fileInfoName) {
        String name;
        name = "<unknown>";
        try {
            Scanner inp = new Scanner(new FileReader(fileInfoName));
            timee = inp.nextInt();
            while (inp.hasNext()) {
                targage .put(inp.next(), inp.nextInt());
            }
            inp.close();
        } catch (NoSuchElementException excp) {
            error("Error: Near entry for %s: %s", name, excp.getMessage());
        } catch (FileNotFoundException excp) {
            error("Error: File not found");
        }
    }

    /** Read make rules from the file named MAKEFILENAME and form the dependence
     *  graph with targets as vertices. */
    void readMakefile(String makefileName) {
        Scanner x;
        String target;
        ArrayList<String> dependencies;
        ArrayList<String> rulez;

        target = null;
        dependencies = null;
        rulez = null;
        try {
            x = new Scanner(new FileReader(makefileName));
        } catch (FileNotFoundException excp) {
            error("Error: Could not find makefile: %s", makefileName);
            return;
        }

        while (x.hasNextLine()) {
            String line = x.nextLine();
            Matcher parsed;
            parsed = IGNORED.matcher(line);
            if (parsed.matches()) {
                continue;
            }
            parsed = HEADER.matcher(line);
            if (parsed.matches()) {
                addRule(target, dependencies, rulez);
                target = parsed.group(1);
                if (!TARGETS.matcher(target).matches()) {
                    error("Error: Bad target: '%s'", target);
                }
                if (!TARGETS.matcher(parsed.group(2)).matches()) {
                    error("Error: One or more bad prerequisites: '%s'",
                            parsed.group(2));
                }
                dependencies = new ArrayList<>();
                if (!parsed.group(2).isEmpty()) {
                    dependencies.addAll(asList(SPACES.split(parsed.group(2))));
                }
                rulez = new ArrayList<>();
                continue;
            }
            parsed = COMMAND.matcher(line);
            if (target != null && parsed.matches()) {
                rulez.add(parsed.group(1));
            } else {
                error("Error: Erroneous input line: '%s'", line);
            }
        }
        addRule(target, dependencies, rulez);
    }

    /** Add rule
     *      TARGET: DEPENDENCIES
     *          COMMANDS
     *  to makegraph, or add DEPENDENCIES and COMMANDS to that rule, if it
     *  already exists.  Returns the rule. */
    private Rule addRule(String target,
                         List<String> dependencies,
                         List<String> commands) {
        if (target != null) {
            Rule rule;
            rule = targetz.get(target);
            if (rule == null) {
                rule = new Rule(this, target);
                targetz.put(target, rule);
            }
            for (String dependency: dependencies) {
                Rule depRule = addRule(dependency,
                        Collections.<String>emptyList(),
                        Collections.<String>emptyList());
                rule.addDependency(depRule);
            }
            rule.addCommands(commands);
            return rule;
        } else {
            return null;
        }
    }

    /** Issue instructions to build TARGET. */
    void build(String target) {
        Rule targetRule = addRule(target, Collections.<String>emptyList(),
                                  Collections.<String>emptyList());
        int v = targetRule.getVertex();
        if (travz == null) {
            travz = new MakeTraversal();
            travz.traverse(v);
        } else {
            travz.traverse(v);
        }
    }

    /** Return my dependence graph. */
    final Depends getGraph() {
        return depz;
    }

    /** Return the initial age of TARGET, if it exists, or null if it
     *  does not. */
    final Integer getInitialAge(String target) {
        return targage .get(target);
    }

    /** Returns the current time (to be attached to rebuilt targets). */
    final int getCurrentTime() {
        return timee;
    }

    /** Traversal for make dependency graph. */
    class MakeTraversal extends DepthFirstTraversal {
        /** A traversal of my dependency graph. */
        MakeTraversal() {
            super(depz);
        }

        @Override
        protected boolean postVisit(int v0) {
            if (depz.getLabel(v0).isUnfinished()) {
                depz.getLabel(v0).rebuild();
            }
            return true;
        }
    }

}

