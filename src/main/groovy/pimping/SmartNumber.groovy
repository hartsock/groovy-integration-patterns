package pimping

/**
 * @author Shawn Hartsock
 */
@Category(Number)
class SmartNumber {
    static Number frobnicate(Number self, Number other) {
        Number out = 1
        self.times { out *= other }
        return out
    }
}
