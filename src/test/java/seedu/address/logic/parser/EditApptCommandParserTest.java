package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.DATE_DESC_APPOINTMENT_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_APPT_AMY;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DATE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NEW_APPOINTMENT_TYPE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NEW_DATE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NEW_END_TIME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NEW_START_TIME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NRIC_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_START_TIME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NEW_DATE_DESC_APPOINTMENT_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NEW_DATE_DESC_APPOINTMENT_BOB;
import static seedu.address.logic.commands.CommandTestUtil.NEW_END_TIME_DESC_APPOINTMENT_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NEW_END_TIME_DESC_APPOINTMENT_BOB;
import static seedu.address.logic.commands.CommandTestUtil.NEW_NOTE_DESC_APPOINTMENT_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NEW_NOTE_DESC_APPOINTMENT_BOB;
import static seedu.address.logic.commands.CommandTestUtil.NEW_START_TIME_DESC_APPOINTMENT_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NEW_START_TIME_DESC_APPOINTMENT_BOB;
import static seedu.address.logic.commands.CommandTestUtil.NEW_TYPE_DESC_APPOINTMENT_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NEW_TYPE_DESC_APPOINTMENT_BOB;
import static seedu.address.logic.commands.CommandTestUtil.NRIC_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.START_TIME_DESC_APPOINTMENT_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_APPOINTMENT_DATE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_APPOINTMENT_DATE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_APPOINTMENT_END_TIME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_APPOINTMENT_END_TIME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_APPOINTMENT_NOTE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_APPOINTMENT_NOTE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_APPOINTMENT_START_TIME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_APPOINTMENT_START_TIME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_APPOINTMENT_TYPE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NRIC_AMY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW_END_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW_START_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW_TAG;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.date.Date;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditApptCommand;
import seedu.address.logic.commands.EditApptCommand.EditApptDescriptor;
import seedu.address.model.appointment.AppointmentType;
import seedu.address.model.appointment.Time;
import seedu.address.model.patient.Nric;
import seedu.address.testutil.EditApptDescriptorBuilder;

