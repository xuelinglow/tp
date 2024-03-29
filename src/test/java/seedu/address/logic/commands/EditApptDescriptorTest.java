package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_APPT_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_APPT_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_APPOINTMENT_DATE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_APPOINTMENT_END_TIME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_APPOINTMENT_NOTE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_APPOINTMENT_START_TIME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_APPOINTMENT_TYPE_BOB;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.EditApptCommand.EditApptDescriptor;
import seedu.address.testutil.EditApptDescriptorBuilder;

public class EditApptDescriptorTest {

    @Test
    public void equals() {
        // same values -> returns true
        EditApptDescriptor descriptorWithSameValues = new EditApptCommand.EditApptDescriptor(DESC_APPT_AMY);
        assertTrue(DESC_APPT_AMY.equals(descriptorWithSameValues));

        // same object -> returns true
        assertTrue(DESC_APPT_AMY.equals(DESC_APPT_AMY));

        // null -> returns false
        assertFalse(DESC_APPT_AMY.equals(null));

        // different types -> returns false
        assertFalse(DESC_APPT_AMY.equals(5));

        // different values -> returns false
        assertFalse(DESC_APPT_AMY.equals(DESC_APPT_BOB));

        // different date -> returns false
        EditApptDescriptor editedAmyAppt = new EditApptDescriptorBuilder(DESC_APPT_AMY)
                .withDate(VALID_APPOINTMENT_DATE_BOB).build();
        assertFalse(DESC_APPT_AMY.equals(editedAmyAppt));

        // different timePeriod -> returns false
        editedAmyAppt = new EditApptDescriptorBuilder(DESC_APPT_AMY)
                .withTimePeriod(VALID_APPOINTMENT_START_TIME_BOB, VALID_APPOINTMENT_END_TIME_BOB).build();
        assertFalse(DESC_APPT_AMY.equals(editedAmyAppt));

        // different appointmentType -> returns false
        editedAmyAppt = new EditApptDescriptorBuilder(DESC_APPT_AMY)
                .withAppointmentType(VALID_APPOINTMENT_TYPE_BOB).build();
        assertFalse(DESC_APPT_AMY.equals(editedAmyAppt));

        // different note -> returns false
        editedAmyAppt = new EditApptDescriptorBuilder(DESC_APPT_AMY)
                .withNote(VALID_APPOINTMENT_NOTE_BOB).build();
        assertFalse(DESC_APPT_AMY.equals(editedAmyAppt));
    }

    @Test
    public void toStringMethod() {
        EditApptDescriptor editApptDescriptor = new EditApptDescriptor();
        String expected = EditApptDescriptor.class.getCanonicalName() + "{date="
                + editApptDescriptor.getDate().orElse(null) + ", timePeriod="
                + editApptDescriptor.getTimePeriod().orElse(null) + ", appointmentType="
                + editApptDescriptor.getAppointmentType().orElse(null) + ", note="
                + editApptDescriptor.getNote().orElse(null) + "}";
        assertEquals(expected, editApptDescriptor.toString());
    }
}
