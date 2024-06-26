package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NRIC;

import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.patient.Patient;
import seedu.address.ui.ViewMode;

/**
 * Finds and lists all patients in address book whose name OR NRIC contains the argument keywords.
 * Keyword matching is case-insensitive.
 */
public class FindPatientCommand extends Command {

    public static final String COMMAND_WORD = "findPatient";

    public static final String COMMAND_WORD_ALT = "fp";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Finds all patient(s) by either name keyword(s) or NRIC keyword, not both. \n"
            + "Specified keywords are case-insensitive and a match is achieved "
            + "when the start of any word in the name or NRIC matches the keyword. \n"
            + "Parameters: "
            + PREFIX_NAME + "NAME_KEYWORD [MORE_NAME_KEYWORDS] OR "
            + PREFIX_NRIC + "NRIC_KEYWORD \n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_NAME + "Alex Ben"
            + " OR " + COMMAND_WORD + " " + PREFIX_NRIC + "T0123456A";

    public static final String MESSAGE_MULTIPLE_FIELDS_FAILURE = "Find by either NRIC or name, not both!";

    public static final String MESSAGE_NRIC_EXCEED_ONE_KEYWORD_FAILURE =
            "You have provided more than one word of NRIC keywords to match. \n"
            + "findPatient supports only one keyword at a time for NRIC search.";

    private final Predicate<Patient> predicate;

    public FindPatientCommand(Predicate<Patient> predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPatientList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_PATIENTS_LISTED_OVERVIEW,
                        model.getFilteredPatientList().size()), ViewMode.OVERALL);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindPatientCommand)) {
            return false;
        }

        FindPatientCommand otherFindPatientCommand = (FindPatientCommand) other;
        return predicate.equals(otherFindPatientCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
