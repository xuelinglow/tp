package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NRIC_DESC_AMY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NRIC;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.FindPatientCommand;
import seedu.address.model.patient.NameContainsKeywordsPredicate;
import seedu.address.model.patient.NricContainsMatchPredicate;

public class FindPatientCommandParserTest {

    private FindPatientCommandParser parser = new FindPatientCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindPatientCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " n/  ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindPatientCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " i/  ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindPatientCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindPatientCommand() {
        // no leading and trailing whitespaces for name
        FindPatientCommand expectedFindPatientCommand =
                new FindPatientCommand(new NameContainsKeywordsPredicate(Arrays.asList("Ali", "Bob")));
        assertParseSuccess(parser, " n/ Ali Bob", expectedFindPatientCommand);

        // multiple whitespaces between keywords for name
        assertParseSuccess(parser, " \n n/ Ali \n \t Bob  \t", expectedFindPatientCommand);

        // leading and trailing whitespaces for nric
        expectedFindPatientCommand =
                new FindPatientCommand(new NricContainsMatchPredicate("T012"));
        assertParseSuccess(parser, " i/   T012      ", expectedFindPatientCommand);
    }

    @Test
    public void parse_multipleInputFields_failure() {
        String userInput = " i/ T012    n/ Alex";
        assertParseFailure(parser, userInput, FindPatientCommand.MESSAGE_MULTIPLE_FIELDS_FAILURE);

        userInput = "  n/ Alex  i/ T012  ";
        assertParseFailure(parser, userInput, FindPatientCommand.MESSAGE_MULTIPLE_FIELDS_FAILURE);
    }

    @Test
    public void parse_multipleRepeatedValues_failure() {
        // multiple NRIC searches
        assertParseFailure(parser, NRIC_DESC_AMY + NRIC_DESC_AMY,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NRIC));
        // multiple Name searches
        assertParseFailure(parser, NAME_DESC_AMY + NAME_DESC_AMY,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));
    }

    @Test
    public void parse_multipleNricKeywords_failure() {
        String userInput = " i/ T012   T0124";
        assertParseFailure(parser, userInput, FindPatientCommand.MESSAGE_NRIC_EXCEED_ONE_KEYWORD_FAILURE);
    }

}
