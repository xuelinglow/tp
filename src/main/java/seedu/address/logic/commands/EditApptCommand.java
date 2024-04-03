package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW_END_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW_START_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NRIC;
import static seedu.address.logic.parser.CliSyntax.PREFIX_START_TIME;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_APPOINTMENT_VIEWS;

import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.core.date.Date;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.appointment.AppointmentType;
import seedu.address.model.appointment.Note;
import seedu.address.model.appointment.Time;
import seedu.address.model.appointment.TimePeriod;
import seedu.address.model.patient.Nric;

/**
 * Edits the details of an existing appointment in CLInic.
 */
public class EditApptCommand extends Command {

    public static final String COMMAND_WORD = "editAppt";

    public static final String COMMAND_WORD_ALT = "ea";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits the details of the appointment identified by its Nric, Date, and Start time.\n"
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: "
            + PREFIX_NRIC + "NRIC (must be a valid NRIC in the system) "
            + PREFIX_DATE + "DATE "
            + PREFIX_START_TIME + "START_TIME "
            + "[" + PREFIX_NEW_DATE + "NEW_DATE] "
            + "[" + PREFIX_NEW_START_TIME + "NEW_START_TIME] "
            + "[" + PREFIX_NEW_END_TIME + "NEW_END_TIME] "
            + "[" + PREFIX_NEW_TAG + "NEW_APPOINTMENT_TYPE] "
            + "[" + PREFIX_NEW_NOTE + "NEW_NOTE] \n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NRIC + "T0123456A "
            + PREFIX_DATE + "2024-02-20 "
            + PREFIX_START_TIME + "11:00 "
            + PREFIX_NEW_END_TIME + "12:30 "
            + PREFIX_NEW_TAG + "Blood test "
            + PREFIX_NEW_NOTE + "May come later ";

    public static final String MESSAGE_EDIT_APPT_SUCCESS = "Edited Appointment: %1$s";
    public static final String MESSAGE_EDIT_APPT_NO_FIELDS_FAILURE = "At least one field to edit must be provided.";

    public static final String MESSAGE_EDIT_OVERLAPPING_APPOINTMENT_FAILURE =
            "New appointment information overlaps with an existing appointment for the same patient.\n"
                    + "Please refer to appointments listed below for that patient on the same date.";

    private final Nric targetNric;
    private final Date targetDate;
    private final Time targetStartTime;
    private final EditApptDescriptor editApptDescriptor;

    /**
     * Creates a EditApptCommand to edit the appointment with the
     * specified {@code Nric, Date, StartTime} using the details
     * from {@code editApptDescriptor}
     * @param nric of the appointment for edit
     * @param date of the appointment to edit
     * @param startTime of the appointment to edit
     * @param editApptDescriptor details to edit the appointment with
     */
    public EditApptCommand(Nric nric, Date date, Time startTime, EditApptDescriptor editApptDescriptor) {
        requireNonNull(nric);
        requireNonNull(date);
        requireNonNull(startTime);
        requireNonNull(editApptDescriptor);

        this.targetNric = nric;
        this.targetDate = date;
        this.targetStartTime = startTime;
        this.editApptDescriptor = new EditApptDescriptor(editApptDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (!model.hasPatientWithNric(targetNric)) {
            throw new CommandException(Messages.MESSAGE_PATIENT_NRIC_NOT_FOUND);
        }

        if (!model.hasAppointmentWithDetails(targetNric, targetDate, targetStartTime)) {
            throw new CommandException(Messages.MESSAGE_APPOINTMENT_NOT_FOUND);
        }

        Appointment apptToEdit = model.getMatchingAppointment(targetNric, targetDate, targetStartTime);
        Appointment editedAppt = createEditedAppointment(apptToEdit, editApptDescriptor);

        // Must check for overlapping appointments of new appt besides current appt
        if (model.hasOverlappingAppointmentExcluding(apptToEdit, editedAppt)) {
            throw new CommandException(MESSAGE_EDIT_OVERLAPPING_APPOINTMENT_FAILURE);
        }
      
        model.setAppointment(apptToEdit, editedAppt);
        model.updateFilteredAppointmentViewList(PREDICATE_SHOW_ALL_APPOINTMENT_VIEWS);
        return new CommandResult(String.format(MESSAGE_EDIT_APPT_SUCCESS, Messages.format(editedAppt)));
    }

    /**
     * Creates and returns a {@code Appointment} with the details of {@code apptToEdit}
     * edited with {@code editAppointmentDescriptor}.
     */
    private static Appointment createEditedAppointment(Appointment apptToEdit, EditApptDescriptor editApptDescriptor) {
        assert apptToEdit != null;

        Date updatedDate = editApptDescriptor.getDate().orElse(apptToEdit.getDate());
        Time updatedStartTime = editApptDescriptor.getStartTime().orElse(apptToEdit.getStartTime());
        Time updatedEndTime = editApptDescriptor.getEndTime().orElse(apptToEdit.getEndTime());
        AppointmentType updatedAppointmentType = editApptDescriptor.getAppointmentType()
                .orElse(apptToEdit.getAppointmentType());
        Note updatedNote = editApptDescriptor.getNote().orElse(apptToEdit.getNote());

        TimePeriod updatedTimePeriod = new TimePeriod(updatedStartTime, updatedEndTime);

        return new Appointment(apptToEdit.getNric(), updatedDate, updatedTimePeriod,
                updatedAppointmentType, updatedNote, apptToEdit.getMark());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditApptCommand)) {
            return false;
        }

