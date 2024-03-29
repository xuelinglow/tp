package seedu.address.model.appointment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class TimePeriodTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Time time = new Time("10:00");
        assertThrows(NullPointerException.class, () -> new TimePeriod(time, null));
        assertThrows(NullPointerException.class, () -> new TimePeriod(null, time));
    }

    @Test
    public void constructor_invalidTimePeriod_throwsIllegalArgumentException() {
        Time startTime = new Time("10:00");
        Time endTime = new Time("09:00");
        assertThrows(IllegalArgumentException.class, () -> new TimePeriod(startTime, endTime));
    }

    @Test
    public void isValidTimePeriod() {
        Time time = new Time("10:00");

        // null time
        assertThrows(NullPointerException.class, () -> TimePeriod.isValidTimePeriod(time, null));
        assertThrows(NullPointerException.class, () -> TimePeriod.isValidTimePeriod(null, time));
        assertThrows(NullPointerException.class, () -> TimePeriod.isValidTimePeriod(null, null));

        assertFalse(TimePeriod.isValidTimePeriod(new Time("12:00"), new Time("12:00"))); // duration 0
        assertFalse(TimePeriod.isValidTimePeriod(new Time("12:01"), new Time("12:00"))); // start after end
        assertFalse(TimePeriod.isValidTimePeriod(new Time("23:59"), new Time("00:01"))); // start after end

        // Valid time period
        assertTrue(TimePeriod.isValidTimePeriod(new Time("11:00"), new Time("12:00")));
        assertTrue(TimePeriod.isValidTimePeriod(new Time("09:00"), new Time("09:01")));
    }

    @Test
    public void overlapsWith_hasOverlappingTimePeriodAfter_returnsTrue() {
        TimePeriod firstPeriod = new TimePeriod(new Time("10:00"), new Time("12:00"));
        TimePeriod secondPeriod = new TimePeriod(new Time("11:00"), new Time("13:00"));

        assertTrue(firstPeriod.overlapsWith(secondPeriod));
    }

    @Test
    public void overlapsWith_hasOverlappingTimePeriodBefore_returnsTrue() {
        TimePeriod firstPeriod = new TimePeriod(new Time("10:00"), new Time("12:00"));
        TimePeriod secondPeriod = new TimePeriod(new Time("09:00"), new Time("11:00"));

        assertTrue(firstPeriod.overlapsWith(secondPeriod));
    }

    @Test
    public void overlapsWith_nonOverlappingTimePeriod_returnsFalse() {
        TimePeriod firstPeriod = new TimePeriod(new Time("10:00"), new Time("12:00"));
        TimePeriod secondPeriod = new TimePeriod(new Time("13:00"), new Time("14:00"));

        assertFalse(firstPeriod.overlapsWith(secondPeriod));
    }

    @Test
    public void overlapsWith_adjacentTimePeriod_returnsFalse() {
        TimePeriod firstPeriod = new TimePeriod(new Time("10:00"), new Time("12:00"));
        TimePeriod secondPeriod = new TimePeriod(new Time("12:00"), new Time("14:00"));

        assertFalse(firstPeriod.overlapsWith(secondPeriod));
    }

    @Test
    public void overlapsWith_sameTimePeriod_returnsTrue() {
        TimePeriod firstPeriod = new TimePeriod(new Time("10:00"), new Time("12:00"));
        TimePeriod secondPeriod = new TimePeriod(new Time("10:00"), new Time("12:00"));

        assertTrue(firstPeriod.overlapsWith(secondPeriod));
    }

    @Test
    public void overlapsWith_otherTimePeriodWithin_returnsTrue() {
        TimePeriod firstPeriod = new TimePeriod(new Time("09:00"), new Time("13:00"));
        TimePeriod secondPeriod = new TimePeriod(new Time("10:00"), new Time("12:00"));

        assertTrue(firstPeriod.overlapsWith(secondPeriod));
    }

    @Test
    public void equals() {
        Time startTime = new Time("12:00");
        Time endTime = new Time("13:00");
        TimePeriod timePeriod = new TimePeriod(startTime, endTime);

        // same values -> returns true
        assertTrue(timePeriod.equals(new TimePeriod(new Time("12:00"), new Time("13:00"))));

        // same object -> returns true
        assertTrue(timePeriod.equals(timePeriod));

        // null -> returns false
        assertFalse(timePeriod.equals(null));

        // different types -> returns false
        assertFalse(timePeriod.equals(5.0f));

        // different values -> returns false
        assertFalse(timePeriod.equals(new TimePeriod(new Time("12:30"), new Time("13:00"))));
    }

    @Test
    public void compareTo_earlierStartTime_returnsNegative() {
        TimePeriod earlierPeriod = new TimePeriod(new Time("10:00"), new Time("11:00"));
        TimePeriod laterPeriod = new TimePeriod(new Time("12:00"), new Time("13:00"));

        assertEquals(-1, earlierPeriod.compareTo(laterPeriod));
    }

    @Test
    public void compareTo_laterStartTime_returnsPositive() {
        TimePeriod laterPeriod = new TimePeriod(new Time("10:00"), new Time("11:00"));
        TimePeriod earlierPeriod = new TimePeriod(new Time("09:00"), new Time("10:30"));

        assertEquals(1, laterPeriod.compareTo(earlierPeriod));
    }

    @Test
    public void compareTo_sameStartTime_returnsZero() {
        TimePeriod firstPeriod = new TimePeriod(new Time("10:00"), new Time("11:00"));
        TimePeriod secondPeriod = new TimePeriod(new Time("10:00"), new Time("12:00"));

        assertEquals(0, firstPeriod.compareTo(secondPeriod));
    }

    @Test
    public void compareTo_differentStartAndEndTimes_returnsNegative() {
        TimePeriod secondPeriod = new TimePeriod(new Time("10:00"), new Time("11:00"));
        TimePeriod firstPeriod = new TimePeriod(new Time("09:00"), new Time("10:30"));

        assertEquals(-1, firstPeriod.compareTo(secondPeriod));
    }

    @Test
    public void toString_validTimePeriod_correctStringRepresentation() {
        Time startTime = new Time("10:00");
        Time endTime = new Time("11:00");
        TimePeriod timePeriod = new TimePeriod(startTime, endTime);

        assertEquals("10:00 to 11:00", timePeriod.toString());
    }
}
