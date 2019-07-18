package make;

/* You MAY add public @Test methods to this class.  You may also add
 * additional public classes containing "UnitTest" in their name. These
 * may not be part of your make package per se (that is, it must be
 * possible to remove them and still have your package work). */

import org.junit.Test;
import ucb.junit.textui;
import static org.junit.Assert.*;

import java.util.Arrays;

/** Unit tests for the make package. */
public class UnitTest {

    /** Run all JUnit tests in the make package. */
    public static void main(String[] ignored) {
        System.exit(textui.runClasses(make.UnitTest.class));
    }


    @Test (expected = IllegalArgumentException.class)
    public void expectError1() {
        Maker lol = new Maker();
        Depends heh = new Depends();
        Rule nope = new Rule(lol, "heh");
        Rule tester = new Rule(lol, "definitely not");
        lol.build("target");
    }

    @Test (expected = IllegalArgumentException.class)
    public void expectError2() {
        Maker lol = new Maker();
        Depends heh = new Depends();
        Rule nope = new Rule(lol, "heh");
        Rule tester = new Rule(lol, "definitely not");
        lol.readMakefile("target");
    }

    @Test
    public void testForErrors() {
        Maker lol = new Maker();
        Depends heh = new Depends();
        Rule nope = new Rule(lol, "heh");
        Rule tester = new Rule(lol, "definitely not");
        assertTrue(nope.isUnfinished());
        nope.addDependency(tester);
        nope.addCommands(Arrays.asList("Hello", "this", "is", "bogus"));
        assertEquals(null, nope.getTime());
    }

    @Test (expected = IllegalArgumentException.class)
    public void expectError3() {
        Maker lol = new Maker();
        Depends heh = new Depends();
        Rule nope = new Rule(lol, "heh");
        Rule tester = new Rule(lol, "definitely not");
        lol.readFileAges("target");
    }

    @Test
    public void dummyTest() {
        Maker lol = new Maker();
        Depends heh = new Depends();
        Rule nope = new Rule(lol, "heh");
    }


    @Test (expected = IllegalArgumentException.class)
    public void expectError() {
        Maker lol = new Maker();
        Depends heh = new Depends();
        Rule nope = new Rule(lol, "heh");
        Rule tester = new Rule(lol, "definitely not");
        nope.rebuild();
    }
}
