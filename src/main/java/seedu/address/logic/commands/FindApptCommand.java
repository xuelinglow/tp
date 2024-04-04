package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NRIC;
import static seedu.address.logic.parser.CliSyntax.PREFIX_START_TIME;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.appointment.AppointmentContainsKeywordsPredicate;
import seedu.address.ui.ViewMode;

/**
 * Finds and lists all appointments in address book whose details fit any of the argument keywords.
 */
public class FindApptCommand extends Command {

    public static final String COMMAND_WORD = "findAppt";

    public static final String COMMAND_WORD_ALT = "fa";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Finds the details of the appointment(s) with the given NRIC, on the given date, \n"
            + "starting on or after the given start time or any combination of these three options.\n"
            + "Parameters (at least one): "
            + "[" + PREFIX_NRIC + " NRIC] "
            + "[" + PREFIX_DATE + "DATE] "
            + "[" + PREFIX_START_TIME + "START_TIME]\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NRIC + " T0123456A "
            + PREFIX_DATE + " 2024-02-03 "
            + PREFIX_START_TIME + " 11:00";

    private final AppointmentContainsKeywordsPredicate predicate;


    /**
     * Creates a FindApptCommand to find and list the appointments with the
     * that fit the given {@code predicate}
     *
     * @param predicate for AppointmentView for conditions to match
     */
    public FindApptCommand(AppointmentContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredAppointmentViewList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_APPOINTMENTS_LISTED_OVERVIEW,
                        model.getFilteredAppointmentViewList().size()), ViewMode.OVERALL);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindApptCommand)) {
            return false;
        }

        FindApptCommand otherFindCommand = (FindApptCommand) other;
        return predicate.equals(otherFindCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
