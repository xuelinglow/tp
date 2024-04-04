package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW_DOB;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NRIC;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import seedu.address.logic.commands.EditPatientCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.patient.Nric;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new EditPatientCommand object
 */
public class EditPatientCommandParser implements Parser<EditPatientCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditPatientCommand
     * and returns an EditPatientCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditPatientCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NRIC, PREFIX_NEW_NAME, PREFIX_NEW_DOB,
                        PREFIX_NEW_PHONE, PREFIX_NEW_EMAIL, PREFIX_NEW_ADDRESS, PREFIX_NEW_TAG);

        if (!arePrefixesPresent(argMultimap, PREFIX_NRIC) || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditPatientCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NRIC, PREFIX_NEW_NAME, PREFIX_NEW_DOB,
                PREFIX_NEW_PHONE, PREFIX_NEW_EMAIL, PREFIX_NEW_ADDRESS);

        Nric targetNric = ParserUtil.parseNric(argMultimap.getValue(PREFIX_NRIC).get());
        EditPatientCommand.EditPatientDescriptor editPatientDescriptor = new EditPatientCommand.EditPatientDescriptor();

        if (argMultimap.getValue(PREFIX_NEW_NAME).isPresent()) {
            editPatientDescriptor.setName(ParserUtil.parseName(argMultimap.getValue(PREFIX_NEW_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_NEW_DOB).isPresent()) {
            editPatientDescriptor.setDateOfBirth(ParserUtil
                    .parseDateOfBirth(argMultimap.getValue(PREFIX_NEW_DOB).get()));
        }
        if (argMultimap.getValue(PREFIX_NEW_PHONE).isPresent()) {
            editPatientDescriptor.setPhone(ParserUtil.parsePhone(argMultimap.getValue(PREFIX_NEW_PHONE).get()));
        }
        if (argMultimap.getValue(PREFIX_NEW_EMAIL).isPresent()) {
            editPatientDescriptor.setEmail(ParserUtil.parseEmail(argMultimap.getValue(PREFIX_NEW_EMAIL).get()));
        }
        if (argMultimap.getValue(PREFIX_NEW_ADDRESS).isPresent()) {
            editPatientDescriptor.setAddress(ParserUtil.parseAddress(argMultimap.getValue(PREFIX_NEW_ADDRESS).get()));
        }
        parseTagsForEdit(argMultimap.getAllValues(PREFIX_NEW_TAG)).ifPresent(editPatientDescriptor::setTags);

        if (!editPatientDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditPatientCommand.MESSAGE_EDIT_PATIENT_NO_FIELDS_FAILURE);
        }

        return new EditPatientCommand(targetNric, editPatientDescriptor);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws ParseException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(tagSet));
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
