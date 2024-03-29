package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PATIENTS;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalAppointments.ALICE_APPT;
import static seedu.address.testutil.TypicalAppointments.BOB_APPT;
import static seedu.address.testutil.TypicalPatients.ALICE;
import static seedu.address.testutil.TypicalPatients.BENSON;
import static seedu.address.testutil.TypicalPatients.BOB;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.GuiSettings;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.patient.NameContainsKeywordsPredicate;
import seedu.address.model.patient.Nric;
import seedu.address.model.patient.exceptions.PatientNotFoundException;
import seedu.address.testutil.AddressBookBuilder;
import seedu.address.testutil.AppointmentBuilder;

public class ModelManagerTest {

    private ModelManager modelManager = new ModelManager();

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(new AddressBook(), new AddressBook(modelManager.getAddressBook()));
    }

    @Test
    public void setUserPrefs_nullUserPrefs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setUserPrefs(null));
    }

    @Test
    public void setUserPrefs_validUserPrefs_copiesUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setAddressBookFilePath(Paths.get("address/book/file/path"));
        userPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        modelManager.setUserPrefs(userPrefs);
        assertEquals(userPrefs, modelManager.getUserPrefs());

        // Modifying userPrefs should not modify modelManager's userPrefs
        UserPrefs oldUserPrefs = new UserPrefs(userPrefs);
        userPrefs.setAddressBookFilePath(Paths.get("new/address/book/file/path"));
        assertEquals(oldUserPrefs, modelManager.getUserPrefs());
    }

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setGuiSettings(null));
    }

    @Test
    public void setGuiSettings_validGuiSettings_setsGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
        modelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, modelManager.getGuiSettings());
    }

    @Test
    public void setAddressBookFilePath_nullPath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setAddressBookFilePath(null));
    }

    @Test
    public void setAddressBookFilePath_validPath_setsAddressBookFilePath() {
        Path path = Paths.get("address/book/file/path");
        modelManager.setAddressBookFilePath(path);
        assertEquals(path, modelManager.getAddressBookFilePath());
    }

    @Test
    public void setAddressBook_validAddressBook_successfullySetsAddressBook() {
        AddressBook newAddressBook = new AddressBook();
        modelManager.setAddressBook(newAddressBook);
        assertEquals(newAddressBook, modelManager.getAddressBook());
    }

    @Test
    public void setAddressBook_nullAddressBook_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setAddressBook(null));
    }

    @Test
    public void hasPatientWithNric_nullNric_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasPatientWithNric(null));
    }

    @Test
    public void hasPatientWithNric_existingNric_returnsTrue() {
        modelManager.addPatient(ALICE);
        assertTrue(modelManager.hasPatientWithNric(ALICE.getNric()));
    }

    @Test
    public void hasPatientWithNric_nonExistingNric_returnsFalse() {
        modelManager.addPatient(ALICE);
        assertFalse(modelManager.hasPatientWithNric(new Nric("S1234567A")));
    }

    @Test
    public void getPatientWithNric_nullNric_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.getPatientWithNric(null));
    }

    @Test
    public void getPatientWithNric_existingNric_returnsPatient() {
        modelManager.addPatient(ALICE);
        assertEquals(ALICE, modelManager.getPatientWithNric(ALICE.getNric()));
    }

    @Test
    public void getPatientWithNric_nonExistingNric_throwsPatientNotFoundException() {
        modelManager.addPatient(ALICE);
        assertThrows(PatientNotFoundException.class, () -> modelManager.getPatientWithNric(new Nric("S1234567A")));
    }

    @Test
    public void deletePatientWithNric_nullNric_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.deletePatientWithNric(null));
    }

    @Test
    public void deletePatientWithNric_existingNric_success() {
        modelManager.addPatient(ALICE);
        assertTrue(modelManager.hasPatient(ALICE));
        modelManager.deletePatientWithNric(ALICE.getNric());
        assertFalse(modelManager.hasPatient(ALICE));
    }

    @Test
    public void deletePatientWithNric_nonExistingNric_throwsPatientNotFoundException() {
        modelManager.addPatient(ALICE);
        assertThrows(PatientNotFoundException.class, () -> modelManager.deletePatientWithNric(new Nric("S1234567A")));
        assertTrue(modelManager.hasPatient(ALICE));
    }

    @Test
    public void hasPatient_nullPatient_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasPatient(null));
    }

    @Test
    public void hasPatient_patientNotInAddressBook_returnsFalse() {
        assertFalse(modelManager.hasPatient(ALICE));
    }

    @Test
    public void hasPatient_patientInAddressBook_returnsTrue() {
        modelManager.addPatient(ALICE);
        assertTrue(modelManager.hasPatient(ALICE));
    }

    @Test
    public void deletePatient_nullPatient_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.deletePatient(null));
    }

    @Test
    public void deletePatient_existingPatient_success() {
        modelManager.addPatient(ALICE);
        assertTrue(modelManager.hasPatient(ALICE));
        modelManager.deletePatient(ALICE);
        assertFalse(modelManager.hasPatient(ALICE));
    }

    @Test
    public void deletePatient_nonExistingPatient_throwsPatientNotFoundException() {
        modelManager.addPatient(ALICE);
        assertThrows(PatientNotFoundException.class, () -> modelManager.deletePatient(BOB));
    }

    @Test
    public void setPatient_bothNullPatient_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setPatient(null, null));
    }

    @Test
    public void setPatient_targetPatientNull_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setPatient(null, ALICE));
    }

    @Test
    public void setPatient_editedPatientNull_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setPatient(ALICE, null));
    }

    @Test
    public void setPatient_existingTargetPatient_success() {
        modelManager.addPatient(ALICE);
        assertTrue(modelManager.hasPatient(ALICE));
        modelManager.setPatient(ALICE, BOB);
        assertFalse(modelManager.hasPatient(ALICE));
        assertTrue(modelManager.hasPatient(BOB));
    }

    @Test
    public void setPatient_nonExistingTargetPatient_throwsPatientNotFoundException() {
        modelManager.addPatient(ALICE);
        assertThrows(PatientNotFoundException.class, () -> modelManager.setPatient(BOB, ALICE));
    }

    @Test
    public void getFilteredPatientList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredPatientList().remove(0));
    }

    @Test
    public void hasAppointment_validAppointment_returnsTrue() {
        Appointment validAppointment = new AppointmentBuilder(ALICE_APPT).build();
        modelManager.addAppointment(validAppointment);
        assertTrue(modelManager.hasAppointment(validAppointment));
    }

    @Test
    public void hasAppointment_appointmentNotFound_returnsFalse() {
        Appointment appointment = new AppointmentBuilder(ALICE_APPT).build();
        assertFalse(modelManager.hasAppointment(appointment));
    }

    @Test
    public void cancelAppointment_validAppointment_success() {
        Appointment appointment = new AppointmentBuilder(ALICE_APPT).build();
        modelManager.addAppointment(appointment);
        assertTrue(modelManager.hasAppointment(appointment));
        modelManager.cancelAppointment(appointment);
        assertFalse(modelManager.hasAppointment(appointment));
    }

    @Test
    public void addAppointment_validAppointment_success() {
        Appointment appointment = new AppointmentBuilder(ALICE_APPT).build();
        modelManager.addAppointment(appointment);
        assertTrue(modelManager.hasAppointment(appointment));
    }

    @Test
    public void setAppointment_validAppointment_success() {
        Appointment appointment = new AppointmentBuilder(ALICE_APPT).build();
        modelManager.addAppointment(appointment);

        // Edit the appointment details
        Appointment editedAppointment = new AppointmentBuilder(BOB_APPT).build();
        modelManager.setAppointment(appointment, editedAppointment);

        assertFalse(modelManager.hasAppointment(appointment)); // original appointment should not exist
        assertTrue(modelManager.hasAppointment(editedAppointment)); // edited appointment should exist
    }

    @Test
    public void getMatchingAppointment_validInputs_returnsMatchingAppointment() {
        Appointment appointment = new AppointmentBuilder(ALICE_APPT).build();
        modelManager.addAppointment(appointment);

        Appointment matchingAppointment = modelManager.getMatchingAppointment(
                ALICE_APPT.getNric(),
                ALICE_APPT.getDate(),
                ALICE_APPT.getTimePeriod()
        );

        assertEquals(appointment, matchingAppointment);
    }

    @Test
    public void deleteAppointmentsWithNric_validNric_success() {
        // Add appointments with the specified NRIC
        Appointment appointment1 = new AppointmentBuilder(ALICE_APPT).build();
        Appointment appointment2 = new AppointmentBuilder(BOB_APPT).build();
        modelManager.addAppointment(appointment1);
        modelManager.addAppointment(appointment2);

        // Delete appointments with the specified NRIC
        modelManager.deleteAppointmentsWithNric(ALICE_APPT.getNric());

        assertFalse(modelManager.hasAppointment(appointment1)); // appointment1 should be deleted
        assertTrue(modelManager.hasAppointment(appointment2)); // appointment2 should still exist
    }

    @Test
    public void samePatientHasOverlappingAppointment_noOverlap_returnsFalse() {
        // Add appointments that don't overlap
        Appointment appointment1 = new AppointmentBuilder()
                .withNric("T0123456A").withDate("2024-03-01")
                .withStartTime("16:00").withEndTime("17:00").build();

        Appointment appointment2 = new AppointmentBuilder()
                .withNric("T0123456A").withDate("2024-03-01")
                .withStartTime("17:00").withEndTime("18:00").build();
        modelManager.addAppointment(appointment1);
        assertFalse(modelManager.samePatientHasOverlappingAppointment(appointment2));
    }

    @Test
    public void samePatientHasOverlappingAppointment_withOverlap_returnsTrue() {
        // Add appointments that don't overlap
        Appointment appointment1 = new AppointmentBuilder()
                .withNric("T0123456A").withDate("2024-03-01")
                .withStartTime("16:00").withEndTime("17:00").build();

        Appointment appointment2 = new AppointmentBuilder()
                .withNric("T0123456A").withDate("2024-03-01")
                .withStartTime("16:30").withEndTime("18:00").build();
        modelManager.addAppointment(appointment1);
        assertTrue(modelManager.samePatientHasOverlappingAppointment(appointment2));
    }

    @Test
    public void getFilteredAppointmentViewList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () ->
            modelManager.getFilteredAppointmentViewList().remove(0));
    }

    @Test
    public void getFilteredAppointmentDayViewList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () ->
            modelManager.getFilteredAppointmentDayViewList().remove(0));
    }



    @Test
    public void equals() {
        AddressBook addressBook = new AddressBookBuilder().withPatient(ALICE).withPatient(BENSON).build();
        AddressBook differentAddressBook = new AddressBook();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(addressBook, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(addressBook, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different addressBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentAddressBook, userPrefs)));

        // different filteredList -> returns false
        String[] keywords = ALICE.getName().fullName.split("\\s+");
        modelManager.updateFilteredPatientList(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(addressBook, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredPatientList(PREDICATE_SHOW_ALL_PATIENTS);

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setAddressBookFilePath(Paths.get("differentFilePath"));
        assertFalse(modelManager.equals(new ModelManager(addressBook, differentUserPrefs)));
    }
}
