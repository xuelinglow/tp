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

import java.util.stream.Stream;

import seedu.address.logic.commands.DeletePatientCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.patient.Nric;

/**
 * Parses input arguments and creates a new DeletePatientCommand object
 */
public class DeletePatientCommandParser implements Parser<DeletePatientCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeletePatientCommand
     * and returns a DeletePatientCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeletePatientCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_NRIC, PREFIX_DATE, PREFIX_DOB, PREFIX_PHONE,
                        PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_TAG, PREFIX_START_TIME, PREFIX_END_TIME, PREFIX_NOTE);

        // Deals with prefixes that are not supposed to be present
        if (arePrefixesPresent(argMultimap, PREFIX_START_TIME) || (arePrefixesPresent(argMultimap, PREFIX_END_TIME))
                || (arePrefixesPresent(argMultimap, PREFIX_DOB)) || (arePrefixesPresent(argMultimap, PREFIX_PHONE))
                || (arePrefixesPresent(argMultimap, PREFIX_EMAIL)) || (arePrefixesPresent(argMultimap, PREFIX_ADDRESS))
                || (arePrefixesPresent(argMultimap, PREFIX_TAG)) || (arePrefixesPresent(argMultimap, PREFIX_DATE))
                || (arePrefixesPresent(argMultimap, PREFIX_NOTE)) || (arePrefixesPresent(argMultimap, PREFIX_NAME))) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeletePatientCommand.MESSAGE_USAGE));
        }

        Nric nric = ParserUtil.parseNric(args);
        return new DeletePatientCommand(nric);
    }

    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
