package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NRIC;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddApptCommand;
import seedu.address.logic.commands.AddPatientCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.DeleteApptCommand;
import seedu.address.logic.commands.DeletePatientCommand;
import seedu.address.logic.commands.EditApptCommand;
import seedu.address.logic.commands.EditPatientCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindPatientCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.SwitchViewCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.patient.NameContainsKeywordsPredicate;
import seedu.address.model.patient.NricContainsMatchPredicate;
import seedu.address.model.patient.Patient;
import seedu.address.testutil.AppointmentBuilder;
import seedu.address.testutil.AppointmentUtil;
import seedu.address.testutil.EditApptDescriptorBuilder;
import seedu.address.testutil.EditPatientDescriptorBuilder;
import seedu.address.testutil.PatientBuilder;
import seedu.address.testutil.PatientUtil;

public class AddressBookParserTest {

    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommand_addPatient() throws Exception {
        Patient patient = new PatientBuilder().build();
        AddPatientCommand command = (AddPatientCommand) parser
                .parseCommand(PatientUtil.getAddPatientCommand(patient));
        assertEquals(new AddPatientCommand(patient), command);
    }

    @Test
    public void parseCommand_deletePatient() throws Exception {
        Patient patient = new PatientBuilder().build();
        DeletePatientCommand command = (DeletePatientCommand) parser
                .parseCommand(PatientUtil.getDeletePatientCommand(patient));

        assertEquals(new DeletePatientCommand(patient.getNric()), command);
    }

    @Test
    public void parseCommand_editPatient() throws Exception {
        Patient patient = new PatientBuilder().build();
        EditPatientCommand.EditPatientDescriptor descriptor = new EditPatientDescriptorBuilder(patient).build();
        EditPatientCommand command = (EditPatientCommand) parser.parseCommand(EditPatientCommand.COMMAND_WORD
                + " " + PREFIX_NRIC + patient.getNric() + " "
                + PatientUtil.getEditPatientDescriptorDetails(descriptor));
        assertEquals(new EditPatientCommand(patient.getNric(), descriptor), command);
    }

    @Test
    public void parseCommand_addAppt() throws Exception {
        Appointment appointment = new AppointmentBuilder().build();
        AddApptCommand command = (AddApptCommand) parser.parseCommand(AppointmentUtil.getAddApptCommand(appointment));
        assertEquals(new AddApptCommand(appointment), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_deleteAppt() throws Exception {
        Appointment appt = new AppointmentBuilder().build();
        DeleteApptCommand command = (DeleteApptCommand) parser.parseCommand(AppointmentUtil
                .getDeleteApptCommand(appt));

        assertEquals(new DeleteApptCommand(appt.getNric(), appt.getDate(), appt.getStartTime()), command);
    }

    @Test
    public void parseCommand_editAppt() throws Exception {
        Appointment appt = new AppointmentBuilder().build();
        EditApptCommand.EditApptDescriptor descriptor = new EditApptDescriptorBuilder(appt).build();
        EditApptCommand command = (EditApptCommand) parser.parseCommand(EditApptCommand.COMMAND_WORD
                + " " + AppointmentUtil.getAppointmentUniqueDetails(appt)
                + " " + AppointmentUtil.getEditApptDescriptorDetails(descriptor));
        assertEquals(new EditApptCommand(appt.getNric(), appt.getDate(), appt.getStartTime(), descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_findPatient() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindPatientCommand command = (FindPatientCommand) parser.parseCommand(
                FindPatientCommand.COMMAND_WORD + " n/" + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindPatientCommand(new NameContainsKeywordsPredicate(keywords)), command);

        String keyword = "foo";
        command = (FindPatientCommand) parser.parseCommand(
                FindPatientCommand.COMMAND_WORD + " i/" + keyword);
        assertEquals(new FindPatientCommand(new NricContainsMatchPredicate(keyword)), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommand_switchView() throws Exception {
        assertTrue(parser.parseCommand(SwitchViewCommand.COMMAND_WORD) instanceof SwitchViewCommand);
        assertTrue(parser.parseCommand(SwitchViewCommand.COMMAND_WORD + " 3") instanceof SwitchViewCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
            -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }
}
