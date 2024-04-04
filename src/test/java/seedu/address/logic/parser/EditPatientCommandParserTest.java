package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NEW_ADDRESS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NEW_DOB_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NEW_EMAIL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NEW_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NEW_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NEW_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NRIC_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NEW_ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NEW_ADDRESS_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.NEW_DOB_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NEW_EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NEW_EMAIL_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.NEW_NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NEW_PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NEW_PHONE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.NEW_TAG_DESC_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.NEW_TAG_DESC_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.NRIC_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NRIC_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DOB_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NRIC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NRIC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW_TAG;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditPatientCommand;
import seedu.address.logic.commands.EditPatientCommand.EditPatientDescriptor;
import seedu.address.model.patient.Address;
import seedu.address.model.patient.DateOfBirth;
import seedu.address.model.patient.Email;
import seedu.address.model.patient.Name;
import seedu.address.model.patient.Nric;
import seedu.address.model.patient.Phone;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.EditPatientDescriptorBuilder;

public class EditPatientCommandParserTest {

    private static final String TAG_EMPTY = " " + PREFIX_NEW_TAG;

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditPatientCommand.MESSAGE_USAGE);

    private EditPatientCommandParser parser = new EditPatientCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no nric specified
        assertParseFailure(parser, VALID_NAME_AMY, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, NRIC_DESC_AMY, EditPatientCommand.MESSAGE_EDIT_PATIENT_NO_FIELDS_FAILURE);

        // no nric and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "some random string" + NRIC_DESC_AMY, MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, INVALID_NRIC_DESC + NEW_NAME_DESC_AMY, Nric.MESSAGE_CONSTRAINTS); // invalid name
        assertParseFailure(parser, NRIC_DESC_AMY + INVALID_NEW_NAME_DESC, Name.MESSAGE_CONSTRAINTS); // invalid name
        assertParseFailure(parser, NRIC_DESC_AMY + INVALID_NEW_DOB_DESC,
                DateOfBirth.MESSAGE_CONSTRAINTS); // invalid dob
        assertParseFailure(parser, NRIC_DESC_AMY + INVALID_NEW_PHONE_DESC, Phone.MESSAGE_CONSTRAINTS); // invalid phone
        assertParseFailure(parser, NRIC_DESC_AMY + INVALID_NEW_EMAIL_DESC, Email.MESSAGE_CONSTRAINTS); // invalid email
        assertParseFailure(parser, NRIC_DESC_AMY
                + INVALID_NEW_ADDRESS_DESC, Address.MESSAGE_CONSTRAINTS); // invalid address
        assertParseFailure(parser, NRIC_DESC_AMY + INVALID_NEW_TAG_DESC, Tag.MESSAGE_CONSTRAINTS); // invalid tag

        // invalid phone followed by valid email
        assertParseFailure(parser, NRIC_DESC_AMY + INVALID_NEW_PHONE_DESC + NEW_EMAIL_DESC_AMY,
                Phone.MESSAGE_CONSTRAINTS);

        // while parsing {@code PREFIX_NEW_TAG} alone will reset the tags of the {@code Patient} being edited,
        // parsing it together with a valid tag results in error
        assertParseFailure(parser, NRIC_DESC_AMY + NEW_TAG_DESC_FRIEND
                + NEW_TAG_DESC_HUSBAND + TAG_EMPTY, Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, NRIC_DESC_AMY + NEW_TAG_DESC_FRIEND
                + TAG_EMPTY + NEW_TAG_DESC_HUSBAND, Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, NRIC_DESC_AMY + TAG_EMPTY + NEW_TAG_DESC_FRIEND
                + NEW_TAG_DESC_HUSBAND, Tag.MESSAGE_CONSTRAINTS);

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser, NRIC_DESC_AMY + INVALID_NEW_NAME_DESC + INVALID_NEW_EMAIL_DESC
                        + VALID_ADDRESS_AMY + VALID_PHONE_AMY, Name.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        String userInput = NRIC_DESC_AMY + NEW_PHONE_DESC_BOB + NEW_TAG_DESC_HUSBAND
                + NEW_DOB_DESC_AMY + NEW_EMAIL_DESC_AMY + NEW_ADDRESS_DESC_AMY
                + NEW_NAME_DESC_AMY + NEW_TAG_DESC_FRIEND;

        EditPatientDescriptor descriptor = new EditPatientDescriptorBuilder().withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY)
                .withDateOfBirth(VALID_DOB_AMY).withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND).build();
        EditPatientCommand expectedCommand = new EditPatientCommand(new Nric(VALID_NRIC_AMY), descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        String userInput = NRIC_DESC_AMY + NEW_PHONE_DESC_BOB + NEW_EMAIL_DESC_AMY;

        EditPatientCommand.EditPatientDescriptor descriptor =
                new EditPatientDescriptorBuilder().withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_AMY).build();
        EditPatientCommand expectedCommand = new EditPatientCommand(new Nric(VALID_NRIC_AMY), descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        Nric targetNric = new Nric(VALID_NRIC_BOB);
        // name
        String userInput = NRIC_DESC_BOB + NEW_NAME_DESC_AMY;
        EditPatientDescriptor descriptor = new EditPatientDescriptorBuilder().withName(VALID_NAME_AMY).build();
        EditPatientCommand expectedCommand = new EditPatientCommand(targetNric, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // dob
        userInput = NRIC_DESC_BOB + NEW_DOB_DESC_AMY;
        descriptor = new EditPatientDescriptorBuilder().withDateOfBirth(VALID_DOB_AMY).build();
        expectedCommand = new EditPatientCommand(targetNric, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // phone
        userInput = NRIC_DESC_BOB + NEW_PHONE_DESC_AMY;
        descriptor = new EditPatientDescriptorBuilder().withPhone(VALID_PHONE_AMY).build();
        expectedCommand = new EditPatientCommand(targetNric, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // email
        userInput = NRIC_DESC_BOB + NEW_EMAIL_DESC_AMY;
        descriptor = new EditPatientDescriptorBuilder().withEmail(VALID_EMAIL_AMY).build();
        expectedCommand = new EditPatientCommand(targetNric, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // address
        userInput = NRIC_DESC_BOB + NEW_ADDRESS_DESC_AMY;
        descriptor = new EditPatientDescriptorBuilder().withAddress(VALID_ADDRESS_AMY).build();
        expectedCommand = new EditPatientCommand(targetNric, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // tags
        userInput = NRIC_DESC_BOB + NEW_TAG_DESC_FRIEND;
        descriptor = new EditPatientDescriptorBuilder().withTags(VALID_TAG_FRIEND).build();
        expectedCommand = new EditPatientCommand(targetNric, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_failure() {
        // More extensive testing of duplicate parameter detections is done in
        // AddCommandParserTest#parse_repeatedNonTagValue_failure()

        // invalid followed by valid
        String userInput = NRIC_DESC_BOB + INVALID_NEW_PHONE_DESC + NEW_PHONE_DESC_BOB;

        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NEW_PHONE));

        // valid followed by invalid
        userInput = NRIC_DESC_BOB + NEW_PHONE_DESC_BOB + INVALID_NEW_PHONE_DESC;

        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NEW_PHONE));

        // multiple valid fields repeated
        userInput = NRIC_DESC_BOB + NEW_PHONE_DESC_AMY + NEW_ADDRESS_DESC_AMY + NEW_EMAIL_DESC_AMY
                + NEW_TAG_DESC_FRIEND + NEW_PHONE_DESC_AMY + NEW_ADDRESS_DESC_AMY + NEW_EMAIL_DESC_AMY
                + NEW_TAG_DESC_FRIEND + NEW_PHONE_DESC_BOB + NEW_ADDRESS_DESC_BOB
                + NEW_EMAIL_DESC_BOB + NEW_TAG_DESC_HUSBAND;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NEW_PHONE, PREFIX_NEW_EMAIL, PREFIX_NEW_ADDRESS));

        // multiple invalid values
        userInput = NRIC_DESC_BOB + INVALID_NEW_PHONE_DESC + INVALID_NEW_ADDRESS_DESC + INVALID_NEW_EMAIL_DESC
                + INVALID_NEW_PHONE_DESC + INVALID_NEW_ADDRESS_DESC + INVALID_NEW_EMAIL_DESC;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NEW_PHONE, PREFIX_NEW_EMAIL, PREFIX_NEW_ADDRESS));
    }

    @Test
    public void parse_resetTags_success() {
        Nric targetNric = new Nric(VALID_NRIC_BOB);
        String userInput = NRIC_DESC_BOB + TAG_EMPTY;

        EditPatientDescriptor descriptor = new EditPatientDescriptorBuilder().withTags().build();
        EditPatientCommand expectedCommand = new EditPatientCommand(targetNric, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
