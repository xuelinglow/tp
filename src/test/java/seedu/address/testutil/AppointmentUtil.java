package seedu.address.testutil;

import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_END_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW_END_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW_START_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NRIC;
import static seedu.address.logic.parser.CliSyntax.PREFIX_START_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.logic.commands.AddApptCommand;
import seedu.address.logic.commands.DeleteApptCommand;
import seedu.address.logic.commands.EditApptCommand;
import seedu.address.model.appointment.Appointment;

/**
 * A utility class for Appointment..
 */
public class AppointmentUtil {

    /**
     * Returns an addApp command string for adding the {@code appointment}.
     */
    public static String getAddApptCommand(Appointment appointment) {
        return AddApptCommand.COMMAND_WORD + " " + getAppointmentDetails(appointment);
    }

    /**
     * Returns a deleteApp command string for deleting the {@code appointment}.
     */
    public static String getDeleteApptCommand(Appointment appointment) {
        return DeleteApptCommand.COMMAND_WORD + " " + getAppointmentUniqueDetails(appointment);
    }

    /**
     * Returns the part of command string for the given {@code appointment}'s details.
     */
    public static String getAppointmentDetails(Appointment appointment) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NRIC + appointment.getNric().value + " ");
        sb.append(PREFIX_DATE + appointment.getDate().toString() + " ");
        sb.append(PREFIX_START_TIME + appointment.getStartTime().toString() + " ");
        sb.append(PREFIX_END_TIME + appointment.getEndTime().toString() + " ");
        sb.append(PREFIX_TAG + appointment.getAppointmentType().typeName + " ");
        sb.append(PREFIX_NOTE + appointment.getNote().note + " ");
        return sb.toString();
    }

    /**
     * Returns the part of command string that uniquely identifies the given {@code appointment}.
     */
    public static String getAppointmentUniqueDetails(Appointment appointment) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NRIC + appointment.getNric().value + " ");
        sb.append(PREFIX_DATE + appointment.getDate().toString() + " ");
        sb.append(PREFIX_START_TIME + appointment.getStartTime().toString() + " ");
        sb.append(PREFIX_END_TIME + appointment.getEndTime().toString() + " ");
        return sb.toString();
    }

    /**
     * Returns the part of command string for the given {@code EditApptDescriptor}'s details.
     */
    public static String getEditApptDescriptorDetails(EditApptCommand.EditApptDescriptor descriptor) {
        StringBuilder sb = new StringBuilder();
        descriptor.getDate().ifPresent(date -> sb.append(PREFIX_NEW_DATE).append(date.value).append(" "));
        descriptor.getTimePeriod().ifPresent(timePeriod -> {
            sb.append(PREFIX_NEW_START_TIME).append(timePeriod.getStartTime().value).append(" ");
            sb.append(PREFIX_NEW_END_TIME).append(timePeriod.getEndTime().value).append(" ");
        });
        descriptor.getAppointmentType().ifPresent(appointmentType -> sb.append(PREFIX_NEW_TAG)
                .append(appointmentType.typeName).append(" "));
        descriptor.getNote().ifPresent(note -> sb.append(PREFIX_NEW_NOTE).append(note).append(" "));
        return sb.toString();
    }
}
