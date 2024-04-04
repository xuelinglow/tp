package seedu.address.model.appointment.exceptions;

/**
 * Signals that the operation will result in Overlapping Appointments. These are considered overlapping
 * if they have the same nric, same date and overlapping Time Periods.
 */

public class OverlappingAppointmentException extends RuntimeException {
    public OverlappingAppointmentException() {
        super("Operation would result in overlapping appointments");
    }
}
