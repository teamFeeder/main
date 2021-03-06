package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SCHEDULE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ScheduleCommand;
import seedu.address.model.person.Appointment;

public class ScheduleCommandParserTest {
    private ScheduleCommandParser parser = new ScheduleCommandParser();
    private final String nonEmptyAppointment = "22.11.2018,1300,1400,Alice,Heart,Betty,S1234567A";

    @Test
    public void parse_indexSpecified_success() {
        // have remark
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + " " + PREFIX_SCHEDULE + nonEmptyAppointment;
        ScheduleCommand expectedCommand = new ScheduleCommand(INDEX_FIRST_PERSON, new Appointment(nonEmptyAppointment));
        assertParseSuccess(parser, userInput, expectedCommand);
        // no remark

        userInput = targetIndex.getOneBased() + " " + PREFIX_SCHEDULE;
        expectedCommand = new ScheduleCommand(INDEX_FIRST_PERSON, new Appointment(""));
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_missingCompulsoryField_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ScheduleCommand.MESSAGE_USAGE);

        // no parameters
        assertParseFailure(parser, ScheduleCommand.COMMAND_WORD, expectedMessage);

        // no index
        assertParseFailure(parser, ScheduleCommand.COMMAND_WORD + " " + nonEmptyAppointment, expectedMessage);
    }
}
