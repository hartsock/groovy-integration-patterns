package monkeypatch

import org.junit.Test

import static org.junit.Assert.assertEquals

/**
 * @author Shawn Hartsock
 */
class CallFromGroovyTests {

    @Test
    public void testMonkeyPatch() {
        Frobnicator frobnicator = new Frobnicator();
        assertEquals( "frob:foo", frobnicator.frobnicate() );
        Frobnicator grobnicate = Grobnicator.frobnicate();
        assertEquals( "grobnicated!", grobnicate.frobnicate() );
    }
}