public class EditApptCommandParserTest {
    private static final String MESSAGE_INVALID_FORMAT = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
            EditApptCommand.MESSAGE_USAGE);
    private EditApptCommandParser parser = new EditApptCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no nric specified
        assertParseFailure(parser, DATE_DESC_APPOINTMENT_AMY
                + START_TIME_DESC_APPOINTMENT_AMY
                + DESC_APPT_AMY, MESSAGE_INVALID_FORMAT);

        // no date specified
        assertParseFailure(parser, NRIC_DESC_AMY
                + START_TIME_DESC_APPOINTMENT_AMY
                + DESC_APPT_AMY, MESSAGE_INVALID_FORMAT);

        // no startTime specified
        assertParseFailure(parser, NRIC_DESC_AMY
                + DATE_DESC_APPOINTMENT_AMY
                + DESC_APPT_AMY, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(
                parser,
                NRIC_DESC_AMY
                + DATE_DESC_APPOINTMENT_AMY
                + START_TIME_DESC_APPOINTMENT_AMY,
                EditApptCommand.MESSAGE_EDIT_APPT_NO_FIELDS_FAILURE);

        // no nric, date, startTime and fields specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "some random string"
                + NRIC_DESC_AMY
                + DATE_DESC_APPOINTMENT_AMY
                + START_TIME_DESC_APPOINTMENT_AMY
                + DESC_APPT_AMY, MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {

        String validTargetAppt = NRIC_DESC_AMY
                + DATE_DESC_APPOINTMENT_AMY
                + START_TIME_DESC_APPOINTMENT_AMY;

        assertParseFailure(parser, INVALID_NRIC_DESC
                + DATE_DESC_APPOINTMENT_AMY
                + START_TIME_DESC_APPOINTMENT_AMY
                + DESC_APPT_AMY, Nric.MESSAGE_CONSTRAINTS); // invalid nric
        assertParseFailure(parser, NRIC_DESC_AMY
                + INVALID_DATE_DESC
                + START_TIME_DESC_APPOINTMENT_AMY
                + DESC_APPT_AMY, Date.MESSAGE_CONSTRAINTS); // invalid date
        assertParseFailure(parser, NRIC_DESC_AMY
                + DATE_DESC_APPOINTMENT_AMY
                + INVALID_START_TIME_DESC
                + DESC_APPT_AMY, Time.MESSAGE_CONSTRAINTS); // invalid startTime

        //More invalid tests here for the descriptors
        assertParseFailure(parser, validTargetAppt
                + INVALID_NEW_DATE_DESC , Date.MESSAGE_CONSTRAINTS); // invalid new date
        assertParseFailure(parser, validTargetAppt
                + INVALID_NEW_START_TIME_DESC , Time.MESSAGE_CONSTRAINTS); // invalid new startTime
        assertParseFailure(parser, validTargetAppt
                + INVALID_NEW_END_TIME_DESC , Time.MESSAGE_CONSTRAINTS); // invalid new endTime
        assertParseFailure(parser, validTargetAppt
                + INVALID_NEW_APPOINTMENT_TYPE_DESC , AppointmentType.MESSAGE_CONSTRAINTS); // invalid new apptType

        //all notes are valid so no test for that

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser, NRIC_DESC_AMY
                + INVALID_DATE_DESC
                + INVALID_START_TIME_DESC
                + DESC_APPT_AMY, Date.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Nric targetNric = new Nric(VALID_NRIC_AMY);
        Date targetDate = new Date(VALID_APPOINTMENT_DATE_AMY);
        Time targetStartTime = new Time(VALID_APPOINTMENT_START_TIME_AMY);

        String validTargetAppt = NRIC_DESC_AMY
                + DATE_DESC_APPOINTMENT_AMY
                + START_TIME_DESC_APPOINTMENT_AMY;

        String userInput = validTargetAppt
                + NEW_DATE_DESC_APPOINTMENT_BOB
                + NEW_START_TIME_DESC_APPOINTMENT_BOB
                + NEW_END_TIME_DESC_APPOINTMENT_BOB
                + NEW_TYPE_DESC_APPOINTMENT_BOB
                + NEW_NOTE_DESC_APPOINTMENT_BOB;

        EditApptDescriptor descriptor = new EditApptDescriptorBuilder().withDate(VALID_APPOINTMENT_DATE_BOB)
                .withStartTime(VALID_APPOINTMENT_START_TIME_BOB)
                .withEndTime(VALID_APPOINTMENT_END_TIME_BOB)
                .withAppointmentType(VALID_APPOINTMENT_TYPE_BOB).withNote(VALID_APPOINTMENT_NOTE_BOB)
                .build();
        EditApptCommand expectedCommand = new EditApptCommand(targetNric, targetDate, targetStartTime, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        Nric targetNric = new Nric(VALID_NRIC_AMY);
        Date targetDate = new Date(VALID_APPOINTMENT_DATE_AMY);
        Time targetStartTime = new Time(VALID_APPOINTMENT_START_TIME_AMY);

        String validTargetAppt = NRIC_DESC_AMY
                + DATE_DESC_APPOINTMENT_AMY
                + START_TIME_DESC_APPOINTMENT_AMY;

        String userInput = validTargetAppt + NEW_TYPE_DESC_APPOINTMENT_BOB + NEW_NOTE_DESC_APPOINTMENT_AMY;

        EditApptCommand.EditApptDescriptor descriptor =
                new EditApptDescriptorBuilder().withAppointmentType(VALID_APPOINTMENT_TYPE_BOB)
                        .withNote(VALID_APPOINTMENT_NOTE_AMY).build();
        EditApptCommand expectedCommand = new EditApptCommand(targetNric, targetDate, targetStartTime, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        Nric targetNric = new Nric(VALID_NRIC_AMY);
        Date targetDate = new Date(VALID_APPOINTMENT_DATE_AMY);
        Time targetStartTime = new Time(VALID_APPOINTMENT_START_TIME_AMY);

        String validTargetAppt = NRIC_DESC_AMY
                + DATE_DESC_APPOINTMENT_AMY
                + START_TIME_DESC_APPOINTMENT_AMY;

        String userInput = validTargetAppt + NEW_NOTE_DESC_APPOINTMENT_BOB;
        EditApptDescriptor descriptor = new EditApptDescriptorBuilder().withNote(VALID_APPOINTMENT_NOTE_BOB).build();
        EditApptCommand expectedCommand = new EditApptCommand(targetNric, targetDate, targetStartTime, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // date
        userInput = validTargetAppt + NEW_DATE_DESC_APPOINTMENT_BOB;
        descriptor = new EditApptDescriptorBuilder().withDate(VALID_APPOINTMENT_DATE_BOB).build();
        expectedCommand = new EditApptCommand(targetNric, targetDate, targetStartTime, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // startTime
        userInput = validTargetAppt + NEW_START_TIME_DESC_APPOINTMENT_AMY;
        descriptor = new EditApptDescriptorBuilder().withStartTime(VALID_APPOINTMENT_START_TIME_AMY).build();
        expectedCommand = new EditApptCommand(targetNric, targetDate, targetStartTime, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        //endTime
        userInput = validTargetAppt + NEW_END_TIME_DESC_APPOINTMENT_AMY;
        descriptor = new EditApptDescriptorBuilder().withEndTime(VALID_APPOINTMENT_END_TIME_AMY).build();
        expectedCommand = new EditApptCommand(targetNric, targetDate, targetStartTime, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        //appointmentType
        userInput = validTargetAppt + NEW_TYPE_DESC_APPOINTMENT_BOB;
        descriptor = new EditApptDescriptorBuilder().withAppointmentType(VALID_APPOINTMENT_TYPE_BOB).build();
        expectedCommand = new EditApptCommand(targetNric, targetDate, targetStartTime, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        //note
        userInput = validTargetAppt + NEW_NOTE_DESC_APPOINTMENT_BOB;
        descriptor = new EditApptDescriptorBuilder().withNote(VALID_APPOINTMENT_NOTE_BOB).build();
        expectedCommand = new EditApptCommand(targetNric, targetDate, targetStartTime, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_failure() {
        // More extensive testing of duplicate parameter detections is done in
        // AddCommandParserTest#parse_repeatedNonTagValue_failure()

        String validTargetAppt = NRIC_DESC_AMY
                + DATE_DESC_APPOINTMENT_AMY
                + START_TIME_DESC_APPOINTMENT_AMY;

        Nric targetNric = new Nric(VALID_NRIC_AMY);
        Date targetDate = new Date(VALID_APPOINTMENT_DATE_AMY);
        Time targetStartTime = new Time(VALID_APPOINTMENT_START_TIME_AMY);

        // invalid followed by valid
        String userInput = validTargetAppt + INVALID_NEW_DATE_DESC + NEW_DATE_DESC_APPOINTMENT_AMY;
        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NEW_DATE));

        // valid followed by invalid
        userInput = validTargetAppt + NEW_DATE_DESC_APPOINTMENT_AMY + INVALID_NEW_DATE_DESC;
        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NEW_DATE));

        // multiple valid fields repeated
        userInput = validTargetAppt + NEW_DATE_DESC_APPOINTMENT_AMY + NEW_START_TIME_DESC_APPOINTMENT_AMY
                + NEW_END_TIME_DESC_APPOINTMENT_AMY
                + NEW_TYPE_DESC_APPOINTMENT_AMY
                + NEW_DATE_DESC_APPOINTMENT_AMY + NEW_START_TIME_DESC_APPOINTMENT_AMY
                + NEW_END_TIME_DESC_APPOINTMENT_AMY + NEW_TYPE_DESC_APPOINTMENT_AMY;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NEW_DATE,
                        PREFIX_NEW_START_TIME, PREFIX_NEW_END_TIME, PREFIX_NEW_TAG));

        // multiple invalid values
        userInput = validTargetAppt + INVALID_NEW_DATE_DESC
                + INVALID_NEW_END_TIME_DESC + INVALID_NEW_APPOINTMENT_TYPE_DESC
                + INVALID_NEW_DATE_DESC + INVALID_NEW_END_TIME_DESC + INVALID_NEW_APPOINTMENT_TYPE_DESC;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NEW_DATE, PREFIX_NEW_END_TIME, PREFIX_NEW_TAG));
    }
}
