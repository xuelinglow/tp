package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DOB;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_END_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NRIC;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_START_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Arrays;

import seedu.address.logic.commands.FindPatientCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.patient.NameContainsKeywordsPredicate;
import seedu.address.model.patient.NricContainsMatchPredicate;

/**
 * Parses input arguments and creates a new FindPatientCommand object
 */
public class FindPatientCommandParser implements Parser<FindPatientCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindPatientCommand
     * and returns a FindPatientCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindPatientCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args);

        // Deals with prefixes that are not supposed to be present
        if (argMultimap.anyPrefixesPresent(PREFIX_START_TIME, PREFIX_END_TIME, PREFIX_DOB, PREFIX_PHONE, PREFIX_EMAIL,
                PREFIX_ADDRESS, PREFIX_TAG, PREFIX_DATE, PREFIX_NOTE) || argMultimap.anyNewPrefixesPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindPatientCommand.MESSAGE_USAGE));
        }

        boolean isFindPatientByNric = argMultimap.getValue(PREFIX_NRIC).isPresent();
        boolean isFindPatientByName = argMultimap.getValue(PREFIX_NAME).isPresent();

        if (isFindPatientByNric && isFindPatientByName) {
            throw new ParseException(FindPatientCommand.MESSAGE_MULTIPLE_FIELDS_FAILURE);
        }

        if (isFindPatientByName) {
            String trimmedNameArgs = argMultimap.getValue(PREFIX_NAME).get().trim();
            if (trimmedNameArgs.isEmpty()) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindPatientCommand.MESSAGE_USAGE));
            }

            String[] nameKeywords = trimmedNameArgs.split("\\s+");

            return new FindPatientCommand(new NameContainsKeywordsPredicate(Arrays.asList(nameKeywords)));
        } else if (isFindPatientByNric) {
            String trimmedNricArgs = argMultimap.getValue(PREFIX_NRIC).get().trim();
            if (trimmedNricArgs.isEmpty()) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindPatientCommand.MESSAGE_USAGE));
            }
            return new FindPatientCommand(new NricContainsMatchPredicate(trimmedNricArgs));
        }
        throw new ParseException(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindPatientCommand.MESSAGE_USAGE));
    }
}
