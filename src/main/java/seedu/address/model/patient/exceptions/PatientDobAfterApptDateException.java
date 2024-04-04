package seedu.address.model.patient.exceptions;

/**
 * Signals that the operation will result in the creation of an invalid appointment
 * with date prior to date of birth of the patient
 */
public class PatientDobAfterApptDateException extends RuntimeException {
    public PatientDobAfterApptDateException() {
        super("Operation would result in scheduling of appointment on a date prior to date of birth");
    }
}
