package exp.yaremchuken.fitnessterra

import exp.yaremchuken.fitnessterra.util.Utils
import org.junit.Assert.assertEquals
import org.junit.Test

class UtilsTest {
    @Test
    fun formatToTime() {
        assertEquals("05:00", Utils.formatToTime(300))
        assertEquals("01:06:40", Utils.formatToTime(4000))
    }
}