        EditApptCommand otherEditApptCommand = (EditApptCommand) other;
        return targetNric.equals(otherEditApptCommand.targetNric)
                && targetDate.equals(otherEditApptCommand.targetDate)
                && targetStartTime.equals(otherEditApptCommand.targetStartTime)
                && editApptDescriptor.equals(otherEditApptCommand.editApptDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("nric", targetNric)
                .add("date", targetDate)
                .add("startTime", targetStartTime)
                .add("editApptDescriptor", editApptDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the appointment with. Each non-empty field value will replace the
     * corresponding field value of the appointment;
     * Nric cannot be edited.
     */
    public static class EditApptDescriptor {
        private Date date;
        private Time startTime;
        private Time endTime;
        private AppointmentType appointmentType;
        private Note note;

        public EditApptDescriptor() {}

        /**
         * Copy constructor.
         */
        public EditApptDescriptor(EditApptDescriptor toCopy) {
            setDate(toCopy.date);
            setStartTime(toCopy.startTime);
            setEndTime(toCopy.endTime);
            setAppointmentType(toCopy.appointmentType);
            setNote(toCopy.note);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(date, startTime, endTime, appointmentType, note);
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public Optional<Date> getDate() {
            return Optional.ofNullable(date);
        }

        public void setStartTime(Time startTime) {
            this.startTime = startTime;
        }

        public Optional<Time> getStartTime() {
            return Optional.ofNullable(startTime);
        }

        public void setEndTime(Time endTime) {
            this.endTime = endTime;
        }

        public Optional<Time> getEndTime() {
            return Optional.ofNullable(endTime);
        }


        public void setAppointmentType(AppointmentType appointmentType) {
            this.appointmentType = appointmentType;
        }

        public Optional<AppointmentType> getAppointmentType() {
            return Optional.ofNullable(appointmentType);
        }

        public void setNote(Note note) {
            this.note = note;
        }

        public Optional<Note> getNote() {
            return Optional.ofNullable(note);
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditApptDescriptor)) {
                return false;
            }

            EditApptDescriptor otherEditApptDescriptor = (EditApptDescriptor) other;
            return Objects.equals(date, otherEditApptDescriptor.date)
                    && Objects.equals(startTime, otherEditApptDescriptor.startTime)
                    && Objects.equals(endTime, otherEditApptDescriptor.endTime)
                    && Objects.equals(appointmentType, otherEditApptDescriptor.appointmentType)
                    && Objects.equals(note, otherEditApptDescriptor.note);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("date", date)
                    .add("startTime", startTime)
                    .add("endTime", endTime)
                    .add("appointmentType", appointmentType)
                    .add("note", note)
                    .toString();
        }
    }
}
