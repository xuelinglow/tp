package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NRIC;
import static seedu.address.logic.parser.CliSyntax.PREFIX_START_TIME;

import seedu.address.commons.core.date.Date;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.appointment.Time;
import seedu.address.model.patient.Nric;

/**
 * Deletes an appointment identified using its NRIC, date, start time and end time.
 */
public class DeleteApptCommand extends Command {

    public static final String COMMAND_WORD = "deleteAppt";

    public static final String COMMAND_WORD_ALT = "da";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the appointment, identified by the given NRIC, date, and start time, from CLInic.\n"
            + "Parameters: "
            + PREFIX_NRIC + "NRIC "
            + PREFIX_DATE + "DATE "
            + PREFIX_START_TIME + "START_TIME \n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NRIC + "T0123456A "
            + PREFIX_DATE + "2024-02-20 "
            + PREFIX_START_TIME + "11:00 ";

    public static final String MESSAGE_DELETE_APPOINTMENT_SUCCESS = "Deleted Appointment: %1$s";
    private final Nric targetNric;
    private final Date targetDate;
    private final Time targetStartTime;

    /**
     * Creates a DeleteApptCommand to delete the appointment with the
     * specified {@code Nric, Date, StartTime}
     *
     * @param targetNric nric of the Patient matching the existing Appointment to be deleted
     * @param targetDate date of the existing Appointment to be deleted
     * @param targetStartTime startTime of the existing Appointment to be deleted
     */
    public DeleteApptCommand(Nric targetNric, Date targetDate, Time targetStartTime) {
        this.targetNric = targetNric;
        this.targetDate = targetDate;
        this.targetStartTime = targetStartTime;
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

        Appointment apptToDelete = model.getMatchingAppointment(targetNric, targetDate, targetStartTime);
        model.deleteAppointment(apptToDelete);
        return new CommandResult(String.format(MESSAGE_DELETE_APPOINTMENT_SUCCESS, Messages.format(apptToDelete)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteApptCommand)) {
            return false;
        }

        DeleteApptCommand otherDeleteApptCommand = (DeleteApptCommand) other;

        return targetNric.equals(otherDeleteApptCommand.targetNric)
                && targetDate.equals(otherDeleteApptCommand.targetDate)
                && targetStartTime.equals(otherDeleteApptCommand.targetStartTime);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("nric", targetNric)
                .add("date", targetDate)
                .add("startTime", targetStartTime)
                .toString();
    }
}
