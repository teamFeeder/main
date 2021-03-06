package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEDICAL_RECORD;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.patient.MedicalRecord;
import seedu.address.model.patient.Patient;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;

/**
 * Updates medical record of a patient in the addressbook.
 */
public class UpdateCommand extends Command {

    public static final String COMMAND_WORD = "update";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": updates the medical record of the person identified by "
            + "the index number used in the last person listing. "
            + "Existing medical record will be overwritten by the input.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + PREFIX_MEDICAL_RECORD + "MEDICAL_RECORD\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_MEDICAL_RECORD + "Diagnosed with flue. Tamiflu prescribed.";

    public static final String MESSAGE_UPDATE_MEDICAL_RECORD_SUCCESS = "Updated medical record of Person: %1$s";
    public static final String MESSAGE_DELETE_MEDICAL_RECORD_SUCCESS = "Medical record deleted from Person: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book.";

    private final Index index;
    private final MedicalRecord medicalRecord;

    /**
     * @param index of the person in the filtered person list to edit
     * @param medicalRecord medical record of the person to update
     */
    public UpdateCommand(Index index, MedicalRecord medicalRecord) {
        requireNonNull(index);
        requireNonNull(medicalRecord);
        this.index = index;
        this.medicalRecord = medicalRecord;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        if (!(lastShownList.get(index.getZeroBased()) instanceof Patient)) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_CHOSEN);
        }

        Patient personToEdit = (Patient) lastShownList.get(index.getZeroBased());
        Patient editedPerson = new Patient(personToEdit.getName(), personToEdit.getPhone(),
                personToEdit.getEmail(), personToEdit.getAddress(), personToEdit.getTags(),
                personToEdit.getAppointment(), personToEdit.getNric(),
                medicalRecord);

        try {
            model.updatePerson(personToEdit, editedPerson);
            model.commitAddressBook();
        } catch (DuplicatePersonException dpe) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        } catch (PersonNotFoundException pnfe) {
            throw new AssertionError("The target person cannot be missing");
        }
        //model.updateFilteredListToShowAll();
        return new CommandResult(generateSuccessMessage(editedPerson));
    }

    /**
     * Returns a success message when updated a medical record.
     * @param personToEdit The person who's medical record is changed.
     * @return A success message.
     */
    private String generateSuccessMessage(Person personToEdit) {
        if (!medicalRecord.value.isEmpty()) {
            return String.format(MESSAGE_UPDATE_MEDICAL_RECORD_SUCCESS, personToEdit);
        } else {
            return String.format(MESSAGE_DELETE_MEDICAL_RECORD_SUCCESS, personToEdit);
        }
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }
        // instanceof handles nulls
        if (!(other instanceof UpdateCommand)) {
            return false;
        }
        // state check
        UpdateCommand e = (UpdateCommand) other;
        return index.equals(e.index)
                && medicalRecord.equals(e.medicalRecord);
    }
}
