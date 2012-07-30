package monkeypatch;

/**
 * @author Shawn Hartsock
 */
public class Frobnicator {
    String foo = "foo";
    public String frobnicate() {
        return "frob:" + foo;
    }
}
