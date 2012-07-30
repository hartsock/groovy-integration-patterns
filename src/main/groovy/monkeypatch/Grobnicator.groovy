package monkeypatch;

/**
 * @author Shawn Hartsock
 */
class Grobnicator {
    Frobnicator frobnicator = frobnicate();

    String grobnicate() {
        frobnicator.frobnicate();
    }

    static Frobnicator frobnicate() {
        def frob = new Frobnicator()
        frob.metaClass.frobnicate = {-> "grobnicated!" }
        return frob
    }

    static Frobnicator coercion() {
        [
                frobnicate:{->"mapped!"}
        ] as Frobnicator
    }
}
