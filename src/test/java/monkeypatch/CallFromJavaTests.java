package monkeypatch;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Shawn Hartsock
 */
public class CallFromJavaTests {

    @Test
    public void testMonkeyPatch() {
        Frobnicator frobnicator = new Frobnicator();
        assertEquals( "frob:foo", frobnicator.frobnicate() );
        Frobnicator grobnicate = Grobnicator.frobnicate();
        assertEquals("frob:foo", grobnicate.frobnicate());
        Grobnicator grobnicator = new Grobnicator();
        assertEquals("grobnicated!", grobnicator.grobnicate());
        Frobnicator map = Grobnicator.coercion();
        assertEquals("mapped!", map.frobnicate());
    }
}
