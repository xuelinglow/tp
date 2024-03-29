package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_APPOINTMENT_DATE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_APPOINTMENT_DATE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_APPOINTMENT_END_TIME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_APPOINTMENT_NOTE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_APPOINTMENT_START_TIME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NRIC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NRIC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalAppointments.ALICE_APPT;
import static seedu.address.testutil.TypicalAppointments.getTypicalAddressBookWithAppointments;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.date.Date;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditApptCommand.EditApptDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.appointment.Time;
import seedu.address.model.appointment.TimePeriod;
import seedu.address.model.patient.Nric;
import seedu.address.testutil.AppointmentBuilder;
import seedu.address.testutil.EditApptDescriptorBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditApptCommand.
 */
public class EditApptCommandTest {

    private Model model = new ModelManager(getTypicalAddressBookWithAppointments(), new UserPrefs());

    //All fields specified with same as original appt
    @Test
    public void execute_allFieldsSpecified_success() {
        Appointment editedAppt = new AppointmentBuilder().withNric(ALICE_APPT.getNric().value).build();
        EditApptCommand.EditApptDescriptor descriptor = new EditApptDescriptorBuilder(editedAppt).build();
        EditApptCommand editApptCommand = new EditApptCommand(ALICE_APPT.getNric(), ALICE_APPT.getDate(),
                ALICE_APPT.getTimePeriod(), descriptor);

        String expectedMessage = String.format(EditApptCommand.MESSAGE_EDIT_APPT_SUCCESS,
                Messages.format(editedAppt));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setAppointment(ALICE_APPT, editedAppt);

        assertCommandSuccess(editApptCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecified_success() {
        AppointmentBuilder appointmentInList = new AppointmentBuilder(ALICE_APPT);
        Appointment editedAppt = appointmentInList.withDate(VALID_APPOINTMENT_DATE_AMY)
                .withStartTime(VALID_APPOINTMENT_START_TIME_AMY).withEndTime(VALID_APPOINTMENT_END_TIME_AMY).build();

        EditApptDescriptor descriptor = new EditApptDescriptorBuilder().withDate(VALID_APPOINTMENT_DATE_AMY)
                .withTimePeriod(VALID_APPOINTMENT_START_TIME_AMY, VALID_APPOINTMENT_END_TIME_AMY).build();
        EditApptCommand editApptCommand = new EditApptCommand(ALICE_APPT.getNric(),
                ALICE_APPT.getDate(), ALICE_APPT.getTimePeriod(), descriptor);

        String expectedMessage = String.format(EditApptCommand.MESSAGE_EDIT_APPT_SUCCESS,
                Messages.format(editedAppt));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setAppointment(ALICE_APPT, editedAppt);

        assertCommandSuccess(editApptCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecified_success() {
        EditApptCommand editApptCommand =
                new EditApptCommand(ALICE_APPT.getNric(),
                        ALICE_APPT.getDate(), ALICE_APPT.getTimePeriod(), new EditApptCommand.EditApptDescriptor());
        Appointment editedAppointment = model.getMatchingAppointment(ALICE_APPT.getNric(),
                ALICE_APPT.getDate(), ALICE_APPT.getTimePeriod());

        String expectedMessage = String.format(EditApptCommand.MESSAGE_EDIT_APPT_SUCCESS,
                Messages.format(editedAppointment));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertEquals(ALICE_APPT, editedAppointment);
        assertCommandSuccess(editApptCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_appointmentNricNotFound_failure() {
        Nric notFoundNric = new Nric("G9999999X");
        EditApptDescriptor descriptor = new EditApptDescriptorBuilder().withDate(VALID_APPOINTMENT_DATE_AMY).build();
        EditApptCommand editApptCommand = new EditApptCommand(notFoundNric,
                ALICE_APPT.getDate(), ALICE_APPT.getTimePeriod(), descriptor);

        assertCommandFailure(editApptCommand, model, Messages.MESSAGE_PATIENT_NRIC_NOT_FOUND);
    }

    @Test
    public void execute_appointmentNotFound_failure() {
        EditApptDescriptor descriptor = new EditApptDescriptorBuilder().withDate(VALID_APPOINTMENT_DATE_AMY).build();
        EditApptCommand editApptCommand = new EditApptCommand(
                ALICE_APPT.getNric(),
                new Date("1900-02-02"),
                ALICE_APPT.getTimePeriod(),
                descriptor
        );

        assertCommandFailure(editApptCommand, model, Messages.MESSAGE_APPOINTMENT_NOT_FOUND);
    }

    @Test
    public void equals() {
        final EditApptDescriptor descriptor = new EditApptDescriptorBuilder()
                .withDate(VALID_APPOINTMENT_DATE_BOB).build();
        final EditApptCommand standardCommand = new EditApptCommand(
                new Nric(VALID_NRIC_AMY),
                new Date(VALID_APPOINTMENT_DATE_AMY),
                new TimePeriod(new Time(VALID_APPOINTMENT_START_TIME_AMY), new Time(VALID_APPOINTMENT_END_TIME_AMY)),
                descriptor);

        // same values -> returns true
        EditApptDescriptor copyDescriptor = new EditApptCommand.EditApptDescriptor(descriptor);
        EditApptCommand commandWithSameValues = new EditApptCommand(
                new Nric(VALID_NRIC_AMY),
                new Date(VALID_APPOINTMENT_DATE_AMY),
                new TimePeriod(new Time(VALID_APPOINTMENT_START_TIME_AMY), new Time(VALID_APPOINTMENT_END_TIME_AMY)),
                copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different nric -> returns false
        assertFalse(standardCommand.equals(new EditApptCommand(
                new Nric(VALID_NRIC_BOB),
                new Date(VALID_APPOINTMENT_DATE_AMY),
                new TimePeriod(new Time(VALID_APPOINTMENT_START_TIME_AMY), new Time(VALID_APPOINTMENT_END_TIME_AMY)),
                descriptor)));

        final EditApptDescriptor diffDescriptor = new EditApptDescriptorBuilder()
                .withNote(VALID_APPOINTMENT_NOTE_BOB).build();

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditApptCommand(
                new Nric(VALID_NRIC_AMY),
                new Date(VALID_APPOINTMENT_DATE_AMY),
                new TimePeriod(new Time(VALID_APPOINTMENT_START_TIME_AMY), new Time(VALID_APPOINTMENT_END_TIME_AMY)),
                diffDescriptor)));
    }

    @Test
    public void toStringMethod() {
        EditApptCommand.EditApptDescriptor editApptDescriptor = new EditApptDescriptor();
        EditApptCommand editApptCommand = new EditApptCommand(
                new Nric(VALID_NRIC_AMY),
                new Date(VALID_APPOINTMENT_DATE_AMY),
                new TimePeriod(new Time(VALID_APPOINTMENT_START_TIME_AMY), new Time(VALID_APPOINTMENT_END_TIME_AMY)),
                editApptDescriptor);
        String expected = EditApptCommand.class.getCanonicalName()
                + "{nric=" + VALID_NRIC_AMY
                + ", date=" + VALID_APPOINTMENT_DATE_AMY
                + ", timePeriod=" + VALID_APPOINTMENT_START_TIME_AMY
                + " to " + VALID_APPOINTMENT_END_TIME_AMY
                + ", editApptDescriptor=" + editApptDescriptor + "}";
        assertEquals(expected, editApptCommand.toString());
    }

}
