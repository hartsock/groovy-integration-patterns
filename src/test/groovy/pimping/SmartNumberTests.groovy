package pimping

import org.junit.Test

/**
 * @author Shawn Hartsock
 */
class SmartNumberTests {

    @Test
    void testFrobnicate() {
        use(SmartNumber) {
            assert 2.frobnicate(2) == 4
            assert 3.frobnicate(2) == 8
            assert 0.frobnicate(-1) == 1
        }
    }

}
