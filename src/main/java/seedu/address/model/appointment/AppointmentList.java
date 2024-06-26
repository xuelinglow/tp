package seedu.address.model.appointment;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.date.Date;
import seedu.address.model.appointment.exceptions.AppointmentNotFoundException;
import seedu.address.model.appointment.exceptions.DuplicateAppointmentException;
import seedu.address.model.appointment.exceptions.OverlappingAppointmentException;
import seedu.address.model.patient.Nric;



/**
 * A list of appointments that enforces uniqueness between its elements and does not allow nulls.
 * An appointment is considered unique by comparing using {@code Appointment#isSameAppointment(Appointment)}.
 * As such, adding and updating of appointments uses Appointment#isSameAppointment(Appointment) for equality
 * to ensure that the appointment being added or updated is unique and not duplicated.
 * However, the removal of an appointment uses Patient#equals(Object) to ensure that the
 * appointment with exactly the same fields will be removed.
 *
 * Supports a minimal set of list operations.
 *
 * @see Appointment#isSameAppointment(Appointment)
 */
public class AppointmentList implements Iterable<Appointment> {

    private final ObservableList<Appointment> internalList = FXCollections.observableArrayList();
    private final ObservableList<Appointment> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent appointment as the given argument.
     */
    public boolean contains(Appointment toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSameAppointment);
    }

    /**
     * Adds an appointment to the list.
     * The appointment must not already exist in the list.
     */
    public void add(Appointment toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateAppointmentException();
        }
        if (samePatientHasOverlappingAppointment(toAdd)) {
            throw new OverlappingAppointmentException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the appointment {@code target} in the list with {@code editedAppointment}.
     * {@code target} must exist in the list.
     * The appointment of {@code editedAppointment} must not be the same as another existing appointment in the list.
     */
    public void setAppointment(Appointment target, Appointment editedAppointment) {
        requireAllNonNull(target, editedAppointment);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new AppointmentNotFoundException();
        }

        if (!target.isSameAppointment(editedAppointment) && contains(editedAppointment)) {
            throw new DuplicateAppointmentException();
        }

        if (hasOverlappingAppointmentExcluding(target, editedAppointment)) {
            throw new OverlappingAppointmentException();
        }

        internalList.set(index, editedAppointment);
    }

    /**
     * Removes the equivalent appointment from the list.
     * The appointment must exist in the list.
     */
    public void remove(Appointment toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new AppointmentNotFoundException();
        }
    }

    public void setAppointments(AppointmentList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code appointments}.
     * {@code appointments} must not contain duplicate appointments.
     */
    public void setAppointments(List<Appointment> appointments) {
        requireAllNonNull(appointments);
        if (!appointmentsAreUnique(appointments)) {
            throw new DuplicateAppointmentException();
        }

        internalList.setAll(appointments);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Appointment> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<Appointment> iterator() {
        return internalList.iterator();
    }

    /**
     * Returns an Appointment that matches from the Appointment list based on {@code Nric, Date, StartTime} given.
     * Throws an {@code AppointmentNotFoundException} if no matching appointment is found.
     */
    public Appointment getMatchingAppointment(Nric nricToMatch, Date dateToMatch, Time startTimeToMatch) {
        requireNonNull(nricToMatch);
        requireNonNull(dateToMatch);
        requireNonNull(startTimeToMatch);

        for (Appointment appointment : this) {
            if (appointment.getNric().equals(nricToMatch)
                    && appointment.getDate().equals(dateToMatch)
                    && appointment.getStartTime().equals(startTimeToMatch)) {
                return appointment;
            }
        }

        throw new AppointmentNotFoundException();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AppointmentList)) {
            return false;
        }

        AppointmentList otherAppointmentList = (AppointmentList) other;
        return internalList.equals(otherAppointmentList.internalList);
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    @Override
    public String toString() {
        return internalList.toString();
    }

    /**
     * Returns true if {@code appointments} contains only unique appointments.
     */
    private boolean appointmentsAreUnique(List<Appointment> appointments) {
        for (int i = 0; i < appointments.size() - 1; i++) {
            for (int j = i + 1; j < appointments.size(); j++) {
                if (appointments.get(i).isSameAppointment(appointments.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }


    /** Delete all appointments with given Nric if such appointments exist without exceptions **/
    public void deleteAppointmentsWithNric(Nric nric) {
        requireNonNull(nric);
        internalList.removeIf(appointment -> appointment.getNric().equals(nric));
    }

    /**
     * Returns true if appointment list has appointment with {@code nric, date, startTime}
     */
    public boolean hasAppointmentWithDetails(Nric nricToMatch, Date dateToMatch, Time startTimeToMatch) {
        requireNonNull(nricToMatch);
        requireNonNull(dateToMatch);
        requireNonNull(startTimeToMatch);

        for (Appointment appointment : this) {
            if (appointment.getNric().equals(nricToMatch)
                    && appointment.getDate().equals(dateToMatch)
                    && appointment.getStartTime().equals(startTimeToMatch)) {
                return true;
            }
        }
        return false;
    }
    /** Return true if new appt to be added overlaps with existing appointment of same Nric **/
    public boolean samePatientHasOverlappingAppointment(Appointment targetAppt) {
        requireNonNull(targetAppt);

        for (Appointment appointment : this) {
            // Check for same patient and same date
            if (appointment.getNric().equals(targetAppt.getNric())
                    && appointment.getDate().equals(targetAppt.getDate())) {
                return appointment.hasOverlappingTimePeriod(targetAppt);
            }
        }

        return false; // No appointment for same patient or same date
    }

    /** Return true if new appt to be added overlaps with existing appointment of same Nric, excluding targetAppt **/
    public boolean hasOverlappingAppointmentExcluding(Appointment targetAppt, Appointment editedAppointment) {
        requireAllNonNull(targetAppt, editedAppointment);

        for (Appointment appointment : this) {
            // Exclude targetAppt since that would be changed already
            if (appointment.equals(targetAppt)) {
                continue;
            }
            //Check if for same Patient, there is overlapping date and time period with editedAppointment
            if (appointment.getNric().equals(editedAppointment.getNric())
                    && appointment.getDate().equals(editedAppointment.getDate())) {
                return appointment.hasOverlappingTimePeriod(editedAppointment);
            }
        }

        return false; // No appointment for same patient or same date
    }
}
