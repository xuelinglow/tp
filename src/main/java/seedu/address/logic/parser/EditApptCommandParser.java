package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_END_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW_END_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW_START_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NRIC;
import static seedu.address.logic.parser.CliSyntax.PREFIX_START_TIME;

import java.util.stream.Stream;

import seedu.address.commons.core.date.Date;
import seedu.address.logic.commands.EditApptCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.appointment.TimePeriod;
import seedu.address.model.patient.Nric;

/**
 * Parses input arguments and creates a new EditApptCommand object
 */
public class EditApptCommandParser implements Parser<EditApptCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditApptCommand
     * and returns an EditApptCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditApptCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args,
                PREFIX_NRIC, PREFIX_DATE, PREFIX_START_TIME, PREFIX_END_TIME,
                PREFIX_NEW_DATE, PREFIX_NEW_START_TIME, PREFIX_NEW_END_TIME,
                PREFIX_NEW_TAG, PREFIX_NEW_NOTE);

        if (!arePrefixesPresent(argMultimap, PREFIX_NRIC, PREFIX_DATE, PREFIX_START_TIME, PREFIX_END_TIME)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditApptCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NRIC, PREFIX_DATE, PREFIX_START_TIME,
                PREFIX_END_TIME, PREFIX_NEW_DATE, PREFIX_NEW_START_TIME, PREFIX_NEW_END_TIME,
                PREFIX_NEW_TAG, PREFIX_NEW_NOTE);

        Nric targetNric = ParserUtil.parseNric(argMultimap.getValue(PREFIX_NRIC).get());
        Date targetDate = ParserUtil.parseDate(argMultimap.getValue(PREFIX_DATE).get());
        TimePeriod targetTimePeriod = ParserUtil.parseTimePeriod(
                argMultimap.getValue(PREFIX_START_TIME).get(),
                argMultimap.getValue(PREFIX_END_TIME).get());



        EditApptCommand.EditApptDescriptor editApptDescriptor = new EditApptCommand.EditApptDescriptor();

        if (argMultimap.getValue(PREFIX_NEW_DATE).isPresent()) {
            editApptDescriptor.setDate(ParserUtil.parseDate(argMultimap.getValue(PREFIX_NEW_DATE).get()));
        }
        if (argMultimap.getValue(PREFIX_NEW_START_TIME).isPresent()
                && argMultimap.getValue(PREFIX_NEW_END_TIME).isPresent()) {
            //with both new times
            editApptDescriptor.setTimePeriod(ParserUtil.parseTimePeriod(
                    argMultimap.getValue(PREFIX_NEW_START_TIME).get(),
                    argMultimap.getValue(PREFIX_NEW_END_TIME).get()));
        }
        if (argMultimap.getValue(PREFIX_NEW_START_TIME).isPresent()
                && argMultimap.getValue(PREFIX_NEW_END_TIME).isEmpty()) {
            //with old end time
            editApptDescriptor.setTimePeriod(ParserUtil.parseTimePeriod(
                    argMultimap.getValue(PREFIX_NEW_START_TIME).get(),
                    argMultimap.getValue(PREFIX_END_TIME).get()));
        }
        if (argMultimap.getValue(PREFIX_NEW_START_TIME).isEmpty()
                && argMultimap.getValue(PREFIX_NEW_END_TIME).isPresent()) {
            //with old start time
            editApptDescriptor.setTimePeriod(ParserUtil.parseTimePeriod(
                    argMultimap.getValue(PREFIX_START_TIME).get(),
                    argMultimap.getValue(PREFIX_NEW_END_TIME).get()));
        }
        if (argMultimap.getValue(PREFIX_NEW_TAG).isPresent()) {
            editApptDescriptor.setAppointmentType(ParserUtil
                    .parseAppointmentType(argMultimap.getValue(PREFIX_NEW_TAG).get()));
        }
        if (argMultimap.getValue(PREFIX_NEW_NOTE).isPresent()) {
            editApptDescriptor.setNote(ParserUtil.parseNote(argMultimap.getValue(PREFIX_NEW_NOTE).get()));
        }


        if (!editApptDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditApptCommand.MESSAGE_EDIT_APPT_NO_FIELDS_FAILURE);
        }

        return new EditApptCommand(targetNric, targetDate, targetTimePeriod, editApptDescriptor);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
