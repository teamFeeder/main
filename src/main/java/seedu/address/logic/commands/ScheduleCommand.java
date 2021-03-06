package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SCHEDULE;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.address.commons.core.Messages;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.doctor.Doctor;
import seedu.address.model.patient.Patient;
import seedu.address.model.person.Appointment;
import seedu.address.model.person.Person;

/**
 * Updates the schedule of a person in the addressbook.
 */
public class ScheduleCommand extends Command {

    public static final String COMMAND_WORD = "schedule";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Schedules appointment for the person identified "
            + "by the index number used in the last person listing.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_SCHEDULE + "[DATE,START_TIME,END_TIME,DOCTOR_NAME,DOCTOR_DEPARTMENT,PATIENT_NAME,PATIENT_NRIC]\n"
            + PREFIX_SCHEDULE + "[DATE, START_TIME, END_TIME, DOCTOR_NAME, DOCTOR_DEPARTMENT, PATIENT_NAME, "
            + "PATIENT_NRIC]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_SCHEDULE + "23.11.2018,1300,1400,Jack,Heart,Jill,S1234567I";

    public static final String MESSAGE_SCHEDULE_APPOINTMENT_SUCCESS = "Scheduled appointment to Person: %1$s";
    public static final String MESSAGE_SCHEDULE_APPOINTMENT_FAILURE_INCORRECT_PARTS_NUMBER =
            "Failed to schedule appointment to Person.\n"
            + "Number of parts of the appointment is wrong.\n";
    public static final String MESSAGE_SCHEDULE_APPOINTMENT_FAILURE_INCORRECT_DOCTOR =
            "Failed to schedule appointment to Person.\n"
            + "Doctor entered is wrong.\n";
    public static final String MESSAGE_SCHEDULE_APPOINTMENT_FAILURE_INCORRECT_PATIENT = "Failed to schedule "
            + "appointment to Person.\n"
            + "Patient entered is wrong.\n";
    public static final String MESSAGE_DELETE_APPOINTMENT_SUCCESS = "Removed appointment from Person: %1$s";
    public static final String MESSAGE_SCHEDULE_APPOINTMENT_FAILURE_CLASH = "There is a clash of appointments. "
            + "Please choose another slot.\n";
    public static final String MESSAGE_SCHEDULE_APPOINTMENT_FAILURE_INCORRECT_NRIC = "NRIC format is wrong. "
            + "NRIC should contain only alphanumeric characters and should not be left blank.\n";;
    private static final String MESSAGE_SCHEDULE_APPOINTMENT_MISMATCH = "Please check input name matches person chosen";

    private final Index index;
    private final Appointment appointment;

    /**
     * @param index of the person in the filtered person list to edit the appointment
     * @param appointment of the person to be updated to
     */
    public ScheduleCommand(Index index, Appointment appointment) {
        requireAllNonNull(index, appointment);
        this.index = index;
        this.appointment = appointment;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        if (!appointment.isOfCorrectNumberOfParts()) {
            throw new CommandException(MESSAGE_SCHEDULE_APPOINTMENT_FAILURE_INCORRECT_PARTS_NUMBER);
        }

        if (!appointment.hasValidNric()) {
            throw new CommandException(MESSAGE_SCHEDULE_APPOINTMENT_FAILURE_INCORRECT_NRIC);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson;

        if (personToEdit instanceof Doctor && !appointment.hasValidDoctor(personToEdit)) {
            throw new CommandException(MESSAGE_SCHEDULE_APPOINTMENT_FAILURE_INCORRECT_DOCTOR);
        }

        if (personToEdit instanceof Patient && !appointment.hasValidPatient(personToEdit)) {
            throw new CommandException(MESSAGE_SCHEDULE_APPOINTMENT_FAILURE_INCORRECT_PATIENT);
        }

        if (personToEdit.hasClash(appointment)) {
            throw new CommandException(MESSAGE_SCHEDULE_APPOINTMENT_FAILURE_CLASH);
        }

        if (personToEdit instanceof Doctor) {
            editedPerson = new Doctor(personToEdit.getName(),
                    personToEdit.getPhone(), personToEdit.getEmail(),
                    personToEdit.getAddress(), personToEdit.getTags(),
                    appointment, ((Doctor) personToEdit).getMedicalDepartment());
        } else {
            assert personToEdit instanceof Patient;
            editedPerson = new Patient(personToEdit.getName(), personToEdit.getPhone(), personToEdit.getEmail(),
                    personToEdit.getAddress(), personToEdit.getTags(),
                    appointment, ((Patient) personToEdit).getNric(), ((Patient) personToEdit).getMedicalRecord());
        }

        model.updatePerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        model.commitAddressBook();

        return new CommandResult(generateSuccessMessage(editedPerson));
    }

    /**
     * Generates a command execution success message based on whether the appointment is added to or removed from
     * {@code personToEdit}.
     */
    private String generateSuccessMessage(Person personToEdit) {
        String message = !appointment.value.isEmpty() ? MESSAGE_SCHEDULE_APPOINTMENT_SUCCESS
                : MESSAGE_DELETE_APPOINTMENT_SUCCESS;
        return String.format(message, personToEdit);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }
        // instanceof handles nulls
        if (!(other instanceof ScheduleCommand)) {
            return false;
        }
        // state check
        ScheduleCommand e = (ScheduleCommand) other;
        return index.equals(e.index)
                && appointment.equals(e.appointment);
    }
}
