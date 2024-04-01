package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_END_TIME;
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
import seedu.address.model.appointment.Mark;
import seedu.address.model.appointment.Note;
import seedu.address.model.appointment.TimePeriod;
import seedu.address.model.patient.Nric;

/**
 * Edits the details of an existing appointment in CLInic.
 */
public class EditApptCommand extends Command {

    public static final String COMMAND_WORD = "editAppt";

    public static final String COMMAND_WORD_ALT = "ea";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits the details of the appointment identified by its Nric, Date, End and Start time.\n"
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: "
            + PREFIX_NRIC + "NRIC (must be a valid NRIC in the system) "
            + PREFIX_DATE + "DATE "
            + PREFIX_START_TIME + "START_TIME "
            + PREFIX_END_TIME + "END_TIME "
            + "[" + PREFIX_NEW_DATE + "NEW_DATE] "
            + "[" + PREFIX_NEW_START_TIME + "NEW_START_TIME] "
            + "[" + PREFIX_NEW_END_TIME + "NEW_END_TIME] "
            + "[" + PREFIX_NEW_TAG + "NEW_APPOINTMENT_TYPE] "
            + "[" + PREFIX_NEW_NOTE + "NEW_NOTE] \n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NRIC + "T0123456A "
            + PREFIX_DATE + "2024-02-20 "
            + PREFIX_START_TIME + "11:00 "
            + PREFIX_END_TIME + "11:30 "
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
    private final TimePeriod targetTimePeriod;
    private Appointment apptToEdit;
    private final EditApptDescriptor editApptDescriptor;

    /**
     * Creates a EditApptCommand to edit the appointment with the
     * specified {@code Nric, Date, TimePeriod} using the details
     * from {@code editApptDescriptor}
     * @param nric of the appointment for edit
     * @param date of the appointment to edit
     * @param timePeriod of the appointment to edit
     * @param editApptDescriptor details to edit the appointment with
     */
    public EditApptCommand(Nric nric, Date date, TimePeriod timePeriod, EditApptDescriptor editApptDescriptor) {
        requireNonNull(nric);
        requireNonNull(date);
        requireNonNull(timePeriod);
        requireNonNull(editApptDescriptor);

        this.targetNric = nric;
        this.targetDate = date;
        this.targetTimePeriod = timePeriod;
        this.editApptDescriptor = new EditApptDescriptor(editApptDescriptor);
        this.apptToEdit = null;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (!model.hasPatientWithNric(targetNric)) {
            throw new CommandException(Messages.MESSAGE_PATIENT_NRIC_NOT_FOUND);
        }

        Appointment mockAppointmentToMatch = new Appointment(targetNric, targetDate, targetTimePeriod,
                new AppointmentType("Anything"), new Note("Anything"), new Mark(false));
        if (!model.hasAppointment(mockAppointmentToMatch)) {
            throw new CommandException(Messages.MESSAGE_APPOINTMENT_NOT_FOUND);
        }

        this.apptToEdit = model.getMatchingAppointment(targetNric, targetDate, targetTimePeriod);

        Appointment editedAppt = createEditedAppointment(apptToEdit, editApptDescriptor);

        // Must check for overlapping appointments of new appt besides current appt
        if (model.hasOverlappingAppointmentExcluding(apptToEdit, editedAppt)) {
            throw new CommandException(MESSAGE_EDIT_OVERLAPPING_APPOINTMENT_FAILURE);
        }

        model.setAppointment(apptToEdit, editedAppt);
        model.updateFilteredAppointmentList(PREDICATE_SHOW_ALL_APPOINTMENT_VIEWS);
        return new CommandResult(String.format(MESSAGE_EDIT_APPT_SUCCESS, Messages.format(editedAppt)));
    }

    /**
     * Creates and returns a {@code Appointment} with the details of {@code apptToEdit}
     * edited with {@code editAppointmentDescriptor}.
     */
    private static Appointment createEditedAppointment(Appointment apptToEdit, EditApptDescriptor editApptDescriptor) {
        assert apptToEdit != null;

        Date updatedDate = editApptDescriptor.getDate().orElse(apptToEdit.getDate());
        TimePeriod updatedTimePeriod = editApptDescriptor.getTimePeriod().orElse(apptToEdit.getTimePeriod());
        AppointmentType updatedAppointmentType = editApptDescriptor.getAppointmentType()
                .orElse(apptToEdit.getAppointmentType());
        Note updatedNote = editApptDescriptor.getNote().orElse(apptToEdit.getNote());

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
                && editApptDescriptor.equals(otherEditApptCommand.editApptDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("nric", targetNric)
                .add("date", targetDate)
                .add("timePeriod", targetTimePeriod)
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
        private TimePeriod timePeriod;
        private AppointmentType appointmentType;
        private Note note;

        public EditApptDescriptor() {}

        /**
         * Copy constructor.
         */
        public EditApptDescriptor(EditApptDescriptor toCopy) {
            setDate(toCopy.date);
            setTimePeriod(toCopy.timePeriod);
            setAppointmentType(toCopy.appointmentType);
            setNote(toCopy.note);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(date, timePeriod, appointmentType, note);
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public Optional<Date> getDate() {
            return Optional.ofNullable(date);
        }

        public void setTimePeriod(TimePeriod timePeriod) {
            this.timePeriod = timePeriod;
        }

        public Optional<TimePeriod> getTimePeriod() {
            return Optional.ofNullable(timePeriod);
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
                    && Objects.equals(timePeriod, otherEditApptDescriptor.timePeriod)
                    && Objects.equals(appointmentType, otherEditApptDescriptor.appointmentType)
                    && Objects.equals(note, otherEditApptDescriptor.note);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("date", date)
                    .add("timePeriod", timePeriod)
                    .add("appointmentType", appointmentType)
                    .add("note", note)
                    .toString();
        }
    }
}
